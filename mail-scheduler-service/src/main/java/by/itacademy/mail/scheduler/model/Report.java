package by.itacademy.mail.scheduler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
public class Report implements Serializable {

    @JsonIgnore
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @NotEmpty
    private String type;
    @NotEmpty
    private Map<String, Object> params;

    public void insertReportInterval(TimeUnit timeUnit) {
        LocalDate dateFrom = LocalDate.now(ZoneOffset.UTC);
        LocalDate dateTo;
        switch (timeUnit) {
            case DAY:
                dateTo = dateFrom.plusDays(1);
                break;
            case WEEK:
                dateFrom = dateFrom.with(DayOfWeek.MONDAY);
                dateTo = dateFrom.plusWeeks(1);
                break;
            case MONTH:
                dateFrom = dateFrom.withDayOfMonth(1);
                dateTo = dateFrom.plusMonths(1);
                break;
            case YEAR:
                dateFrom = dateFrom.withDayOfYear(1);
                dateTo = dateFrom.plusYears(1);
                break;
            default:
                throw new IllegalStateException("Failure during calculating report interval (Wrong type)");
        }
        params.put("from", dateFrom.format(formatter));
        params.put("to", dateTo.format(formatter));
    }
}
