package rajendrapatil.api.redis;

import java.net.URI;
import java.net.URISyntaxException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class HerokuRedisConnector {
  public static JedisPool getPool() throws URISyntaxException {
    URI redisURI = new URI(System.getenv("REDIS_URL"));
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(10);
    poolConfig.setMaxIdle(5);
    poolConfig.setMinIdle(1);
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    poolConfig.setTestWhileIdle(true);
    JedisPool pool = new JedisPool(poolConfig, redisURI);
    return pool;
  }
}
