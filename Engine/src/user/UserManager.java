package user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class UserManager {

    private final Set<User> usersSet;
    // private static UserManager userManager = null;

    public UserManager() {
        usersSet = new HashSet<>();
    }

//    public static UserManager getInstance(){
//        if (userManager == null)
//            userManager = new UserManager();
//
//        return userManager;
//    }



    public synchronized void addUser(String username, String userType) {
        usersSet.add(new User(username, UserType.valueOf(userType.toUpperCase(Locale.ROOT)) ));
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        boolean isExists = false;
        for(User user : this.usersSet){
            if(user.getUserName().equals(username)){
                isExists = true;
            }
        }
        return isExists;
    }
}

