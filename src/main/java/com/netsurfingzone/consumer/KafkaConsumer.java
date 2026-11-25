package com.netsurfingzone.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.netsurfingzone.constant.ApplicationConstant;
import com.netsurfingzone.dto.Student;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(groupId = ApplicationConstant.GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void receivedMessage(Student message) throws JacksonException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(message);
		logger.info("Json message received using Kafka listener " + jsonString);
	}
}
