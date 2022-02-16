package dto;

public class UserDTO {

    private String userName;
    private String userType;

    public UserDTO(String userName, String userType) {
        this.userName = userName;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserType() {
        return userType;
    }
}
