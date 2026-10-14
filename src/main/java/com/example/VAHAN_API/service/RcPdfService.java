package com.example.VAHAN_API.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.VAHAN_API.entity.RegistrationCertificate;
import com.example.VAHAN_API.repository.RegistrationCertificateRepository;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class RcPdfService {

    @Autowired
    private RegistrationCertificateRepository rcRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    // ------------------------------------------------------------------
    // PALETTE
    // ------------------------------------------------------------------
    private static final BaseColor NAVY         = new BaseColor(0, 51, 102);
    private static final BaseColor NAVY_LIGHT   = new BaseColor(232, 240, 250);
    private static final BaseColor IND_BLUE     = new BaseColor(0, 51, 153);
    private static final BaseColor BORDER_GREY  = new BaseColor(120, 130, 145);
    private static final BaseColor GREEN_BG     = new BaseColor(220, 245, 220);
    private static final BaseColor GREEN_FG     = new BaseColor(0, 110, 0);
    private static final BaseColor ORANGE_BG    = new BaseColor(255, 238, 215);
    private static final BaseColor ORANGE_FG    = new BaseColor(180, 90, 0);
    private static final BaseColor RED_BG       = new BaseColor(255, 222, 222);
    private static final BaseColor RED_FG       = new BaseColor(170, 0, 0);
    private static final BaseColor YELLOW_BG    = new BaseColor(248, 246, 220);
    private static final BaseColor YELLOW_FG    = new BaseColor(120, 90, 0);

    // ------------------------------------------------------------------
    // FONTS
    // ------------------------------------------------------------------
    private static final Font F_GOVT    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, NAVY);
    private static final Font F_MIN     = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.DARK_GRAY);
    private static final Font F_VAHAN   = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);
    private static final Font F_TITLE   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.WHITE);
    private static final Font F_SUB     = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7.5f, BaseColor.DARK_GRAY);
    private static final Font F_SECTION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9.5f, BaseColor.WHITE);
    private static final Font F_LABEL   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, new BaseColor(60, 70, 85));
    private static final Font F_VALUE   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9.5f, BaseColor.BLACK);
    private static final Font F_PLATE   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
    private static final Font F_IND     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    private static final Font F_BADGE   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);
    private static final Font F_NOTE    = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.DARK_GRAY);
    private static final Font F_FOOT    = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7.5f, BaseColor.GRAY);
    private static final Font F_SIGN    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);
    private static final Font F_OWNER   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, NAVY);

    // ==================================================================
    // PUBLIC API
    // ==================================================================

    /**
     * Generate RC PDF using registration number + last 5 digits of chassis (verification)
     */
    public byte[] generateRcPdf(String registrationNumber, String chassisLast5) {

        RegistrationCertificate rc = rcRepository
                .findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException(
                        "RC not found for registration number: " + registrationNumber));

        String chassis = rc.getChassisNumber();
        if (chassis == null || chassis.length() < 5) {
            throw new RuntimeException("Chassis number is invalid or too short.");
        }

        String actualLast5 = chassis.substring(chassis.length() - 5);

        if (chassisLast5 == null || !actualLast5.equalsIgnoreCase(chassisLast5.trim())) {
            throw new RuntimeException(
                    "Verification failed: Chassis last 5 digits do not match for registration number "
                            + registrationNumber);
        }

        return buildPdf(rc);
    }

    /**
     * Generate RC PDF using registration number only
     */
    public byte[] generateRcPdf(String registrationNumber) {
        RegistrationCertificate rc = rcRepository
                .findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException(
                        "RC not found for registration number: " + registrationNumber));
        return buildPdf(rc);
    }

    /**
     * Generate RC PDF using RC id
     */
    public byte[] generateRcPdfById(Long id) {
        RegistrationCertificate rc = rcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RC not found with id: " + id));
        return buildPdf(rc);
    }

    // ==================================================================
    // PDF BUILDER
    // ==================================================================
    private byte[] buildPdf(RegistrationCertificate rc) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 22, 22, 20, 20);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // ---- Outer government-style frame -------------------------
            PdfPTable frame = new PdfPTable(1);
            frame.setWidthPercentage(100);
            frame.setSplitLate(false);

            PdfPCell frameCell = new PdfPCell();
            frameCell.setPadding(0);
            frameCell.setBorder(Rectangle.BOX);
            frameCell.setBorderWidth(1.6f);
            frameCell.setBorderColor(NAVY);

            frameCell.addElement(buildHeader());
            frameCell.addElement(buildNumberPlate(rc));
            frameCell.addElement(buildOwnerBanner(rc));      // <-- OWNER NAME (new)
            frameCell.addElement(buildStatusBadge(rc));
            frameCell.addElement(gap(6));

            // ---- OWNER PARTICULARS (new section) ----------------------
            frameCell.addElement(buildSectionHeader("OWNER PARTICULARS"));
            frameCell.addElement(buildOwnerTable(rc));

            frameCell.addElement(gap(8));
            frameCell.addElement(buildSectionHeader("VEHICLE PARTICULARS"));
            frameCell.addElement(buildVehicleTable(rc));

            frameCell.addElement(gap(8));
            frameCell.addElement(buildSectionHeader("REGISTRATION PARTICULARS"));
            frameCell.addElement(buildRegistrationTable(rc));

            frameCell.addElement(gap(8));
            frameCell.addElement(buildSectionHeader("REGISTERING AUTHORITY"));
            frameCell.addElement(buildRtoTable(rc));

            frameCell.addElement(gap(10));
            frameCell.addElement(buildDeclaration(rc));

            frameCell.addElement(buildFooter(rc));

            frame.addCell(frameCell);
            document.add(frame);

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error while generating RC PDF: " + e.getMessage(), e);
        }

        return out.toByteArray();
    }

    // ------------------------------------------------------------------
    // HEADER
    // ------------------------------------------------------------------
    private PdfPTable buildHeader() {

        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);

        PdfPCell top = new PdfPCell();
        top.setBorder(Rectangle.NO_BORDER);
        top.setBackgroundColor(BaseColor.WHITE);
        top.setPaddingTop(12);
        top.setPaddingBottom(8);

        Paragraph govt = new Paragraph("GOVERNMENT OF INDIA", F_GOVT);
        govt.setAlignment(Element.ALIGN_CENTER);
        top.addElement(govt);

        Paragraph ministry = new Paragraph("MINISTRY OF ROAD TRANSPORT AND HIGHWAYS", F_MIN);
        ministry.setAlignment(Element.ALIGN_CENTER);
        ministry.setSpacingBefore(2);
        top.addElement(ministry);

        Paragraph vahan = new Paragraph("STATE TRANSPORT AUTHORITY   |   VAHAN NATIONAL REGISTER", F_VAHAN);
        vahan.setAlignment(Element.ALIGN_CENTER);
        vahan.setSpacingBefore(2);
        top.addElement(vahan);

        header.addCell(top);

        // Title bar
        PdfPCell title = new PdfPCell(new Phrase("REGISTRATION CERTIFICATE", F_TITLE));
        title.setBackgroundColor(NAVY);
        title.setBorder(Rectangle.NO_BORDER);
        title.setPaddingTop(7);
        title.setPaddingBottom(7);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.addCell(title);

        // Sub-title
        PdfPCell sub = new PdfPCell(new Phrase(
                "Issued under Section 41 of the Motor Vehicles Act, 1988 read with Rule 47 of the "
                        + "Central Motor Vehicles Rules, 1989",
                F_SUB));
        sub.setBorder(Rectangle.NO_BORDER);
        sub.setPaddingTop(5);
        sub.setPaddingBottom(3);
        sub.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.addCell(sub);

        return header;
    }

    // ------------------------------------------------------------------
    // NUMBER PLATE
    // ------------------------------------------------------------------
    private PdfPTable buildNumberPlate(RegistrationCertificate rc) throws DocumentException {

        PdfPTable plate = new PdfPTable(2);
        plate.setWidthPercentage(62);
        plate.setHorizontalAlignment(Element.ALIGN_CENTER);
        plate.setWidths(new float[]{1f, 5.5f});
        plate.setSpacingBefore(4);

        // IND strip
        PdfPCell ind = new PdfPCell(new Phrase("IND", F_IND));
        ind.setBackgroundColor(IND_BLUE);
        ind.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        ind.setBorderWidth(1.4f);
        ind.setBorderColor(BaseColor.BLACK);
        ind.setHorizontalAlignment(Element.ALIGN_CENTER);
        ind.setVerticalAlignment(Element.ALIGN_MIDDLE);
        ind.setPaddingTop(14);
        ind.setPaddingBottom(14);
        plate.addCell(ind);

        // Registration number
        PdfPCell num = new PdfPCell(new Phrase(
                safe(rc.getRegistrationNumber()).toUpperCase(), F_PLATE));
        num.setBackgroundColor(BaseColor.WHITE);
        num.setBorder(Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM);
        num.setBorderWidth(1.4f);
        num.setBorderColor(BaseColor.BLACK);
        num.setHorizontalAlignment(Element.ALIGN_CENTER);
        num.setVerticalAlignment(Element.ALIGN_MIDDLE);
        num.setPaddingTop(6);
        num.setPaddingBottom(6);
        plate.addCell(num);

        return plate;
    }

    // ------------------------------------------------------------------
    // OWNER BANNER (shown right below the number plate)
    // ------------------------------------------------------------------
    private PdfPTable buildOwnerBanner(RegistrationCertificate rc) {

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(62);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(NAVY_LIGHT);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(NAVY);
        cell.setBorderWidth(0.8f);
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph label = new Paragraph("REGISTERED OWNER",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, new BaseColor(60, 70, 85)));
        label.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(label);

        Paragraph name = new Paragraph(
                safe(rc.getOwnerName()).toUpperCase(), F_OWNER);
        name.setAlignment(Element.ALIGN_CENTER);
        name.setSpacingBefore(2);
        cell.addElement(name);

        table.addCell(cell);
        return table;
    }

    // ------------------------------------------------------------------
    // OWNER PARTICULARS TABLE
    // ------------------------------------------------------------------
    private PdfPTable buildOwnerTable(RegistrationCertificate rc) throws DocumentException 
    {

        PdfPTable t = newDetailTable();
        
        return t;
    }

    // ------------------------------------------------------------------
    // STATUS BADGE
    // ------------------------------------------------------------------
    private PdfPTable buildStatusBadge(RegistrationCertificate rc) {

        String status = safe(rc.getRegistrationStatus()).toUpperCase();

        BaseColor bg;
        BaseColor fg;

        if ("ACTIVE".equals(status)) {
            bg = GREEN_BG;
            fg = GREEN_FG;
        } else if ("EXPIRED".equals(status)) {
            bg = ORANGE_BG;
            fg = ORANGE_FG;
        } else if ("CANCELLED".equals(status)) {
            bg = RED_BG;
            fg = RED_FG;
        } else {
            bg = YELLOW_BG;
            fg = YELLOW_FG;
        }

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(62);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell(new Phrase("STATUS : " + status,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, fg)));
        cell.setBackgroundColor(bg);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(fg);
        cell.setBorderWidth(0.7f);
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cell);
        return table;
    }

    // ------------------------------------------------------------------
    // SECTION HEADER
    // ------------------------------------------------------------------
    private PdfPTable buildSectionHeader(String title) {

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase(title, F_SECTION));
        cell.setBackgroundColor(NAVY);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        cell.setPaddingLeft(8);

        table.addCell(cell);
        return table;
    }

    // ------------------------------------------------------------------
    // DETAIL TABLES
    // ------------------------------------------------------------------
    private PdfPTable newDetailTable() throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.15f, 1.85f, 1.15f, 1.85f});
        return table;
    }

    private PdfPTable buildVehicleTable(RegistrationCertificate rc) throws DocumentException {

        PdfPTable t = newDetailTable();

        addRow(t,
                "Chassis Number", safe(rc.getChassisNumber()),
                "Engine Number", safe(rc.getEngineNumber()));

        addRow(t,
                "Vehicle Class", safe(rc.getVehicleClass()),
                "Vehicle Type", safe(rc.getVehicleType()));

        addRow(t,
                "Maker / Manufacturer", safe(rc.getManufacturer()),
                "Model", safe(rc.getModel()));

        addRow(t,
                "Fuel Type", safe(rc.getFuelType()),
                "Year of Manufacture", rc.getYearOfManufacture() != null
                        ? String.valueOf(rc.getYearOfManufacture()) : "N/A");

        addRow(t,
                "Colour", safe(rc.getColor()),
                "Seating Capacity", rc.getSeatingCapacity() != null
                        ? String.valueOf(rc.getSeatingCapacity()) : "N/A");

        return t;
    }

    private PdfPTable buildRegistrationTable(RegistrationCertificate rc) throws DocumentException {

        PdfPTable t = newDetailTable();

        addRow(t,
                "Registration Number", safe(rc.getRegistrationNumber()),
                "Date of Registration", formatDate(rc.getRegistrationDate()));

        addRow(t,
                "Registration Type", safe(rc.getRegistrationType()),
                "Registration Valid Upto", formatDate(rc.getRegistrationExpiryDate()));

        return t;
    }

    private PdfPTable buildRtoTable(RegistrationCertificate rc) throws DocumentException {

        PdfPTable t = newDetailTable();

        addRow(t,
                "State Code", safe(rc.getStateCode()),
                "Registering Authority (RTO)", safe(rc.getRtoOffice()));

        return t;
    }

    // ------------------------------------------------------------------
    // DECLARATION
    // ------------------------------------------------------------------
    private PdfPTable buildDeclaration(RegistrationCertificate rc) {

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(BORDER_GREY);
        cell.setBorderWidth(0.6f);
        cell.setBackgroundColor(new BaseColor(250, 250, 250));
        cell.setPadding(8);

        Paragraph heading = new Paragraph("DECLARATION",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, NAVY));
        heading.setSpacingAfter(3);
        cell.addElement(heading);

        Paragraph body = new Paragraph(
                "This Certificate of Registration is issued subject to the provisions of the Motor Vehicles Act, 1988 "
                        + "and the Central Motor Vehicles Rules, 1989. The vehicle shall not be used for any purpose other "
                        + "than that for which it is registered. Any change in the particulars of the vehicle or its "
                        + "ownership shall be intimated to the Registering Authority within thirty (30) days. This "
                        + "certificate is not transferable except as provided under the Act.",
                F_NOTE);
        body.setAlignment(Element.ALIGN_JUSTIFIED);
        cell.addElement(body);

        String status = safe(rc.getRegistrationStatus()).toUpperCase();
        String statusLine;
        if ("ACTIVE".equals(status)) {
            statusLine = "Current Status: VALID AND ACTIVE.";
        } else if ("EXPIRED".equals(status)) {
            statusLine = "Current Status: EXPIRED - renewal is required before the vehicle is used on public roads.";
        } else if ("CANCELLED".equals(status)) {
            statusLine = "Current Status: CANCELLED - this certificate is no longer valid.";
        } else {
            statusLine = "Current Status: " + status + ".";
        }

        Paragraph st = new Paragraph(statusLine,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK));
        st.setSpacingBefore(5);
        cell.addElement(st);

        table.addCell(cell);
        return table;
    }

    // ------------------------------------------------------------------
    // FOOTER
    // ------------------------------------------------------------------
    private PdfPTable buildFooter(RegistrationCertificate rc) {

        PdfPTable outer = new PdfPTable(1);
        outer.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(8);
        cell.setPaddingTop(4);

        // separator line
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        PdfPCell lineCell = new PdfPCell(new Phrase(" ",
                FontFactory.getFont(FontFactory.HELVETICA, 1)));
        lineCell.setBorder(Rectangle.TOP);
        lineCell.setBorderColor(BORDER_GREY);
        lineCell.setBorderWidth(0.6f);
        lineCell.setPadding(0);
        line.addCell(lineCell);
        cell.addElement(line);

        Paragraph note = new Paragraph(
                "Note: This is a computer-generated document. It is valid only when verified with the original "
                        + "Registration Certificate available at the Registering Authority (RTO).",
                F_NOTE);
        note.setAlignment(Element.ALIGN_CENTER);
        note.setSpacingBefore(5);
        cell.addElement(note);

        Paragraph generated = new Paragraph(
                "Generated on: " + DATE_FORMAT.format(new Date())
                        + "   |   RC No: " + safe(rc.getRegistrationNumber())
                        + "   |   VAHAN Portal",
                F_FOOT);
        generated.setAlignment(Element.ALIGN_CENTER);
        generated.setSpacingBefore(3);
        cell.addElement(generated);

        Paragraph sign = new Paragraph("_________________________\nRegistering Authority", F_SIGN);
        sign.setAlignment(Element.ALIGN_RIGHT);
        sign.setSpacingBefore(16);
        cell.addElement(sign);

        outer.addCell(cell);
        return outer;
    }

    // ==================================================================
    // HELPERS
    // ==================================================================
    private void addRow(PdfPTable table,
                        String label1, String value1,
                        String label2, String value2) {

        table.addCell(labelCell(label1));
        table.addCell(valueCell(value1));
        table.addCell(labelCell(label2));
        table.addCell(valueCell(value2));
    }

    /** Adds a full-width row (label spanning column 1, value spanning 2-4). */
    private void addFullWidthRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = labelCell(label);
        labelCell.setColspan(1);
        table.addCell(labelCell);

        PdfPCell valueCell = valueCell(value);
        valueCell.setColspan(3);
        table.addCell(valueCell);
    }

    private PdfPCell labelCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, F_LABEL));
        cell.setBackgroundColor(NAVY_LIGHT);
        cell.setPadding(5);
        cell.setBorderColor(BORDER_GREY);
        cell.setBorderWidth(0.6f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell valueCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, F_VALUE));
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setPadding(5);
        cell.setBorderColor(BORDER_GREY);
        cell.setBorderWidth(0.6f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private Paragraph gap(float height) {
        Paragraph p = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, height));
        p.setLeading(height);
        p.setSpacingAfter(0);
        return p;
    }

    private String safe(String value) {
        return (value == null || value.trim().isEmpty()) ? "N/A" : value.trim();
    }

    private String formatDate(Date date) {
        return (date == null) ? "N/A" : DATE_FORMAT.format(date);
    }

   
  
}