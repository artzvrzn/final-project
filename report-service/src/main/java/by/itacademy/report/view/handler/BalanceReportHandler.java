package by.itacademy.report.view.handler;

import by.itacademy.report.model.Account;
import by.itacademy.report.model.Currency;
import by.itacademy.report.utils.MapParser;
import by.itacademy.report.view.api.CommunicatorService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log4j2
public class BalanceReportHandler extends ExcelReportHandler {

    private final CommunicatorService communicatorService;
    private final MapParser parser;

    public BalanceReportHandler(MapParser parser, CommunicatorService communicatorService) {
        this.communicatorService = communicatorService;
        this.parser = parser;
    }

    @Override
    protected Workbook createWorkbook(Map<String, Object> params) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Баланс");
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        createHeader(sheet, headerStyle(workbook));
        int rowIndex = 1;
        List<UUID> accountIds = parser.readList("accounts", params, UUID.class);
        if (accountIds == null) {
            throw new IllegalStateException("Handler failed to get accounts from params");
        }
        for (UUID uuid: accountIds) {
            int cellIndex = 0;
            Account account = communicatorService.getAccount(uuid);
            if (account == null) {
                continue;
            }
            Row row = sheet.createRow(rowIndex++);
            row.setRowStyle(contentStyle(workbook));
            fillCell(row, cellIndex++, account.getId().toString());
            Currency currency = communicatorService.getCurrency(account.getCurrency());
            fillCell(row, cellIndex++, currency.getTitle());
            fillCell(row, cellIndex, account.getBalance().toString());
        }
        return workbook;
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
    private void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row header = sheet.createRow(0);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Аккаунт");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Валюта");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Сумма");
        headerCell.setCellStyle(headerStyle);
    }

    private void fillCell(Row row, int index, String value) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
    }
}
