package by.itacademy.telegram.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equal(id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
