package uns.ac.rs.userservice.kafka.domain;

import java.util.List;

import uns.ac.rs.userservice.domain.User;

public class UsersFollowBlockMute {
	private List<User> following;
	private List<User> block;
	private List<User> mute;
	
	
	public UsersFollowBlockMute() {
		super();
	}


	public UsersFollowBlockMute(List<User> following, List<User> block, List<User> mute) {
		super();
		this.following = following;
		this.block = block;
		this.mute = mute;
	}


	public List<User> getFollowing() {
		return following;
	}


	public void setFollowing(List<User> following) {
		this.following = following;
	}


	public List<User> getBlock() {
		return block;
	}


	public void setBlock(List<User> block) {
		this.block = block;
	}


	public List<User> getMute() {
		return mute;
	}


	public void setMute(List<User> mute) {
		this.mute = mute;
	}


	@Override
	public String toString() {
		return "UsersFollowBlockMute [following=" + following + ", block=" + block + ", mute=" + mute + "]";
	}

}
