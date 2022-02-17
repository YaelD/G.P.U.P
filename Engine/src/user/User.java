package user;

import dto.UserDTO;

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

    public UserDTO makeUserDTO(){
        UserDTO userDTO = new UserDTO(this.userName, this.userType.toString());
        return userDTO;
    }
}
