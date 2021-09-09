package uns.ac.rs.userservice.kafka.domain;

import java.util.List;

import uns.ac.rs.userservice.domain.User;

public class UsersMessage {
	
	private List<User> users;
	private String type;
	
	public UsersMessage() {
		super();
	}
	
	public UsersMessage(List<User> users, String type) {
		super();
		this.users = users;
		this.type = type;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	
}
