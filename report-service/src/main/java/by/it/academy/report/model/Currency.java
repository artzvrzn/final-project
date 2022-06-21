package by.it.academy.report.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Currency {

    @JsonProperty("uuid")
    private UUID id;
    private String title;
    private String description;
}
