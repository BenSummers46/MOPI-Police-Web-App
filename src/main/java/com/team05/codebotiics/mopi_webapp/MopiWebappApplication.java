package com.team05.codebotiics.mopi_webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.team05.codebotiics.mopi_webapp"})
public class MopiWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MopiWebappApplication.class, args);
	}

}
