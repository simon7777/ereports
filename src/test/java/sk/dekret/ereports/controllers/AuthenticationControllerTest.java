package sk.dekret.ereports.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.dekret.ereports.EReportsApplication;
import sk.dekret.ereports.db.entities.UserRole;
import sk.dekret.ereports.models.AuthRequest;
import sk.dekret.ereports.models.AuthResponse;
import sk.dekret.ereports.services.AuthenticationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = {EReportsApplication.class})
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLogin() throws Exception {
        final String jwtToken = "token";
        AuthResponse response = new AuthResponse(jwtToken, UserRole.ROLE_MANAGER);
        when(authenticationService.login(any())).thenReturn(response);

        AuthRequest request = new AuthRequest();
        request.setUsername("username");
        request.setPassword("pass");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.token").value(jwtToken))
                .andExpect(jsonPath("$.role").value(UserRole.ROLE_MANAGER.toString()));
    }
}
