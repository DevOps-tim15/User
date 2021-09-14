package uns.ac.rs.userservice.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uns.ac.rs.userservice.domain.User;
import uns.ac.rs.userservice.kafka.domain.UserMessage;
import uns.ac.rs.userservice.kafka.domain.UsersFollowBlockMute;
import uns.ac.rs.userservice.kafka.domain.UsersMessage;
import uns.ac.rs.userservice.service.UserService;
import uns.ac.rs.userservice.util.InvalidDataException;

@Service
public class Consumer {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
	Producer producer;
	
	@Autowired 
	private KafkaTemplate<String, String> kafkaTemp;
	
	@KafkaListener(topics="auth-topic", groupId="mygroup-user")
	public void consumeFromTopic(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException, InvalidDataException {
		String value = consumerRecord.value();
		System.out.println("Consummed message " + value);
		
		UserMessage message = null;
		try {
			message = objectMapper.readValue(value, UserMessage.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Consumer has read message!");
		if(message.getType().equals("registration")) {
			try {
				userService.saveRegisteredUser(message.getUser());
			} catch (Exception e) {
				UserMessage um = new UserMessage(message.getUser(), "registration-rollback");
				String val = objectMapper.writeValueAsString(um);
				kafkaTemp.send("user-topic" ,val);
			}
		}else if(message.getType().equals("update")) {
			userService.updateUser(message.getUser(), message.getOldUsername(), message.getRole());
		}

	}
	
	@SendTo
    @KafkaListener(topics = "following", groupId = "mygroup")
    public String getFollowers(String username) throws JsonProcessingException {
		List<User> usersThatIFollow = userService.usersThatIFollow(username);
		UsersMessage users = new UsersMessage(usersThatIFollow, "following");
		return objectMapper.writeValueAsString(users);
    }
	
	@SendTo
    @KafkaListener(topics = "follow-block-mute", groupId = "mygroup-user")
    public String getBlocked(String username) throws JsonProcessingException {
		UsersFollowBlockMute users = userService.usersThatIFollowBlockedAndMuted(username);
		return objectMapper.writeValueAsString(users);
    }
	
	@SendTo
    @KafkaListener(topics = "requested", groupId = "mygroup-user")
    public String getRequested(String username) throws JsonProcessingException {
		List<User> followingRequests = userService.getRequested(username);
		UsersMessage users = new UsersMessage(followingRequests, "requested");
		return objectMapper.writeValueAsString(users);
    }
}
