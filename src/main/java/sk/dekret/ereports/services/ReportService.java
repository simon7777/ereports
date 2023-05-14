package sk.dekret.ereports.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sk.dekret.ereports.models.Report;

import java.util.List;

@Slf4j
@Service
public class ReportService {

    public Report createReport(Report report) {
        return null;
    }

    public Report updateReport(Long id, Report report) {
        return null;
    }

    public Boolean deleteReport(Long id) {
        return false;
    }

    public List<Report> findReportsByUserId(Long userId) {
        return null;
    }
}
