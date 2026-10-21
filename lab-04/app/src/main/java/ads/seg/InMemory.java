package ads.seg;

import java.util.HashMap;

public class InMemory implements UserRepository {

    private HashMap<String,User> users = new HashMap<>();

    @Override
    public boolean save(User user) {
        if(users.get(user.getLogin()) != null){
            return false;
        }

        users.put(user.getLogin(), user);
        return true;
    }

    @Override
    public boolean update(User user) {
        if(users.get(user.getLogin()) == null){
            return false;
        }

        users.put(user.getLogin(), user);
        return true;
    }

    @Override
    public User findByLogin(String login) {
        return users.get(login);
    }
}
