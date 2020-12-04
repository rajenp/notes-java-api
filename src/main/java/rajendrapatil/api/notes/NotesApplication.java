package rajendrapatil.api.notes;

import java.net.URISyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import rajendrapatil.api.redis.HerokuRedisConnector;
import redis.clients.jedis.Jedis;

@SpringBootApplication
public class NotesApplication {

  @Bean
  ProtobufHttpMessageConverter protobufHttpMessageConverter() {
    return new ProtobufHttpMessageConverter();
  }

  @Bean
  ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter() {
    return new ProtobufJsonFormatHttpMessageConverter();
  }


  @Bean(name = "connection")
  Jedis jedis() throws URISyntaxException {
    return HerokuRedisConnector.getPool().getResource();
  }

  public static void main(String[] args) {
    SpringApplication.run(NotesApplication.class, args);
  }
}