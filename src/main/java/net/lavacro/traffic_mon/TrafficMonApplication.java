package net.lavacro.traffic_mon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrafficMonApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrafficMonApplication.class, args);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {}
	}

}
