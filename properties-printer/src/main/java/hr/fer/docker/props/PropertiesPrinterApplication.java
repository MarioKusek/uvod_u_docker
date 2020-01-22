package hr.fer.docker.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class PropertiesPrinterApplication implements CommandLineRunner {
  @Value("${some-test.var}")
  private String propValue;

  @Value("${iotlab.admin_app.url}")
  private String url;


  public static void main(String[] args) {
    SpringApplication.run(PropertiesPrinterApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("prop: {}", propValue);
    log.info("prop: {}", url);
  }
}
