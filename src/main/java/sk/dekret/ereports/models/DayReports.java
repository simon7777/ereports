package sk.dekret.ereports.models;

import lombok.Data;

import java.util.List;

@Data
public class DayReports {
    private String date;

    private List<Report> reports;
}
