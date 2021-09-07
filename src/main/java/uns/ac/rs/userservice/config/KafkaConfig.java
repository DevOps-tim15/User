package uns.ac.rs.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfig {

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
        ProducerFactory<String, String> producerFactory,
        ConcurrentKafkaListenerContainerFactory<String, String> factory) 
    {
        ConcurrentMessageListenerContainer<String, String> replyContainer = factory.createContainer("reply-topic");
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId("mygroup");
        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
    }
}
