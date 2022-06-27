package by.itacademy.telegram.view.api;

import by.itacademy.telegram.model.Authentication;

public interface AuthenticationService {

    String getToken(Authentication authentication) throws SecurityException;
}
