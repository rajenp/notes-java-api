package rajendrapatil.api.notes;

import java.net.URISyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import rajendrapatil.api.redis.HerokuRedisConnector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootApplication
@CrossOrigin(origins = {"http://localhost:3000", "https://rajendrapatil.herokuapp.com"})
public class Main {

  @Bean
  ProtobufHttpMessageConverter protobufHttpMessageConverter() {
    return new ProtobufHttpMessageConverter();
  }

  @Bean
  ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter() {
    return new ProtobufJsonFormatHttpMessageConverter();
  }


  @Bean(name = "pool")
  JedisPool jedisPool() throws URISyntaxException {
    return HerokuRedisConnector.getPool();
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}