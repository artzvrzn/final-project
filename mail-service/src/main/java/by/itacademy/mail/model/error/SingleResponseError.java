package by.itacademy.mail.model.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponseError extends ResponseError {

    private String message;
}
