package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.order.entity.PhotoDefaultConfig;
import com.ruoyi.common.order.vo.PhotoDefaultConfigVO;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.service.IPhotoDefaultConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 照片默认配置Controller
 *
 * @author 陈思伟
 * @date 2024/12/12
 */
@Api(tags = "照片默认配置管理")
@RestController
@RequestMapping("/system/photoConfig")
public class PhotoDefaultConfigController extends BaseController
{
    @Autowired
    private IPhotoDefaultConfigService photoDefaultConfigService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询照片默认配置列表
     */
    @ApiOperation("查询照片默认配置列表")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(PhotoDefaultConfig photoDefaultConfig)
    {
        startPage();
        List<PhotoDefaultConfigVO> list = photoDefaultConfigService.selectPhotoDefaultConfigList(photoDefaultConfig);
        return getDataTable(list);
    }

    /**
     * 获取照片默认配置详细信息
     */
    @ApiOperation("获取照片默认配置详细信息")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@ApiParam("配置ID") @PathVariable("configId") Long configId)
    {
        return success(photoDefaultConfigService.selectPhotoDefaultConfigByConfigId(configId));
    }

    /**
     * 根据配置名称查询配置
     */
    @ApiOperation("根据配置名称查询配置")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:query')")
    @GetMapping(value = "/name/{configName}")
    public AjaxResult getInfoByName(@ApiParam("配置名称") @PathVariable("configName") String configName)
    {
        return success(photoDefaultConfigService.selectPhotoDefaultConfigByConfigName(configName));
    }

    /**
     * 新增照片默认配置
     */
    @ApiOperation("新增照片默认配置")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:add')")
    @Log(title = "照片默认配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PhotoDefaultConfig photoDefaultConfig)
    {
        photoDefaultConfig.setCreateBy(SecurityUtils.getUsername());
        return toAjax(photoDefaultConfigService.insertPhotoDefaultConfig(photoDefaultConfig));
    }

    /**
     * 修改照片默认配置
     */
    @ApiOperation("修改照片默认配置")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:edit')")
    @Log(title = "照片默认配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PhotoDefaultConfig photoDefaultConfig)
    {
        photoDefaultConfig.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(photoDefaultConfigService.updatePhotoDefaultConfig(photoDefaultConfig));
    }

    /**
     * 删除照片默认配置
     */
    @ApiOperation("删除照片默认配置")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:remove')")
    @Log(title = "照片默认配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@ApiParam("配置ID数组") @PathVariable Long[] configIds)
    {
        return toAjax(photoDefaultConfigService.deletePhotoDefaultConfigByConfigIds(configIds));
    }

    /**
     * 更新配置状态
     */
    @ApiOperation("更新配置状态")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:edit')")
    @Log(title = "照片默认配置", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult updateStatus(@ApiParam("配置ID") @RequestParam Long configId,
                                  @ApiParam("状态 0-禁用 1-启用") @RequestParam Integer isActive)
    {
        return toAjax(photoDefaultConfigService.updateConfigStatus(configId, isActive));
    }

    /**
     * 查询启用的默认模板配置列表
     */
    @ApiOperation("查询启用的默认模板配置列表")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:list')")
    @GetMapping("/defaultTemplates")
    public AjaxResult getDefaultTemplates()
    {
        List<PhotoDefaultConfigVO> list = photoDefaultConfigService.selectDefaultTemplateConfigs();
        return success(list);
    }

    /**
     * 根据配置类型查询配置列表
     */
    @ApiOperation("根据配置类型查询配置列表")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:list')")
    @GetMapping("/type/{configType}")
    public AjaxResult getConfigsByType(@ApiParam("配置类型 1-默认模板 2-自定义模板") @PathVariable("configType") Integer configType,
                                       @ApiParam("启用状态") @RequestParam(required = false) Integer isActive)
    {
        List<PhotoDefaultConfigVO> list = photoDefaultConfigService.selectConfigsByType(configType, isActive);
        return success(list);
    }

    /**
     * 复制配置
     */
    @ApiOperation("复制配置")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:add')")
    @Log(title = "照片默认配置", businessType = BusinessType.INSERT)
    @PostMapping("/copy/{sourceConfigId}")
    public AjaxResult copyConfig(@ApiParam("源配置ID") @PathVariable("sourceConfigId") Long sourceConfigId,
                                @ApiParam("新配置名称") @RequestParam String newConfigName)
    {
        String operator = SecurityUtils.getUsername();
        PhotoDefaultConfigVO newConfig = photoDefaultConfigService.copyConfig(sourceConfigId, newConfigName, operator);
        return success(newConfig);
    }

    /**
     * 校验配置名称是否唯一
     */
    @ApiOperation("校验配置名称是否唯一")
    @GetMapping("/checkConfigNameUnique")
    public AjaxResult checkConfigNameUnique(@ApiParam("配置名称") @RequestParam String configName,
                                           @ApiParam("配置ID") @RequestParam(required = false) Long configId)
    {
        PhotoDefaultConfig photoDefaultConfig = new PhotoDefaultConfig();
        photoDefaultConfig.setConfigName(configName);
        photoDefaultConfig.setConfigId(configId);
        boolean unique = photoDefaultConfigService.checkConfigNameUnique(photoDefaultConfig);
        return success(unique);
    }


    /**
     * 上传示例图片
     */
    @ApiOperation("上传示例图片")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:upload')")
    @Log(title = "照片默认配置", businessType = BusinessType.INSERT)
    @PostMapping("/uploadExample")
    public AjaxResult uploadExampleImage(@ApiParam("示例图片文件") @RequestParam("file") MultipartFile file)
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath() + "/photoExample";
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("originalFilename", file.getOriginalFilename());
            ajax.put("size", file.getSize());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error("上传示例图片失败：" + e.getMessage());
        }
    }

    /**
     * 删除示例图片
     */
    @ApiOperation("删除示例图片")
    @PreAuthorize("@ss.hasPermi('system:photoConfig:upload')")
    @Log(title = "照片默认配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteExample")
    public AjaxResult deleteExampleImage(@ApiParam("文件名") @RequestParam String fileName)
    {
        try
        {
            String filePath = RuoYiConfig.getProfile() + "/upload/photoExample/" + fileName;
            java.io.File file = new java.io.File(filePath);
            if (file.exists() && file.delete())
            {
                return AjaxResult.success("删除成功");
            }
            else
            {
                return AjaxResult.error("文件不存在或删除失败");
            }
        }
        catch (Exception e)
        {
            return AjaxResult.error("删除示例图片失败：" + e.getMessage());
        }
    }
}