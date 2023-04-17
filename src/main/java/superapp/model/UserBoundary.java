package superapp.model;



public class UserBoundary {
	
    private UserId userID;
    private String role;//can change to enum
    private String username;
    private String avatar;

    public UserBoundary() {}
    
    //check if constructor needed here
    public UserBoundary(String email) {
    	this.userID = new UserId(email);
    	
    	//get from DB
    	role = "Student";
    	username = "username";
    	avatar = "avater.url"; 
    }
    
  //check if constructor needed here
    public UserBoundary(String superapp, String email) {
    	this.userID = new UserId(superapp, email);
    	
    	//get from DB
    	role = "Student";
    	username = "username";
    	avatar = "avater.url"; 
    }

	public UserId getUserId() {
		return userID;
	}

	public void setUserId(UserId userID) {
		this.userID = userID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public boolean equals(Object obj){
		UserBoundary other = (UserBoundary) obj;
		return this.userID.equals(other.userID);
	}
}
