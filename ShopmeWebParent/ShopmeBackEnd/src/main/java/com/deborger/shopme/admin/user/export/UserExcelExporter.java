package com.deborger.shopme.admin.user.export;

import com.deborger.shopme.admin.AbstractExporter;
import com.deborger.shopme.common.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet =  workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row,0,"User ID",cellStyle);
        createCell(row,1,"E-Mail",cellStyle);
        createCell(row,2,"First Name",cellStyle);
        createCell(row,3,"Last Name",cellStyle);
        createCell(row,4,"Roles",cellStyle);
        createCell(row,5,"Enabled",cellStyle);

    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {

        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }

    public void export(List<User> userList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response,"application/octet-stream",".xlsx","users_");

        writeHeaderLine();
        writeDataLines(userList);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);

        workbook.close();
        outputStream.close();

    }

    private void writeDataLines(List<User> userList) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        XSSFCellStyle cellStyleGreen = workbook.createCellStyle();
        // Setting Background color
        cellStyleGreen.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        cellStyleGreen.setFillPattern(FillPatternType.FINE_DOTS);
        // setting font 14 - White
        XSSFFont fontWhite = workbook.createFont();
        fontWhite.setFontHeight(14);
        fontWhite.setColor(IndexedColors.WHITE.getIndex());
        cellStyleGreen.setFont(fontWhite);

        for (User user : userList) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row,columnIndex++,user.getId(),cellStyle);
            createCell(row,columnIndex++,user.getEmail(),cellStyle);
            createCell(row,columnIndex++,user.getFirstName(),cellStyle);
            createCell(row,columnIndex++,user.getLastName(),cellStyle);
            createCell(row,columnIndex++,user.getRoles().toString(),cellStyle);
            if (user.getEnabled()) {
                createCell(row, columnIndex++, user.getEnabled(), cellStyleGreen);
            } else {
                createCell(row, columnIndex++, user.getEnabled(), cellStyle);
            }
        }
    }

}
