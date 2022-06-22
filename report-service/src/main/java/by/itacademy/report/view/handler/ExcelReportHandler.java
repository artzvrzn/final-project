package by.itacademy.report.view.handler;

import by.itacademy.report.model.FileData;
import by.itacademy.report.view.handler.api.ReportHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Log4j2
public abstract class ExcelReportHandler implements ReportHandler {

    @Override
    public FileData generate(UUID id, Map<String, Object> params) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Workbook workbook = createWorkbook(params);
            workbook.write(os);
            FileData file = new FileData.Builder()
                    .withFilename(id + ".xlsx")
                    .withContent(os.toByteArray())
                    .build();
            workbook.close();
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Workbook createWorkbook(Map<String, Object> params);
}
