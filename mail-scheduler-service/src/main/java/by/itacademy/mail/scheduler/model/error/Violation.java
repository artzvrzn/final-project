package by.itacademy.mail.scheduler.model.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Violation {

    private final String field;
    private final String message;

    public Violation(@JsonProperty("field") String field, @JsonProperty("message") String message) {
        this.field = field;
        this.message = message;
    }
}
