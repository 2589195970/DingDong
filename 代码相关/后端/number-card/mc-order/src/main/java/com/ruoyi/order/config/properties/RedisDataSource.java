package com.ruoyi.order.config.properties;

import lombok.Data;

/**
 * @Description
 */
@Data
public class RedisDataSource {
	private Integer database;
	private String host;
	private Integer port;
	private String password;
	private Long timeout;
	private String username;
}
