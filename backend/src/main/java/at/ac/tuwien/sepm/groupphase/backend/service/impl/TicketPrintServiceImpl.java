package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE1;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE2;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_MAIL;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_PHONE;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_WEB;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketPrintService;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class TicketPrintServiceImpl implements TicketPrintService {

    String title = "Ticketline";
    String subtitle = "Ticket";
    PDFont plain = PDType1Font.HELVETICA;
    PDFont italic = PDType1Font.HELVETICA_OBLIQUE;
    PDFont bold = PDType1Font.HELVETICA_BOLD;
    private final TicketRepository ticketRepository;
    private final AuthenticationUtil authenticationFacade;
    private final UserRepository userRepository;

    public TicketPrintServiceImpl(TicketRepository ticketRepository,
        AuthenticationUtil authenticationFacade, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @Override
    public Resource getTicketPdf(Long ticketId) throws IOException {
        Optional<Ticket> optionalTicket = this.ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new NotFoundException(
                String.format("Could not find ticket with id %s", ticketId));
        }
        Ticket ticket = optionalTicket.get();
        ApplicationUser user = this.userRepository.findUserByEmail(
            authenticationFacade.getAuthentication().getPrincipal().toString());
        if (user.getUserId() != ticket.getPurchasedBy().getUserId()) {
            //TODO: ADD CORRECT error
            throw new NotFoundException(
                String.format("Could not find ticket with id %s", ticketId));
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDDocument pdDocument = buildInvoicePdf(ticket, user);
        pdDocument.save(os);
        pdDocument.close();
        return new ByteArrayResource(os.toByteArray());
    }

    public PDDocument buildInvoicePdf(Ticket ticket, ApplicationUser user) throws IOException {

        float marginBody = 30;

        PDDocument invoice = new PDDocument();

        PDPage invoicePage = new PDPage(PDRectangle.A4);

        PDRectangle rectangle = invoicePage.getMediaBox();
        invoice.addPage(invoicePage);

        PDPageContentStream cs = new PDPageContentStream(invoice, invoicePage);

        float xoffset = marginBody;
        float yoffset = rectangle.getHeight() - marginBody;
        float xend = rectangle.getWidth() - marginBody;
        float yend = marginBody;
        float xcompanydata = xend - 150;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        cs.beginText();
        cs.setFont(plain, 24);
        cs.setNonStrokingColor(Color.DARK_GRAY);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.showText(title);
        cs.endText();

        cs.beginText();
        cs.setFont(italic, 12);
        cs.setLeading(15f);
        cs.newLineAtOffset(xcompanydata, yoffset);
        cs.showText(COMPANY_NAME);
        cs.newLine();
        cs.showText(COMPANY_ADDRESS_LINE1);
        cs.newLine();
        cs.showText(COMPANY_ADDRESS_LINE2);
        cs.newLine();
        cs.showText(COMPANY_PHONE);
        cs.newLine();
        cs.showText(COMPANY_MAIL);
        cs.newLine();
        cs.showText(COMPANY_WEB);
        cs.endText();

        yoffset -= 20;

        cs.beginText();
        cs.setFont(plain, 20);
        cs.setStrokingColor(Color.BLACK);
        cs.newLineAtOffset(xoffset + 2, yoffset);
        cs.showText(subtitle);
        cs.endText();

        yoffset -= 80;
        cs.moveTo(xoffset, yoffset);
        cs.lineTo(xend, yoffset);
        cs.setStrokingColor(Color.BLACK);
        cs.stroke();

        yoffset -= 30;
        cs.beginText();
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.setFont(plain, 14);
        String seatingPlanName = ticket.getSeat().getSector().getSeatingPlan().getName();
        Location location = ticket.getSeat().getSector().getSeatingPlan().getLocation();
        cs.showText(location.getName() + " - " + seatingPlanName);
        cs.newLine();
        cs.setFont(plain, 20);
        Event event = ticket.getShow().getEvent();
        cs.showText(event.getName() + " (" + event.getCategory() + ")");
        cs.newLine();
        cs.setFont(plain, 12);
        cs.showText("| ");
        Set<Artist> artists = ticket.getShow().getArtists();
        for (Artist artist : artists) {
            if (artist.getFirstName() != null) {
                cs.showText(artist.getFirstName() + " ");
            }
            if (artist.getLastName() != null) {
                cs.showText(artist.getLastName() + " ");
            }
            if (artist.getBandName() != null) {
                cs.showText(artist.getBandName() + " ");
            }
            cs.showText(" | ");
        }
        cs.endText();

        cs.beginText();
        cs.newLineAtOffset(xcompanydata, yoffset);
        cs.setFont(plain, 10);
        Address address = location.getAddress();
        cs.showText(address.getStreet() + " " + address.getHouseNumber());
        cs.newLine();
        cs.showText(address.getZipCode() + " " + address.getCity() + ", " + address.getCountry());
        cs.newLine();

        cs.endText();

        yoffset -= 60;
        cs.moveTo(xoffset, yoffset);
        cs.lineTo(xend, yoffset);
        cs.setStrokingColor(Color.BLACK);
        cs.stroke();

        yoffset -= 30;
        cs.beginText();
        cs.setFont(bold, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, yoffset);
        OffsetDateTime showDate = ticket.getShow().getDate();
        cs.showText(showDate.getDayOfWeek().toString() + ", " + showDate.getDayOfMonth() + ". "
            + showDate.getMonth().toString() + " " + showDate.getYear() + " " + String.format(
            "%02d", showDate.getHour()) + ":" + String.format("%02d", showDate.getMinute()));
        cs.setFont(plain, 14);
        Seat seat = ticket.getSeat();
        if (seat.getRowNumber() != null || seat.getSeatNumber() != null) {
            cs.newLine();
        }
        if (seat.getRowNumber() != null) {
            cs.showText("Row " + seat.getRowNumber() + " ");
        }
        if (seat.getSeatNumber() != null) {
            cs.showText("Seat " + seat.getSeatNumber());
        }
        cs.endText();

        yoffset -= 30;

        cs.moveTo(xoffset, yoffset);
        cs.lineTo(xend, yoffset);
        cs.setStrokingColor(Color.BLACK);
        cs.stroke();
        yoffset -= 30;

        cs.beginText();
        cs.setFont(bold, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.showText("This ticket was bought by: ");
        cs.newLine();
        cs.setFont(plain, 14);
        cs.showText(user.getLastName() + " " + user.getFirstName());
        cs.newLine();
        cs.showText(user.getAddress().getStreet() + " " + user.getAddress().getHouseNumber());
        cs.newLine();
        cs.showText(user.getAddress().getZipCode() + " " + user.getAddress().getCity() + ", "
            + user.getAddress().getCountry());
        cs.newLine();
        cs.showText("and is only valid with a photographic identification.");
        cs.newLine();
        cs.newLine();
        cs.setFont(bold, 14);
        cs.showText("DISCLAIMER:");
        cs.newLine();
        cs.setFont(plain, 14);
        cs.showText("This ticket is not allowed to be copied! Do not give away a copy/reprint!");
        cs.newLine();
        cs.showText(
            "If the event is cancelled, the costs of the ticket is refunded automatically.");
        cs.newLine();
        cs.showText(
            "Please keep the secret in good condition, it has to be readable at the entrance.");
        cs.newLine();
        cs.showText(
            "Print this ticket on a A4 page (do not cut or manipulate it in any way).");
        cs.newLine();
        cs.showText(
            "Only in this form the ticket is valid.");
        cs.endText();


        yoffset -= 20;

        cs.close();

        return invoice;
    }
}
