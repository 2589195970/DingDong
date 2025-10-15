-- 订单照片审核功能数据库升级脚本
-- 版本: V2.1.3
-- 作者: 陈思伟
-- 日期: 2024/12/12

-- 1. 为订单表添加照片相关字段
ALTER TABLE t_order ADD COLUMN `id_card_front_url` VARCHAR(500) DEFAULT '' COMMENT '身份证正面照片URL';
ALTER TABLE t_order ADD COLUMN `id_card_back_url` VARCHAR(500) DEFAULT '' COMMENT '身份证反面照片URL';
ALTER TABLE t_order ADD COLUMN `person_photo_url` VARCHAR(500) DEFAULT '' COMMENT '免冠照片URL';
ALTER TABLE t_order ADD COLUMN `custom_photo_url` VARCHAR(500) DEFAULT '' COMMENT '自定义照片URL';

ALTER TABLE t_order ADD COLUMN `photo_status` TINYINT(1) DEFAULT 0 COMMENT '照片审核状态 0 无需审核 1 待上传照片 2 代理商待提交 3 管理员待审核 4 审核通过 5 审核拒绝';
ALTER TABLE t_order ADD COLUMN `photo_upload_time` BIGINT(20) DEFAULT 0 COMMENT '照片上传时间';
ALTER TABLE t_order ADD COLUMN `photo_audit_user_id` BIGINT(20) DEFAULT 0 COMMENT '照片审核员ID';
ALTER TABLE t_order ADD COLUMN `photo_audit_time` BIGINT(20) DEFAULT 0 COMMENT '照片审核时间';
ALTER TABLE t_order ADD COLUMN `photo_audit_remark` VARCHAR(500) DEFAULT '' COMMENT '照片审核备注';

-- 2. 为照片相关字段添加索引（提高查询性能）
CREATE INDEX idx_order_photo_status ON t_order(`photo_status`);
CREATE INDEX idx_order_photo_audit_time ON t_order(`photo_audit_time`);

-- 3. 为现有需要照片审核的商品初始化状态
-- 注意：此步骤需要根据实际的商品表结构进行调整
-- UPDATE t_order o
-- LEFT JOIN t_product p ON o.product_code = p.product_code
-- SET o.photo_status = 1
-- WHERE p.photo_required = 1 AND o.photo_status = 0;

-- 4. 插入升级记录（如果存在版本管理表）
-- INSERT INTO t_database_version (version, description, create_time)
-- VALUES ('V2.1.3', '订单照片审核功能', UNIX_TIMESTAMP() * 1000);

-- 5. 创建视图，方便查询需要照片审核的订单
CREATE OR REPLACE VIEW v_order_photo_audit AS
SELECT
    o.order_id,
    o.order_downstream_id,
    o.card_name,
    o.card_phone,
    o.product_name,
    o.photo_status,
    CASE o.photo_status
        WHEN 0 THEN '无需审核'
        WHEN 1 THEN '待上传照片'
        WHEN 2 THEN '代理商待提交'
        WHEN 3 THEN '管理员待审核'
        WHEN 4 THEN '审核通过'
        WHEN 5 THEN '审核拒绝'
        ELSE '未知状态'
    END AS photo_status_name,
    o.id_card_front_url,
    o.id_card_back_url,
    o.person_photo_url,
    o.custom_photo_url,
    o.photo_upload_time,
    o.photo_audit_user_id,
    o.photo_audit_time,
    o.photo_audit_remark,
    o.downstream_name,
    o.create_time,
    o.order_status
FROM t_order o
WHERE o.photo_status IN (1, 2, 3, 5) -- 只显示需要操作的状态
ORDER BY o.create_time DESC;

-- 6. 创建存储过程，用于批量更新照片审核状态
DELIMITER $$

