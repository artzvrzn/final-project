package by.itacademy.report.view.handler;

import by.itacademy.report.model.Operation;
import by.itacademy.report.utils.MapParser;
import by.itacademy.report.view.api.CommunicatorService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ByDateReportHandler extends ExcelReportHandler {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final CommunicatorService communicatorService;
    private final MapParser parser;

    public ByDateReportHandler(MapParser parser, CommunicatorService communicatorService) {
        this.communicatorService = communicatorService;
        this.parser = parser;
    }

    @Override
    protected Workbook createWorkbook(Map<String, Object> params) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = createSheet(workbook);
        List<UUID> accountIds = parser.readList("accounts", params, UUID.class);
        int rowIndex = 1;
        for (UUID uuid: accountIds) {
            LocalDate from = parser.readValue("from", params, LocalDate.class);
            LocalDate to = parser.readValue("to", params, LocalDate.class);
            List<UUID> categoryIds = parser.readList("categories", params, UUID.class);
            List<Operation> operations = communicatorService.getOperations(uuid, from, to, categoryIds)
                    .stream()
                    .sorted(Comparator.comparing(Operation::getDate))
                    .collect(Collectors.toList());
            if (operations.isEmpty()) {
                createEmptyRow(uuid.toString(), sheet, rowIndex++);
            }
            for (Operation operation: operations) {
                createRow(uuid.toString(), operation, sheet, rowIndex++);
            }
        }
        return workbook;
    }

    private Sheet createSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("По дате");
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 8000);
        createHeader(sheet, headerStyle(workbook));
        return sheet;
    }

    private void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row header = sheet.createRow(0);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Дата");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Аккаунт");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Сумма");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Валюта");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Категория");
        headerCell.setCellStyle(headerStyle);
    }

    private void createRow(String account, Operation operation, Sheet sheet, int index) {
        Row row = sheet.createRow(index);
        row.setRowStyle(contentStyle(sheet.getWorkbook()));
        createCell(row, 0, operation.getDate().format(dateTimeFormatter));
        createCell(row, 1, account);
        createCell(row, 2, operation.getValue().toString());
        createCell(row,3, communicatorService.getCurrency(operation.getCurrency()).getTitle());
        createCell(row, 4, communicatorService.getCategory(operation.getCategory()).getTitle());
    }

    private void createEmptyRow(String account, Sheet sheet, int index) {
        Row row = sheet.createRow(index);
        row.setRowStyle(contentStyle(sheet.getWorkbook()));
        createCell(row, 0, "");
        createCell(row, 1, account);
        createCell(row, 2, "Операции отсутствуют");
    }


    private void createCell(Row row, int index, String value) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
    }

    private CellStyle headerStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }

    private CellStyle contentStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }
}
