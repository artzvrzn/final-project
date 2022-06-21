package by.it.academy.report.converter;

import by.it.academy.report.dao.entity.ReportEntity;
import by.it.academy.report.model.Report;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReportEntityToDtoConverter implements Converter<ReportEntity, Report> {

    @Override
    public Report convert(ReportEntity entity) {
        return new Report.Builder()
                .withCreated(entity.getCreated())
                .withUpdated(entity.getUpdated())
                .withId(entity.getId())
                .withStatus(entity.getStatus())
                .withReportType(entity.getType())
                .withDescription(entity.getDescription())
                .withParams(entity.getParams())
                .build();
    }
}
