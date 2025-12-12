package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Utilise le profil "test"
public class StudentManagementApplicationTests {

    @Test
    void contextLoads() {
        // Test simple pour vérifier que le contexte se charge
    }
}