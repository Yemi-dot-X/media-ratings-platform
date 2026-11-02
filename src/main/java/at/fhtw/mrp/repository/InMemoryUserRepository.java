package at.fhtw.mrp.repository;


import at.fhtw.mrp.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository{

    private final Map<Integer, User> userMap = new HashMap<>();
    @Override
    public User findById(int id){
        return userMap.get(id);
    }

    @Override
    public User findByUsername(String username) {
        for(User user : userMap.values()){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void save(User user) {
        userMap.put(user.getId(), user);
        }
}
