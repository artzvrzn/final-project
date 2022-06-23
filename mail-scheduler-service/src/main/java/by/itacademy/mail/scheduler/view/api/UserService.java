package by.itacademy.mail.scheduler.view.api;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getUserDetails();

    boolean isAdmin(UserDetails userDetails);
}
