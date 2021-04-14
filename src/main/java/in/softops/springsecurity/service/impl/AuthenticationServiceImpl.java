package in.softops.springsecurity.service.impl;

import in.softops.springsecurity.dto.JWTResponseDTO;
import in.softops.springsecurity.dto.LoginRequestDTO;
import in.softops.springsecurity.security.jwt.JwtUtils;
import in.softops.springsecurity.security.service.CustomerDetailsImpl;
import in.softops.springsecurity.security.service.EmployeeDetailsImpl;;
import in.softops.springsecurity.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        JWTResponseDTO jwtResponse = new JWTResponseDTO();

        if(loginRequest.getAuthType().equalsIgnoreCase("employee")) {
            EmployeeDetailsImpl employeeDetails = (EmployeeDetailsImpl) authentication.getPrincipal();
            jwtResponse.setToken(jwt);
            jwtResponse.setId(employeeDetails.getId());
            jwtResponse.setUsername(employeeDetails.getUsername());
            jwtResponse.setRoles(employeeDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        } else if (loginRequest.getAuthType().equalsIgnoreCase("customer")) {
            CustomerDetailsImpl customerDetails = (CustomerDetailsImpl) authentication.getPrincipal();
            jwtResponse.setToken(jwt);
            jwtResponse.setId(customerDetails.getId());
            jwtResponse.setUsername(customerDetails.getUsername());
            jwtResponse.setRoles(customerDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        } else {
            return new ResponseEntity<>("Invalid Authentication Type", HttpStatus.BAD_REQUEST);
        }

        jwtResponse.setType("Bearer");
        return ResponseEntity.ok(jwtResponse);
    }
}
