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
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomAuthenticationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketPrintService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketPrintServiceImpl implements TicketPrintService {

    PDFont plain = PDType1Font.HELVETICA;
    PDFont italic = PDType1Font.HELVETICA_OBLIQUE;
    PDFont bold = PDType1Font.HELVETICA_BOLD;
    private final TicketRepository ticketRepository;
    private final AuthenticationUtil authenticationFacade;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public TicketPrintServiceImpl(TicketRepository ticketRepository,
        AuthenticationUtil authenticationFacade, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @Override
    public Resource getTicketPdf(List<Long> tickets) throws IOException {

        PDDocument pdDocument = new PDDocument();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (Long ticketId : tickets) {
            Optional<Ticket> optionalTicket = this.ticketRepository.findById(ticketId);
            if (optionalTicket.isEmpty()) {
                throw new NotFoundException(
                    String.format("Could not find purchased ticket with id %s", ticketId));
            }
            Ticket ticket = optionalTicket.get();
            if (ticket.getPurchasedBy() == null && ticket.getReservedBy() == null) {
                throw new NotFoundException(
                    String.format("Could not find purchased/reserved ticket with id %s", ticketId));
            }
            ApplicationUser user = this.userRepository.findUserByEmail(
                authenticationFacade.getAuthentication().getPrincipal().toString());
            Long userOnTicket =
                ticket.getPurchasedBy() != null ? ticket.getPurchasedBy().getUserId()
                    : ticket.getReservedBy().getUserId();
            if (user.getUserId() != userOnTicket) {
                throw new CustomAuthenticationException("Not authorized to access this resource");
            }

            LOGGER.debug("Trying to generate PDF for ticket {}", ticketId);
            buildTicketPdf(ticket, user, pdDocument);
        }
        pdDocument.save(os);
        pdDocument.close();
        return new ByteArrayResource(os.toByteArray());
    }

    public PDDocument buildTicketPdf(Ticket ticket, ApplicationUser user, PDDocument ticketDocument)
        throws IOException {

        float marginBody = 30;

        PDPage ticketPage = new PDPage(PDRectangle.A4);

        PDRectangle rectangle = ticketPage.getMediaBox();
        ticketDocument.addPage(ticketPage);

        PDPageContentStream cs = new PDPageContentStream(ticketDocument, ticketPage);

        float xoffset = marginBody;
        float yoffset = rectangle.getHeight() - marginBody;
        float xend = rectangle.getWidth() - marginBody;
        float xcompanydata = xend - 150;

        drawTitle(cs, xoffset, yoffset);
        cs.setNonStrokingColor(Color.black);
        drawCompanyAddress(cs, xcompanydata, yoffset);

        yoffset -= 60;
        boolean isPurchased = ticket.getPurchasedBy() != null;
        drawSubtitle(cs, xoffset + 2, yoffset, isPurchased);
        cs.setNonStrokingColor(Color.black);
        yoffset -= 40;

        drawSeparatorLine(cs, xoffset, yoffset, xend);

        yoffset -= 30;
        Location location = ticket.getSeat().getSector().getSeatingPlan().getLocation();
        drawDateAndSeatInfo(ticket, cs, xoffset, yoffset);

        drawLocationAddress(cs, yoffset, xcompanydata, location);

        yoffset -= 60;

        drawSeparatorLine(cs, xoffset, yoffset, xend);

        yoffset -= 30;

        yoffset = drawEventInformation(ticket, cs, xoffset, yoffset, location);

        drawSeparatorLine(cs, xoffset, yoffset, xend);

        yoffset -= 30;
        if (isPurchased) {
            drawTicketBody(user, cs, xoffset, yoffset);
            addQrCode(ticketDocument, cs, ticket, xoffset, 30);
        } else {
            drawReservationBody(user, cs, xoffset, yoffset, ticket);
        }

        // drawTicketId(ticket, marginBody, cs, xoffset);
        cs.close();
        return ticketDocument;
    }

    private float drawEventInformation(Ticket ticket, PDPageContentStream cs, float xoffset,
        float yoffset, Location location) throws IOException {
        float yoffsetAfter = yoffset;
        cs.beginText();
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.setFont(plain, 14);
        String seatingPlanName = ticket.getSeat().getSector().getSeatingPlan().getName();
        cs.showText(location.getName() + " - " + seatingPlanName);
        cs.newLine();
        cs.setFont(plain, 20);
        Event event = ticket.getShow().getEvent();
        cs.showText(event.getName());
        cs.newLine();
        cs.setFont(plain, 12);
        cs.showText("| ");
        List<Artist> artists = ticket.getShow().getArtists().stream().toList();

        int lengthOfLine = 85;
        float yoffsetLine = 60;

        int lengthUntilNextLine = lengthOfLine;
        for (Artist artist : artists) {
            if (artist.getFirstName() != null) {
                int l = artist.getFirstName().length();
                if (lengthUntilNextLine >= l) {
                    lengthUntilNextLine -= l;
                } else {
                    cs.newLine();
                    yoffsetAfter -= yoffsetLine;
                    lengthUntilNextLine = lengthOfLine;
                }
                cs.showText(artist.getFirstName() + " ");

            }
            if (artist.getLastName() != null) {
                int l = artist.getLastName().length();
                if (lengthUntilNextLine >= l) {
                    lengthUntilNextLine -= l;
                } else {
                    cs.newLine();
                    yoffsetAfter -= yoffsetLine;
                    lengthUntilNextLine = lengthOfLine;
                }
                cs.showText(artist.getLastName() + " ");
            }
            if (artist.getBandName() != null) {
                int l = artist.getBandName().length();
                if (lengthUntilNextLine >= l) {
                    lengthUntilNextLine -= l;
                } else {
                    cs.newLine();
                    yoffsetAfter -= yoffsetLine;
                    lengthUntilNextLine = lengthOfLine;
                }
                cs.showText(artist.getBandName() + " ");
            }
            cs.showText(" | ");
        }
        cs.endText();
        yoffsetAfter -= yoffsetLine;
        return yoffsetAfter;
    }

    private void drawLocationAddress(PDPageContentStream cs, float yoffset, float xcompanydata,
        Location location) throws IOException {
        cs.beginText();
        cs.newLineAtOffset(xcompanydata, yoffset);
        cs.setFont(plain, 10);
        Address address = location.getAddress();
        cs.showText(address.getStreet() + " " + address.getHouseNumber());
        cs.newLine();
        cs.showText(address.getZipCode() + " " + address.getCity());
        cs.newLine();
        cs.showText(address.getCountry());
        cs.newLine();

        cs.endText();
    }

    private void drawDateAndSeatInfo(Ticket ticket, PDPageContentStream cs, float xoffset,
        float yoffset) throws IOException {
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
    }

    private void drawTicketBody(ApplicationUser user, PDPageContentStream cs, float xoffset,
        float yoffset) throws IOException {
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
        cs.showText("Print this ticket on a A4 page (do not cut or manipulate it in any way).");
        cs.newLine();
        cs.showText("Only in this form the ticket is valid.");
        cs.endText();
    }

    private void drawTicketId(Ticket ticket, float marginBody, PDPageContentStream cs,
        float xoffset) throws IOException {
        cs.beginText();
        cs.setFont(italic, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, -marginBody + 100);
        cs.showText("Ticket ID: " + ticket.getTicketId());
        cs.endText();
    }

    private void drawTitle(PDPageContentStream cs, float xoffset, float yoffset)
        throws IOException {
        cs.beginText();
        cs.setFont(plain, 24);
        cs.setNonStrokingColor(Color.BLUE);
        cs.newLineAtOffset(xoffset, yoffset);
        String title = "Ticketline";
        cs.showText(title);
        cs.endText();
    }

    private void drawCompanyAddress(PDPageContentStream cs, float xposition, float yposition)
        throws IOException {
        cs.beginText();
        cs.setFont(italic, 12);
        cs.setLeading(15f);
        cs.newLineAtOffset(xposition, yposition);
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
    }

    private void drawSubtitle(PDPageContentStream cs, float xposition, float yposition,
        boolean isPurchased)

        throws IOException {
        String subtitle;
        if (isPurchased) {
            subtitle = "Ticket";
        } else {
            subtitle = "RESERVATION";
        }
        cs.beginText();
        cs.setFont(plain, 20);
        if (!isPurchased) {
            cs.setNonStrokingColor(Color.LIGHT_GRAY);
        }
        cs.newLineAtOffset(xposition, yposition);
        cs.showText(subtitle);
        cs.endText();
    }

    private void drawSeparatorLine(PDPageContentStream cs, float xoffset, float yoffset, float xend)
        throws IOException {
        cs.moveTo(xoffset, yoffset);
        cs.lineTo(xend, yoffset);
        cs.setStrokingColor(Color.BLACK);
        cs.stroke();
    }

    private void drawReservationBody(ApplicationUser user, PDPageContentStream cs, float xoffset,
        float yoffset, Ticket ticket) throws IOException {

        cs.beginText();
        cs.setFont(bold, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.showText("This seat was reserved by: ");
        cs.newLine();
        cs.setFont(plain, 14);
        cs.showText(user.getLastName() + " " + user.getFirstName());
        cs.newLine();
        cs.showText(user.getAddress().getStreet() + " " + user.getAddress().getHouseNumber());
        cs.newLine();
        cs.showText(user.getAddress().getZipCode() + " " + user.getAddress().getCity() + ", "
            + user.getAddress().getCountry());
        cs.newLine();
        cs.setFont(bold, 14);
        cs.newLine();
        cs.showText("RESERVATION CODE");
        cs.newLine();
        cs.showText(
            ticket.getBookedIns().iterator().next().getTransaction().getTransactionId().toString());
        cs.newLine();
        cs.endText();
    }

    private void addQrCode(PDDocument document, PDPageContentStream cs, Ticket ticket,
        float xposition, float yposition) {
        try {
            String ticketMetadata =
                "U" + ticket.getPurchasedBy().getUserId() + "ID" + ticket.getTicketId() + "SI"
                    + ticket.getSeat().getSeatId();
            BitMatrix matrix = new MultiFormatWriter().encode(
                new String(ticketMetadata.getBytes("UTF-8"), "UTF-8"),
                BarcodeFormat.QR_CODE, 200, 200);

            MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
            BufferedImage bimage = MatrixToImageWriter.toBufferedImage(matrix, config);
            PDImageXObject image = JPEGFactory.createFromImage(document, bimage);

            cs.drawImage(image, xposition, yposition, 120, 120);
        } catch (IOException e) {
            LOGGER.error("failed to create QR code: ", e);

        } catch (WriterException e) {
            LOGGER.error("failed to create QR code: ", e);
        }
    }
}
