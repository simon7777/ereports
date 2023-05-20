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
import sk.dekret.ereports.models.Report;
import sk.dekret.ereports.models.ResponseResultList;
import sk.dekret.ereports.services.ReportService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = {EReportsApplication.class})
@AutoConfigureMockMvc
class ReportControllerTest implements JwtTokenTestGenerator {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @Value("${app.jwt.secret}")
    String secret;

    @MockBean
    private ReportService reportService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getSecret() {
        return secret;
    }

    @Test
    void createReport() throws Exception {
        Report report = new Report();
        report.setId(1L);
        report.setUserAccountId(1L);
        report.setDate(LocalDate.now().toString());

        when(reportService.createReport(any())).thenReturn(report);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test"))
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(status().isCreated());
    }

    @Test
    void updateReport() throws Exception {
        Report report = new Report();
        report.setId(1L);
        report.setUserAccountId(1L);
        report.setDate(LocalDate.now().toString());

        when(reportService.updateReport(any(), any())).thenReturn(report);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/report/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test"))
                        .content(objectMapper.writeValueAsString(report)))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    void deleteReport() throws Exception {
        when(reportService.deleteReport(any())).thenReturn(Boolean.TRUE);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/report/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test")))
                .andExpect(jsonPath("$").value(true))
                .andExpect(status().isOk());
    }

    @Test
    void findReportsByUser() throws Exception {
        Report report = new Report();
        report.setId(1L);
        report.setUserAccountId(1L);
        report.setDate(LocalDate.now().toString());

        when(reportService.findReportsByUserId(any(), any(), any())).thenReturn(new ResponseResultList(List.of(report), 100L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/report/byUser/1?page=0&pageSize=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test")))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    void findReportsForCurrentUser() throws Exception {
        Report report = new Report();
        report.setId(1L);
        report.setUserAccountId(1L);
        report.setDate(LocalDate.now().toString());

        when(reportService.findReportsForCurrentUser(any(), any())).thenReturn(new ResponseResultList(List.of(report), 100L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/report/byCurrentUser?page=0&pageSize=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getJwtTokenForUser("test")))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.totalItems").value(100L))
                .andExpect(status().isOk());
    }
}
