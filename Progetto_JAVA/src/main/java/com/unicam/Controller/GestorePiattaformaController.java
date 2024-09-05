package com.unicam.Controller;

import com.unicam.Model.Ruolo;
import com.unicam.Service.AdminService;
import com.unicam.dto.RegistrazioneAdminDTO;
import com.unicam.dto.Risposte.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "Api/GestorePiattaforma")
public class GestorePiattaformaController {

    @Autowired
    private AdminService servizioAdmin;

    @PostMapping("Api/GestorePiattaforma/Registrazione")
    public void RegistrazioneGestorePiattaforma(@RequestBody RegistrazioneAdminDTO registrazione){
        //TODO
        /*LoginResponseDTO login = new LoginResponseDTO();

        login.setMessage("Registrazione avvenuta con successo!");
        login.setToken(this.servizioAdmin.RegistrazioneUtente(registrazione));
        login.setRole(Ruolo.ADMIN);
        login.setUsername(registrazione.getUsername());
        return ResponseEntity.ok(login);*/
    }
}
