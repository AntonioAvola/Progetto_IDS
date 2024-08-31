package com.unicam.Controller;

import com.unicam.Model.User;
import com.unicam.Repository.IUtenteRepository;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.dto.LoginDTO;
import com.unicam.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUtenteRepository utenteRepository;
    //private
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginRequest) {
        /*
        User user = utenteRepository.findByUsername(loginRequest.getUsername());

        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!user.checkPassword(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = tokenProvider.createToken(user.getUsername(), user.getRuolo());

        LoginResponseDTO response = new LoginResponseDTO(
                token, // Token
                user.getUsername(), // Username
                user.getRuolo() // Ruolo
        );
        */
        //TODO implementare logica login da utente service

        return null;
    }
}
