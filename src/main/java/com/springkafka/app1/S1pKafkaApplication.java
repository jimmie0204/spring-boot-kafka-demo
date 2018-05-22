package com.springkafka.app1;

import com.springkafka.CommonConfiguration;
import com.springkafka.ConfigProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 *
 */
@SpringBootApplication
@Import({ CommonConfiguration.class, ConfigProperties.class })
public class S1pKafkaApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(S1pKafkaApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);
		TestBean testBean = context.getBean(TestBean.class);
		testBean.send("fookafka");
		System.out.println("done=====");
	}

	@Bean
	public TestBean test() {
		return new TestBean();
	}

	public static class TestBean {

		@Autowired
		private ConfigProperties configProperties;

		@Autowired
		private KafkaTemplate<String, String> template;

		public void send(String foo) {
			ListenableFuture<SendResult<String, String>> send = this.template.send(this.configProperties.getTopic(), foo);
			String topic = null;
			try {
				topic = send.get().getRecordMetadata().topic();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.println(topic);

		}

	}

}
