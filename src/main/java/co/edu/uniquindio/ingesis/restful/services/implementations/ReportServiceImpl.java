package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Report;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.users.implementations.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.ReportMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ReportRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.ReportService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Inject
    ReportMapper reportMapper;
    ReportRepository reportRepository;

    private static final Logger auditLogger = LoggerFactory.getLogger("audit");


    @Override
    public ReportResponse getReportById(Long id) {
        Report report = Report.findById(id);
        if( report == null ){
            new ResourceNotFoundException("Reporte no encontrado");
        }

        auditLogger.info("Consulta de reporte por ID: id='{}'", id);

        return reportMapper.toReportResponse(report);
    }

    @Override
    public List<ReportResponse> findReportsByProfessorId(Long professorId) {
        // Se buscan los reportes en base al id del profesor en la base de datos
        List<Report> reports = reportRepository.findReportsByProfessorId(professorId);


        auditLogger.info("Consulta de reportes por profesor: profesorId='{}', total='{}'", professorId, reports.size());


        // Convertir la lista de entidades en una lista de respuestas DTO
        return reports.stream()
                .map(reportMapper::toReportResponse)
                .collect( Collectors.toList());
    }

    @Override
    public List<ReportResponse> findReportsByGroupId(Long groupId) {
        // Se buscan los reportes en base al id del grupo en la base de datos
        List<Report> reports = reportRepository.findReportsByGroupId(groupId);

        auditLogger.info("Consulta de reportes por grupo: groupId='{}', total='{}'", groupId, reports.size());

        // Convertir la lista de entidades en una lista de respuestas DTO
        return reports.stream()
                .map(reportMapper::toReportResponse)
                .collect( Collectors.toList());
    }

    @Override
    public List<ReportResponse> findReportsByCreationDate(LocalDate creationDate) {
        // Se buscan los reportes en base a la fecha en que fueron creados en la base de datos
        List<Report> reports = reportRepository.findReportsByCreationDate(creationDate);

        auditLogger.info("Consulta de reportes por fecha: creationDate='{}', total='{}'", creationDate, reports.size());

        // Convertir la lista de entidades en una lista de respuestas DTO
        return reports.stream()
                .map(reportMapper::toReportResponse)
                .collect( Collectors.toList());
    }

    @Override
    public ReportResponse createReport(ReportCreationRequest request) {
        Report report = reportMapper.parseOf(request);
        report.setCreationDate(LocalDate.now());
        report.persist();

        auditLogger.info("Reporte creado: grupoId='{}', profesorId='{}'",
                request.groupId(), request.professorId());


        return reportMapper.toReportResponse(report);
    }

    @Override
    public ReportResponse deleteReport(Long id) {
        // Validar si el reporte a eliminar se encuentra en la base de datos
        Optional<Report> optionalReport = reportRepository.findByIdOptional(id);
        if (optionalReport.isEmpty()) {
            new ResourceNotFoundException("Reporte no encontrado");
        }

        // Obtener el reporte y eliminarlo
        Report report = optionalReport.get();
        report.delete();

        auditLogger.info("Reporte eliminado: id='{}'", id);

        return reportMapper.toReportResponse(report);
    }
}
