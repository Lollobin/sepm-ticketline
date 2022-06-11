package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE1;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_ADDRESS_LINE2;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_MAIL;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_PHONE;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.COMPANY_WEB;

import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

@Service
public class InvoicePdfServiceImpl {


    private static String TITLE = "Ticketline";
    private static String SUBTITLE = "Invoice";
    private static PDFont plain = PDType1Font.HELVETICA;
    private static PDFont italic = PDType1Font.HELVETICA_OBLIQUE;
    private static PDFont bold = PDType1Font.HELVETICA_BOLD;
    private static float marginBody = 30;




    public PDDocument buildInvoicePdf() throws IOException {
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

        cs.beginText();
        cs.setFont(bold, 14);
        cs.setLeading(20f);
        cs.newLineAtOffset(xoffset, yoffset);
        cs.showText("Customer: ");
        cs.newLine();
        cs.setFont(plain, 14);
        cs.newLine();
        //hier Kundendaten einfügen!
        cs.endText();
        yoffset -= 20;

        cs.close();
        invoice.save("/Users/lilla/Desktop/testpdf.pdf");
        invoice.close();

        return invoice;
    }
}
