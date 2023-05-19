package sk.dekret.ereports.mocks;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sk.dekret.ereports.db.entities.UserRole;
import sk.dekret.ereports.exceptions.EReportsException;
import sk.dekret.ereports.models.Report;
import sk.dekret.ereports.models.UserAccount;
import sk.dekret.ereports.security.UserContext;
import sk.dekret.ereports.services.ReportService;
import sk.dekret.ereports.services.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MockService {

    private final UserService userService;
    private final ReportService reportService;

    private final Random random = new Random();

    public void mockUserAccounts() throws EReportsException {
        this.buildUserAccountAndSave("simon", "Simon", "Dekret", "simon", UserRole.ROLE_MANAGER);
        this.buildUserAccountAndSave("peter", "Peter", "Petrovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("filip", "Filip", "Filipovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("ondrej", "Ondrej", "Ondrejovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("lubica", "Lubica", "Lubicova", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("jakub", "Jakub", "Jakubisko", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("tomas", "Tomas", "Tomasovy", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("martin", "Martin", "Martinovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("juraj", "Juraj", "Jurovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("matej", "Matej", "Matejovsky", "simon", UserRole.ROLE_USER);
    }

    private void buildUserAccountAndSave(String username, String firstName, String lastName, String password, UserRole role) throws EReportsException {
        UserAccount userAccount = new UserAccount.Builder()
                .withUsername(username)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withPassword(password)
                .withRole(role)
                .build();

        saveUserAccount(userAccount);
    }

    private void saveUserAccount(UserAccount model) throws EReportsException {
        UserAccount userAccount = this.userService.createUser(model);

        setSecurityContextHolder(userAccount);

        for (int i = 0; i < 10; i++) {
            Report report = buildReport(userAccount.getId());
            this.reportService.createReport(report);
        }
    }

    private void setSecurityContextHolder(UserAccount userAccount) {
        UserContext userContext = new UserContext(userAccount.getUsername(), userAccount.getId(), null);

        SecurityContextHolder.getContext().setAuthentication(userContext);
    }

    private Report buildReport(Long userId) {
        int fromHour = random.nextInt(23);
        int fromMinute = random.nextInt(55);

        return new Report.Builder()
                .withDate(generateRandomDate())
                .withUserAccountId(userId)
                .withFrom(formatTime(fromHour, fromMinute))
                .withTo(generateToTime(fromHour, fromMinute))
                .withActivity(generateRandomText(15))
                .withProject(generateRandomText(5))
                .build();
    }


    private String generateRandomDate() {
        return LocalDate.now().minusDays(5).plusDays(random.nextInt(5)).toString();
    }

    private String formatTime(int hour, int minute) {
        LocalTime time = LocalTime.of(hour, minute);

        return time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String generateToTime(int fromHour, int fromMinute) {
        int hour = random.nextInt(fromHour, 24);
        int minute = random.nextInt(fromMinute, 60);

        return formatTime(hour, minute);
    }

    public static String generateRandomText(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
