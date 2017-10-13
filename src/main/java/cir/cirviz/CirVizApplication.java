package cir.cirviz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class CirVizApplication {

  public static void main(String[] args) {
    SpringApplication.run(CirVizApplication.class, args);
  }
}
