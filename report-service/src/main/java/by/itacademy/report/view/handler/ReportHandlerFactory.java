package by.itacademy.report.view.handler;

import by.itacademy.report.model.ReportType;
import by.itacademy.report.utils.MapParser;
import by.itacademy.report.view.api.CommunicatorService;
import by.itacademy.report.view.handler.api.ReportHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportHandlerFactory {

    @Autowired
    private CommunicatorService communicatorService;
    @Autowired
    private MapParser parser;

    public ReportHandler getHandler(ReportType reportType) {
        switch (reportType) {
            case BALANCE:
                return new BalanceReportHandler(parser, communicatorService);
            case BY_DATE:
                return new ByDateReportHandler(parser, communicatorService);
            case BY_CATEGORY:
                return new ByCategoryReportHandler(parser, communicatorService);
            default:
                throw new IllegalArgumentException("Wrong report type");
        }
    }
}
