package by.itacademy.mail.model.error;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultipleResponseError extends ResponseError {

    private List<Violation> errors;

}
