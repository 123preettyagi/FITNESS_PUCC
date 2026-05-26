package com.example.VAHAN_API.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

import com.example.VAHAN_API.entity.VehicleTax;


public class PdfGenerator {

    public static byte[] generateTaxReceiptPdf(VehicleTax tax) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("Vahan Tax Receipt", titleFont));
            document.add(new Paragraph("------------------------------------------------", normalFont));

            document.add(new Paragraph("Receipt Number: " + tax.getReceiptNumber(), normalFont));
            document.add(new Paragraph("Registration Number: " + tax.getRegistrationNumber(), normalFont));
            document.add(new Paragraph("Tax Type: " + tax.getTaxType(), normalFont));
            document.add(new Paragraph("Tax Period: " + tax.getTaxPeriod(), normalFont));
            document.add(new Paragraph("Amount Paid: ₹" + tax.getAmount(), normalFont));
            document.add(new Paragraph("Payment Date: " + tax.getPaymentDate().format(DateTimeFormatter.ISO_DATE), normalFont));
            document.add(new Paragraph("Valid Till: " + tax.getValidTill().format(DateTimeFormatter.ISO_DATE), normalFont));

            document.add(new Paragraph("------------------------------------------------", normalFont));
            document.add(new Paragraph("Thank you for paying your vehicle tax!", normalFont));

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    public static void savePdf(byte[] pdfBytes, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error saving PDF: " + e.getMessage());
        }
    }
}