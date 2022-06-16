package by.itacademy.account.view.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getUserDetails();

    boolean isAdmin(UserDetails userDetails);
}
