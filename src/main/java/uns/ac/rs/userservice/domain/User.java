package uns.ac.rs.userservice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_t")
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "verified")
	private boolean verified;
	
	@Column(name = "website_url")
	private String websiteUrl;
	
	@Column(name = "sex")
	public String sex;
	
	@Column(name = "birth_date")
	public String birthDate;

	@Column(name = "biography")
	public String biography;
	
	@Column(name = "canBeTagged")
	public Boolean canBeTagged;

	@Column(name = "isPrivate")
	public Boolean isPrivate;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	@JsonBackReference(value = "user-authority")
	private List<Authority> authorities;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<User> followers;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<User> following;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)

	@ManyToMany
	private List<User> blockedUsers;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<User> blockedByUsers;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<User> mutedUsers;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<User> followingRequests;
	
	public User() {
		super();
		this.followers = new ArrayList<User>();
		this.following = new ArrayList<User>();
		this.blockedUsers = new ArrayList<User>();
		this.blockedByUsers = new ArrayList<User>();
		this.followingRequests = new ArrayList<User>();
	}

	public User(Long id, String username, String password, String email, String firstName, String lastName,
			String phone, boolean verified, String websiteUrl, String sex, String birthDate, String biography, Boolean canBeTagged,
			Boolean isPrivate, List<Authority> authorities, List<User> mutedUsers) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.verified = verified;
		this.websiteUrl = websiteUrl;
		this.sex = sex;
		this.birthDate = birthDate;
		this.biography = biography;
		this.canBeTagged = canBeTagged;
		this.isPrivate = isPrivate;
		this.authorities = authorities;
		this.followers = new ArrayList<User>();
		this.following = new ArrayList<User>();
		this.followingRequests = new ArrayList<User>();
		this.mutedUsers = new ArrayList<User>();
		}

	public User(String username, String password, String email, String firstName, String lastName,
			String phone, String websiteUrl, String sex, String birthDate, String biography, Boolean canBeTagged,
			Boolean isPrivate) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.websiteUrl = websiteUrl;
		this.sex = sex;
		this.birthDate = birthDate;
		this.biography = biography;
		this.canBeTagged = canBeTagged;
		this.isPrivate = isPrivate;
		this.followers = new ArrayList<User>();
		this.following = new ArrayList<User>();
		this.followingRequests = new ArrayList<User>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}
	
	public Boolean getCanBeTagged() {
		return canBeTagged;
	}

	public void setCanBeTagged(Boolean canBeTagged) {
		this.canBeTagged = canBeTagged;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}
	
	public List<User> getBlockedUsers() {
		return blockedUsers;
	}

	public void setBlockedUsers(List<User> blockedUsers) {
		this.blockedUsers = blockedUsers;
		
	}

	public List<User> getBlockedByUsers() {
		return blockedByUsers;
	}

	public void setBlockedByUsers(List<User> blockedByUsers) {
		this.blockedByUsers = blockedByUsers;
	}
	
	public List<User> getFollowingRequests() {
		return followingRequests;
	}

	public void setFollowingRequests(List<User> followingRequests) {
		this.followingRequests = followingRequests;
	}

	public List<User> getMutedUsers() {
		return mutedUsers;
	}

	public void setMutedUsers(List<User> mutedUsers) {
		this.mutedUsers = mutedUsers;
	}

	@Override
	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", verified=" + verified
				+ ", websiteUrl=" + websiteUrl + ", sex=" + sex + ", birthDate=" + birthDate + ", biography="
				+ biography + ", canBeTagged=" + canBeTagged + ", isPrivate=" + isPrivate + ", authorities="
				+ authorities + "]";
	}

}
