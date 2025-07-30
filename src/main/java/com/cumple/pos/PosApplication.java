package com.cumple.pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;

@SpringBootApplication(exclude = LoadBalancerAutoConfiguration.class)
public class PosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosApplication.class, args);
	}

}
