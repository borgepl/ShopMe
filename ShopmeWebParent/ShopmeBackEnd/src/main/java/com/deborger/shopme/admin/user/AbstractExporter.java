package com.deborger.shopme.admin.user;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String contentType, String extension) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "users_" + timestamp + extension;

        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName ;
        response.setHeader(headerKey,headerValue);
    }
}
