package com.bujidao.cache;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * 创建redis连接池，并做相关配置
 *
 */
public class JedisPoolWriper {
	private JedisPool jedisPool;
	@Value("${redis.password}")
	private String redisPassword;
	public JedisPoolWriper(final JedisPoolConfig config,
				final String host,final int port,final int timeout,final String redisPassword){
		try{
			jedisPool=new JedisPool(config,host,port,timeout,redisPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
}
