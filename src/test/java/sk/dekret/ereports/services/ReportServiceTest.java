package sk.dekret.ereports.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sk.dekret.ereports.EReportsApplication;
import sk.dekret.ereports.repositories.ReportRepository;
import sk.dekret.ereports.repositories.UserAccountRepository;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = {EReportsApplication.class})
@AutoConfigureMockMvc
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @AfterEach
    void tearDown() {
        userAccountRepository.deleteAll();
        reportRepository.deleteAll();
    }
}
