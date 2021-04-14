package in.softops.springsecurity.controller;

import in.softops.springsecurity.dto.LoginRequestDTO;
import in.softops.springsecurity.repository.CustomerRepository;
import in.softops.springsecurity.repository.EmployeeRepository;
import in.softops.springsecurity.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/erp-api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }
}
