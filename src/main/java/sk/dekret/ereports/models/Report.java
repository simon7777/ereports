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

    public static class Builder {
        private Long userAccountId;
        private String date;
        private String from;
        private String to;
        private String project;
        private String activity;

        public Builder withUserAccountId(Long userAccountId) {
            this.userAccountId = userAccountId;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public Builder withFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder withTo(String to) {
            this.to = to;
            return this;
        }

        public Builder withProject(String project) {
            this.project = project;
            return this;
        }

        public Builder withActivity(String activity) {
            this.activity = activity;
            return this;
        }

        public Report build() {
            Report report = new Report();

            report.setDate(date);
            report.setFrom(from);
            report.setTo(to);
            report.setProject(project);
            report.setActivity(activity);

            return report;
        }
    }
}
