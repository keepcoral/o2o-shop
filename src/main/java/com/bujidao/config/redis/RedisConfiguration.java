package com.bujidao.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bujidao.cache.JedisPoolWriper;
import com.bujidao.cache.JedisUtil;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
	@Value("${redis.hostname}")
	private String hostname;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.pool.maxActive}")
	private int maxTotal;
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	@Value("${redis.pool.maxWait}")
	private long maxWaitMillis;
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${redis.password}")
	private String redisPassword;
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	@Autowired
	private JedisPoolWriper jedisPoolWriper;
	@Autowired
	private JedisUtil jedisUtil;
	
	/**
	 * 创建redis连接池的设置
	 */
	@Bean(name="jedisPoolConfig")
	public JedisPoolConfig createJedisPoolConfig(){
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
//		最多空闲连接 
		jedisPoolConfig.setMaxIdle(maxIdle);
//		控制一个pool可分配多少个jedis实例
		jedisPoolConfig.setMaxTotal(maxTotal);
//		最大等待时间，当没有可用连接时，超过该时间则抛出异常
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//		获取连接时检查有效性
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
	}
	
	/**
	 * 创建redis连接池并做好相关的配置
	 * @return
	 */
	@Bean(name="jedisPoolWriper")
	public JedisPoolWriper createJedisPoolWriper(){
		JedisPoolWriper jedisPoolWriper=
				new JedisPoolWriper(jedisPoolConfig, hostname, port,10000,redisPassword);
		return jedisPoolWriper;
	}
	
	/**
	 * 创建redis的工具类，封装好redis的连接以及相关操作
	 * @return
	 */
	@Bean(name="jedisUtil")
	public JedisUtil createJedisUtil(){
		JedisUtil jedisUtil=new JedisUtil();
		jedisUtil.setJedisPool(jedisPoolWriper);
		return jedisUtil;
	}
	
	/**
	 * redis中Keys的操作
	 * @return
	 */
	@Bean(name="jedisKeys")
	public JedisUtil.Keys createJedisKeys(){
		JedisUtil.Keys jedisKeys=jedisUtil.new Keys();
		return jedisKeys;
	}
	
	/**
	 * redis中Strings的操作
	 * @return
	 */
	@Bean(name="jedisStrings")
	public JedisUtil.Strings createJedisStrings(){
		JedisUtil.Strings jedisStrings=jedisUtil.new Strings();
		return jedisStrings;
	}
	
}
