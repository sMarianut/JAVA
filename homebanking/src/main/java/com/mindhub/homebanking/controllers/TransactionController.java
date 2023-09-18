package com.mindhub.homebanking.controllers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.transactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @GetMapping("/transactions/findDate")
    public ResponseEntity<Object> getTransactionsbyDateTime(@RequestParam String dateInit,
                                                            @RequestParam String dateEnd,
                                                            @RequestParam String numberAcc,
                                                            Authentication authentication) throws DocumentException, IOException {
        Client current = clientService.findByEmail(authentication.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


        if (current == null) {
            return new ResponseEntity<>("you are not allowed to see this", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(numberAcc)) {
            return new ResponseEntity<>("this account dont exist", HttpStatus.BAD_REQUEST);
        }
        if (dateInit.isBlank()) {
            return new ResponseEntity<>("Please, fill the date requeriment", HttpStatus.BAD_REQUEST);
        }
        if (dateEnd.isBlank()) {
            return new ResponseEntity<>("Please, fill the date end requeriment",HttpStatus.BAD_REQUEST);
        }
        if (dateInit.equals(dateEnd)) {
            return new ResponseEntity<>("You cant use the same date", HttpStatus.BAD_REQUEST);
        }
        LocalDateTime localDateTime = LocalDateTime.parse(dateInit, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateEnd, formatter);
        List<Transaction> transf = transactionRepository.findByDateBetweenAndAccountNumber(localDateTime, localDateTime2, numberAcc);
        if (transf.size() <= 0){
            return new ResponseEntity<>("No transactions finded.",HttpStatus.NOT_FOUND);
        }

        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();
        PdfPTable tableTitle = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10);
        cell.addElement(new Paragraph("Your transactions", new Font(Font.HELVETICA, 24)));
        tableTitle.addCell(cell);
        doc.add(tableTitle);

        PdfPTable table = new PdfPTable(4);
        table.addCell("Type");
        table.addCell("Description");
        table.addCell("Amount");
        table.addCell("Date");

        for (Transaction transaction : transf) {
            table.addCell(transaction.getType().toString());
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDate().format(formatter));
        }
        doc.add(table);
        PdfPCell spacerCell = new PdfPCell();
        spacerCell.setFixedHeight(50);
        spacerCell.setBorder(PdfPCell.NO_BORDER);
        spacerCell.setColspan(4);
        doc.add(spacerCell);
        PdfPTable logo = new PdfPTable(2);
        logo.setWidthPercentage(100);
        Image img = Image.getInstance("C:\\Users\\leone\\OneDrive\\Escritorio\\Cosas\\Homebanking Mindhub Tasks\\homebanking\\src\\main\\resources\\static\\web\\images\\banklogo.png");
        img.scaleToFit(50, 50);
        img.setAbsolutePosition(50, 50);
        img.setAlignment(Image.ALIGN_BASELINE);
        PdfPCell imageCell = new PdfPCell(img);
        imageCell.setBorder(PdfPCell.NO_BORDER);
        logo.addCell(imageCell);
        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(PdfPCell.NO_BORDER);
        textCell.addElement(new Phrase("MindHub Brothers, pa"));
        logo.addCell(textCell);

        doc.add(logo);
        doc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=transactions-Table.pdf");
        byte[] pdf = out.toByteArray();
        return new ResponseEntity<>(pdf,headers, HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam String amount,
                                                    @RequestParam String description,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String destinationAccountNumber, Authentication authentication){
        Client currentC = clientService.findByEmail(authentication.getName());
        Account accOrigin = accountService.findByNumber(originAccountNumber);
        Account destinationA = accountService.findByNumber(destinationAccountNumber);
        if (destinationAccountNumber.isBlank()){
            return new ResponseEntity<>("Destination account number cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (originAccountNumber.isBlank()){
            return new ResponseEntity<>("Origin account number cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (accOrigin == null){
           return new ResponseEntity<>("Origin account not found", HttpStatus.FORBIDDEN);
       }
       if(destinationA == null){
          return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
        }
        if(accOrigin.getNumber() == destinationA.getNumber()){
            return new ResponseEntity<>("You cannot transfer to the same account", HttpStatus.FORBIDDEN);
        }
        if(amount.isBlank() || Double.parseDouble(amount) <= 0){
            return new ResponseEntity<>("Please enter a valid amount", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Description cannot be empty", HttpStatus.FORBIDDEN);
        }
       if( accOrigin.getBalance() < Double.parseDouble(amount) ){
           return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
       }else{
           accOrigin.setBalance(accOrigin.getBalance() - Double.parseDouble(amount));
           destinationA.setBalance(destinationA.getBalance() + Double.parseDouble(amount));
           Transaction TransacDebit = new Transaction(Double.parseDouble(amount),description, LocalDateTime.now(), transactionType.DEBIT,accOrigin.getBalance(), true);
           Transaction TransacCredit = new Transaction(Double.parseDouble(amount),description, LocalDateTime.now(), transactionType.CREDIT, destinationA.getBalance(), true);
           accOrigin.addTransaction(TransacDebit);
           destinationA.addTransaction(TransacCredit);
           accountService.addAccount(accOrigin);
           accountService.addAccount(destinationA);
           transactionService.addTransaction(TransacDebit);
           transactionService.addTransaction(TransacCredit);
       }
       return new ResponseEntity<>("Transaction succesfully created", HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/transactions/posnet")
    public ResponseEntity<Object> cardPayment(@RequestParam long idCard, @RequestBody cardPaymentDTO)

}
