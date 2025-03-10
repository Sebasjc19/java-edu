package co.edu.uniquindio.ingesis.restful.services.implementations;

import co.edu.uniquindio.ingesis.restful.domain.Report;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportResponse;
import co.edu.uniquindio.ingesis.restful.exceptions.usuarios.ResourceNotFoundException;
import co.edu.uniquindio.ingesis.restful.mappers.ReportMapper;
import co.edu.uniquindio.ingesis.restful.repositories.interfaces.ReportRepository;
import co.edu.uniquindio.ingesis.restful.services.interfaces.ReportService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Inject
    ReportMapper reportMapper;
    final ReportRepository reportRepository;


    @Override
    public ReportResponse getReportById(Long id) {
        Report report = Report.findById(id);
        if( report == null ){
            new ResourceNotFoundException();
        }
        return reportMapper.toReportResponse(report);
    }

    @Override
    public List<ReportResponse> findReportsByProfessorId(Long professorId) {
        // Se buscan los reportes en base al id del profesor en la base de datos
        List<Report> reports = reportRepository.findReportsByProfessorId(professorId);

        // Convertir la lista de entidades en una lista de respuestas DTO
        return reports.stream()
                .map(reportMapper::toReportResponse)
                .collect( Collectors.toList());
    }

    @Override
    public List<ReportResponse> findReportsByGroupId(Long groupId) {
        // Se buscan los reportes en base al id del grupo en la base de datos
        List<Report> reports = reportRepository.findReportsByGroupId(groupId);

        // Convertir la lista de entidades en una lista de respuestas DTO
        return reports.stream()
                .map(reportMapper::toReportResponse)
                .collect( Collectors.toList());
    }

    @Override
    public List<ReportResponse> findReportsByCreationDate(LocalDate creationDate) {
        // Se buscan los reportes en base a la fecha en que fueron creados en la base de datos
        List<Report> reports = reportRepository.findReportsByCreationDate(creationDate);

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

        return reportMapper.toReportResponse(report);
    }

    @Override
    public ReportResponse deleteReport(Long id) {
        // Validar si el reporte a eliminar se encuentra en la base de datos
        Optional<Report> optionalReport = reportRepository.findByIdOptional(id);
        if (optionalReport.isEmpty()) {
            new ResourceNotFoundException();
        }

        // Obtener el reporte y eliminarlo
        Report report = optionalReport.get();
        report.delete();

        return reportMapper.toReportResponse(report);
    }
}
