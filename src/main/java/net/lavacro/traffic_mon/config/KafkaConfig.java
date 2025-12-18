package net.lavacro.traffic_mon.config;

import net.lavacro.traffic_mon.model.Ulogd;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaConfig {

	// Inject the consumer factory (Spring Boot autoconfigures this from application.yml)
	private final ConsumerFactory<String, Ulogd> consumerFactory;

	public KafkaConfig(ConsumerFactory<String, Ulogd> consumerFactory) {
		this.consumerFactory = consumerFactory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Ulogd> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Ulogd> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setBatchListener(true);
		// Explicitly set MANUAL ack mode so Acknowledgment is available in listener method
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		return factory;
	}
}
