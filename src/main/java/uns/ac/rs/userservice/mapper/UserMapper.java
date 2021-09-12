package uns.ac.rs.userservice.mapper;

import java.util.ArrayList;
import java.util.List;

import uns.ac.rs.userservice.domain.User;
import uns.ac.rs.userservice.dto.UserDTO;

public class UserMapper {
	
	public static UserDTO fromEntity(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());
	}
	
	public static List<UserDTO> fromEntityList(List<User> users) {
		List<UserDTO> usersDTO = new ArrayList<>();
		for(User user:users) {
			usersDTO.add(fromEntity(user));
		}
		return usersDTO;
	}
	
	public static List<String> fromEntityToString(List<User> users) {
		List<String> usernames = new ArrayList<String>();
		for (User user : users) {
			usernames.add(user.getUsername());
		}
		return usernames;
	}
}
