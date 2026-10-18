package net.lavacro.traffic_mon.config;

import lombok.RequiredArgsConstructor;
import net.lavacro.traffic_mon.model.Ulogd;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
	private final ConsumerFactory<String, Ulogd> consumerFactory;

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Ulogd> kafkaListenerContainerFactory() {
		IO.println("Initializing Kafka listener container factory");
		ConcurrentKafkaListenerContainerFactory<String, Ulogd> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.setBatchListener(true);
		// Explicitly set MANUAL ack mode so Acknowledgment is available in listener method
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		return factory;
	}
}
