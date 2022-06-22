package by.itacademy.mail.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Getter
@Setter
public class Report {

    @NotEmpty
    private String type;
    @NotEmpty
    private Map<String, Object> params;
}
