package com.deborger.shopme.admin.category.export;

import com.deborger.shopme.admin.AbstractExporter;
import com.deborger.shopme.common.entity.Category;
import com.deborger.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter {

    public void export(List<Category> categoryList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response,"text/csv",".csv","categories_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Category ID","Category Name","Enabled"};
        String[] fieldMapping = {"id","name","enabled"};
        csvBeanWriter.writeHeader(csvHeader);

        for (Category category : categoryList) {
            category.setName(category.getName().replace("--","  "));
            csvBeanWriter.write(category,fieldMapping);
        }
        csvBeanWriter.close();
    }
}
