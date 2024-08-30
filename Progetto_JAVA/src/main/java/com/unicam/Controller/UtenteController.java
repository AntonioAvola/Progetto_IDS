package com.unicam.Controller;

import com.unicam.Model.User;
import com.unicam.Service.UtenteService;
import com.unicam.dto.LoginDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("utente")
public class UtenteController {

    private UtenteService servizio;

    @Autowired
    public UtenteController(UtenteService servizio){
        this.servizio = servizio;
    }

    @PostMapping("registrazioneUtente")
    public void Registrazione(User user){
        this.servizio.RegistrazioneUtente(user);
    }

    public void Login(LoginDTO login){
        this.servizio.LoginUtente(login.getUsername(), login.getPassword());
    }
}
