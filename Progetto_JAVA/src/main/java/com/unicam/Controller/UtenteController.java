package com.unicam.Controller;

import com.unicam.Model.User;
import com.unicam.Service.UtenteService;
import com.unicam.dto.LoginDTO;
import com.unicam.dto.LoginResponseDTO;
import com.unicam.dto.RegistrazioneDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/utente")
public class UtenteController {

    private UtenteService servizio;

    @Autowired
    public UtenteController(UtenteService servizio){
        this.servizio = servizio;
    }

    @PostMapping("registrazioneUtente")
    public ResponseEntity<LoginResponseDTO> Registrazione(@RequestBody RegistrazioneDTO registrazione){
        LoginResponseDTO login = new LoginResponseDTO();
        login.setToken(this.servizio.RegistrazioneUtente(registrazione));
        login.setRole(this.servizio.GetUtente(registrazione.getUsername()));
        login.setUsername(registrazione.getUsername());
        return ResponseEntity.ok(login);
    }

    /*public void Login(LoginDTO login){
        this.servizio.LoginUtente(login.getUsername(), login.getPassword());
    }*/
}
