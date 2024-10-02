package prueba.chat.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptionServiceTest {

    @InjectMocks
    private EncryptionService encryptionService;

    private static final String VALID_KEY = "1234567890123456";  
    private static final String INVALID_KEY = "invalidkey";
    private static final String PLAIN_TEXT = "Hello, World!";
    private static final String ENCRYPTED_TEXT = "encryptedMessageInBase64";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(encryptionService, "SECRET_KEY", VALID_KEY);
    }

    @Test
    void testEncryptSuccess() throws Exception {
        String encryptedMessage = encryptionService.encrypt(PLAIN_TEXT);
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(VALID_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] expectedEncryptedMessage = cipher.doFinal(PLAIN_TEXT.getBytes());
        String expectedBase64Message = Base64.getEncoder().encodeToString(expectedEncryptedMessage);

        assertEquals(expectedBase64Message, encryptedMessage);
    }

    @Test
    void testEncryptInvalidKey() {
        ReflectionTestUtils.setField(encryptionService, "SECRET_KEY", INVALID_KEY);
        assertThrows(IllegalArgumentException.class, () -> encryptionService.encrypt(PLAIN_TEXT));
    }

    @Test
    void testDecryptSuccess() throws Exception {
        // Encrypt first to get a valid encrypted message
        String encryptedMessage = encryptionService.encrypt(PLAIN_TEXT);
        String decryptedMessage = encryptionService.decrypt(encryptedMessage);

        assertEquals(PLAIN_TEXT, decryptedMessage);
    }

    @Test
    void testDecryptInvalidKey() {
        ReflectionTestUtils.setField(encryptionService, "SECRET_KEY", INVALID_KEY);
        assertThrows(IllegalArgumentException.class, () -> encryptionService.decrypt(ENCRYPTED_TEXT));
    }
}
