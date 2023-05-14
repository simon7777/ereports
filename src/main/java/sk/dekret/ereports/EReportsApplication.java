package sk.dekret.ereports;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import sk.dekret.ereports.mocks.MockService;

@SpringBootApplication
@RequiredArgsConstructor
public class EReportsApplication implements ApplicationListener<ApplicationReadyEvent> {

    private final MockService mockService;

    public static void main(String[] args) {
        SpringApplication.run(EReportsApplication.class, args);
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        mockService.mockUserAccounts();
    }
}
