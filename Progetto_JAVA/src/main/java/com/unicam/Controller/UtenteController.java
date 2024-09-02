package com.unicam.Controller;

import com.unicam.Model.User;
import com.unicam.Service.UtenteService;
import com.unicam.dto.EliminaUtenteDTO;
import com.unicam.dto.LoginDTO;
import com.unicam.dto.LoginResponseDTO;
import com.unicam.dto.RegistrazioneDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/utente")
public class UtenteController {

    private UtenteService servizio;

    @Autowired
    public UtenteController(UtenteService servizio){
        this.servizio = servizio;
    }

    @PostMapping("/RegistrazioneUtente")
    public ResponseEntity<LoginResponseDTO> Registrazione(@RequestBody RegistrazioneDTO registrazione){
        LoginResponseDTO login = new LoginResponseDTO();
        login.setToken(this.servizio.RegistrazioneUtente(registrazione));
        login.setRole(this.servizio.GetUtente(registrazione.getUsername()));
        login.setUsername(registrazione.getUsername());
        return ResponseEntity.ok(login);
    }

    @DeleteMapping("/EliminaAccount")
    public void EliminaAccount(@RequestBody EliminaUtenteDTO userDeleted){
        this.servizio.EliminaUtenteByUsername(userDeleted.getUsername());
    }
}
