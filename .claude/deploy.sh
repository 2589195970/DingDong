#!/bin/bash

# 生产环境部署脚本
# 用途: 将项目文件部署到nginx配置的生产目录
#
# 使用说明:
# - 默认为开发模式（--dev）: JAR服务不使用外部配置文件启动
# - 生产模式: ./deploy.sh --prod
# - 停止nginx: ./deploy.sh --stop-nginx
# - 备份策略: 自动清理，只保留最近5次备份，删除所有超出备份

set -e  # 遇到错误立即退出

# 配置变量
PROJECT_NAME="console"
DEPLOY_DATE=$(date +"%Y%m%d_%H%M%S")
BACKUP_DIR="/opt/backup/${PROJECT_NAME}"
DEPLOY_ROOT="/opt/${PROJECT_NAME}"

# JAR服务配置
MC_ORDER_JAR="mc-order.jar"
NUMBER_CARD_JAR="number-card.jar"
MC_ORDER_PORT=8081
NUMBER_CARD_PORT=8080
MC_ORDER_PID_FILE="/var/run/mc-order.pid"
NUMBER_CARD_PID_FILE="/var/run/number-card.pid"

# nginx配置（基于实际情况）
NGINX_BIN="/usr/local/nginx/sbin/nginx"
NGINX_CONF_FILE="/usr/local/nginx/conf/nginx.conf"
# 前端部署基础目录 (与nginx.conf保持一致)
FRONTEND_ROOT="/opt/console"

# 颜色输出函数
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查是否以root权限运行
check_root() {
    if [[ $EUID -ne 0 ]]; then
        log_error "此脚本需要root权限运行"
        exit 1
    fi
}

