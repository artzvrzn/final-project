package by.itacademy.telegram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Authentication implements Stateful {

    private String login;
    private String password;
    @JsonIgnore
    private int state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authentication that = (Authentication) o;
        return Objects.equal(login, that.login) && Objects.equal(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login, password);
    }
}
