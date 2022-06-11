package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE1;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE2;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_MAIL;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_PHONE;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_UID;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_WEB;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.TAX_RATE_CULTURE;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.line.LineStyle;
import java.awt.Color;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.stereotype.Service;

@Service
public class InvoicePdfServiceImpl {


    private static String TITLE = "Ticketline";
    private static String SUBTITLE = "Invoice";
    private static PDFont plain = PDType1Font.HELVETICA;
    private static PDFont italic = PDType1Font.HELVETICA_OBLIQUE;
    private static PDFont bold = PDType1Font.HELVETICA_BOLD;
    private static float marginBody = 30;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private TransactionRepository transactionRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public InvoicePdfServiceImpl(
        TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public PDDocument getInvoicePdf(Long id) throws IOException {
        Optional<Transaction> t = transactionRepository.findById(id);
        if (t.isPresent()) {
            return buildInvoicePdf(t.get());

        }
        return null;
    }

    private PDDocument buildInvoicePdf(Transaction transaction) throws IOException {

        ApplicationUser user = transaction.getUser();
        Set<BookedIn> bookedIns = transaction.getBookedIns();

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

        LocalDate issuingDate = transaction.getDate().toLocalDate();

        cs.beginText();
        cs.setFont(plain, 24);
        cs.setNonStrokingColor(Color.DARK_GRAY);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.showText(TITLE);
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
        cs.showText("UID:" +COMPANY_UID);
        cs.endText();
        yoffset -= 20;

        cs.beginText();
        cs.setFont(plain, 20);
        cs.setStrokingColor(Color.BLACK);
        cs.newLineAtOffset(xoffset + 2, yoffset);
        cs.showText(SUBTITLE);
        cs.endText();

        yoffset -= 80;

        cs.moveTo(xoffset, yoffset);
        cs.lineTo(xend, yoffset);
        cs.setStrokingColor(Color.lightGray);
        cs.stroke();

        yoffset -= 30;

        float yoffsetBeforeCustData = yoffset;
        float xhalfOfPage = rectangle.getWidth() * 0.5f;

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

        yoffset -= 100;

        cs.beginText();
        cs.setFont(bold, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xhalfOfPage, yoffsetBeforeCustData);
        cs.showText("Issuing Date: ");

        cs.newLine();
        cs.showText("Bill No.: ");

        cs.endText();

        cs.beginText();
        cs.setFont(plain, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xhalfOfPage + xhalfOfPage * 0.5f, yoffsetBeforeCustData);
        cs.showText(issuingDate.toString());
        cs.newLine();
        cs.showText(transaction.getTransactionId().toString());
        cs.endText();

        boolean drawLines = true;
        boolean drawContent = true;
        float tableWidth = rectangle.getWidth() - 2 * marginBody - 30;
        BaseTable table = new BaseTable(yoffset, yoffset - marginBody, 70, tableWidth, marginBody,
            invoice, invoicePage, drawLines, drawContent);

        Row<PDPage> headerRow = table.createRow(40);
        Cell<PDPage> headerCell = headerRow.createCell(100, "Items");
        headerCell.setFont(bold);
        headerCell.setFontSize(14);
        // vertical alignment
        headerCell.setValign(VerticalAlignment.MIDDLE);
        // border style
        headerCell.setTopBorderStyle(new LineStyle(Color.BLACK, 1));
        table.addHeaderRow(headerRow);

        Row<PDPage> itemRow = table.createRow(20);
        Cell<PDPage> itemCell;

        itemCell = itemRow.createCell(12, "<b>Amount</b>");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        itemCell = itemRow.createCell(33, "<b>Description</b>");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        itemCell = itemRow.createCell(15, "<b>Unit Price</b><br />(Gross)");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);
        itemCell = itemRow.createCell(25, "<b>Total</b><br />(Gross)");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);
        itemCell = itemRow.createCell(15, "<b>Tax rate</b><br />(Ust.)");
        itemCell.setFontSize(12);
        itemCell.setFont(plain);

        float subTotal = 0f;

