package com.unicam.Service;

import com.unicam.Model.Ruolo;
import com.unicam.Model.User;
import com.unicam.Repository.IUtenteRepository;
import com.unicam.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {

    private IUtenteRepository repository;

    private JwtTokenProvider tokenProvider;

    @Autowired
    public UtenteService(IUtenteRepository repo){
        this.repository = repo;
    }

    public String RegistrazioneUtente(User user){
        ControlloCampiUse(user);
        PresenzaNomeEmailNelDB(user);
        this.repository.save(user);
        //TODO creare token e auto log-in CONTROLLRE

        String username = user.getUsername();
        Ruolo ruolo = user.getRuolo();

        return tokenProvider.createToken(username, ruolo);
    }

    private void PresenzaNomeEmailNelDB(User user) {
        List<User> utenti = repository.findAll();
        for (User utente: utenti) {
            if(utente.getUsername() == user.getUsername())
                throw new IllegalArgumentException("Username già in uso, sceglierne un altro");
            if(utente.getEmail() == user.getEmail())
                throw new IllegalArgumentException("L'email è già collegata ad un account esistente");
        }
    }

    private void ControlloCampiUse(User user) {
        if(user == null)
            throw new NullPointerException("L'utente passato è nullo");
        if(user.getName().isBlank() || user.getName().isEmpty())
            throw new IllegalArgumentException("Il nome non è stato inserito");
        if(user.getUsername().isEmpty() || user.getUsername().isBlank())
            throw new IllegalArgumentException("L'username non è stato inserito");
        if(user.getComune().isBlank() || user.getComune().isEmpty())
            throw new IllegalArgumentException("Il Comune di residenza non è stato inserito");
        if(user.getEmail().isEmpty() || user.getEmail().isBlank())
            throw new IllegalArgumentException("L'email non è stata inserita");
        if(user.getPassword().isBlank() || user.getPassword().isEmpty())
            throw new IllegalArgumentException("La password non è stata inserita");
    }

    public void EliminaUtente(Long idUtente){
        if(!repository.existsById(idUtente))
            throw new IllegalArgumentException("L'utente non esiste");
        this.repository.deleteById(idUtente);
    }

    public String LoginUtente(String username, String password){
        ConfrontoCredenzialiDB(username, password);

        User user = repository.findByUsername(username);
        String token = tokenProvider.createToken(username, user.getRuolo());
        //TODO in caso affermativo creare token CONTROLLARE

        return token;

    }

    private void ConfrontoCredenzialiDB(String username, String password) {
        User utenteLogin = null;
        List<User> utenti = repository.findAll();
        for (User utente: utenti) {
            if(utente.getUsername() == username){
                utenteLogin = repository.getById(utente.getId());
                break;
            }
        }
        if(utenteLogin == null)
            throw new NullPointerException("Non esiste alcun utente con l'username passato");
        if(utenteLogin.getPassword() != password)
            throw new IllegalArgumentException("Password errata");
    }

    public String AccessoOspite(){
        //TODO
        return "";
    }
}
