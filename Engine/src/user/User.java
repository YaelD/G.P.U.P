package user;

public class User {

    private String userName;
    private UserType userType;

    public User(String userName, UserType userType) {
        this.userName = userName;
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }
}
