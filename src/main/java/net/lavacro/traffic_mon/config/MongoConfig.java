package net.lavacro.traffic_mon.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.mongodb")
@Getter
@Setter
public class MongoConfig {
	private String uri;
	private String database;
	private String collection;

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(uri);
	}
}
