package in.softops.springsecurity.service;

import in.softops.springsecurity.dto.LoginRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> authenticateUser(LoginRequestDTO loginRequest);
}