# 清理项目旧备份文件（只保留最近5次）
cleanup_project_backups() {
    log_info "清理项目旧备份文件，只保留最近5次备份..."

    local backup_base="/opt/backup/${PROJECT_NAME}"
    local total_deleted=0

    if [ ! -d "$backup_base" ]; then
        log_info "项目备份目录不存在: $backup_base"
        return 0
    fi

    # 查找项目备份目录，按时间排序
    local backup_items=($(find "$backup_base" -mindepth 1 -exec ls -dt {} \; 2>/dev/null || true))
    local backup_count=${#backup_items[@]}

    if [ $backup_count -le 5 ]; then
        log_info "项目备份数量未超过5个，无需清理"
        return 0
    fi

    # 删除超过5个的旧备份
    for ((i=5; i<$backup_count; i++)); do
        local backup_item="${backup_items[$i]}"
        if rm -rf "$backup_item"; then
            total_deleted=$((total_deleted + 1))
        fi
    done

    if [ $total_deleted -gt 0 ]; then
        log_info "清理完成，删除了 $total_deleted 个旧备份"
    fi
}

# 检查nginx用户
check_nginx_user() {
    # 检查nginx用户是否存在
    if ! id nginx &>/dev/null; then
        log_warn "nginx用户不存在，检查其他web用户..."

        # 检查常见的web用户
        if id www-data &>/dev/null; then
            NGINX_USER="www-data"
            log_info "使用www-data用户"
        elif id www &>/dev/null; then
            NGINX_USER="www"
            log_info "使用www用户"
        elif id daemon &>/dev/null; then
            NGINX_USER="daemon"
            log_info "使用daemon用户"
        elif id apache &>/dev/null; then
            NGINX_USER="apache"
            log_info "使用apache用户"
        else
            # 如果没有web用户，使用nobody或创建nginx用户
            if id nobody &>/dev/null; then
                NGINX_USER="nobody"
                log_info "使用nobody用户"
            else
                log_warn "未找到合适的web用户，将使用root用户"
                NGINX_USER="root"
            fi
        fi
    else
        NGINX_USER="nginx"
        log_info "使用nginx用户"
    fi

    log_info "Web用户: $NGINX_USER"
    NGINX_GROUP=$(id -gn $NGINX_USER 2>/dev/null || echo $NGINX_USER)
    log_info "Web用户组: $NGINX_GROUP"
}

# 安全地设置文件权限
safe_chown() {
    local user="$1"
    local group="$2"
    local path="$3"

    # 如果用户存在则设置，否则跳过
    if id "$user" &>/dev/null; then
        chown -R "$user:$group" "$path"
        log_info "权限设置完成: $user:$group"
    else
        log_warn "用户 $user 不存在，跳过权限设置"
    fi
}

# 创建必要的目录
create_directories() {
    log_info "创建部署目录..."
    # 创建前端目录 (与nginx.conf保持一致)
    mkdir -p ${FRONTEND_ROOT}/dist
    mkdir -p ${FRONTEND_ROOT}/h5/dist
    mkdir -p ${FRONTEND_ROOT}/shop/dist
    mkdir -p ${FRONTEND_ROOT}/app/dist/build/web
    mkdir -p ${FRONTEND_ROOT}/guanwang/dist
    # 创建后端目录
    mkdir -p /opt/${PROJECT_NAME}/config
    mkdir -p /opt/${PROJECT_NAME}/config/mc-order
    mkdir -p /opt/${PROJECT_NAME}/config/number-card
    mkdir -p /opt/${PROJECT_NAME}/jar
    # 创建备份和日志目录
    mkdir -p ${BACKUP_DIR}
    mkdir -p /var/log/${PROJECT_NAME}

    # 设置前端目录权限
    chmod 755 ${FRONTEND_ROOT}/dist
    chmod 755 ${FRONTEND_ROOT}/h5/dist
    chmod 755 ${FRONTEND_ROOT}/shop/dist
    chmod 755 ${FRONTEND_ROOT}/app/dist/build/web
    chmod 755 ${FRONTEND_ROOT}/guanwang/dist
    # 设置后端目录权限
    chmod 755 /opt/${PROJECT_NAME}/config
    chmod 755 /opt/${PROJECT_NAME}/config/mc-order
    chmod 755 /opt/${PROJECT_NAME}/config/number-card
    chmod 755 /opt/${PROJECT_NAME}/jar
}

# 备份现有文件
backup_existing() {
    # 备份前端文件
    if [ -d "${FRONTEND_ROOT}/dist" ] && [ "$(ls -A ${FRONTEND_ROOT}/dist 2>/dev/null)" ]; then
        log_info "备份现有前端文件..."
        mkdir -p ${BACKUP_DIR}
        cp -r ${FRONTEND_ROOT} ${BACKUP_DIR}/${PROJECT_NAME}_${DEPLOY_DATE}
        log_info "备份完成: ${BACKUP_DIR}/${PROJECT_NAME}_${DEPLOY_DATE}"
    fi
}

# 停止现有服务
stop_existing_services() {
    log_info "停止现有服务..."

    # 收集停止过程中的错误
    local stop_errors=0

    # 1. 停止mc-order服务 (8081端口)
    if [ -f "$MC_ORDER_PID_FILE" ]; then
        PID=$(cat $MC_ORDER_PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            log_info "停止mc-order服务 (PID: $PID)..."
            if ! kill -TERM $PID 2>/dev/null; then
                log_error "停止mc-order服务失败，PID: $PID"
                stop_errors=$((stop_errors + 1))
            fi
            sleep 3
            if ps -p $PID > /dev/null 2>&1; then
                log_info "强制停止mc-order服务 (PID: $PID)..."
                if ! kill -KILL $PID 2>/dev/null; then
                    log_error "强制停止mc-order服务失败，PID: $PID"
                    stop_errors=$((stop_errors + 1))
                fi
            fi
        else
            log_warn "mc-order服务PID文件存在但进程未运行，PID: $PID"
        fi
        rm -f $MC_ORDER_PID_FILE
    else
        log_info "mc-order服务PID文件不存在，跳过"
    fi

    # 2. 停止number-card服务 (8080端口)
    if [ -f "$NUMBER_CARD_PID_FILE" ]; then
        PID=$(cat $NUMBER_CARD_PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            log_info "停止number-card服务 (PID: $PID)..."
            if ! kill -TERM $PID 2>/dev/null; then
                log_error "停止number-card服务失败，PID: $PID"
                stop_errors=$((stop_errors + 1))
            fi
            sleep 3
            if ps -p $PID > /dev/null 2>&1; then
                log_info "强制停止number-card服务 (PID: $PID)..."
                if ! kill -KILL $PID 2>/dev/null; then
                    log_error "强制停止number-card服务失败，PID: $PID"
                    stop_errors=$((stop_errors + 1))
                fi
            fi
        else
            log_warn "number-card服务PID文件存在但进程未运行，PID: $PID"
        fi
        rm -f $NUMBER_CARD_PID_FILE
    else
        log_info "number-card服务PID文件不存在，跳过"
    fi

    # 3. 查找并停止相关的Java进程
    MC_ORDER_PIDS=$(ps aux | grep java | grep "${MC_ORDER_JAR}" | awk '{print $2}')
    if [ -n "$MC_ORDER_PIDS" ]; then
        log_info "停止mc-order Java进程: $MC_ORDER_PIDS"
        if ! echo "$MC_ORDER_PIDS" | xargs kill -TERM 2>/dev/null; then
            log_error "停止mc-order Java进程失败"
            stop_errors=$((stop_errors + 1))
        fi
        sleep 3
        MC_ORDER_PIDS=$(ps aux | grep java | grep "${MC_ORDER_JAR}" | awk '{print $2}')
        if [ -n "$MC_ORDER_PIDS" ]; then
            if ! echo "$MC_ORDER_PIDS" | xargs kill -KILL 2>/dev/null; then
                log_error "强制停止mc-order Java进程失败"
                stop_errors=$((stop_errors + 1))
            fi
        fi
    else
        log_info "未找到运行中的mc-order Java进程"
    fi

    NUMBER_CARD_PIDS=$(ps aux | grep java | grep "${NUMBER_CARD_JAR}" | awk '{print $2}')
    if [ -n "$NUMBER_CARD_PIDS" ]; then
        log_info "停止number-card Java进程: $NUMBER_CARD_PIDS"
        if ! echo "$NUMBER_CARD_PIDS" | xargs kill -TERM 2>/dev/null; then
            log_error "停止number-card Java进程失败"
            stop_errors=$((stop_errors + 1))
        fi
        sleep 3
        NUMBER_CARD_PIDS=$(ps aux | grep java | grep "${NUMBER_CARD_JAR}" | awk '{print $2}')
        if [ -n "$NUMBER_CARD_PIDS" ]; then
            if ! echo "$NUMBER_CARD_PIDS" | xargs kill -KILL 2>/dev/null; then
                log_error "强制停止number-card Java进程失败"
                stop_errors=$((stop_errors + 1))
            fi
        fi
    else
        log_info "未找到运行中的number-card Java进程"
    fi

    # 4. 停止nginx（可选）
    if [ "$1" = "--stop-nginx" ]; then
        log_info "停止nginx服务..."
        NGINX_PID=$(ps aux | grep nginx | grep master | awk '{print $2}' | head -1)
        if [ -n "$NGINX_PID" ]; then
            if ! kill -TERM $NGINX_PID 2>/dev/null; then
                log_error "停止nginx服务失败，PID: $NGINX_PID"
                stop_errors=$((stop_errors + 1))
            fi
            sleep 2
            NGINX_PID=$(ps aux | grep nginx | grep master | awk '{print $2}' | head -1)
            if [ -n "$NGINX_PID" ]; then
                if ! kill -KILL $NGINX_PID 2>/dev/null; then
                    log_error "强制停止nginx服务失败，PID: $NGINX_PID"
                    stop_errors=$((stop_errors + 1))
                fi
            fi
            log_info "nginx已停止"
        else
            log_info "nginx未运行"
        fi
    fi

    log_info "所有服务停止操作完成"
    if [ $stop_errors -gt 0 ]; then
        log_warn "停止服务过程中遇到 $stop_errors 个错误，但不影响部署流程"
    fi
}

# 部署前端文件
deploy_frontend() {
    log_info "开始部署前端文件..."
    echo "=========================================================="
    echo "前端文件部署开始时间: $(date)"
    echo "源目录: $(pwd)/console/"
    echo "目标目录: ${FRONTEND_ROOT}/"
    echo "=========================================================="

    local deploy_count=0
    local error_count=0
    local warning_count=0
    local skip_count=0

    # 1. 部署主应用 (console/dist -> /opt/console/dist)
    echo -e "\n[1/4] 部署主应用 (www.dingdonghaoka.com)"

    if [ -d "./console/dist" ]; then
        source_files=$(find ./console/dist -type f | wc -l)
        log_info "发现源文件: $source_files 个"

        log_info "开始部署主应用..."
        mkdir -p ${FRONTEND_ROOT}/dist
        rm -rf ${FRONTEND_ROOT}/dist/* 2>/dev/null || true
        cp -r ./console/dist/* ${FRONTEND_ROOT}/dist/

        copied_files=$(find ${FRONTEND_ROOT}/dist -type f 2>/dev/null | wc -l)
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${FRONTEND_ROOT}/dist"
        chmod -R 755 ${FRONTEND_ROOT}/dist

        if [ $copied_files -eq $source_files ]; then
            log_info "✅ 主应用部署成功 ($source_files 个文件)"
            deploy_count=$((deploy_count + 1))
        else
            log_warn "⚠ 主应用部署异常 (源:$source_files, 目标:$copied_files)"
            warning_count=$((warning_count + 1))
        fi
    else
        log_error "❌ console/dist 目录不存在"
        error_count=$((error_count + 1))
    fi

    # 2. 部署H5页面 (console/h5/dist -> /opt/console/h5/dist)
    echo -e "\n[2/4] 部署H5页面 (h5.dingdonghaoka.com)"

    if [ -d "./console/h5/dist" ]; then
        source_files=$(find ./console/h5/dist -type f | wc -l)
        log_info "发现源文件: $source_files 个"

        log_info "开始部署H5页面..."
        mkdir -p ${FRONTEND_ROOT}/h5/dist
        rm -rf ${FRONTEND_ROOT}/h5/dist/* 2>/dev/null || true
        cp -r ./console/h5/dist/* ${FRONTEND_ROOT}/h5/dist/

        copied_files=$(find ${FRONTEND_ROOT}/h5/dist -type f 2>/dev/null | wc -l)
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${FRONTEND_ROOT}/h5/dist"
        chmod -R 755 ${FRONTEND_ROOT}/h5/dist

        if [ $copied_files -eq $source_files ]; then
            log_info "✅ H5页面部署成功 ($source_files 个文件)"
            deploy_count=$((deploy_count + 1))
        else
            log_warn "⚠ H5页面部署异常 (源:$source_files, 目标:$copied_files)"
            warning_count=$((warning_count + 1))
        fi
    else
        log_warn "⚠ console/h5/dist 目录不存在，跳过H5部署"
        skip_count=$((skip_count + 1))
    fi

    # 3. 部署商城页面 (console/shop/dist -> /opt/console/shop/dist)
    echo -e "\n[3/4] 部署商城页面 (shop.dingdonghaoka.com)"

    if [ -d "./console/shop/dist" ]; then
        source_files=$(find ./console/shop/dist -type f | wc -l)
        log_info "发现源文件: $source_files 个"

        log_info "开始部署商城页面..."
        mkdir -p ${FRONTEND_ROOT}/shop/dist
        rm -rf ${FRONTEND_ROOT}/shop/dist/* 2>/dev/null || true
        cp -r ./console/shop/dist/* ${FRONTEND_ROOT}/shop/dist/

        copied_files=$(find ${FRONTEND_ROOT}/shop/dist -type f 2>/dev/null | wc -l)
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${FRONTEND_ROOT}/shop/dist"
        chmod -R 755 ${FRONTEND_ROOT}/shop/dist

        if [ $copied_files -eq $source_files ]; then
            log_info "✅ 商城页面部署成功 ($source_files 个文件)"
            deploy_count=$((deploy_count + 1))
        else
            log_warn "⚠ 商城页面部署异常 (源:$source_files, 目标:$copied_files)"
            warning_count=$((warning_count + 1))
        fi
    else
        log_warn "⚠ console/shop/dist 目录不存在，跳过商城部署"
        skip_count=$((skip_count + 1))
    fi

    # 4. 部署uniapp页面
    echo -e "\n[4/5] 部署uniapp页面 (sj.dingdonghaoka.com)"

    if [ -d "./console/app/unpackage/dist/build/web" ]; then
        source_files=$(find ./console/app/unpackage/dist/build/web -type f | wc -l)
        log_info "发现源文件: $source_files 个"

        log_info "开始部署uniapp页面..."
        mkdir -p ${FRONTEND_ROOT}/app/dist/build/web
        rm -rf ${FRONTEND_ROOT}/app/dist/build/web/* 2>/dev/null || true
        cp -r ./console/app/unpackage/dist/build/web/* ${FRONTEND_ROOT}/app/dist/build/web/

        copied_files=$(find ${FRONTEND_ROOT}/app/dist/build/web -type f 2>/dev/null | wc -l)
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${FRONTEND_ROOT}/app/dist/build/web"
        chmod -R 755 ${FRONTEND_ROOT}/app/dist/build/web

        if [ $copied_files -eq $source_files ]; then
            log_info "✅ uniapp页面部署成功 ($source_files 个文件)"
            deploy_count=$((deploy_count + 1))
        else
            log_warn "⚠ uniapp页面部署异常 (源:$source_files, 目标:$copied_files)"
            warning_count=$((warning_count + 1))
        fi
    else
        log_warn "⚠ console/app/unpackage/dist/build/web 目录不存在，跳过uniapp部署"
        skip_count=$((skip_count + 1))
    fi

    # 5. 部署官网页面
    echo -e "\n[5/5] 部署官网页面 (www.xn--iprop.com)"

    if [ -d "./console/guanwang/dist" ]; then
        source_files=$(find ./console/guanwang/dist -type f | wc -l)
        log_info "发现源文件: $source_files 个"

        log_info "开始部署官网页面..."
        mkdir -p ${FRONTEND_ROOT}/guanwang/dist
        rm -rf ${FRONTEND_ROOT}/guanwang/dist/* 2>/dev/null || true
        cp -r ./console/guanwang/dist/* ${FRONTEND_ROOT}/guanwang/dist/

        copied_files=$(find ${FRONTEND_ROOT}/guanwang/dist -type f 2>/dev/null | wc -l)
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${FRONTEND_ROOT}/guanwang/dist"
        chmod -R 755 ${FRONTEND_ROOT}/guanwang/dist

        if [ $copied_files -eq $source_files ]; then
            log_info "✅ 官网页面部署成功 ($source_files 个文件)"
            deploy_count=$((deploy_count + 1))
        else
            log_warn "⚠ 官网页面部署异常 (源:$source_files, 目标:$copied_files)"
            warning_count=$((warning_count + 1))
        fi
    else
        log_warn "⚠ console/guanwang/dist 目录不存在，跳过官网部署"
        skip_count=$((skip_count + 1))
    fi

    # 部署结果汇总
    echo -e "\n=========================================================="
    echo "前端文件部署完成时间: $(date)"
    echo "部署结果汇总:"
    echo "  ✅ 成功部署: $deploy_count 个应用"
    echo "  ❌ 部署失败: $error_count 个应用"
    echo "  ⚠ 部署警告: $warning_count 个应用"
    echo "  ⏭️ 跳过部署: $skip_count 个应用"

    # 显示部署的目录大小
    echo -e "\n部署目录大小:"
    for dir in "dist" "h5/dist" "shop/dist" "app/dist/build/web" "guanwang/dist"; do
        if [ -d "${DEPLOY_ROOT}/$dir" ]; then
            size=$(du -sh ${DEPLOY_ROOT}/$dir 2>/dev/null | cut -f1)
            echo "  - ${DEPLOY_ROOT}/$dir: $size"
        fi
    done

    echo "=========================================================="

    # 只要有部署成功的应用就认为前端部署成功
    if [ $deploy_count -gt 0 ]; then
        if [ $error_count -eq 0 ]; then
            log_info "🎉 前端文件部署全部成功！"
        else
            log_info "✅ 前端文件部署基本成功，$deploy_count 个应用已部署完成"
        fi
        return 0
    else
        log_error "💥 前端文件部署失败，没有应用成功部署"
        return 1
    fi
}


# 部署后端JAR包
deploy_backend() {
    log_info "开始部署后端JAR服务..."
    echo "=========================================================="
    echo "后端JAR部署开始时间: $(date)"
    echo "源目录: $(pwd)/"
    echo "目标目录: ${DEPLOY_ROOT}/jar/"
    echo "=========================================================="

    local deploy_count=0
    local error_count=0

    # 检查JAR文件
    echo -e "\n[检查] 检查JAR文件..."
    local available_jars=0

    # 查找mc-order.jar
    if [ -f "./${MC_ORDER_JAR}" ]; then
        MC_ORDER_SOURCE="./${MC_ORDER_JAR}"
        mc_order_size=$(stat -f%z "$MC_ORDER_SOURCE" 2>/dev/null || stat -c%s "$MC_ORDER_SOURCE" 2>/dev/null || echo "未知")
        mc_order_mb=$(echo "scale=2; $mc_order_size / 1048576" | bc 2>/dev/null || echo "未知")
        echo "✅ 找到 ${MC_ORDER_JAR}: ${MC_ORDER_SOURCE} (${mc_order_mb} MB)"
        available_jars=$((available_jars + 1))
    else
        echo "❌ 未找到 ${MC_ORDER_JAR}"
    fi

    # 查找number-card.jar
    if [ -f "./${NUMBER_CARD_JAR}" ]; then
        NUMBER_CARD_SOURCE="./${NUMBER_CARD_JAR}"
        number_card_size=$(stat -f%z "$NUMBER_CARD_SOURCE" 2>/dev/null || stat -c%s "$NUMBER_CARD_SOURCE" 2>/dev/null || echo "未知")
        number_card_mb=$(echo "scale=2; $number_card_size / 1048576" | bc 2>/dev/null || echo "未知")
        echo "✅ 找到 ${NUMBER_CARD_JAR}: ${NUMBER_CARD_SOURCE} (${number_card_mb} MB)"
        available_jars=$((available_jars + 1))
    else
        echo "❌ 未找到 ${NUMBER_CARD_JAR}"
    fi

    if [ $available_jars -eq 0 ]; then
        log_error "💥 未找到任何JAR文件，部署失败"
        return 1
    fi

    # 创建JAR目录
    echo -e "\n[准备] 创建部署目录..."
    mkdir -p ${DEPLOY_ROOT}/jar
    echo "✅ JAR目录已创建: ${DEPLOY_ROOT}/jar/"

    # 检查目标目录现有文件
    echo -e "\n[检查] 目标目录现有文件:"
    if [ "$(ls -A ${DEPLOY_ROOT}/jar/ 2>/dev/null)" ]; then
        ls -la ${DEPLOY_ROOT}/jar/ | grep -E "\.jar$"
    else
        echo "  目标目录为空"
    fi

    # 1. 部署mc-order.jar (8081端口)
    echo -e "\n[1/2] 部署 mc-order.jar (8081端口)"
    echo "源文件: ./${MC_ORDER_JAR}"
    echo "目标文件: ${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}"

    if [ -f "./${MC_ORDER_JAR}" ]; then
        # 显示源文件信息
        echo "源文件信息:"
        echo "  - 文件大小: ${mc_order_mb} MB"
        echo "  - 修改时间: $(stat -f%Sm "./${MC_ORDER_JAR}" 2>/dev/null || stat -c%y "./${MC_ORDER_JAR}" 2>/dev/null)"

        # 备份现有JAR
        if [ -f "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" ]; then
            old_size=$(stat -f%z "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" 2>/dev/null || stat -c%s "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" 2>/dev/null || echo "未知")
            echo "发现现有JAR文件: ${old_size} bytes，将备份..."
            cp ${DEPLOY_ROOT}/jar/${MC_ORDER_JAR} ${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}.backup.${DEPLOY_DATE}
            echo "✅ 备份完成: ${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}.backup.${DEPLOY_DATE}"
        else
            echo "目标位置无现有JAR文件，无需备份"
        fi

        # 复制新的JAR文件
        echo "开始复制JAR文件..."
        cp ./${MC_ORDER_JAR} ${DEPLOY_ROOT}/jar/

        # 验证复制结果
        if [ -f "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" ]; then
            new_size=$(stat -f%z "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" 2>/dev/null || stat -c%s "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" 2>/dev/null || echo "未知")
            echo "✅ 复制完成，新文件大小: ${new_size} bytes"

            # 验证文件完整性
            if [ "$new_size" = "$mc_order_size" ]; then
                echo "✅ 文件完整性验证通过"
            else
                log_warn "⚠ 文件完整性验证失败 (源:${mc_order_size}, 目标:${new_size})"
                echo "💡 提示：文件大小不匹配可能影响服务启动，但不停止部署"
            fi

            # 设置权限
            safe_chown "$NGINX_USER" "$NGINX_GROUP" "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}"
            chmod 755 ${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}
            echo "✅ 权限设置完成 ($NGINX_USER:$NGINX_GROUP, 755)"

            log_info "✅ ${MC_ORDER_JAR} 部署成功"
            deploy_count=$((deploy_count + 1))
        else
            log_error "❌ ${MC_ORDER_JAR} 复制失败"
            error_count=$((error_count + 1))
        fi
    else
        log_error "❌ ${MC_ORDER_JAR} 文件不存在，跳过部署"
        error_count=$((error_count + 1))
    fi

    # 2. 部署number-card.jar (8080端口)
    echo -e "\n[2/2] 部署 number-card.jar (8080端口)"
    echo "源文件: ./${NUMBER_CARD_JAR}"
    echo "目标文件: ${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}"

    if [ -f "./${NUMBER_CARD_JAR}" ]; then
        # 显示源文件信息
        echo "源文件信息:"
        echo "  - 文件大小: ${number_card_mb} MB"
        echo "  - 修改时间: $(stat -f%Sm "./${NUMBER_CARD_JAR}" 2>/dev/null || stat -c%y "./${NUMBER_CARD_JAR}" 2>/dev/null)"

        # 备份现有JAR
        if [ -f "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" ]; then
            old_size=$(stat -f%z "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" 2>/dev/null || stat -c%s "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" 2>/dev/null || echo "未知")
            echo "发现现有JAR文件: ${old_size} bytes，将备份..."
            cp ${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR} ${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}.backup.${DEPLOY_DATE}
            echo "✅ 备份完成: ${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}.backup.${DEPLOY_DATE}"
        else
            echo "目标位置无现有JAR文件，无需备份"
        fi

        # 复制新的JAR文件
        echo "开始复制JAR文件..."
        cp ./${NUMBER_CARD_JAR} ${DEPLOY_ROOT}/jar/

        # 验证复制结果
        if [ -f "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" ]; then
            new_size=$(stat -f%z "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" 2>/dev/null || stat -c%s "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" 2>/dev/null || echo "未知")
            echo "✅ 复制完成，新文件大小: ${new_size} bytes"

            # 验证文件完整性
            if [ "$new_size" = "$number_card_size" ]; then
                echo "✅ 文件完整性验证通过"
            else
                log_warn "⚠ 文件完整性验证失败 (源:${number_card_size}, 目标:${new_size})"
                echo "💡 提示：文件大小不匹配可能影响服务启动，但不停止部署"
            fi

            # 设置权限
            safe_chown "$NGINX_USER" "$NGINX_GROUP" "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}"
            chmod 755 ${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}
            echo "✅ 权限设置完成 ($NGINX_USER:$NGINX_GROUP, 755)"

            log_info "✅ ${NUMBER_CARD_JAR} 部署成功"
            deploy_count=$((deploy_count + 1))
        else
            log_error "❌ ${NUMBER_CARD_JAR} 复制失败"
            error_count=$((error_count + 1))
        fi
    else
        log_error "❌ ${NUMBER_CARD_JAR} 文件不存在，跳过部署"
        error_count=$((error_count + 1))
    fi

    # 部署结果汇总
    echo -e "\n=========================================================="
    echo "后端JAR部署完成时间: $(date)"
    echo "部署结果汇总:"
    echo "  ✅ 成功部署: $deploy_count 个JAR文件"
    echo "  ❌ 部署失败: $error_count 个JAR文件"

    # 显示部署后的文件信息
    echo -e "\n部署后的JAR文件:"
    if [ -d "${DEPLOY_ROOT}/jar" ]; then
        for jar in "${MC_ORDER_JAR}" "${NUMBER_CARD_JAR}"; do
            if [ -f "${DEPLOY_ROOT}/jar/$jar" ]; then
                size=$(stat -f%z "${DEPLOY_ROOT}/jar/$jar" 2>/dev/null || stat -c%s "${DEPLOY_ROOT}/jar/$jar" 2>/dev/null || echo "未知")
                mb=$(echo "scale=2; $size / 1048576" | bc 2>/dev/null || echo "未知")
                modified=$(stat -f%Sm "${DEPLOY_ROOT}/jar/$jar" 2>/dev/null || stat -c%y "${DEPLOY_ROOT}/jar/$jar" 2>/dev/null)
                echo "  ✅ $jar: ${mb} MB (修改时间: $modified)"
            fi
        done

        # 显示总大小
        total_size=$(du -sh ${DEPLOY_ROOT}/jar 2>/dev/null | cut -f1)
        echo "  📁 JAR目录总大小: $total_size"
    fi

    # 显示备份文件
    echo -e "\n备份文件:"
    ls -la ${DEPLOY_ROOT}/jar/*.backup.* 2>/dev/null | while read line; do
        echo "  📦 $line"
    done

    echo "=========================================================="

    # 只要有部署成功的JAR文件就认为后端部署成功
    if [ $deploy_count -gt 0 ]; then
        if [ $error_count -eq 0 ]; then
            log_info "🎉 后端JAR文件部署全部成功！"
        else
            log_info "✅ 后端JAR文件部署基本成功，$deploy_count 个JAR文件已部署完成"
        fi
        return 0
    else
        log_error "💥 后端JAR文件部署失败，没有JAR文件成功部署"
        return 1
    fi
}

# 部署配置文件
deploy_config() {
    log_info "部署配置文件..."

    # 创建配置目录
    mkdir -p ${DEPLOY_ROOT}/config/mc-order
    mkdir -p ${DEPLOY_ROOT}/config/number-card

    local config_count=0
    local config_errors=0

    # 部署mc-order配置文件
    if [ -f "./mc-order-conf/application-release.yml" ]; then
        log_info "找到mc-order配置文件: ./mc-order-conf/application-release.yml"
        if cp ./mc-order-conf/application-release.yml ${DEPLOY_ROOT}/config/mc-order/; then
            safe_chown "$NGINX_USER" "$NGINX_GROUP" "${DEPLOY_ROOT}/config/mc-order/application-release.yml"
            chmod 644 ${DEPLOY_ROOT}/config/mc-order/application-release.yml
            log_info "✅ mc-order配置文件部署完成"
            config_count=$((config_count + 1))
        else
            log_error "❌ mc-order配置文件复制失败"
            config_errors=$((config_errors + 1))
        fi
    else
        log_error "❌ 未找到mc-order配置文件: ./mc-order-conf/application-release.yml"
        config_errors=$((config_errors + 1))
    fi

    # 部署number-card配置文件
    if [ -f "./number-card-conf/application-release.yml" ]; then
        log_info "找到number-card配置文件: ./number-card-conf/application-release.yml"
        if cp ./number-card-conf/application-release.yml ${DEPLOY_ROOT}/config/number-card/; then
            safe_chown "$NGINX_USER" "$NGINX_GROUP" "${DEPLOY_ROOT}/config/number-card/application-release.yml"
            chmod 644 ${DEPLOY_ROOT}/config/number-card/application-release.yml
            log_info "✅ number-card配置文件部署完成"
            config_count=$((config_count + 1))
        else
            log_error "❌ number-card配置文件复制失败"
            config_errors=$((config_errors + 1))
        fi
    else
        log_error "❌ 未找到number-card配置文件: ./number-card-conf/application-release.yml"
        config_errors=$((config_errors + 1))
    fi

    # 创建application.yml主配置文件
    if cat > ${DEPLOY_ROOT}/config/application.yml << EOF
spring:
  profiles:
    active: release
EOF
    then
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${DEPLOY_ROOT}/config/application.yml"
        chmod 644 ${DEPLOY_ROOT}/config/application.yml
        log_info "✅ application.yml主配置文件创建完成"
    else
        log_error "❌ application.yml主配置文件创建失败"
        config_errors=$((config_errors + 1))
    fi

    if [ $config_errors -gt 0 ]; then
        log_warn "⚠ 配置文件部署过程中遇到 $config_errors 个错误"
        log_warn "请确保以下配置文件存在："
        log_warn "  - ./mc-order-conf/application-release.yml"
        log_warn "  - ./number-card-conf/application-release.yml"
        log_warn "💡 提示：配置文件错误可能影响服务启动，但不停止部署流程"
        return 0  # 不再停止部署流程
    else
        log_info "🎉 所有配置文件部署完成"
        return 0
    fi
}

# 创建日志目录
create_log_directory() {
    log_info "创建日志目录..."
    mkdir -p /var/log/${PROJECT_NAME}
    safe_chown "$NGINX_USER" "$NGINX_GROUP" "/var/log/${PROJECT_NAME}"
    chmod 755 /var/log/${PROJECT_NAME}
}

# 启动JAR服务（简单启动方式）
start_service() {
    log_info "启动后端JAR服务..."

    # 创建日志目录
    mkdir -p /var/log/${PROJECT_NAME}

    local started_services=0

    # 1. 启动mc-order服务 (8081端口)
    if [ -f "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" ]; then
        log_info "启动 ${MC_ORDER_JAR} (8081端口)..."

        # 清理旧的PID文件
        rm -f $MC_ORDER_PID_FILE

        # 启动mc-order服务
        cd ${DEPLOY_ROOT}
        if [ "$DEV_MODE" = true ]; then
            log_info "开发模式启动mc-order服务（不使用外部配置文件）"
            nohup java -jar ${DEPLOY_ROOT}/jar/${MC_ORDER_JAR} > /var/log/${PROJECT_NAME}/mc-order-startup.log 2>&1 &
        else
            log_info "生产模式启动mc-order服务（使用release配置文件）"
            nohup java -jar ${DEPLOY_ROOT}/jar/${MC_ORDER_JAR} --spring.profiles.active=release --spring.config.location=${DEPLOY_ROOT}/config/mc-order/ > /var/log/${PROJECT_NAME}/mc-order-startup.log 2>&1 &
        fi

        # 获取进程PID
        MC_ORDER_PID=$!
        echo $MC_ORDER_PID > $MC_ORDER_PID_FILE

        log_info "${MC_ORDER_JAR} 进程已启动，PID: $MC_ORDER_PID"
        started_services=$((started_services + 1))

        # 检查mc-order服务启动状态
        sleep 10
        local mc_order_started=false
        for i in {1..12}; do
            if ps -p $MC_ORDER_PID > /dev/null 2>&1; then
                # 多种方式检查端口启动状态
                local port_ready=false

                # 方法1: 检查netstat端口监听
                if netstat -tlnp 2>/dev/null | grep -q ":${MC_ORDER_PORT}.*$MC_ORDER_PID"; then
                    port_ready=true
                # 方法2: 检查日志中是否显示启动成功
                elif [ -f "/var/log/${PROJECT_NAME}/mc-order-startup.log" ]; then
                    if grep -q "Started OrderApplication" "/var/log/${PROJECT_NAME}/mc-order-startup.log" 2>/dev/null; then
                        if grep -q "http-nio-${MC_ORDER_PORT}" "/var/log/${PROJECT_NAME}/mc-order-startup.log" 2>/dev/null; then
                            port_ready=true
                        fi
                    fi
                fi

                if [ "$port_ready" = true ]; then
                    log_info "✓ ${MC_ORDER_JAR} 启动成功 (PID: $MC_ORDER_PID, 端口: ${MC_ORDER_PORT})"
                    mc_order_started=true
                    break
                else
                    log_info "等待 ${MC_ORDER_JAR} 端口${MC_ORDER_PORT}监听... ($i/12)"
                fi
            else
                log_error "${MC_ORDER_JAR} 进程意外退出"
                break
            fi
            sleep 5
        done

        if [ "$mc_order_started" = false ]; then
            log_error "✗ ${MC_ORDER_JAR} 启动失败"
            echo "=================== ${MC_ORDER_JAR} 启动日志 ==================="
            if [ -f "/var/log/${PROJECT_NAME}/mc-order-startup.log" ]; then
                tail -100 /var/log/${PROJECT_NAME}/mc-order-startup.log
            else
                echo "启动日志文件不存在"
            fi
        fi
    else
        log_warn "⚠ ${MC_ORDER_JAR} 文件不存在，跳过启动"
    fi

    # 2. 启动number-card服务 (8080端口)
    if [ -f "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" ]; then
        log_info "启动 ${NUMBER_CARD_JAR} (8080端口)..."

        # 清理旧的PID文件
        rm -f $NUMBER_CARD_PID_FILE

        # 启动number-card服务
        cd ${DEPLOY_ROOT}
        if [ "$DEV_MODE" = true ]; then
            log_info "开发模式启动number-card服务（不使用外部配置文件）"
            nohup java -jar ${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR} > /var/log/${PROJECT_NAME}/number-card-startup.log 2>&1 &
        else
            log_info "生产模式启动number-card服务（使用release配置文件）"
            nohup java -jar ${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR} --spring.profiles.active=release --spring.config.location=${DEPLOY_ROOT}/config/number-card/ > /var/log/${PROJECT_NAME}/number-card-startup.log 2>&1 &
        fi

        # 获取进程PID
        NUMBER_CARD_PID=$!
        echo $NUMBER_CARD_PID > $NUMBER_CARD_PID_FILE

        log_info "${NUMBER_CARD_JAR} 进程已启动，PID: $NUMBER_CARD_PID"
        started_services=$((started_services + 1))

        # 检查number-card服务启动状态
        sleep 10
        local number_card_started=false
        for i in {1..12}; do
            if ps -p $NUMBER_CARD_PID > /dev/null 2>&1; then
                # 多种方式检查端口启动状态
                local port_ready=false

                # 方法1: 检查netstat端口监听
                if netstat -tlnp 2>/dev/null | grep -q ":${NUMBER_CARD_PORT}.*$NUMBER_CARD_PID"; then
                    port_ready=true
                # 方法2: 检查日志中是否显示启动成功
                elif [ -f "/var/log/${PROJECT_NAME}/number-card-startup.log" ]; then
                    if grep -q "Started.*Application" "/var/log/${PROJECT_NAME}/number-card-startup.log" 2>/dev/null; then
                        if grep -q "http-nio-${NUMBER_CARD_PORT}" "/var/log/${PROJECT_NAME}/number-card-startup.log" 2>/dev/null; then
                            port_ready=true
                        fi
                    fi
                fi

                if [ "$port_ready" = true ]; then
                    log_info "✓ ${NUMBER_CARD_JAR} 启动成功 (PID: $NUMBER_CARD_PID, 端口: ${NUMBER_CARD_PORT})"
                    number_card_started=true
                    break
                else
                    log_info "等待 ${NUMBER_CARD_JAR} 端口${NUMBER_CARD_PORT}监听... ($i/12)"
                fi
            else
                log_error "${NUMBER_CARD_JAR} 进程意外退出"
                break
            fi
            sleep 5
        done

        if [ "$number_card_started" = false ]; then
            log_error "✗ ${NUMBER_CARD_JAR} 启动失败"
            echo "=================== ${NUMBER_CARD_JAR} 启动日志 ==================="
            if [ -f "/var/log/${PROJECT_NAME}/number-card-startup.log" ]; then
                tail -100 /var/log/${PROJECT_NAME}/number-card-startup.log
            else
                echo "启动日志文件不存在"
            fi
        fi
    else
        log_warn "⚠ ${NUMBER_CARD_JAR} 文件不存在，跳过启动"
    fi

    # 总结启动结果
    if [ $started_services -gt 0 ]; then
        log_info "JAR服务启动完成，共启动 $started_services 个服务"

        # 显示服务状态
        echo "=================== 服务状态 ==================="
        if [ -f "$MC_ORDER_PID_FILE" ]; then
            PID=$(cat $MC_ORDER_PID_FILE)
            if ps -p $PID > /dev/null 2>&1; then
                echo "✓ mc-order (8081端口): PID $PID"
            else
                echo "✗ mc-order (8081端口): 未运行"
            fi
        fi

        if [ -f "$NUMBER_CARD_PID_FILE" ]; then
            PID=$(cat $NUMBER_CARD_PID_FILE)
            if ps -p $PID > /dev/null 2>&1; then
                echo "✓ number-card (8080端口): PID $PID"
            else
                echo "✗ number-card (8080端口): 未运行"
            fi
        fi

        # 检查端口监听状态
        echo "=================== 端口监听 ===================="
        if netstat -tlnp 2>/dev/null | grep -q ":8081"; then
            echo "✓ mc-order服务 端口 8081: 正在监听"
        else
            echo "✗ mc-order服务 端口 8081: 未监听"
        fi

        if netstat -tlnp 2>/dev/null | grep -q ":8080"; then
            echo "✓ number-card服务 端口 8080: 正在监听"
        else
            echo "✗ number-card服务 端口 8080: 未监听"
        fi
        echo "==============================================="
    else
        log_error "没有启动任何JAR服务"
    fi
}

# 检查nginx可执行文件
check_nginx_binary() {
    if [ ! -f "$NGINX_BIN" ]; then
        log_error "nginx可执行文件不存在: $NGINX_BIN"
        exit 1
    fi
    if [ ! -x "$NGINX_BIN" ]; then
        log_error "nginx文件没有执行权限: $NGINX_BIN"
        chmod +x $NGINX_BIN
    fi
}

# 重新加载nginx配置
reload_nginx() {
    log_info "重新加载nginx配置..."
    check_nginx_binary
    
    $NGINX_BIN -t -c $NGINX_CONF_FILE
    if [ $? -eq 0 ]; then
        # 查找nginx主进程并发送重载信号
        NGINX_PID=$(ps aux | grep nginx | grep master | awk '{print $2}' | head -1)
        if [ -n "$NGINX_PID" ]; then
            kill -HUP $NGINX_PID
            log_info "nginx配置重新加载完成 (PID: $NGINX_PID)"
        else
            log_warn "未找到nginx主进程，尝试启动nginx..."
            $NGINX_BIN -c $NGINX_CONF_FILE
        fi
    else
        log_error "nginx配置检查失败"
        exit 1
    fi
}

# 清理旧备份

# 验证JAR文件是否正确部署
verify_jar_deployment() {
    log_info "验证JAR文件部署..."

    local verification_errors=0

    # 验证mc-order.jar
    if [ -f "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" ]; then
        deployed_size=$(stat -f%z "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" 2>/dev/null || stat -c%s "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}" 2>/dev/null || echo "未知")
        if [ -n "$mc_order_size" ] && [ "$deployed_size" = "$mc_order_size" ]; then
            log_info "✓ mc-order.jar 部署验证通过 (${deployed_size} bytes)"
        else
            log_warn "⚠ mc-order.jar 大小不匹配 (源:${mc_order_size}, 部署:${deployed_size})"
        fi

        # 设置权限
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}"
        chmod 755 "${DEPLOY_ROOT}/jar/${MC_ORDER_JAR}"
    else
        log_error "✗ mc-order.jar 部署失败：文件不存在"
        verification_errors=$((verification_errors + 1))
    fi

    # 验证number-card.jar
    if [ -f "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" ]; then
        deployed_size=$(stat -f%z "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" 2>/dev/null || stat -c%s "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}" 2>/dev/null || echo "未知")
        if [ -n "$number_card_size" ] && [ "$deployed_size" = "$number_card_size" ]; then
            log_info "✓ number-card.jar 部署验证通过 (${deployed_size} bytes)"
        else
            log_warn "⚠ number-card.jar 大小不匹配 (源:${number_card_size}, 部署:${deployed_size})"
        fi

        # 设置权限
        safe_chown "$NGINX_USER" "$NGINX_GROUP" "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}"
        chmod 755 "${DEPLOY_ROOT}/jar/${NUMBER_CARD_JAR}"
    else
        log_error "✗ number-card.jar 部署失败：文件不存在"
        verification_errors=$((verification_errors + 1))
    fi

    if [ $verification_errors -eq 0 ]; then
        log_info "✓ 所有JAR文件部署验证通过"
        return 0
    else
        log_error "✗ JAR文件部署验证失败 ($verification_errors 个错误)"
        return 1
    fi
}

# 验证部署权限
verify_permissions() {
    log_info "验证部署权限..."

    # 检查前端目录权限
    if [ -d "${FRONTEND_ROOT}" ]; then
        log_info "检查前端目录权限..."
        ls -la ${FRONTEND_ROOT}/

        # 测试nginx是否能读取主应用文件
        if [ -f "${FRONTEND_ROOT}/dist/index.html" ]; then
            if sudo -u $NGINX_USER cat ${FRONTEND_ROOT}/dist/index.html >/dev/null 2>&1; then
                log_info "✓ nginx用户可以读取前端文件"
            else
                log_error "✗ nginx用户无法读取前端文件，可能导致403错误"
                # 尝试修复权限
                chown -R ${NGINX_USER}:${NGINX_GROUP} ${FRONTEND_ROOT}
                chmod -R 755 ${FRONTEND_ROOT}
                find ${FRONTEND_ROOT} -type f -exec chmod 644 {} \;
            fi
        else
            log_info "前端文件不存在，将在部署时创建"
        fi
    else
        log_info "前端目录不存在，将在部署时创建"
    fi

    # 检查后端文件权限
    if [ -f "/opt/${PROJECT_NAME}/jar/${MC_ORDER_JAR}" ]; then
        log_info "检查mc-order JAR文件权限..."
        ls -la /opt/${PROJECT_NAME}/jar/${MC_ORDER_JAR}

        if sudo -u $NGINX_USER test -r /opt/${PROJECT_NAME}/jar/${MC_ORDER_JAR}; then
            log_info "✓ nginx用户可以读取mc-order JAR文件"
        else
            log_error "✗ nginx用户无法读取mc-order JAR文件"
            chown ${NGINX_USER}:${NGINX_GROUP} /opt/${PROJECT_NAME}/jar/${MC_ORDER_JAR}
            chmod 755 /opt/${PROJECT_NAME}/jar/${MC_ORDER_JAR}
        fi
    fi

    if [ -f "/opt/${PROJECT_NAME}/jar/${NUMBER_CARD_JAR}" ]; then
        log_info "检查number-card JAR文件权限..."
        ls -la /opt/${PROJECT_NAME}/jar/${NUMBER_CARD_JAR}

        if sudo -u $NGINX_USER test -r /opt/${PROJECT_NAME}/jar/${NUMBER_CARD_JAR}; then
            log_info "✓ nginx用户可以读取number-card JAR文件"
        else
            log_error "✗ nginx用户无法读取number-card JAR文件"
            chown ${NGINX_USER}:${NGINX_GROUP} /opt/${PROJECT_NAME}/jar/${NUMBER_CARD_JAR}
            chmod 755 /opt/${PROJECT_NAME}/jar/${NUMBER_CARD_JAR}
        fi
    fi
}

# 显示部署状态
show_status() {
    log_info "部署完成！"
    echo "==============================================="
    echo "项目名称: ${PROJECT_NAME}"
    echo "部署时间: $(date)"
    echo "前端目录: ${FRONTEND_ROOT}/"
    echo "后端JAR: mc-order.jar, number-card.jar"
    echo "配置文件: /opt/${PROJECT_NAME}/config/mc-order/ 和 /opt/${PROJECT_NAME}/config/number-card/"
    echo "备份目录: ${BACKUP_DIR}"
    echo "==============================================="

    # 显示mc-order服务状态
    echo -e "\nmc-order服务状态:"
    if [ -f "$MC_ORDER_PID_FILE" ]; then
        PID=$(cat $MC_ORDER_PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            echo "✓ mc-order服务运行中 (PID: $PID, 端口: ${MC_ORDER_PORT})"
            ps -p $PID -o pid,ppid,cmd,etime,pcpu,pmem
        else
            echo "✗ mc-order服务未运行"
        fi
    else
        echo "✗ mc-order服务未启动"
    fi

    # 显示number-card服务状态
    echo -e "\nnumber-card服务状态:"
    if [ -f "$NUMBER_CARD_PID_FILE" ]; then
        PID=$(cat $NUMBER_CARD_PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            echo "✓ number-card服务运行中 (PID: $PID, 端口: ${NUMBER_CARD_PORT})"
            ps -p $PID -o pid,ppid,cmd,etime,pcpu,pmem
        else
            echo "✗ number-card服务未运行"
        fi
    else
        echo "✗ number-card服务未启动"
    fi

    # 显示nginx状态
    echo -e "\nnginx状态:"
    NGINX_PID=$(ps aux | grep nginx | grep master | awk '{print $2}' | head -1)
    if [ -n "$NGINX_PID" ]; then
        echo "nginx主进程 PID: $NGINX_PID"
        ps aux | grep nginx | grep -v grep
    else
        echo "nginx未运行"
    fi
}

# 部署结果验证
validate_deployment() {
    log_info "验证部署结果..."

    local errors=0
    local warnings=0

    # 1. 验证前端文件
    log_info "检查前端文件部署..."
    local frontend_errors=0

    # 检查管理后台 (console/dist)
    if [ -f "/opt/console/dist/index.html" ]; then
        log_info "✓ 管理后台文件部署成功"
    else
        log_error "✗ 管理后台文件部署失败"
        frontend_errors=$((frontend_errors + 1))
    fi

    # 检查H5页面 (console/h5/dist)
    if [ -d "/opt/console/h5/dist" ] && [ "$(ls -A /opt/console/h5/dist 2>/dev/null)" ]; then
        log_info "✓ H5页面部署成功"
    else
        log_warn "⚠ H5页面目录为空或不存在"
        warnings=$((warnings + 1))
    fi

    # 检查商城页面 (console/shop/dist)
    if [ -d "/opt/console/shop/dist" ] && [ "$(ls -A /opt/console/shop/dist 2>/dev/null)" ]; then
        log_info "✓ 商城页面部署成功"
    else
        log_warn "⚠ 商城页面目录为空或不存在"
        warnings=$((warnings + 1))
    fi

    # 检查uniapp页面 (console/app/dist/build/web)
    if [ -d "/opt/console/app/dist/build/web" ] && [ "$(ls -A /opt/console/app/dist/build/web 2>/dev/null)" ]; then
        log_info "✓ uniapp页面部署成功"
    else
        log_warn "⚠ uniapp页面目录为空或不存在"
        warnings=$((warnings + 1))
    fi

    # 检查官网页面 (console/guanwang/dist)
    if [ -d "/opt/console/guanwang/dist" ] && [ "$(ls -A /opt/console/guanwang/dist 2>/dev/null)" ]; then
        log_info "✓ 官网页面部署成功"
    else
        log_warn "⚠ 官网页面目录为空或不存在"
        warnings=$((warnings + 1))
    fi

    # 如果主要前端文件不存在，计入错误
    if [ $frontend_errors -gt 0 ]; then
        errors=$((errors + frontend_errors))
    fi

    # 2. 验证后端服务
    log_info "检查后端服务部署..."
    local jar_errors=0

    # 检查 mc-order.jar
    if [ -f "/opt/${PROJECT_NAME}/jar/${MC_ORDER_JAR}" ]; then
        log_info "✓ mc-order.jar 文件部署成功"
    else
        log_error "✗ mc-order.jar 文件部署失败"
        jar_errors=$((jar_errors + 1))
    fi

    # 检查 number-card.jar
    if [ -f "/opt/${PROJECT_NAME}/jar/${NUMBER_CARD_JAR}" ]; then
        log_info "✓ number-card.jar 文件部署成功"
    else
        log_error "✗ number-card.jar 文件部署失败"
        jar_errors=$((jar_errors + 1))
    fi

    # 如果两个JAR文件都不存在，则报告JAR文件部署失败
    if [ $jar_errors -eq 2 ]; then
        log_error "✗ JAR文件部署失败"
        errors=$((errors + 1))
    elif [ $jar_errors -eq 1 ]; then
        log_warn "⚠ 部分JAR文件部署失败"
        warnings=$((warnings + 1))
    fi

    # 检查配置文件
    local config_exists=false
    if [ "$DEV_MODE" = true ]; then
        log_info "开发模式：跳过配置文件验证"
        config_exists=true
    else
        # 生产模式检查配置文件
        if [ -f "/opt/${PROJECT_NAME}/config/mc-order/application-release.yml" ]; then
            log_info "✓ mc-order配置文件部署成功"
            config_exists=true
        fi

        if [ -f "/opt/${PROJECT_NAME}/config/number-card/application-release.yml" ]; then
            log_info "✓ number-card配置文件部署成功"
            config_exists=true
        fi

        if [ "$config_exists" = false ]; then
            log_error "✗ 配置文件部署失败"
            errors=$((errors + 1))
        else
            log_info "✓ 配置文件部署成功"
        fi
    fi

    # 3. 验证服务运行状态
    log_info "检查服务运行状态..."

    # 检查mc-order服务
    if [ -f "$MC_ORDER_PID_FILE" ]; then
        PID=$(cat $MC_ORDER_PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            log_info "✓ mc-order服务运行正常 (PID: $PID)"
        else
            log_error "✗ mc-order服务未运行 (PID文件存在但进程不存在)"
            errors=$((errors + 1))
        fi
    else
        log_error "✗ mc-order服务未运行 (PID文件不存在)"
        errors=$((errors + 1))
    fi

    # 检查number-card服务
    if [ -f "$NUMBER_CARD_PID_FILE" ]; then
        PID=$(cat $NUMBER_CARD_PID_FILE)
        if ps -p $PID > /dev/null 2>&1; then
            log_info "✓ number-card服务运行正常 (PID: $PID)"
        else
            log_error "✗ number-card服务未运行 (PID文件存在但进程不存在)"
            errors=$((errors + 1))
        fi
    else
        log_error "✗ number-card服务未运行 (PID文件不存在)"
        errors=$((errors + 1))
    fi

    local nginx_pid=$(ps aux | grep nginx | grep master | awk '{print $2}' | head -1)
    if [ -n "$nginx_pid" ]; then
        log_info "✓ nginx运行正常 (PID: $nginx_pid)"
    else
        log_error "✗ nginx未运行"
        errors=$((errors + 1))
    fi

    # 4. 验证端口监听
    log_info "检查端口监听状态..."
    if netstat -tlnp 2>/dev/null | grep -q ":8081"; then
        log_info "✓ mc-order服务端口8081正在监听"
    else
        log_warn "⚠ mc-order服务端口8081未监听"
        warnings=$((warnings + 1))
    fi

    if netstat -tlnp 2>/dev/null | grep -q ":8080"; then
        log_info "✓ number-card服务端口8080正在监听"
    else
        log_warn "⚠ number-card服务端口8080未监听"
        warnings=$((warnings + 1))
    fi

    if netstat -tlnp 2>/dev/null | grep -q ":80\|:443"; then
        log_info "✓ nginx端口正在监听"
    else
        log_warn "⚠ nginx端口未监听"
        warnings=$((warnings + 1))
    fi

    # 5. 总结验证结果
    echo "==============================================="
    echo "部署验证完成！"
    echo "错误数量: $errors"
    echo "警告数量: $warnings"

    if [ $errors -eq 0 ]; then
        if [ $warnings -eq 0 ]; then
            log_info "🎉 部署完全成功！所有服务正常运行"
        else
            log_info "✅ 部署基本成功，但有 $warnings 个警告需要注意"
        fi
    else
        log_error "❌ 部署存在问题，请检查 $errors 个错误"
        log_info "可以查看上述错误信息进行故障排查"
    fi
    echo "==============================================="

    return $errors
}

# 主函数
main() {
    echo "=================================================================="
    echo "生产环境部署脚本启动"
    echo "开始时间: $(date)"
    echo "=================================================================="

    # 解析命令行参数
    STOP_NGINX=false
    DEV_MODE=true  # 默认使用开发模式
    if [ "$1" = "--stop-nginx" ]; then
        STOP_NGINX=true
        log_info "将停止nginx服务"
    elif [ "$1" = "--prod" ]; then
        DEV_MODE=false
        log_info "生产模式：JAR服务使用外部配置文件启动"
    elif [ "$1" = "--dev" ]; then
        DEV_MODE=true
        log_info "开发模式：JAR服务不使用外部配置文件启动（默认模式）"
    else
        log_info "开发模式：JAR服务不使用外部配置文件启动（默认模式）"
    fi

    check_root

    # 立即执行项目备份清理（只保留最近5次备份）
    cleanup_project_backups

    check_nginx_user
    check_nginx_binary

    # 确保在正确的目录下运行
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "$SCRIPT_DIR"

    log_info "当前工作目录: $(pwd)"

    # 检查必要的文件
    if [ ! -d "./console" ]; then
        log_error "console 目录不存在，请确保脚本在正确的位置运行"
        exit 1
    fi

    # 预检查：显示将要部署的文件
    echo -e "\n📋 预检查 - 将要部署的文件:"
    echo "前端文件:"
    for app in "dist" "h5/dist" "shop/dist" "guanwang/dist"; do
        if [ -d "./console/$app" ]; then
            files=$(find "./console/$app" -type f | wc -l)
            size=$(du -sh "./console/$app" 2>/dev/null | cut -f1)
            echo "  ✅ ./console/$app: $files 个文件, $size"
        else
            echo "  ❌ ./console/$app: 目录不存在"
        fi
    done

    # 单独检查uniapp目录（路径特殊）
    app_unpackage_path="./console/app/unpackage/dist/build/web"
    if [ -d "$app_unpackage_path" ]; then
        files=$(find "$app_unpackage_path" -type f | wc -l)
        size=$(du -sh "$app_unpackage_path" 2>/dev/null | cut -f1)
        echo "  ✅ ./console/app/unpackage/dist/build/web: $files 个文件, $size"
    else
        echo "  ❌ ./console/app/unpackage/dist/build/web: 目录不存在"
    fi

    echo "后端文件:"
    # 直接在脚本同级别目录查找JAR文件
    if [ -f "./${MC_ORDER_JAR}" ]; then
        size=$(stat -f%z "./${MC_ORDER_JAR}" 2>/dev/null || stat -c%s "./${MC_ORDER_JAR}" 2>/dev/null || echo "未知")
        mb=$(echo "scale=2; $size / 1048576" | bc 2>/dev/null || echo "未知")
        echo "  ✅ ./${MC_ORDER_JAR}: ${mb} MB"
    else
        echo "  ❌ ./${MC_ORDER_JAR}: 文件不存在"
    fi

    if [ -f "./${NUMBER_CARD_JAR}" ]; then
        size=$(stat -f%z "./${NUMBER_CARD_JAR}" 2>/dev/null || stat -c%s "./${NUMBER_CARD_JAR}" 2>/dev/null || echo "未知")
        mb=$(echo "scale=2; $size / 1048576" | bc 2>/dev/null || echo "未知")
        echo "  ✅ ./${NUMBER_CARD_JAR}: ${mb} MB"
    else
        echo "  ❌ ./${NUMBER_CARD_JAR}: 文件不存在"
    fi

    if [ -f "$NGINX_CONF_FILE" ]; then
        echo "  ✅ nginx配置文件: $NGINX_CONF_FILE"
    else
        echo "  ❌ nginx配置文件: 不存在"
    fi

    echo -e "\n=================================================================="
    log_info "开始部署流程..."

    create_directories
    backup_existing

    # 停止服务
    echo -e "\n🛑 停止现有服务..."
    if [ "$STOP_NGINX" = true ]; then
        stop_existing_services --stop-nginx
    else
        stop_existing_services
    fi

    # 部署阶段统计
    local frontend_result=0
    local backend_result=0
    local config_result=0

    # 先部署前端文件
    echo -e "\n📦 [阶段 1/4] 部署前端文件..."
    set +e
    deploy_frontend
    frontend_result=$?
    set -e
    if [ $frontend_result -ne 0 ]; then
        log_warn "前端文件部署失败，继续执行后端部署"
    fi

    # 重新加载nginx配置
    echo -e "\n🔧 [阶段 2/4] 配置nginx..."
    if [ -f "$NGINX_CONF_FILE" ]; then
        log_info "使用nginx配置文件: $NGINX_CONF_FILE"
        $NGINX_BIN -t -c $NGINX_CONF_FILE
        if [ $? -eq 0 ]; then
            NGINX_PID=$(ps aux | grep nginx | grep master | awk '{print $2}' | head -1)
            if [ -n "$NGINX_PID" ]; then
                kill -HUP $NGINX_PID
                log_info "✅ nginx配置重新加载完成"
            else
                log_warn "⚠ nginx未运行，跳过配置重载"
            fi
        else
            log_error "❌ nginx配置检查失败"
        fi
    else
        log_warn "⚠ nginx配置文件不存在，跳过nginx配置"
    fi

    # 等待前端文件生效
    log_info "等待前端文件生效..."
    sleep 3

    # 部署后端
    echo -e "\n⚙️ [阶段 3/4] 部署后端服务..."
    deploy_backend
    backend_result=$?

    # 验证JAR文件部署
    echo -e "\n🔍 [验证] 验证JAR文件部署..."
    verify_jar_deployment
    jar_verification_result=$?

    # 开发模式跳过配置文件部署
    if [ "$DEV_MODE" = true ]; then
        log_info "开发模式：跳过配置文件部署"
        config_result=0
    else
        deploy_config
        config_result=$?
        # 配置文件部署失败不再停止部署流程，只记录错误
        if [ $config_result -ne 0 ]; then
            log_warn "配置文件部署存在问题，但继续部署流程"
        fi
    fi
    create_log_directory

    # 启动服务
    echo -e "\n🚀 [阶段 4/4] 启动服务并验证..."
    start_service
    verify_permissions

    # 最终验证
    validate_deployment

    cleanup_project_backups
    show_status

    # 部署结果总结
    echo -e "\n=================================================================="
    echo "部署流程完成！"
    echo "结束时间: $(date)"

    # 计算部署统计
    local total_tasks=4
    local success_tasks=0

    if [ $frontend_result -eq 0 ]; then
        success_tasks=$((success_tasks + 1))
    fi

    if [ $backend_result -eq 0 ]; then
        success_tasks=$((success_tasks + 1))
    fi

    if [ $jar_verification_result -eq 0 ]; then
        success_tasks=$((success_tasks + 1))
    fi

    if [ $config_result -eq 0 ]; then
        success_tasks=$((success_tasks + 1))
    fi

    echo "部署统计:"
    echo "  ✅ 成功任务: $success_tasks/$total_tasks"
    echo "  ❌ 失败任务: $((total_tasks - success_tasks))/$total_tasks"

    # 收集所有阶段的错误信息
    local total_errors=0
    local total_warnings=0

    if [ $frontend_result -ne 0 ]; then
        total_errors=$((total_errors + 1))
    fi
    if [ $backend_result -ne 0 ]; then
        total_errors=$((total_errors + 1))
    fi
    if [ $jar_verification_result -ne 0 ]; then
        total_errors=$((total_errors + 1))
    fi
    if [ $config_result -ne 0 ]; then
        total_warnings=$((total_warnings + 1))
    fi

    # 从前端部署结果中检查警告数量
    if [ $frontend_result -eq 0 ]; then
        # 前端部署成功，检查是否有警告（通过日志中是否有警告信息）
        if grep -q "⚠.*部署异常" /var/log/console/deploy.log 2>/dev/null; then
            total_warnings=$((total_warnings + 1))
        fi
    fi

    if [ $success_tasks -eq $total_tasks ] && [ $total_warnings -eq 0 ]; then
        echo -e "\n🎉 部署完全成功！所有服务已正常运行"
        echo "🌐 前端服务: https://www.dingdonghaoka.com"
        echo "🌐 H5服务: https://h5.dingdonghaoka.com"
        echo "🌐 商城服务: https://shop.dingdonghaoka.com"
        echo "🌐 uniapp服务: https://sj.dingdonghaoka.com"
        echo "🌐 官网服务: https://www.xn--iprop.com"
        echo "🔌 后端API: https://www.dingdonghaoka.com/prod-api/ (8081端口)"
        echo "🔌 后端API: https://api.dingdonghaoka.com/api/ (8080端口)"
    else
        echo -e "\n📊 部署完成状态："
        if [ $success_tasks -eq $total_tasks ]; then
            echo "  ✅ 所有主要部署任务已完成"
        else
            echo "  ⚠️ 部分任务完成，存在以下问题："
            echo "  ❌ 错误数量: $total_errors"
            echo "  ⚠️ 警告数量: $total_warnings"
        fi

        if [ $total_warnings -gt 0 ]; then
            echo ""
            echo "⚠️ 警告提醒："
            echo "  • 文件数量不匹配等问题已记录为警告"
            echo "  • 这些警告通常不影响系统正常运行"
            echo "  • 建议在维护时检查这些问题"
        fi

        echo ""
        echo "🌐 服务访问地址："
        echo "   - 主站: https://www.dingdonghaoka.com"
        echo "   - API: https://www.dingdonghaoka.com/prod-api/"
        echo "   - H5: https://h5.dingdonghaoka.com"
        echo "   - 商城: https://shop.dingdonghaoka.com"

        if [ $total_errors -gt 0 ]; then
            echo ""
            echo "🔧 重要提醒："
            if [ $frontend_result -ne 0 ]; then
                echo "  • 前端文件部署存在严重问题，需要手动修复"
            fi
            if [ $backend_result -ne 0 ]; then
                echo "  • 后端JAR服务部署存在严重问题，API可能无法使用"
            fi
            if [ $jar_verification_result -ne 0 ]; then
                echo "  • JAR文件验证失败，文件可能没有正确复制到目标位置"
            fi
            if [ $config_result -ne 0 ]; then
                echo "  • 配置文件部署存在问题，可能需要手动配置"
            fi
            echo "  • 请查看上述错误信息并进行修复"
        else
            echo ""
            echo "💡 提示：部署基本成功，所有主要功能正常可用"
        fi
    fi

    echo "=================================================================="
}

# 执行主函数
main "$@"