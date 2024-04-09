package com.perfumescent.msg.agent;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;

@SpringBootApplication	@Slf4j
public class MsgAgentApplication implements CommandLineRunner {
	@Resource
	private RocketMQTemplate rocketMQTemplate;
	public static void main(String[] args) {
		SpringApplication.run(MsgAgentApplication.class, args);
	}
	public void run(String... args) throws Exception {
		//send message synchronously
		rocketMQTemplate.convertAndSend("test-topic-1", "Hello, World!");
		//send spring message
		rocketMQTemplate.send("test-topic-1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
		//send messgae asynchronously
		rocketMQTemplate.asyncSend("test-topic-2", new OrderPaidEvent("T_001", new BigDecimal("88.00")), new SendCallback() {
			@Override
			public void onSuccess(SendResult var1) {
				System.out.printf("async onSucess SendResult=%s %n", var1);
			}

			@Override
			public void onException(Throwable var1) {
				System.out.printf("async onException Throwable=%s %n", var1);
			}

		});
		//Send messages orderly
		rocketMQTemplate.syncSendOrderly("test-topic-1",MessageBuilder.withPayload("Hello, World syncSendOrderly").build(),"hashkey");

		//rocketMQTemplate.destroy(); // notes:  once rocketMQTemplate be destroyed, you can not send any message again with this rocketMQTemplate
	}

	@Service
	@RocketMQMessageListener(topic = "test-topic-1", consumerGroup = "my-consumer_test-topic-1")
	public class MyConsumer1 implements RocketMQListener<String> {
		public void onMessage(String message) {
			log.info("received message: {}", message);
		}
	}


	@Service
	@RocketMQMessageListener(topic = "test-topic-2", consumerGroup = "my-consumer_test-topic-2")
	public class MyConsumer2 implements RocketMQListener<OrderPaidEvent>{
		public void onMessage(OrderPaidEvent orderPaidEvent) {
			log.info("received orderPaidEvent: {}", orderPaidEvent);
		}
	}
	
	@Data
	@AllArgsConstructor
	public class OrderPaidEvent implements Serializable {
		private String orderId;

		private BigDecimal paidMoney;
	}
}
