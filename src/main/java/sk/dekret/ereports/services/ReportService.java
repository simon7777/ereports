package sk.dekret.ereports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sk.dekret.ereports.db.entities.UserAccount;
import sk.dekret.ereports.mappers.ReportMapper;
import sk.dekret.ereports.models.Report;
import sk.dekret.ereports.models.ResponseResultList;
import sk.dekret.ereports.repositories.ReportRepository;
import sk.dekret.ereports.repositories.UserAccountRepository;
import sk.dekret.ereports.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

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

    public List<Report> findReportsByUserId(Long userId, Integer page, Integer pageSize) {
        this.checkThatUserExistsOrThrowException(userId);

        List<sk.dekret.ereports.db.entities.Report> reportList = this.reportRepository.findAllByUserAccountId(userId, PageRequest.of(page, pageSize));

        List<Report> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(reportList)) {
            for (sk.dekret.ereports.db.entities.Report report : reportList) {
                result.add(ReportMapper.toModel(report));
            }
        }

        return result;
    }

    public ResponseResultList<Report> findReportsForCurrentUser(Integer page, Integer pageSize) {
        UserAccount currentUser = loadUserAccountForCurrentlyLoggedInUser();

        List<sk.dekret.ereports.db.entities.Report> reports = this.reportRepository.findAllByUserAccountId(currentUser.getId(), PageRequest.of(page, pageSize));

        return new ResponseResultList<>(ReportMapper.toModels(reports));
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
