package com.example.message;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MessageApplication {

	private static final Logger logger = LoggerFactory.getLogger(MessageApplication.class);

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(MessageApplication.class, args);
	}

	@PostConstruct
	public void logPortNumber() {
		String serverPort = env.getProperty("server.port");
		logger.info("MMQS Server started. Listening on port " + serverPort);
	}
}
