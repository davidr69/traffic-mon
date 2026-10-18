package net.lavacro.traffic_mon.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lavacro.traffic_mon.model.Ulogd;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyConsumer {
	private final SpawnThreads  spawnThreads;

	@KafkaListener(topics = "#{'${kafka.topics}'.split(',')}", batch = "true", containerFactory = "kafkaListenerContainerFactory")
	public void listen(List<ConsumerRecord<String, Ulogd>> messages, Acknowledgment ack) {
		log.info("{} messages received", messages.size());
		spawnThreads.process(
				messages.stream().map(ConsumerRecord::value).toList()
		);
		ack.acknowledge();
	}
}
