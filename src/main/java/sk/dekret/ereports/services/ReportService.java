package sk.dekret.ereports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.dekret.ereports.db.entities.UserAccount;
import sk.dekret.ereports.mappers.ReportMapper;
import sk.dekret.ereports.models.DayReports;
import sk.dekret.ereports.models.Report;
import sk.dekret.ereports.models.ResponseResultList;
import sk.dekret.ereports.repositories.ReportRepository;
import sk.dekret.ereports.repositories.UserAccountRepository;
import sk.dekret.ereports.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public Report createReport(Report report) {
        UserAccount userAccount = this.loadUserAccountForCurrentlyLoggedInUser();

        sk.dekret.ereports.db.entities.Report entity = ReportMapper.toEntity(report, new sk.dekret.ereports.db.entities.Report());
        entity.setUserAccount(userAccount);

        return ReportMapper.toModel(this.reportRepository.save(entity));
    }

    public Report updateReport(Long id, Report report) {
        sk.dekret.ereports.db.entities.Report entity = loadReportByIdOrThrowException(id);

        ReportMapper.toEntity(report, entity);

        return ReportMapper.toModel(this.reportRepository.save(entity));
    }

    public Boolean deleteReport(Long id) {
        sk.dekret.ereports.db.entities.Report entity = loadReportByIdOrThrowException(id);

        this.reportRepository.delete(entity);

        return true;
    }

    public ResponseResultList<DayReports> findReportsByUserId(Long userId, Integer page, Integer pageSize) {
        this.checkThatUserExistsOrThrowException(userId);

        List<sk.dekret.ereports.db.entities.Report> reportList = this.reportRepository.findAllByUserAccountId(userId, PageRequest.of(page, pageSize).withSort(Sort.by("date").descending()));

        List<DayReports> dayReports = transformToDayReports(ReportMapper.toModels(reportList));

        return new ResponseResultList<>(dayReports);
    }

    public ResponseResultList<DayReports> findReportsForCurrentUser(Integer page, Integer pageSize) {
        UserAccount currentUser = loadUserAccountForCurrentlyLoggedInUser();

        List<sk.dekret.ereports.db.entities.Report> reports = this.reportRepository.findAllByUserAccountId(currentUser.getId(), PageRequest.of(page, pageSize).withSort(Sort.by("date").descending()));

        List<DayReports> dayReports = transformToDayReports(ReportMapper.toModels(reports));

        return new ResponseResultList<>(dayReports);
    }

    private List<DayReports> transformToDayReports(List<Report> reports) {
        Map<String, List<Report>> dayReportsMap = new HashMap<>();

        for (Report report : reports) {
            if (!dayReportsMap.containsKey(report.getDate())) {
                dayReportsMap.put(report.getDate(), new ArrayList<>());
            }

            dayReportsMap.get(report.getDate()).add(report);
        }

        return this.transformMapToListOfDayReports(dayReportsMap);
    }

    private List<DayReports> transformMapToListOfDayReports(Map<String, List<Report>> dayReportsMap) {
        List<DayReports> result = new ArrayList<>();

        for (Map.Entry<String, List<Report>> entry : dayReportsMap.entrySet()) {
            DayReports dayReports = new DayReports();
            dayReports.setDate(entry.getKey());
            dayReports.setReports(entry.getValue());

            result.add(dayReports);
        }

        return result;
    }

    private UserAccount loadUserAccountForCurrentlyLoggedInUser() {
        Long userAccountId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AccessDeniedException("User is not logged in."));

        return this.userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> new IllegalArgumentException("User account for this user was not found"));
    }

    private sk.dekret.ereports.db.entities.Report loadReportByIdOrThrowException(Long id) {
        return this.reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report with ID does not exist."));
    }

    private void checkThatUserExistsOrThrowException(Long userId) {
        this.userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with ID=%s does not exist", userId)));
    }

}
