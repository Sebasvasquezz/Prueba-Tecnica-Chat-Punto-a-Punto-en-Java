package prueba.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * EncryptionService provides methods to encrypt and decrypt messages using the AES algorithm.
 * It relies on a secret key defined in the application properties.
 */
@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    @Value("${encryption.key}")
    private String SECRET_KEY;

    /**
     * Encrypts a message using AES encryption.
     * 
     * @param message The plain text message to be encrypted.
     * @return The encrypted message encoded in Base64.
     * @throws Exception If encryption fails or the key is invalid.
     */
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

    /**
     * Decrypts a previously encrypted message using AES decryption.
     * 
     * @param encryptedMessage The encrypted message in Base64 encoding.
     * @return The original decrypted plain text message.
     * @throws Exception If decryption fails or the key is invalid.
     */
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

