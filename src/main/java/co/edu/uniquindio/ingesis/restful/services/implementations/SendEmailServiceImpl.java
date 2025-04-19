package co.edu.uniquindio.ingesis.restful.services.implementations;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
public class SendEmailServiceImpl {

    @Inject
    Mailer mailer;
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        mailer.send(Mail.withText(destinatario, asunto, cuerpo));
    }
}
