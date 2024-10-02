package prueba.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    @Value("${encryption.key}")
    private String SECRET_KEY;

    public String encrypt(String message) throws Exception {
        if (SECRET_KEY == null || SECRET_KEY.length() != 16) {
            throw new IllegalArgumentException("Invalid encryption key");
        }

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        if (SECRET_KEY == null || SECRET_KEY.length() != 16) {
            throw new IllegalArgumentException("Invalid encryption key");
        }

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage);
        byte[] originalMessage = cipher.doFinal(decodedMessage);
        return new String(originalMessage);
    }
}

