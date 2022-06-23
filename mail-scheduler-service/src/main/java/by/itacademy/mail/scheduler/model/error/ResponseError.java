package by.itacademy.mail.scheduler.model.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "logref")
@JsonSubTypes({
        @Type(value = SingleResponseError.class, name = "error"),
        @Type(value = MultipleResponseError.class, name = "structured_error")
})
public abstract class ResponseError {
    @JsonIgnore
    private String logref;
}
