package co.edu.uniquindio.ingesis.restful.resources;

import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportCreationRequest;
import co.edu.uniquindio.ingesis.restful.dtos.reports.ReportResponse;
import co.edu.uniquindio.ingesis.restful.services.interfaces.ReportService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ReportResources {

    @Inject
    ReportService reportService;
    @Inject
    @Location("ReportResources/reportList.html")
    Template reportList; // inyecta templates/reports/reportList.html

    @Inject
    @Location("ReportResources/singleReport.html")
    Template singleReport;



    @GET
    @Path("/report_list_pdf/{profesorId}")
    @Produces("application/pdf")
    @PermitAll
    public Response exportReportListToPdf(@PathParam("profesorId") Long profesorId) {
        List<ReportResponse> reports = reportService.findReportsByProfessorId(profesorId);

        String htmlContent = reportList
                .data("reports", reports)
                .data("title", "Reportes del Profesor ID " + profesorId)
                .data("id",profesorId)
                .render();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(baos);

            return Response.ok(baos.toByteArray(), "application/pdf")
                    .header("Content-Disposition", "attachment; filename=Lista_Reportes_" + profesorId + ".pdf")
                    .build();
        } catch (Exception e) {
            e.printStackTrace(); // o log.error(...)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al generar PDF de lista").build();
        }
    }
    @GET
    @Path("/report_single_pdf/{id}")
    @Produces("application/pdf")
    @PermitAll
    public Response exportSingleToPdf(@PathParam("id") Long id) {
        ReportResponse reportResponse  =reportService.getReportById(id);

        String htmlContent = singleReport
                .data("report", reportResponse)
                .data("title", "Reporte del ID " + id)
                .data("reportId", id)
                .render();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(baos);

            return Response.ok(baos.toByteArray(), "application/pdf")
                    .header("Content-Disposition", "attachment; filename=single_Report" + id + ".pdf")
                    .build();
        } catch (Exception e) {
            e.printStackTrace(); // o log.error(...)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al generar PDF de lista").build();
        }
    }

    @GET
    @Path("/report_view/{id}")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public String getReportByIdHtml(@PathParam("id") Long id) {
        ReportResponse report;
        try {
            report = reportService.getReportById(id);
        } catch (NotFoundException e) {
            report = null;
        }
        return singleReport
                .data("report", report)
                .data("reportId", id)
                .data("id",id)
                .render();
    }


    @GET
    @Path("/profesor/{id}")
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public String listReportsHtml(@PathParam("id") Long id) {
        List<ReportResponse> reports = reportService.findReportsByProfessorId(id);
        return reportList
                .data("reports", reports)
                .data("title", "Reportes del Profesor ID " + id)
                .data("id",id)
                .render();
    }




    @GET
    @Path("/{id}")
    @RolesAllowed({"TUTOR", "ADMIN"})
    public Response getReportById(@PathParam("id") Long id) {
        ReportResponse reportResponse = reportService.getReportById(id);
        return Response.ok(reportResponse).build();
    }

    @GET
    @Path("/professor/{professorId}")
    @RolesAllowed({"TUTOR", "ADMIN"})
    public Response getReportsByProfessor(@PathParam("professorId") Long professorId) {
        List<ReportResponse> reportResponses = reportService.findReportsByProfessorId(professorId);
        return Response.ok(reportResponses).build();
    }

    @GET
    @Path("/group/{groupId}")
    @RolesAllowed({"TUTOR", "ADMIN"})
    public Response getReportsByGroup(@PathParam("groupId") Long groupId) {
        List<ReportResponse> reportResponses = reportService.findReportsByGroupId(groupId);
        return Response.ok(reportResponses).build();
    }

    // el parámetro creationDateStr es por como se ingresa la fecha
    @GET
    @Path("/date/{creationDate}")
    @RolesAllowed({"TUTOR", "ADMIN"})
    public Response getReportsByCreationDate(@PathParam("creationDate") String creationDateStr) {
        LocalDate creationDate = LocalDate.parse(creationDateStr);
        List<ReportResponse> reportResponses = reportService.findReportsByCreationDate(creationDate);
        return Response.ok(reportResponses).build();
    }

    @POST
    @RolesAllowed({"TUTOR"})
    public Response createReport(@Valid ReportCreationRequest request) {
        ReportResponse reportResponse = reportService.createReport(request);
        return Response.status(Response.Status.CREATED).entity(reportResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"TUTOR"})
    public Response deleteReport(@PathParam("id") Long id) {
        ReportResponse reportResponse = reportService.deleteReport(id);
        return Response.ok(reportResponse).build();
    }
}
