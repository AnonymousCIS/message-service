package org.anonymous;

import com.netflix.discovery.EurekaClient;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageServiceApplication {
	@Autowired
	private EurekaClient eurekaClient;

	@PreDestroy
	public void unregister() {
		eurekaClient.shutdown();
	}
	public static void main(String[] args) {
		SpringApplication.run(MessageServiceApplication.class, args);
	}

}
