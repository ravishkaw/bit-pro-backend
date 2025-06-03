package com.ravi.waterlilly.utils;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.ravi.waterlilly.payload.pdfGenerator.LineItem;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGenerator {

    // Optimized color palette - using only specified colors
    private static final DeviceRgb SUCCESS_GREEN = new DeviceRgb(82, 196, 26);     // #52c41a
    private static final DeviceRgb PRIMARY_BLUE = new DeviceRgb(102, 108, 255);    // #666cff
    private static final DeviceRgb ERROR_RED = new DeviceRgb(245, 34, 45);         // #f5222d
    private static final DeviceRgb WARNING_ORANGE = new DeviceRgb(250, 173, 20);   // #faad14

    // Additional neutral colors for backgrounds
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(248, 249, 250);
    private static final DeviceRgb DARK_TEXT = new DeviceRgb(45, 45, 45);
    private static final DeviceRgb WHITE = new DeviceRgb(255, 255, 255);

    // Document type enum
    public enum DocumentType {
        INVOICE, RECEIPT
    }

    /**
     * Optimized method for generating compact, professional hotel receipts
     */
    public byte[] generateDetailedHotelReceipt(
            DocumentType documentType,
            String documentNo,
            LocalDateTime issueDate,
            LocalDateTime dueDate,
            String guestName,
            String guestEmail,
            String guestPhone,
            String guestAddress,
            String roomNumber,
            String roomType,
            LocalDateTime checkInDate,
            LocalDateTime reservedCheckOutDate,
            LocalDateTime actualCheckInDate,
            LocalDateTime actualCheckOutDate,
            Integer totalNights,
            List<LineItem> lineItems,
            BigDecimal subtotal,
            BigDecimal discount,
            BigDecimal tax,
            BigDecimal totalPrice,
            BigDecimal netAmountPayable,
            BigDecimal overpaymentRefund,
            String paymentMethod,
            String paymentStatus
    ) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(25, 25, 25, 25);

            // Load fonts
            PdfFont regularFont = PdfFontFactory.createFont();
            PdfFont boldFont = PdfFontFactory.createFont();

            // Compact header section
            createCompactHeader(document, documentType, boldFont, regularFont);

            // Horizontal document info and guest info
            createHorizontalInfoSection(document, documentType, documentNo, issueDate, dueDate,
                    paymentStatus, guestName, guestEmail, guestPhone, guestAddress,
                    regularFont, boldFont);

            // Compact reservation details
            createCompactReservationInfo(document, roomNumber, roomType, checkInDate,
                    reservedCheckOutDate, actualCheckInDate, actualCheckOutDate,
                    totalNights, regularFont, boldFont);

            // Optimized line items section
            createOptimizedLineItemsSection(document, lineItems, regularFont, boldFont);

            // Compact totals section
            createCompactTotalsSection(document, subtotal, discount, tax, totalPrice,
                    netAmountPayable, overpaymentRefund, paymentMethod, paymentStatus,
                    regularFont, boldFont);

            // Minimal footer
            createMinimalFooter(document, documentType, regularFont);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    private void createCompactHeader(Document document, DocumentType docType, PdfFont boldFont, PdfFont regularFont) throws MalformedURLException {
        // Single row header with logo, company info, and document type
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1.5f})).useAllAvailableWidth();
        headerTable.setBorder(Border.NO_BORDER);
        headerTable.setMarginBottom(10);

        // Logo section
        Cell logoCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setPadding(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        ImageData imageData = ImageDataFactory.create("src/main/resources/assets/logo-black.png");
        Image logoImage = new Image(imageData);

        logoImage.setWidth(100);

        // Add the image to the cell
        logoCell.add(logoImage);

        // Company info section
        Cell companyCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setPaddingLeft(15)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        companyCell.add(new Paragraph("Villa Water Lilly")
                .setFont(boldFont)
                .setFontSize(18)
                .setFontColor(DARK_TEXT)
                .setMarginBottom(2));

        companyCell.add(new Paragraph("508 Akuressa Road, Galle 80000 | +94 74 243 5447")
                .setFont(regularFont)
                .setFontSize(9)
                .setFontColor(DARK_TEXT)
                .setMarginBottom(1));

        companyCell.add(new Paragraph("reservations@villawaterlilly.com")
                .setFont(regularFont)
                .setFontSize(9)
                .setFontColor(PRIMARY_BLUE));

        // Document type section
        Cell docTypeCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(getDocumentTypeColor(docType))
                .setPadding(12)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        docTypeCell.add(new Paragraph(docType.name())
                .setFont(boldFont)
                .setFontSize(16)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMargin(0));

        headerTable.addCell(logoCell);
        headerTable.addCell(companyCell);
        headerTable.addCell(docTypeCell);
        document.add(headerTable);
    }

    private void createHorizontalInfoSection(Document document, DocumentType docType, String documentNo,
                                             LocalDateTime issueDate, LocalDateTime dueDate, String paymentStatus,
                                             String guestName, String guestEmail, String guestPhone, String guestAddress,
                                             PdfFont regularFont, PdfFont boldFont) {

        // Horizontal layout: Document info | Guest info | Payment status
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2, 1})).useAllAvailableWidth();
        infoTable.setMarginTop(5).setMarginBottom(10);

        // Document info
        Cell docInfoCell = new Cell()
                .setBorder(new SolidBorder(LIGHT_GRAY, 1))
                .setPadding(10)
                .setBackgroundColor(LIGHT_GRAY);

        docInfoCell.add(createCompactInfoRow(docType.name() + " #", documentNo, regularFont, boldFont));
        docInfoCell.add(createCompactInfoRow("Date", issueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), regularFont, boldFont));

        if (dueDate != null && docType == DocumentType.INVOICE) {
            docInfoCell.add(createCompactInfoRow("Due", dueDate.format(DateTimeFormatter.ofPattern("MMM dd")), regularFont, boldFont));
        }

        // Guest info
        Cell guestInfoCell = new Cell()
                .setBorder(new SolidBorder(LIGHT_GRAY, 1))
                .setPadding(10)
                .setBackgroundColor(WHITE);

        guestInfoCell.add(new Paragraph(guestName)
                .setFont(boldFont)
                .setFontSize(12)
                .setFontColor(DARK_TEXT)
                .setMarginBottom(3));

        if (guestAddress != null && !guestAddress.trim().isEmpty()) {
            guestInfoCell.add(new Paragraph(guestAddress)
                    .setFont(regularFont)
                    .setFontSize(8)
                    .setFontColor(DARK_TEXT)
                    .setMarginBottom(2));
        }

        guestInfoCell.add(new Paragraph(guestEmail + " | " + guestPhone)
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(DARK_TEXT));

        // Payment status
        Cell statusCell = new Cell()
                .setBorder(new SolidBorder(getPaymentStatusColor(paymentStatus), 2))
                .setPadding(10)
                .setBackgroundColor(WHITE)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        statusCell.add(new Paragraph("STATUS")
                .setFont(boldFont)
                .setFontSize(8)
                .setFontColor(DARK_TEXT)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(3));

        statusCell.add(new Paragraph(paymentStatus)
                .setFont(boldFont)
                .setFontSize(12)
                .setFontColor(getPaymentStatusColor(paymentStatus))
                .setTextAlignment(TextAlignment.CENTER)
                .setMargin(0));

        infoTable.addCell(docInfoCell);
        infoTable.addCell(guestInfoCell);
        infoTable.addCell(statusCell);
        document.add(infoTable);
    }

    private void createCompactReservationInfo(Document document, String roomNumber, String roomType,
                                              LocalDateTime checkInDate, LocalDateTime reservedCheckOutDate,
                                              LocalDateTime actualCheckInDate, LocalDateTime actualCheckOutDate,
                                              Integer totalNights, PdfFont regularFont, PdfFont boldFont) {

        // Single row reservation details
        Table reservationTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1, 1})).useAllAvailableWidth();
        reservationTable.setMarginBottom(10);

        String[] labels = {"Room", "Reserved In", "Reserved Out", "Actual In", "Actual Out", "Nights"};
        String[] values = {
                roomNumber + " - " + roomType,
                checkInDate.format(DateTimeFormatter.ofPattern("MMM dd HH:mm")),
                reservedCheckOutDate.format(DateTimeFormatter.ofPattern("MMM dd HH:mm")),
                actualCheckInDate.format(DateTimeFormatter.ofPattern("MMM dd HH:mm")),
                actualCheckOutDate.format(DateTimeFormatter.ofPattern("MMM dd HH:mm")),
                totalNights.toString()
        };

        for (int i = 0; i < labels.length; i++) {
            Cell cell = new Cell()
                    .setBorder(new SolidBorder(LIGHT_GRAY, 0.5f))
                    .setPadding(6)
                    .setBackgroundColor(i % 2 == 0 ? LIGHT_GRAY : WHITE);

            cell.add(new Paragraph(labels[i])
                    .setFont(boldFont)
                    .setFontSize(8)
                    .setFontColor(DARK_TEXT)
                    .setMarginBottom(1));

            cell.add(new Paragraph(values[i])
                    .setFont(regularFont)
                    .setFontSize(9)
                    .setFontColor(DARK_TEXT)
                    .setMargin(0));

            reservationTable.addCell(cell);
        }

        document.add(reservationTable);
    }

    private void createOptimizedLineItemsSection(Document document, List<LineItem> lineItems,
                                                 PdfFont regularFont, PdfFont boldFont) {

        // Header with minimal spacing
        document.add(new Paragraph("CHARGES & SERVICES")
                .setFont(boldFont)
                .setFontSize(12)
                .setFontColor(DARK_TEXT)
                .setMarginBottom(5));

        // Compact line items table
        Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{2.5f, 2f, 1})).useAllAvailableWidth();
        itemsTable.setBorder(new SolidBorder(PRIMARY_BLUE, 1));

        // Compact header
        String[] headers = {"DESCRIPTION", "DETAILS", "AMOUNT"};
        for (String header : headers) {
            itemsTable.addHeaderCell(new Cell()
                    .add(new Paragraph(header).setFont(boldFont).setFontSize(9).setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(PRIMARY_BLUE)
                    .setPadding(6)
                    .setTextAlignment(header.equals("AMOUNT") ? TextAlignment.RIGHT : TextAlignment.LEFT));
        }

        // Compact line items
        boolean alternate = false;
        for (LineItem item : lineItems) {
            DeviceRgb bgColor = alternate ? LIGHT_GRAY : WHITE;
            DeviceRgb amountColor = getAmountColor(item.getType());

            itemsTable.addCell(new Cell()
                    .add(new Paragraph(item.getDescription()).setFont(boldFont).setFontSize(9))
                    .setBackgroundColor(bgColor)
                    .setPadding(5)
                    .setBorder(Border.NO_BORDER));

            itemsTable.addCell(new Cell()
                    .add(new Paragraph(item.getDetails()).setFont(regularFont).setFontSize(8).setFontColor(DARK_TEXT))
                    .setBackgroundColor(bgColor)
                    .setPadding(5)
                    .setBorder(Border.NO_BORDER));

            String amountText = formatAmount(item.getAmount(), item.getType());
            itemsTable.addCell(new Cell()
                    .add(new Paragraph(amountText).setFont(boldFont).setFontSize(9).setFontColor(amountColor))
                    .setBackgroundColor(bgColor)
                    .setPadding(5)
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.RIGHT));

            alternate = !alternate;
        }

        document.add(itemsTable);
    }

    private void createCompactTotalsSection(Document document, BigDecimal subtotal, BigDecimal discount,
                                            BigDecimal tax, BigDecimal totalPrice, BigDecimal netAmountPayable,
                                            BigDecimal overpaymentRefund, String paymentMethod, String paymentStatus,
                                            PdfFont regularFont, PdfFont boldFont) {

        // Horizontal layout: Payment info | Totals
        Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{1.5f, 1})).useAllAvailableWidth();
        totalsTable.setMarginTop(10);

        // Payment info cell
        Cell paymentCell = new Cell()
                .setBorder(new SolidBorder(LIGHT_GRAY, 1))
                .setPadding(10)
                .setBackgroundColor(LIGHT_GRAY);

        paymentCell.add(new Paragraph("PAYMENT INFO")
                .setFont(boldFont)
                .setFontSize(10)
                .setFontColor(DARK_TEXT)
                .setMarginBottom(5));

        paymentCell.add(createCompactInfoRow("Method", paymentMethod, regularFont, boldFont));
        paymentCell.add(createCompactInfoRow("Status", paymentStatus, regularFont, boldFont)
                .setFontColor(getPaymentStatusColor(paymentStatus)));

        // Totals cell
        Cell totalsCell = new Cell()
                .setBorder(new SolidBorder(PRIMARY_BLUE, 2))
                .setPadding(10)
                .setBackgroundColor(WHITE);

        totalsCell.add(createTotalRow("Subtotal", "Rs. " + subtotal, regularFont, regularFont));

        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            totalsCell.add(createTotalRow("Discount", "- Rs. " + discount, regularFont, regularFont)
                    .setFontColor(SUCCESS_GREEN));
        }

        totalsCell.add(createTotalRow("Tax & Service", "Rs. " + tax, regularFont, regularFont));

        // Separator line
        totalsCell.add(new LineSeparator(new SolidLine(1))
                .setMarginTop(5)
                .setMarginBottom(5));

        totalsCell.add(createTotalRow("TOTAL", "Rs. " + totalPrice, boldFont, boldFont)
                .setFontSize(14)
                .setFontColor(PRIMARY_BLUE));

        if (netAmountPayable.compareTo(totalPrice) != 0) {
            totalsCell.add(createTotalRow("NET PAYABLE", "Rs. " + netAmountPayable, boldFont, boldFont)
                    .setFontSize(12)
                    .setFontColor(WARNING_ORANGE));
        }

        if (overpaymentRefund.compareTo(BigDecimal.ZERO) > 0) {
            totalsCell.add(createTotalRow("REFUNDED", "Rs. " + overpaymentRefund, boldFont, boldFont)
                    .setFontColor(SUCCESS_GREEN));
        }

        totalsTable.addCell(paymentCell);
        totalsTable.addCell(totalsCell);
        document.add(totalsTable);
    }

    private void createMinimalFooter(Document document, DocumentType docType, PdfFont regularFont) {
        // Thank you message
        document.add(new Paragraph("Thank you for choosing Villa Water Lilly!")
                .setFont(regularFont)
                .setFontSize(16)
                .setFontColor(PRIMARY_BLUE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30)
                .setMarginBottom(20));

        // Contact information
        document.add(new Paragraph("For questions about this " + docType.name().toLowerCase() +
                ", please contact our billing department at billing@villawaterlilly.com")
                .setFont(regularFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));

        // Generation timestamp
        document.add(new Paragraph("Generated on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a")))
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(ColorConstants.GRAY)
                .setTextAlignment(TextAlignment.CENTER));
    }

    // Helper methods
    private DeviceRgb getDocumentTypeColor(DocumentType docType) {
        return docType == DocumentType.INVOICE ? WARNING_ORANGE : SUCCESS_GREEN;
    }

    private DeviceRgb getPaymentStatusColor(String paymentStatus) {
        return switch (paymentStatus.toUpperCase()) {
            case "PAID", "COMPLETED" -> SUCCESS_GREEN;
            case "PENDING", "PARTIAL" -> WARNING_ORANGE;
            case "FAILED", "CANCELLED" -> ERROR_RED;
            default -> PRIMARY_BLUE;
        };
    }

    private DeviceRgb getAmountColor(LineItem.LineItemType type) {
        return switch (type) {
            case DISCOUNT, REFUND -> SUCCESS_GREEN;
            case CHARGE, PENALTY -> ERROR_RED;
            default -> DARK_TEXT;
        };
    }

    private String formatAmount(BigDecimal amount, LineItem.LineItemType type) {
        String formattedAmount = "Rs. " + amount;
        return switch (type) {
            case DISCOUNT, REFUND -> "- " + formattedAmount;
            default -> formattedAmount;
        };
    }

    private Paragraph createCompactInfoRow(String label, String value, PdfFont regularFont, PdfFont boldFont) {
        return new Paragraph()
                .add(new Text(label + ": ").setFont(boldFont).setFontSize(8).setFontColor(DARK_TEXT))
                .add(new Text(value).setFont(regularFont).setFontSize(9).setFontColor(DARK_TEXT))
                .setMarginBottom(2);
    }

    private Paragraph createTotalRow(String label, String amount, PdfFont labelFont, PdfFont amountFont) {
        return new Paragraph()
                .add(new Text(label).setFont(labelFont))
                .add(new Tab())
                .add(new Text(amount).setFont(amountFont))
                .addTabStops(new TabStop(150, TabAlignment.RIGHT))
                .setMarginBottom(3);
    }

    // Keep original method for backward compatibility
    public byte[] generateProfessionalDocument(
            DocumentType documentType,
            String documentNo,
            LocalDateTime issueDate,
            LocalDateTime dueDate,
            String guestName,
            String guestEmail,
            String guestPhone,
            String guestAddress,
            BigDecimal subtotal,
            BigDecimal discount,
            BigDecimal tax,
            BigDecimal totalPrice,
            String paymentMethod,
            String paymentStatus,
            String notes) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            PdfFont regularFont = PdfFontFactory.createFont();
            PdfFont boldFont = PdfFontFactory.createFont();

            createCompactHeader(document, documentType, boldFont, regularFont);

            // Simplified version for backward compatibility
            document.add(new Paragraph("BILL TO")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setFontColor(DARK_TEXT)
                    .setMarginTop(10)
                    .setMarginBottom(5));

            document.add(new Paragraph(guestName + "\n" + guestEmail + " | " + guestPhone)
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setFontColor(DARK_TEXT)
                    .setMarginBottom(15));

            // Simple totals
            Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
            Cell totalsCell = new Cell()
                    .setBorder(new SolidBorder(PRIMARY_BLUE, 1))
                    .setPadding(10);

            totalsCell.add(createTotalRow("Subtotal", "Rs. " + subtotal, regularFont, regularFont));
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                totalsCell.add(createTotalRow("Discount", "- Rs. " + discount, regularFont, regularFont));
            }
            totalsCell.add(createTotalRow("Tax", "Rs. " + tax, regularFont, regularFont));
            totalsCell.add(createTotalRow("TOTAL", "Rs. " + totalPrice, boldFont, boldFont)
                    .setFontColor(PRIMARY_BLUE));

            totalsTable.addCell(new Cell().setBorder(Border.NO_BORDER));
            totalsTable.addCell(totalsCell);
            document.add(totalsTable);

            if (notes != null && !notes.trim().isEmpty()) {
                document.add(new Paragraph("Notes: " + notes)
                        .setFont(regularFont)
                        .setFontSize(9)
                        .setFontColor(DARK_TEXT)
                        .setMarginTop(10));
            }

            createMinimalFooter(document, documentType, regularFont);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}