        for (BigDecimal key : mappedByPrice.keySet()) {
            ArrayList<BookedIn> list = mappedByPrice.get(key);
            int amount = list.size();
            float unitPrice = key.floatValue();
            float total = unitPrice * amount;
            subTotal += total;

            Row<PDPage> itemDetailRow = table.createRow(20);
            Cell<PDPage> itemDetailCell;

            itemDetailCell = itemDetailRow.createCell(12, amount + "");
            itemDetailCell.setFontSize(11);
            itemDetailCell.setFont(plain);

            BookedIn bookedIn = list.get(0);
            String seatingPlan = bookedIn.getTicket().getSeat().getSector().getSeatingPlan()
                .getName();
            String location = bookedIn.getTicket().getSeat().getSector().getSeatingPlan()
                .getLocation().getName();
            Show show = bookedIns.iterator().next().getTicket().getShow();
            String date = show.getDate().toLocalDate().toString();
            String eventName = show.getEvent().getName();
            String descriptionLine1 = eventName + ", " + date;
            String descriptionLine2 = seatingPlan + ", " + location;
            itemDetailCell = itemDetailRow.createCell(33,
                descriptionLine1 + "<br />" + descriptionLine2);
            itemDetailCell.setFontSize(12);
            itemDetailCell.setFont(plain);

            itemDetailCell = itemDetailRow.createCell(15, unitPrice +" €");
            itemDetailCell.setFontSize(12);
            itemDetailCell.setFont(plain);

            itemDetailCell = itemDetailRow.createCell(25, total +" €");
            itemDetailCell.setFontSize(12);
            itemDetailCell.setFont(plain);

            itemDetailCell = itemDetailRow.createCell(15, TAX_RATE_CULTURE + "");
            itemDetailCell.setFontSize(12);
            itemDetailCell.setFont(plain);
        }
        Row<PDPage> endRow  = table.createRow(5);
        Cell<PDPage> separator = endRow.createCell(100,"");
        separator.setFillColor(Color.BLACK);
        Row<PDPage> footerRow1 = table.createRow(30);
        LineStyle noLine = new LineStyle(Color.WHITE, 0);
        Cell<PDPage> footerDetail1 = footerRow1.createCell(75, "Subtotal: ");
        footerDetail1.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        footerDetail1.setRightBorderStyle(noLine);
        footerDetail1.setFontSize(13);
        footerDetail1.setFont(bold);
        footerDetail1.setAlign(HorizontalAlignment.RIGHT);
        footerDetail1 = footerRow1.createCell(25, df.format(subTotal) +" €");
        footerDetail1.setTopBorderStyle(new LineStyle(Color.BLACK, 10));
        footerDetail1.setLeftBorderStyle(noLine);
        footerDetail1.setFontSize(12);
        footerDetail1.setFont(plain);

        Row<PDPage> footerRow2 = table.createRow(30);
        Cell<PDPage> footerDetail2 = footerRow2.createCell(75, "Net: ");
        footerDetail2.setTopBorderStyle(noLine);
        footerDetail2.setBottomBorderStyle(noLine);
        footerDetail2.setRightBorderStyle(noLine);
        footerDetail2.setFontSize(13);
        footerDetail2.setFont(bold);
        footerDetail2.setAlign(HorizontalAlignment.RIGHT);
        footerDetail2 = footerRow2.createCell(25, df.format( calculateNet(subTotal, TAX_RATE_CULTURE))  +" €");
        footerDetail2.setTopBorderStyle(noLine);
        footerDetail2.setBottomBorderStyle(noLine);
        footerDetail2.setLeftBorderStyle(noLine);
        footerDetail2.setFontSize(12);
        footerDetail2.setFont(plain);

        Row<PDPage> footerRow3 = table.createRow(30);
        Cell<PDPage> footerDetail3 = footerRow3.createCell(75, "Tax: ");
        footerDetail3.setTopBorderStyle(noLine);
        footerDetail3.setRightBorderStyle(noLine);
        footerDetail3.setFontSize(13);
        footerDetail3.setFont(bold);
        footerDetail3.setAlign(HorizontalAlignment.RIGHT);

        footerDetail3 = footerRow3.createCell(25, df.format(calculateTax(subTotal, TAX_RATE_CULTURE)) +" €");
        footerDetail3.setTopBorderStyle(noLine);
        footerDetail3.setLeftBorderStyle(noLine);
        footerDetail3.setFontSize(12);
        footerDetail3.setFont(plain);




        cs.close();

        table.draw();
        invoice.save("/Users/lilla/Desktop/testpdf100.pdf");
        invoice.close();

        return invoice;
    }


    private float calculateNet(float gross, float taxRate) {
        return gross * (100 / (100 + taxRate));
    }

    private float calculateTax(float gross, float taxRate) {
        return gross - calculateNet(gross, taxRate);
    }
}
