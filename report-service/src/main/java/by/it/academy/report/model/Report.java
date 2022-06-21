package by.it.academy.report.model;

import by.it.academy.report.serializer.LocalDateTimeMillisDeserializer;
import by.it.academy.report.serializer.LocalDateTimeMillisSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@ToString
@JsonDeserialize(builder = Report.Builder.class)
public class Report {

    private Report() {}

    private UUID id;
    @JsonSerialize(using = LocalDateTimeMillisSerializer.class)
    @JsonDeserialize(using = LocalDateTimeMillisDeserializer.class)
    private LocalDateTime created;
    @JsonSerialize(using = LocalDateTimeMillisSerializer.class)
    @JsonDeserialize(using = LocalDateTimeMillisDeserializer.class)
    private LocalDateTime updated;
    private ReportStatus status;
    private ReportType type;
    private String description;
    private Map<String, Object> params;
    @JsonIgnore
    private String filename;


    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public ReportType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @JsonPOJOBuilder
    public static class Builder {

        private Report report;

        public Builder() {
            this.report = new Report();
        }

        public Builder withId(UUID id) {
            report.id = id;
            return this;
        }

        public Builder withCreated(LocalDateTime created) {
            report.created = created;
            return this;
        }

        public Builder withUpdated(LocalDateTime updated) {
            report.updated = updated;
            return this;
        }

        public Builder withStatus(ReportStatus reportStatus) {
            report.status = reportStatus;
            return this;
        }

        public Builder withReportType(ReportType reportType) {
            report.type = reportType;
            return this;
        }

        public Builder withDescription(String description) {
            report.description = description;
            return this;
        }

        public Builder withParams(Map<String, Object> params) {
            report.params = params;
            return this;
        }

        public Builder withFilename(String filename) {
            report.filename = filename;
            return this;
        }

        public Report build() {
            return report;
        }
    }
}
