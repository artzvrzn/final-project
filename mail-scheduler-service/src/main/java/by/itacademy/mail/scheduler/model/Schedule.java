package by.itacademy.mail.scheduler.model;

import by.itacademy.mail.scheduler.validation.anno.FutureTime;
import by.itacademy.mail.scheduler.validation.anno.StopAfterStart;
import by.itacademy.mail.scheduler.validation.anno.ValidType;
import by.itacademy.mail.scheduler.validation.groups.FirstOrder;
import by.itacademy.mail.scheduler.validation.groups.SecondOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@GroupSequence({Schedule.class, FirstOrder.class, SecondOrder.class})
@StopAfterStart(groups = SecondOrder.class)
public class Schedule implements Serializable {

    @NotNull
    @FutureTime(groups = FirstOrder.class)
    private LocalDateTime startTime;
    @NotNull
    @FutureTime(groups = FirstOrder.class)
    private LocalDateTime stopTime;
    @Min(1)
    private int interval;
    @NotNull
    @ValidType
    private TimeUnit timeUnit;
}
