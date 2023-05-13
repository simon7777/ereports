package sk.dekret.ereports.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.dekret.ereports.EReportsApplication;
import sk.dekret.ereports.JwtTokenTestGenerator;
import sk.dekret.ereports.models.UserAccount;
import sk.dekret.ereports.repositories.UserAccountRepository;
import sk.dekret.ereports.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = {EReportsApplication.class})
@AutoConfigureMockMvc
public class UserControllerTest implements JwtTokenTestGenerator {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserAccountRepository userAccountRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.jwt.secret}")
    String secret;

    @Test
    void getAllUsers() throws Exception {
        List<UserAccount> userList = new ArrayList<>();
        userList.add(new UserAccount());
        userList.add(new UserAccount());

        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getUserById() throws Exception {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);

        when(userService.findById(any())).thenReturn(userAccount);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + 1)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForManager("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.id").value(1))
                .andExpect(status().isOk());
    }

    @Test
    void createUserAccount() throws Exception {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);

        when(userService.createUser(any())).thenReturn(userAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForManager("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccount))
                ).andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(status().isCreated());
    }

    @Test
    void createUserAccountWithoutManagerRole() throws Exception {
        UserAccount userAccount = new UserAccount();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAccount))
        ).andExpect(status().isForbidden());
    }

    @Test
    void updateUserAccount() throws Exception {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(2L);

        when(userService.updateUser(any(), any())).thenReturn(userAccount);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + 2)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForManager("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccount))
                ).andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void updateUserAccountWithoutManagerRole() throws Exception {
        UserAccount userAccount = new UserAccount();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/" + 2)
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAccount))
        ).andExpect(status().isForbidden());
    }

    @Test
    void deleteUserAccount() throws Exception {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(2L);

        when(userService.deleteUser(any())).thenReturn(Boolean.TRUE);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + 2)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForManager("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAccount))
                ).andExpect(jsonPath("$").value(true))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserAccountWithoutManagerRole() throws Exception {
        UserAccount userAccount = new UserAccount();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + 2)
                .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userAccount))
        ).andExpect(status().isForbidden());
    }

    @Override
    public String getSecret() {
        return secret;
    }
}
