package ads.seg;

public interface UserRepository {
    boolean save(User user);
    boolean update(User user);
    User findByLogin(String login);

}
