package com.deborger.shopme.admin.user.export;

import com.deborger.shopme.admin.AbstractExporter;
import com.deborger.shopme.common.entity.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPDFExporter extends AbstractExporter {

    public void export(List<User> userList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response,"application/pdf",".pdf","users_");
        ServletOutputStream outputStream = response.getOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(16);
        font.setColor(Color.BLUE);
        Paragraph paragraph =  new Paragraph("List of Users",font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {1.2f, 3.5f, 3.0f,3.0f, 3.0f, 1.7f});

        writeTableHeader(table);
        writeTableData(table,userList);
        document.add(table);

        document.close();
        outputStream.close();

    }

    private void writeTableData(PdfPTable table, List<User> userList) {
        for (User user : userList) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.getEnabled()));
        }
    }

    private void writeTableHeader(PdfPTable table) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("E-mail",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("First Name",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Last Name",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Roles",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Enabled",font));
        table.addCell(cell);
    }
}
