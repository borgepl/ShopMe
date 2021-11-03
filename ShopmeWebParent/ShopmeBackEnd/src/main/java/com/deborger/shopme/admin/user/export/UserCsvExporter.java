package com.deborger.shopme.admin.user.export;

import com.deborger.shopme.admin.AbstractExporter;
import com.deborger.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> userList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response,"text/csv",".csv","users_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID","E-mail","First Name","Last Name","Roles","Enabled"};
        String[] fieldMapping = {"id","email","firstName","lastName","roles","enabled"};
        csvBeanWriter.writeHeader(csvHeader);

        for (User user : userList) {
            csvBeanWriter.write(user,fieldMapping);
        }
        csvBeanWriter.close();
    }
}
