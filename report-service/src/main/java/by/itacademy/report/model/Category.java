package by.itacademy.report.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Category {

    @JsonProperty("uuid")
    private UUID id;
    private String title;
}
