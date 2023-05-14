package sk.dekret.ereports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.dekret.ereports.db.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
