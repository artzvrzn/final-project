package by.it.academy.report.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum ReportStatus {

    LOADED, PROGRESS, DONE, ERROR;

}
