package sk.dekret.ereports.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "from_time", length = 5)
    private String from;

    @Column(name = "to_time", length = 5)
    private String to;

    @Column(name = "project", length = 128)
    private String project;

    @Column(name = "activity", length = 512)
    private String activity;
}
