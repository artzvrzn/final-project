package by.itacademy.mail.model;

import by.itacademy.mail.validation.anno.ValidEmail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Mail<T> {

    @NotEmpty
    @ValidEmail
    private String receiver;
    @NotNull
    @Valid
    private T subject;
}
