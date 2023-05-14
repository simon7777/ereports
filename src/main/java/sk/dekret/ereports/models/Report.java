package sk.dekret.ereports.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class Report {
    private Long id;
    @NotEmpty
    private String date;
    @NotNull
    private Long userAccountId;
    private String from;
    private String to;
    private String project;
    private String activity;
}
