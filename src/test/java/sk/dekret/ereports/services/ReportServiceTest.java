package sk.dekret.ereports.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sk.dekret.ereports.EReportsApplication;
import sk.dekret.ereports.db.entities.UserAccount;
import sk.dekret.ereports.db.entities.UserRole;
import sk.dekret.ereports.models.Report;
import sk.dekret.ereports.repositories.ReportRepository;
import sk.dekret.ereports.repositories.UserAccountRepository;
import sk.dekret.ereports.security.UserContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = {EReportsApplication.class})
@AutoConfigureMockMvc
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void createReport() {
        UserAccount userAccount = createUserAccount();

        Report report = new Report();
        report.setDate("2023-05-05");
        report.setUserAccountId(userAccount.getId());

        UserContext userContext = new UserContext("username", userAccount.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(userContext);

        Report response = this.reportService.createReport(report);

        assertThat(response).isNotNull();
        assertThat(response.getDate()).isEqualTo(report.getDate());
        assertThat(response.getUserAccountId()).isEqualTo(report.getUserAccountId());
    }

    @Test
    void updateReport() {
        sk.dekret.ereports.db.entities.Report reportEntity = this.createRandomReport();

        Report report = new Report();
        report.setId(reportEntity.getId());
        report.setUserAccountId(reportEntity.getUserAccount().getId());
        report.setActivity("a1");
        report.setFrom("11:11");
        report.setTo("22:22");
        report.setProject("project");
        report.setDate("2022-05-06");

        Report response = this.reportService.updateReport(reportEntity.getId(), report);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(report.getId());
        assertThat(response.getUserAccountId()).isEqualTo(report.getUserAccountId());
        assertThat(response.getActivity()).isEqualTo(report.getActivity());
        assertThat(response.getFrom()).isEqualTo(report.getFrom());
        assertThat(response.getTo()).isEqualTo(report.getTo());
        assertThat(response.getProject()).isEqualTo(report.getProject());
        assertThat(response.getDate()).isEqualTo(report.getDate());
    }

    @Test
    void updateNonExistentReport() {
        Report report = new Report();
        report.setId(10000L);

        assertThrows(IllegalArgumentException.class, () -> this.reportService.updateReport(10000L, report));
    }

    @Test
    void deleteNonExistentReport() {
        assertThrows(IllegalArgumentException.class, () -> this.reportService.deleteReport(10000L));
    }

    @Test
    void deleteReport() {
        sk.dekret.ereports.db.entities.Report entity = createRandomReport();

        assertThat(this.reportRepository.findById(entity.getId())).isNotEmpty();

        Boolean response = this.reportService.deleteReport(entity.getId());

        assertThat(response).isEqualTo(Boolean.TRUE);
        assertThat(this.reportRepository.findById(entity.getId())).isEmpty();
    }

    @Test
    void findReportsByUserId() {
        UserAccount userAccount = createUserAccount();

        sk.dekret.ereports.db.entities.Report report1 = createEmptyReport();
        report1.setUserAccount(userAccount);
        this.reportRepository.save(report1);

        sk.dekret.ereports.db.entities.Report report2 = createEmptyReport();
        report2.setUserAccount(userAccount);
        this.reportRepository.save(report2);

        List<Report> response = this.reportService.findReportsByUserId(userAccount.getId());

        assertThat(response).hasSize(2);
    }

    @Test
    void findReportsByNonExistentUser() {
        assertThrows(IllegalArgumentException.class, () -> this.reportService.findReportsByUserId(100L));
    }

    @Test
    void findNoReportsByUser() {
        UserAccount userAccount = createUserAccount();

        List<Report> response = this.reportService.findReportsByUserId(userAccount.getId());

        assertThat(response).isNotNull().isEmpty();
    }

    UserAccount createUserAccount() {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName("firstName");
        userAccount.setLastName("lastName");
        userAccount.setUsername("username");
        userAccount.setPassword("pass");
        userAccount.setRole(UserRole.ROLE_MANAGER);

        return userAccountRepository.save(userAccount);
    }

    sk.dekret.ereports.db.entities.Report createRandomReport() {
        sk.dekret.ereports.db.entities.Report entity = createEmptyReport();

        entity.setUserAccount(createUserAccount());

        return this.reportRepository.save(entity);
    }

    sk.dekret.ereports.db.entities.Report createEmptyReport() {
        sk.dekret.ereports.db.entities.Report entity = new sk.dekret.ereports.db.entities.Report();
        entity.setFrom("10:10");
        entity.setTo("12:10");
        entity.setProject("ci");
        entity.setActivity("Activity");
        entity.setDate(LocalDate.parse("2023-05-05"));

        return entity;
    }

    @AfterEach
    void tearDown() {
        userAccountRepository.deleteAll();
        reportRepository.deleteAll();
    }
}
