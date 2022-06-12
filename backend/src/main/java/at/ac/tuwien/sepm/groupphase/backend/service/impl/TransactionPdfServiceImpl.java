package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE1;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE2;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_BANK_BIC;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_BANK_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_IBAN;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_MAIL;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_PHONE;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_UID;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_WEB;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.TAX_RATE_CULTURE;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomAuthenticationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.TransactionPdfService;
import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.line.LineStyle;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class TransactionPdfServiceImpl implements TransactionPdfService {


    private static final PDFont plain = PDType1Font.HELVETICA;
    private static final PDFont italic = PDType1Font.HELVETICA_OBLIQUE;
    private static final PDFont bold = PDType1Font.HELVETICA_BOLD;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final String TABLE_HEADER = "Items";

    private final TransactionRepository transactionRepository;
    private final AuthenticationUtil authenticationFacade;
    private final UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public TransactionPdfServiceImpl(
        TransactionRepository transactionRepository,
        AuthenticationUtil authenticationFacade,
        UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @Override
    public Resource getTransactionPdf(Long id) throws IOException {

        Optional<Transaction> t = transactionRepository.findById(id);

        if (t.isPresent()) {
            Transaction transaction = t.get();
            String email = authenticationFacade.getEmail();
            ApplicationUser user = userRepository.findUserByEmail(email);

            if (user == null || user.getUserId() != transaction.getUser().getUserId()) {
                throw new CustomAuthenticationException("Not authorized to get this resource.");
            }

            BookingType type = transaction.getBookedIns().iterator().next().getBookingType();
            if (!type.equals(BookingType.DERESERVATION)) {
                return buildTransactionPdf(transaction);
            } else {
                throw new NotFoundException("No PDF available for dereservations.");
            }

        }

        throw new NotFoundException("This transaction does not exist.");
    }


    private Resource buildTransactionPdf(Transaction transaction) throws IOException {

        BookingType type = transaction.getBookedIns().iterator().next().getBookingType();
        LOGGER.debug("Trying to build PDF for booking type {}", type);

        Set<BookedIn> bookedIns = transaction.getBookedIns();

        PDDocument invoice = new PDDocument();

        PDPage invoicePage = new PDPage(PDRectangle.A4);

        PDRectangle rectangle = invoicePage.getMediaBox();
        invoice.addPage(invoicePage);

        PDPageContentStream cs = new PDPageContentStream(invoice, invoicePage);

        float marginBody = 30;
        float xoffset = marginBody;
        float yoffset = rectangle.getHeight() - marginBody;
        float xend = rectangle.getWidth() - marginBody;
        float xcompanydata = xend - 150;

        drawTitle(cs, xoffset, yoffset);
        cs.setNonStrokingColor(Color.black);
        drawCompanyAddress(cs, xcompanydata, yoffset);

        yoffset -= 60;

        drawSubtitle(cs, xoffset + 2, yoffset, type);

        yoffset -= 40;

        drawSeparatorLine(cs, xoffset, yoffset, xend);

        yoffset -= 30;

        float yoffsetBeforeCustData = yoffset;
        float xhalfOfPage = rectangle.getWidth() * 0.5f;

        drawCustomerData(transaction.getUser(), cs, xoffset, yoffset, xhalfOfPage);

        yoffset -= 100;

        drawBillDetails(transaction, cs, yoffsetBeforeCustData, xhalfOfPage, type);

        boolean drawLines = true;
        boolean drawContent = true;
        float tableWidth = rectangle.getWidth() - 2 * marginBody - 30;
        float marginTable = (rectangle.getWidth() - tableWidth) / 2;
        float bottomMargin = 70;
        float ystartNewPage = yoffset - marginBody;

        BaseTable table = new BaseTable(yoffset, ystartNewPage, bottomMargin, tableWidth,
            marginTable,
            invoice, invoicePage, drawLines, drawContent);

        if (type.equals(BookingType.RESERVATION)) {
            getReservationItems(bookedIns, table);
        } else {
            getItemTable(bookedIns, table);
        }
        yoffset = 115;

        drawSeparatorLine(cs, xoffset, yoffset, xend);

        yoffset = 100;
        drawCompanyBankDetails(cs, xoffset, yoffset);

        cs.close();

        table.draw();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        invoice.save(os);
        invoice.close();
        return new ByteArrayResource(os.toByteArray());
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

    private void drawCompanyBankDetails(PDPageContentStream cs, float xoffset, float yoffset)
        throws IOException {
        cs.beginText();
        cs.setFont(italic, 12);
        cs.setLeading(15f);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.showText(COMPANY_NAME);
        cs.newLine();
        cs.showText(COMPANY_BANK_NAME);
        cs.newLine();
        cs.showText(COMPANY_BANK_BIC);
        cs.newLine();
        cs.showText(COMPANY_IBAN);
        cs.newLine();
        cs.showText("UID:" + COMPANY_UID);
        cs.endText();
    }

    private void drawBillDetails(Transaction transaction, PDPageContentStream cs,
        float yoffsetBeforeCustData, float xhalfOfPage, BookingType type) throws IOException {

        cs.beginText();
        cs.setFont(bold, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xhalfOfPage, yoffsetBeforeCustData);
        cs.showText("Issuing Date: ");

        if (type.equals(BookingType.RESERVATION)) {
            cs.newLine();
            cs.showText("RESERVATION");
            cs.newLine();
            cs.showText("CODE: ");
        } else {
            cs.newLine();
            cs.showText("Bill No.: ");
        }
        cs.endText();

        LocalDate issuingDate = transaction.getDate().toLocalDate();

        cs.beginText();
        cs.setFont(plain, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xhalfOfPage + xhalfOfPage * 0.5f, yoffsetBeforeCustData);
        cs.showText(issuingDate.toString());
        cs.newLine();
        if (type.equals(BookingType.RESERVATION)) {
            cs.newLine();
        }
        cs.showText(transaction.getTransactionId().toString());
        cs.endText();
    }

    private void drawCustomerData(ApplicationUser user, PDPageContentStream cs, float xoffset,
        float yoffset, float xhalfOfPage) throws IOException {
        cs.beginText();
        cs.setFont(bold, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.showText("Customer: ");
        cs.endText();

        cs.beginText();
        cs.setFont(plain, 14);
        cs.newLineAtOffset(xoffset + xhalfOfPage * 0.3f, yoffset);
        cs.showText(user.getFirstName() + " " + user.getLastName());
        cs.newLine();
        cs.showText(user.getAddress().getStreet() + " " + user.getAddress().getHouseNumber() + ",");
        cs.newLine();
        cs.showText(user.getAddress().getZipCode() + " " + user.getAddress().getCity());
        cs.newLine();
        cs.showText(user.getAddress().getCountry());
        cs.endText();
    }

    private void drawSeparatorLine(PDPageContentStream cs, float xoffset, float yoffset, float xend)
        throws IOException {
        cs.moveTo(xoffset, yoffset);
        cs.lineTo(xend, yoffset);
        cs.setStrokingColor(Color.lightGray);
        cs.stroke();
    }


    private float calculateNet(float gross, float taxRate) {
        return gross * (100 / (100 + taxRate));
    }

    private float calculateTax(float gross, float taxRate) {
        return gross - calculateNet(gross, taxRate);
    }


    private HashMap<BigDecimal, ArrayList<BookedIn>> mapByPrice(Set<BookedIn> bookedIns) {

        HashMap<BigDecimal, ArrayList<BookedIn>> mappedByPrice = new HashMap<>();
        for (BookedIn booking : bookedIns) {
            if (mappedByPrice.get(booking.getPriceAtBookingTime()) == null
                || mappedByPrice.isEmpty()) {
                ArrayList<BookedIn> list = new ArrayList<>();
                list.add(booking);
                mappedByPrice.put(booking.getPriceAtBookingTime(), list);
            } else {
                ArrayList<BookedIn> list = mappedByPrice.get(booking.getPriceAtBookingTime());
                list.add(booking);
            }
        }
        return mappedByPrice;
    }

    private void getItemTable(Set<BookedIn> bookedIns, BaseTable table) {
        boolean purchase = bookedIns.iterator().next().getBookingType()
            .equals(BookingType.PURCHASE);
        //width in %
        final float amountWidth = 12;
        final float descriptionWidth = 40;
        final float unitWidth = 15;
        final float totalWidth = 18;
        final float taxRateWidth = 15;

        Row<PDPage> headerRow = table.createRow(40);
        Cell<PDPage> headerCell;
        if (purchase) {
            headerCell = headerRow.createCell(100, TABLE_HEADER);
        } else {
            String tableHeaderCancel = "Items to be refunded";
            headerCell = headerRow.createCell(100, tableHeaderCancel);
        }
        headerCell.setFont(bold);
        headerCell.setFontSize(14);
        headerCell.setAlign(HorizontalAlignment.CENTER);
        headerCell.setValign(VerticalAlignment.MIDDLE);

        table.addHeaderRow(headerRow);

        Row<PDPage> itemRow = table.createRow(30);
        Cell<PDPage> itemCell;

        itemCell = itemRow.createCell(amountWidth, "<b>Amount</b>");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        itemCell = itemRow.createCell(descriptionWidth, "<b>Description</b>");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        itemCell = itemRow.createCell(unitWidth, "<b>Unit Price</b><br />(Gross)");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        itemCell = itemRow.createCell(taxRateWidth, "<b>Tax rate</b><br />(Ust.)");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        itemCell = itemRow.createCell(totalWidth, "<b>Total</b><br />(Gross)");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        float subTotal = 0f;

        //if cancellation the values are multiplied by (-1) because the money will be refunded
        int factor;
        if (purchase) {
            factor = 1;
        } else {
            factor = -1;
        }

        HashMap<BigDecimal, ArrayList<BookedIn>> mappedByPrice = mapByPrice(bookedIns);

        //for each pricecategory render one row with amount, price and total
        for (Entry<BigDecimal, ArrayList<BookedIn>> e : mappedByPrice.entrySet()) {
            BigDecimal key = e.getKey();
            ArrayList<BookedIn> list = e.getValue();

            int amount = list.size();
            float unitPrice = key.floatValue();
            float total = unitPrice * amount;
            subTotal += total;

            Row<PDPage> itemDetailRow = table.createRow(30);
            Cell<PDPage> itemDetailCell;

            itemDetailCell = itemDetailRow.createCell(amountWidth, amount + "");
            itemDetailCell.setAlign(HorizontalAlignment.CENTER);
            itemDetailCell.setFontSize(8);
            itemDetailCell.setFont(plain);

            //Itemdescription consists of eventname, date, seatingplan,location and sector
            BookedIn bookedIn = list.get(0);
            Sector sector = bookedIn.getTicket().getSeat().getSector();
            SeatingPlan seatingPlan = sector.getSeatingPlan();
            Location location = seatingPlan.getLocation();
            Show show = bookedIns.iterator().next().getTicket().getShow();
            String date = show.getDate().toLocalDate().toString();
            String eventName = show.getEvent().getName();
            String descriptionLine1 = eventName + ", " + date;
            String descriptionLine2 =
                " Sector " + sector.getSectorId() + ", " + seatingPlan.getName() + ", "
                    + location.getName();

            itemDetailCell = itemDetailRow.createCell(descriptionWidth,
                descriptionLine1 + "<br />" + descriptionLine2);
            itemDetailCell.setFontSize(8);
            itemDetailCell.setFont(plain);

            itemDetailCell = itemDetailRow.createCell(unitWidth,
                df.format(factor * unitPrice) + " €");
            itemDetailCell.setFontSize(8);
            itemDetailCell.setFont(plain);

            itemDetailCell = itemDetailRow.createCell(taxRateWidth, TAX_RATE_CULTURE + "%");
            itemDetailCell.setFontSize(8);
            itemDetailCell.setFont(plain);

            itemDetailCell = itemDetailRow.createCell(totalWidth, df.format(factor * total) + " €");
            itemDetailCell.setFontSize(8);
            itemDetailCell.setFont(plain);


        }

        Row<PDPage> endRow = table.createRow(2);
        Cell<PDPage> separator = endRow.createCell(100, "");
        separator.setFillColor(Color.BLACK);

        LineStyle noLine = new LineStyle(Color.WHITE, 0);
        endRow = table.createRow(30);

        Cell<PDPage> footerDetail = endRow.createCell(100 - totalWidth, "Subtotal: ");
        footerDetail.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        footerDetail.setRightBorderStyle(noLine);
        footerDetail.setFontSize(13);
        footerDetail.setFont(bold);
        footerDetail.setAlign(HorizontalAlignment.RIGHT);
        footerDetail = endRow.createCell(totalWidth, df.format(factor * subTotal) + " €");
        footerDetail.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        footerDetail.setLeftBorderStyle(noLine);
        footerDetail.setFontSize(12);
        footerDetail.setFont(bold);

        endRow = table.createRow(30);
        footerDetail = endRow.createCell(100 - totalWidth, "Net: ");
        footerDetail.setTopBorderStyle(noLine);
        footerDetail.setBottomBorderStyle(noLine);
        footerDetail.setRightBorderStyle(noLine);
        footerDetail.setFontSize(13);
        footerDetail.setFont(bold);
        footerDetail.setAlign(HorizontalAlignment.RIGHT);
        footerDetail = endRow.createCell(totalWidth,
            df.format(calculateNet(subTotal, TAX_RATE_CULTURE) * factor) + " €");
        footerDetail.setTopBorderStyle(noLine);
        footerDetail.setBottomBorderStyle(noLine);
        footerDetail.setLeftBorderStyle(noLine);
        footerDetail.setFontSize(12);
        footerDetail.setFont(plain);

        endRow = table.createRow(30);
        footerDetail = endRow.createCell(100 - totalWidth, "Tax: ");
        footerDetail.setTopBorderStyle(noLine);
        footerDetail.setRightBorderStyle(noLine);
        footerDetail.setFontSize(13);
        footerDetail.setFont(bold);
        footerDetail.setAlign(HorizontalAlignment.RIGHT);

        footerDetail = endRow.createCell(totalWidth,
            df.format(calculateTax(subTotal, TAX_RATE_CULTURE) * factor) + " €");
        footerDetail.setTopBorderStyle(noLine);
        footerDetail.setLeftBorderStyle(noLine);
        footerDetail.setFontSize(12);
        footerDetail.setFont(plain);

    }


    private void getReservationItems(Set<BookedIn> bookedIns, BaseTable table) {
        final float amountWidth = 20;
        final float descriptionWidth = 80;

        Row<PDPage> headerRow = table.createRow(40);
        Cell<PDPage> headerCell;

        headerCell = headerRow.createCell(100, TABLE_HEADER);

        headerCell.setFont(bold);
        headerCell.setFontSize(14);
        headerCell.setAlign(HorizontalAlignment.CENTER);
        headerCell.setValign(VerticalAlignment.MIDDLE);

        table.addHeaderRow(headerRow);

        Row<PDPage> itemRow = table.createRow(30);
        Cell<PDPage> itemCell;

        itemCell = itemRow.createCell(amountWidth, "<b>Amount</b>");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        itemCell = itemRow.createCell(descriptionWidth, "<b>Description</b>");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        HashMap<BigDecimal, ArrayList<BookedIn>> mappedByPrice = mapByPrice(bookedIns);

        //for each pricecategory render one row with amount, price and total
        for (Entry<BigDecimal, ArrayList<BookedIn>> e : mappedByPrice.entrySet()) {
            ArrayList<BookedIn> list = e.getValue();

            int amount = list.size();

            Row<PDPage> itemDetailRow = table.createRow(30);
            Cell<PDPage> itemDetailCell;

            itemDetailCell = itemDetailRow.createCell(amountWidth, amount + "");
            itemDetailCell.setAlign(HorizontalAlignment.CENTER);
            itemDetailCell.setFontSize(14);
            itemDetailCell.setFont(plain);

            BookedIn bookedIn = list.get(0);
            Sector sector = bookedIn.getTicket().getSeat().getSector();
            SeatingPlan seatingPlan = sector.getSeatingPlan();
            Location location = seatingPlan.getLocation();
            Show show = bookedIns.iterator().next().getTicket().getShow();
            String date = show.getDate().toLocalDate().toString();
            String eventName = show.getEvent().getName();
            String descriptionLine1 = eventName + ", " + date;
            String descriptionLine2 =
                " Sector " + sector.getSectorId() + ", " + seatingPlan.getName() + ", "
                    + location.getName();

            itemDetailCell = itemDetailRow.createCell(descriptionWidth,
                descriptionLine1 + "<br />" + descriptionLine2);
            itemDetailCell.setFontSize(14);
            itemDetailCell.setFont(plain);

        }

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
        BookingType type)
        throws IOException {
        cs.beginText();
        cs.setFont(plain, 20);
        cs.newLineAtOffset(xposition, yposition);
        if (type.equals(BookingType.PURCHASE)) {
            String subtitle = "Invoice";
            cs.showText(subtitle);
        } else if (type.equals(BookingType.CANCELLATION)) {
            cs.setNonStrokingColor(Color.RED);
            String subtitleCancellation = "Cancellation Invoice";
            cs.showText(subtitleCancellation);
        } else {
            cs.setNonStrokingColor(Color.gray);
            String subtitleReservation = "Reservation";
            cs.showText(subtitleReservation);
        }
        cs.setNonStrokingColor(Color.BLACK);
        cs.endText();
    }
}
