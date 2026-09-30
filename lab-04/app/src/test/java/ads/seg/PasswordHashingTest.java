package ads.seg;
import module java.base; // requer java 25
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
public class PasswordHashingTest {
    private static final SecureRandom S_RANDOM = new SecureRandom();
    @Test
    public void testHashPasswordWithMessageDigest() {
        byte[] salt = new byte[16];
        S_RANDOM.nextBytes(salt);
        String[] algorithms = { "MD5", "SHA-1", "SHA-256", "SHA-512" };
        byte[] hashedPassword;
        char[] password = {'1', '2', '3', '4', '5', '6'}; // Senha a ser testada
        for (String algorithm : algorithms) {
            try {
                hashedPassword = PasswordHashing.hashPasswordWithMessageDigest(password, salt, algorithm);
                assertTrue(PasswordHashing.verifyPasswordWithMessageDigest(password, salt, algorithm, hashedPassword));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testHashPasswordWithPBKDF2() {
        byte[] salt = new byte[16];
        S_RANDOM.nextBytes(salt);
        int iterations = 600000;
        int keyLength = 128;
        String[] algorithms = { "PBKDF2WithHmacSHA1", "PBKDF2WithHmacSHA256", "PBKDF2WithHmacSHA512" };
        byte[] hashedPassword;
        char[] password = {'1', '2', '3', '4', '5', '6'}; // Senha a ser testada
        for (String algorithm : algorithms) {
            try {
                hashedPassword = PasswordHashing.hashPasswordWithPBKDF2(password, salt, algorithm, iterations,
                        keyLength);
                assertTrue(PasswordHashing.verifyPasswordWithPBKDF2(password, salt, algorithm, iterations, keyLength,
                        hashedPassword));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testHashPasswordWithBCrypt() {
        char[] password = {'1', '2', '3', '4', '5', '6'}; // Senha a ser testada
        byte[] hashedPassword = PasswordHashing.hashPasswordWithBCrypt(password);
        assertTrue(PasswordHashing.verifyPasswordWithBCrypt(password, hashedPassword));
    }
}
