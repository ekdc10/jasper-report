package com.eka.user.service;

import com.eka.user.model.User;
import com.eka.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReportServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserReportService userReportService;

    @BeforeEach
    void setUp() {
        userReportService = new UserReportService(userRepository);
    }

    @Test
    void testGeneratePdfReturnsByteArray() {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUserName("user1");
        user1.setFullName("User One");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUserName("user2");
        user2.setFullName("User Two");

        List<User> mockUsers = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(mockUsers);

        byte[] pdfBytes = userReportService.generatePdf();

        assertNotNull(pdfBytes, "PDF bytes should not be null");
        assertTrue(pdfBytes.length > 0, "PDF bytes should not be empty");
    }
}
