package co.edu.uniquindio.ingesis.restful.repositories.interfaces;

import co.edu.uniquindio.ingesis.restful.domain.Comment;
import co.edu.uniquindio.ingesis.restful.domain.Report;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportResponse;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends PanacheRepository<Report> {
    List<Report> findReportsByProfessorId(Long professorId);
    List<Report> findReportsByGroupId(Long groupId);
    List<Report> findReportsByCreationDate(LocalDate creationDate);
}
