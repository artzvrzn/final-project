package by.itacademy.report.converter;

import by.itacademy.report.dao.entity.ReportFilePropertyEntity;
import by.itacademy.report.dao.entity.ReportEntity;
import by.itacademy.report.model.Report;
import by.itacademy.report.view.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReportDtoToEntityConverter implements Converter<Report, ReportEntity> {

    @Autowired
    private UserService userService;

    @Override
    public ReportEntity convert(Report dto) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setCreated(dto.getCreated());
        reportEntity.setUpdated(dto.getUpdated());
        reportEntity.setId(dto.getId());
        reportEntity.setStatus(dto.getStatus());
        reportEntity.setType(dto.getType());
        reportEntity.setDescription(dto.getDescription());
        reportEntity.setParams(dto.getParams());
        reportEntity.setUsername(userService.getUserDetails().getUsername());
        ReportFilePropertyEntity fileProperties = new ReportFilePropertyEntity();
        fileProperties.setReport(reportEntity);
        fileProperties.setFilename(dto.getFilename());
        reportEntity.setFileProperty(fileProperties);
        return reportEntity;
    }
}
