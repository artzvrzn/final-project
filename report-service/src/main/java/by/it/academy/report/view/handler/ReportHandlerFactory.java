package by.it.academy.report.view.handler;

import by.it.academy.report.model.ReportType;
import by.it.academy.report.utils.MapParser;
import by.it.academy.report.view.api.CommunicatorService;
import by.it.academy.report.view.handler.api.ReportHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
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
