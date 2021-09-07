package uns.ac.rs.userservice.kafka;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uns.ac.rs.userservice.kafka.domain.UserMessage;

@Service
public class Producer {

	  @Autowired 
	  private KafkaTemplate<String, String> kafkaTemp;
	  
	  @Autowired
	  private ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
	  
	//  @Autowired 
	//  private ReplyingKafkaTemplate<String, String, String> rkafkaTemp;
	  
	  @Autowired
	  private ObjectMapper objectMapper;
	  
	  @Async
	  public void sendMessageToTopic(String topic, UserMessage userMssage) throws JsonProcessingException {
			String value = objectMapper.writeValueAsString(userMssage);
			kafkaTemp.send(topic ,value);
			System.out.println("Publishing to topic "+topic);
	  }
	  
	 public String checkUser(String authToken) throws JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException{
	     	System.out.println("HI!");   
	     	ProducerRecord<String, String> producerRecord = new ProducerRecord<>("logged-in", authToken);
	        RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(producerRecord);
	        ConsumerRecord<String, String> consumerRecord = future.get();
	        String username = consumerRecord.value();
	        System.out.println(username);
	        return username;
	   }
}
