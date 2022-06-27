package by.itacademy.telegram.model;

import by.itacademy.telegram.model.constant.MenuState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    private String id;
    private MenuState state;
    private UUID chosenAccount;
    private UUID chosenOperation;
    @JsonIgnore
    private String jwtToken;
}
