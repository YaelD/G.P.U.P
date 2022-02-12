package users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private final Set<AdminUser> adminUserSet;
    private final Set<WorkerUser> workerUserSet;

    public UserManager() {
        this.adminUserSet = new HashSet<>();
        this.workerUserSet = new HashSet<>();
    }

    public synchronized void addUser(User user) {
        if(user instanceof AdminUser){
            adminUserSet.add((AdminUser) user);
        }
        else{
            workerUserSet.add((WorkerUser) user);
        }
    }

    public synchronized void removeUser(User user) {
        if(user instanceof AdminUser){
            adminUserSet.remove((AdminUser) user);
        }
        else{
            workerUserSet.remove((WorkerUser) user);
        }
    }

//    public synchronized Set<User> getUsers() {
//
//        return Collections.unmodifiableSet(usersSet);
//    }

//    public boolean isUserExists(User user) {
//        return usersSet.contains(username);
//    }
}
