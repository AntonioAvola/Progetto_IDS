package com.unicam.Controller;

import com.unicam.Model.User;
import com.unicam.Repository.IUtenteRepository;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.Service.UtenteService;
import com.unicam.dto.LoginDTO;
import com.unicam.dto.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UtenteService servizioUtente;
    //private
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/Login")
    public ResponseEntity<LoginResponseDTO> Login(@RequestBody LoginDTO loginRequest) {
        LoginResponseDTO risposta = new LoginResponseDTO();
        risposta.setToken(servizioUtente.LoginUtente(loginRequest.getUsername(), loginRequest.getPassword()));
        risposta.setUsername(loginRequest.getUsername());
        risposta.setRole(servizioUtente.GetUtente(loginRequest.getUsername()));
        return ResponseEntity.ok(risposta);

    }
}
