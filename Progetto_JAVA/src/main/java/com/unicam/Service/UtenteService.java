package com.unicam.Service;

import com.unicam.Model.User;
import com.unicam.Repository.IUtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {

    private IUtenteRepository repository;

    @Autowired
    public UtenteService(IUtenteRepository repo){
        this.repository = repo;
    }

    public void RegistrazioneUtente(User user){
        //TODO controlla che i campi siano tutti inseriti
        /**
         * TODO controlla che username e email non siano già
         * stati usati da altre persone (già presenti nel database)
         */
        this.repository.save(user);
        //TODO creare token e auto log-in
    }

    public void EliminaUtente(Long idUtente){
        //TODO controllare che esista tale utente nel database
        this.repository.deleteById(idUtente);
    }

    public void LoginUtente(String username, String password){
        //TODO controllare se presenti nel database
        //TODO in caso affermativo creare token
    }

    public String AccessoOspite(){
        //TODO
        return "";
    }
}
