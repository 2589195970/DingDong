-- 照片默认配置表
-- 用于存储不同类型的照片配置模板，支持JSON格式的配置项
-- 创建时间：2024-12-12
-- 作者：陈思伟

DROP TABLE IF EXISTS `t_photo_default_config`;

CREATE TABLE `t_photo_default_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) NOT NULL COMMENT '配置名称',
  `config_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '配置类型 1-默认模板 2-自定义模板',
  `photo_config` text COMMENT '照片配置JSON',
  `description` varchar(500) DEFAULT NULL COMMENT '配置描述',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 0-禁用 1-启用',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_config_name` (`config_name`),
  KEY `idx_config_type` (`config_type`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='照片默认配置表';

-- 插入示例数据
INSERT INTO `t_photo_default_config` (`config_name`, `config_type`, `photo_config`, `description`, `is_active`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('身份证照片配置', 1, '[
  {
    "photoType": 1,
    "title": "身份证正面",
    "description": "请上传身份证正面照片，确保证件信息清晰可见",
    "exampleUrl": "https://example.com/id-front.jpg",
    "maxSize": 5,
    "required": true,
    "tips": "请确保照片完整，无遮挡，四角齐全"
  },
  {
    "photoType": 2,
    "title": "身份证反面",
    "description": "请上传身份证反面照片，确保证件信息清晰可见",
    "exampleUrl": "https://example.com/id-back.jpg",
    "maxSize": 5,
    "required": true,
    "tips": "请确保照片完整，无遮挡，国徽面清晰"
  }
]', '身份证正反面照片配置模板', 1, 'admin', NOW(), 'admin', NOW(), '默认身份证照片配置'),
('免冠照片配置', 1, '[
  {
    "photoType": 3,
    "title": "免冠照片",
    "description": "请上传白色背景免冠照片",
    "exampleUrl": "https://example.com/portrait.jpg",
    "maxSize": 3,
    "required": true,
    "tips": "照片尺寸为358x441像素，白色背景，免冠"
  }
]', '免冠证件照配置模板', 1, 'admin', NOW(), 'admin', NOW(), '标准免冠照片配置'),
('银行卡照片配置', 1, '[
  {
    "photoType": 4,
    "title": "银行卡正面",
    "description": "请上传银行卡正面照片",
    "exampleUrl": "https://example.com/bank-card.jpg",
    "maxSize": 5,
    "required": true,
    "tips": "请确保银行卡号清晰可见"
  }
]', '银行卡照片配置模板', 1, 'admin', NOW(), 'admin', NOW(), '银行卡照片配置'),
('完整实名认证配置', 2, '[
  {
    "photoType": 1,
    "title": "身份证正面",
    "description": "请上传身份证正面照片，确保证件信息清晰可见",
    "exampleUrl": "https://example.com/id-front.jpg",
    "maxSize": 5,
    "required": true,
    "tips": "请确保照片完整，无遮挡，四角齐全"
  },
  {
    "photoType": 2,
    "title": "身份证反面",
    "description": "请上传身份证反面照片，确保证件信息清晰可见",
    "exampleUrl": "https://example.com/id-back.jpg",
    "maxSize": 5,
    "required": true,
    "tips": "请确保照片完整，无遮挡，国徽面清晰"
  },
  {
    "photoType": 3,
    "title": "免冠照片",
    "description": "请上传白色背景免冠照片",
    "exampleUrl": "https://example.com/portrait.jpg",
    "maxSize": 3,
    "required": true,
    "tips": "照片尺寸为358x441像素，白色背景，免冠"
  },
  {
    "photoType": 4,
    "title": "银行卡正面",
    "description": "请上传银行卡正面照片",
    "exampleUrl": "https://example.com/bank-card.jpg",
    "maxSize": 5,
    "required": false,
    "tips": "请确保银行卡号清晰可见"
  }
]', '完整实名认证所需照片配置', 1, 'admin', NOW(), 'admin', NOW(), '包含所有实名认证需要的照片类型');