CREATE PROCEDURE `sp_batch_photo_audit`(
    IN p_order_ids TEXT,
    IN p_audit_action INT,
    IN p_audit_remark VARCHAR(500),
    IN p_audit_user_id BIGINT,
    OUT p_result INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;
    DECLARE v_order_id BIGINT;
    DECLARE v_pos INT DEFAULT 1;
    DECLARE v_str_len INT;

    -- 结果代码：0-成功，1-失败
    SET p_result = 0;

    -- 检查参数有效性
    IF p_order_ids IS NULL OR p_order_ids = '' THEN
        SET p_result = 1;
        SELECT '订单ID列表不能为空' AS error_message;
    ELSEIF p_audit_action NOT IN (1, 2) THEN
        SET p_result = 1;
        SELECT '审核操作必须是1(通过)或2(拒绝)' AS error_message;
    ELSE
        -- 开始事务
        START TRANSACTION;

        -- 循环处理每个订单ID
        SET v_str_len = CHAR_LENGTH(p_order_ids);

        WHILE v_pos <= v_str_len DO
            -- 提取单个订单ID
            SET v_order_id = CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(p_order_ids, ',', v_pos), ',', -1) AS UNSIGNED);

            -- 更新订单照片审核状态
            IF p_audit_action = 1 THEN
                -- 审核通过
                UPDATE t_order
                SET photo_status = 4,
                    photo_audit_user_id = p_audit_user_id,
                    photo_audit_time = UNIX_TIMESTAMP() * 1000,
                    photo_audit_remark = p_audit_remark,
                    update_time = UNIX_TIMESTAMP() * 1000
                WHERE order_id = v_order_id AND photo_status = 3;
            ELSE
                -- 审核拒绝
                UPDATE t_order
                SET photo_status = 5,
                    photo_audit_user_id = p_audit_user_id,
                    photo_audit_time = UNIX_TIMESTAMP() * 1000,
                    photo_audit_remark = p_audit_remark,
                    update_time = UNIX_TIMESTAMP() * 1000
                WHERE order_id = v_order_id AND photo_status = 3;
            END IF;

            -- 检查是否更新成功
            IF ROW_COUNT() > 0 THEN
                SET v_count = v_count + 1;

                -- 插入订单日志
                INSERT INTO t_order_log (order_id, order_log, remark, create_time)
                VALUES (v_order_id,
                       IF(p_audit_action = 1, '照片审核通过', '照片审核拒绝'),
                       CONCAT('批量审核，备注：', p_audit_remark),
                       UNIX_TIMESTAMP() * 1000);
            END IF;

            SET v_pos = v_pos + 1;
        END WHILE;

        -- 提交事务
        COMMIT;

        -- 返回处理结果
        SELECT CONCAT('成功处理 ', v_count, ' 个订单') AS result_message;
    END IF;
END$$

DELIMITER ;

-- 7. 添加权限数据（如果存在权限系统）
-- 管理员照片审核权限
-- INSERT INTO t_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
-- VALUES ('照片审核', (SELECT menu_id FROM t_menu WHERE menu_name = '订单管理' LIMIT 1), 6, 'photoAudit', 'business/orders/photoAudit', '', 1, 0, 'C', '0', '0', 'business:orders:photoAudit', 'picture', 'admin', UNIX_TIMESTAMP() * 1000, '', NULL, '照片审核菜单');

-- 照片审核操作权限
-- INSERT INTO t_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
-- VALUES ('照片审核详情', (SELECT menu_id FROM t_menu WHERE menu_name = '照片审核' LIMIT 1), 1, '#', '', '', 1, 0, 'F', '0', '0', 'business:orders:photoAudit:detail', '#', 'admin', UNIX_TIMESTAMP() * 1000, '', NULL, '照片审核详情权限');

-- INSERT INTO t_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
-- VALUES ('照片审核操作', (SELECT menu_id FROM t_menu WHERE menu_name = '照片审核' LIMIT 1), 2, '#', '', '', 1, 0, 'F', '0', '0', 'business:orders:photoAudit:audit', '#', 'admin', UNIX_TIMESTAMP() * 1000, '', NULL, '照片审核操作权限');

-- 8. 创建统计视图，用于照片审核数据统计
CREATE OR REPLACE VIEW v_photo_audit_statistics AS
SELECT
    photo_status,
    CASE photo_status
        WHEN 0 THEN '无需审核'
        WHEN 1 THEN '待上传照片'
        WHEN 2 THEN '代理商待提交'
        WHEN 3 THEN '管理员待审核'
        WHEN 4 THEN '审核通过'
        WHEN 5 THEN '审核拒绝'
        ELSE '未知状态'
    END AS photo_status_name,
    COUNT(*) AS order_count,
    COUNT(DISTINCT downstream_code) AS agent_count,
    MIN(create_time) AS earliest_create_time,
    MAX(create_time) AS latest_create_time
FROM t_order
WHERE photo_status IS NOT NULL
GROUP BY photo_status
ORDER BY photo_status;

-- 脚本执行完成提示
SELECT '订单照片审核功能数据库升级脚本执行完成！' AS message;