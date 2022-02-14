package users;

import engine.Engine;
import engine.SystemEngine;

import java.util.Collections;
import java.util.HashSet;
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



    public synchronized void addUser(String username) {
        usersSet.add(new User(username));
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

