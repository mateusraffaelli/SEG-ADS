package ads.seg;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;

public class UserService {
    private final static Random S_RANDOM = new Random();
    private UserRepository repository = new InMemory();
    private String algoritmoHash;

    private final int INTERATIONS = 10000;
    private final int KEY_LENGTH = 128;
    private final String algorithm = "PBKDF2WithHmacSHA512";

    public UserService(UserRepository db) {
        this.repository = db;
        this.algoritmoHash = "BCrypt";
    }

    public UserService(UserRepository db, String algoritmoHash) {
        this.repository = db;

        if (algoritmoHash.equals("PBKDF2")){
            this.algoritmoHash = algoritmoHash;
        }else {
            this.algoritmoHash = "BCrypt";
        }
    }

    private String hashPassword(String senha) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] password = senha.toCharArray();
        byte[] salt = new byte[16];
        S_RANDOM.nextBytes(salt);
        Base64.Encoder encoder = Base64.getEncoder();

        if (algoritmoHash.equals("PBKDF2")){
            byte[] senhaHash = PasswordHashing.hashPasswordWithPBKDF2(password, salt, algorithm, INTERATIONS, KEY_LENGTH);
            return encoder.encodeToString(salt) + "$" + encoder.encodeToString(senhaHash);
        }else{
            byte[] senhaHash = PasswordHashing.hashPasswordWithBCrypt(password);
            return encoder.encodeToString(senhaHash);
        }
    }

    private boolean verificaHashedPasswords(String password, String hashedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Base64.Decoder decoder = Base64.getDecoder();
        String[] sP = hashedPassword.split("\\$");
        char[] senha = password.toCharArray();
        byte[] salt = decoder.decode(sP[0]);
        byte[] pass = decoder.decode(sP[1]);

        if (algoritmoHash.equals("PBKDF2")){
            return PasswordHashing.verifyPasswordWithPBKDF2(senha, salt,  algorithm,  INTERATIONS, KEY_LENGTH, pass);
        }else{
            return PasswordHashing.verifyPasswordWithBCrypt(senha, pass);
        }
    }

    public boolean register(String login, String senha) throws UserAlreadyExistsException, NoSuchAlgorithmException, InvalidKeySpecException {
        if(repository.findByLogin(login) != null) {
            throw new UserAlreadyExistsException("Usuário já existe");
        }

        repository.save(new User(login,hashPassword(senha)));
        return true;
    }

    public boolean updatePassword (String login, String senhaAntiga , String novaSenha) throws InvalidLoginException, InvalidPasswordException, NoSuchAlgorithmException, InvalidKeySpecException {
        authenticate(login,senhaAntiga);

        repository.update(new User(login, hashPassword(novaSenha)));
        return true;
    }

    public boolean authenticate(String login, String senha) throws InvalidPasswordException, InvalidLoginException {
        User usuario = repository.findByLogin(login);

        if (usuario == null) {
            throw new InvalidLoginException("Login Incorreto");
        }

        try {
            if (!verificaHashedPasswords(senha, usuario.getPassword())) {
                throw new InvalidPasswordException("Senha Incorreta");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


}
