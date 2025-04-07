package co.edu.uniquindio.ingesis.restful.repositories.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Report;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ReportRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ReportRepositoryImpl implements ReportRepository {
    @Override
    public List<Report> findReportsByProfessorId(Long professorId) {
        return list("professorId", professorId);
    }

    @Override
    public List<Report> findReportsByGroupId(Long groupId) {
        return list("groupId", groupId);
    }

    @Override
    public List<Report> findReportsByCreationDate(LocalDate creationDate) {
        return list("creationDate", creationDate);
    }
}
