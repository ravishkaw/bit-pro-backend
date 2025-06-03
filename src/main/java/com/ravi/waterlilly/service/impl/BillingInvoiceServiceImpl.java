package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.service.BillingInvoiceService;
import com.ravi.waterlilly.utils.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// implementation of billing invoice service
@Service
@RequiredArgsConstructor
public class BillingInvoiceServiceImpl implements BillingInvoiceService {
    private final PdfGenerator pdfGenerator;

    //
    public byte[] generatePrintReadyInvoice(
            String invoiceNo,
            LocalDateTime billingDate,
            String guestName,
            String guestEmail,
            String guestPhone,
            BigDecimal basePrice,
            BigDecimal discount,
            BigDecimal tax,
            BigDecimal totalPrice,
            String paymentMethod,
            String paymentStatus) {

//        List<InvoiceItem> items = new ArrayList<>();
//        items.add(new InvoiceItem("Resort Accommodation", 1, basePrice));

        // 30 days payment term
        // no address
        //                items,

        return pdfGenerator.generateProfessionalDocument(
                PdfGenerator.DocumentType.INVOICE,
                invoiceNo,
                billingDate,
                billingDate.plusDays(30), // 30 days payment term
                guestName,
                guestEmail,
                guestPhone,
                null, // no address
//                items,
                basePrice,
                discount,
                tax,
                totalPrice,
                paymentMethod,
                paymentStatus,
                "Thank you for staying with us. We hope you enjoyed your tropical paradise experience!"
        );
    }

}
