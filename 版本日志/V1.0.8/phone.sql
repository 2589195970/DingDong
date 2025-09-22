##新增订单佣金说明字段 @陈思伟20250712
ALTER TABLE t_order ADD `order_commission_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单佣金说明' after order_commission_status;

ALTER TABLE t_order_commission ADD `order_commission_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单佣金说明' after order_commission_status;

ALTER TABLE t_order_commission ADD `downstream_father_list` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '下游代理商归属列表 json格式 记录订单各级代理及佣金信息' after downstream_agent_name;




UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1118144808512487424') WHERE order_id='1118144808512487424';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1118605205363326976') WHERE order_id='1118605205363326976';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1119383367814762496') WHERE order_id='1119383367814762496';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1120086251049115648') WHERE order_id='1120086251049115648';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1118678278775078912') WHERE order_id='1118678278775078912';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1122219682310561792') WHERE order_id='1122219682310561792';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1129002315573010432') WHERE order_id='1129002315573010432';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1129001937523613696') WHERE order_id='1129001937523613696';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1129753154302418944') WHERE order_id='1129753154302418944';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1130544486327427072') WHERE order_id='1130544486327427072';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1130542965107892224') WHERE order_id='1130542965107892224';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1130538265558032384') WHERE order_id='1130538265558032384';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1130534896936329216') WHERE order_id='1130534896936329216';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1130535409081819136') WHERE order_id='1130535409081819136';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1131968653304381440') WHERE order_id='1131968653304381440';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1131991422473715712') WHERE order_id='1131991422473715712';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1131938603616026624') WHERE order_id='1131938603616026624';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1132716468225818624') WHERE order_id='1132716468225818624';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1137093500166537216') WHERE order_id='1137093500166537216';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1137093119126601728') WHERE order_id='1137093119126601728';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1125095026126397440') WHERE order_id='1125095026126397440';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1122242896478916608') WHERE order_id='1122242896478916608';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1120115548614877184') WHERE order_id='1120115548614877184';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1119403430538137600') WHERE order_id='1119403430538137600';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1118234379648598016') WHERE order_id='1118234379648598016';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1118158018166751232') WHERE order_id='1118158018166751232';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1118158867068715008') WHERE order_id='1118158867068715008';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1121879168721936384') WHERE order_id='1121879168721936384';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1125051568464502784') WHERE order_id='1125051568464502784';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1123753866455957504') WHERE order_id='1123753866455957504';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1123704409282592768') WHERE order_id='1123704409282592768';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1123703823560622080') WHERE order_id='1123703823560622080';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1123645973392015360') WHERE order_id='1123645973392015360';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1123603298903146496') WHERE order_id='1123603298903146496';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1123595240609169408') WHERE order_id='1123595240609169408';
UPDATE t_order_commission SET downstream_father_list = (SELECT downstream_father_list FROM t_order WHERE order_id = '1123316925386211328') WHERE order_id='1123316925386211328';



UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1118144808512487424') WHERE order_id='1118144808512487424';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1118605205363326976') WHERE order_id='1118605205363326976';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1119383367814762496') WHERE order_id='1119383367814762496';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1120086251049115648') WHERE order_id='1120086251049115648';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1118678278775078912') WHERE order_id='1118678278775078912';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1122219682310561792') WHERE order_id='1122219682310561792';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1129002315573010432') WHERE order_id='1129002315573010432';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1129001937523613696') WHERE order_id='1129001937523613696';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1129753154302418944') WHERE order_id='1129753154302418944';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1130544486327427072') WHERE order_id='1130544486327427072';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1130542965107892224') WHERE order_id='1130542965107892224';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1130538265558032384') WHERE order_id='1130538265558032384';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1130534896936329216') WHERE order_id='1130534896936329216';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1130535409081819136') WHERE order_id='1130535409081819136';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1131968653304381440') WHERE order_id='1131968653304381440';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1131991422473715712') WHERE order_id='1131991422473715712';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1131938603616026624') WHERE order_id='1131938603616026624';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1132716468225818624') WHERE order_id='1132716468225818624';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1137093500166537216') WHERE order_id='1137093500166537216';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1137093119126601728') WHERE order_id='1137093119126601728';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1125095026126397440') WHERE order_id='1125095026126397440';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1122242896478916608') WHERE order_id='1122242896478916608';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1120115548614877184') WHERE order_id='1120115548614877184';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1119403430538137600') WHERE order_id='1119403430538137600';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1118234379648598016') WHERE order_id='1118234379648598016';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1118158018166751232') WHERE order_id='1118158018166751232';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1125051568464502784') WHERE order_id='1125051568464502784';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1123753866455957504') WHERE order_id='1123753866455957504';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1123704409282592768') WHERE order_id='1123704409282592768';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1123703823560622080') WHERE order_id='1123703823560622080';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1123645973392015360') WHERE order_id='1123645973392015360';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1123603298903146496') WHERE order_id='1123603298903146496';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1123595240609169408') WHERE order_id='1123595240609169408';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1123316925386211328') WHERE order_id='1123316925386211328';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1118158867068715008') WHERE order_id='1118158867068715008';
UPDATE t_order SET order_commission_status = (SELECT order_commission_status FROM t_order_commission WHERE order_id = '1121879168721936384') WHERE order_id='1121879168721936384';