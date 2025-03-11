package co.edu.uniquindio.ingesis.restful.services.interfaces;

import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.programs.ProgramResponse;
import co.edu.uniquindio.ingesis.restful.dtos.programs.UpdateProgramRequest;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    ReportResponse getReportById(Long id);
    List<ReportResponse> findReportsByProfessorId(Long professorId);
    List<ReportResponse> findReportsByGroupId(Long groupId);
    List<ReportResponse> findReportsByCreationDate(LocalDate creationDate);
    ReportResponse createReport(ReportCreationRequest request);
    ReportResponse deleteReport(Long id);
}
