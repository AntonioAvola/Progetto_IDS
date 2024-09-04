package com.unicam.Service;

import com.unicam.Model.Ruolo;
import com.unicam.Model.User;
import com.unicam.Repository.IUtenteRepository;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.Validator.EmailValidator;
import com.unicam.Validator.PasswordValidator;
import com.unicam.dto.RegistrazioneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UtenteService implements UserDetailsService {

    private IUtenteRepository repository;


    private JwtTokenProvider tokenProvider = new JwtTokenProvider();

    @Autowired
    public UtenteService(IUtenteRepository repo){
        this.repository = repo;
    }

    public String RegistrazioneUtente(RegistrazioneDTO registrazione){
        User user = registrazione.ToEntity();
        ControlloCampiUse(user);
        user.CriptaPassword();
        PresenzaNomeEmailNelDB(user);
        this.repository.save(user);
        return tokenProvider.createToken(user);
    }

    private void PresenzaNomeEmailNelDB(User user) {
        User utentePresente = repository.findByUsername(user.getUsername());
        if (utentePresente != null)
            throw new IllegalArgumentException("L'username è già in uso");
        utentePresente = repository.findByEmail(user.getEmail());
        if(utentePresente != null)
            throw new IllegalArgumentException("L'email è già in uso");
    }

    private void ControlloCampiUse(User user) {
        if(user == null)
            throw new NullPointerException("L'utente passato è nullo");
        if(user.getName().isBlank() /*|| user.getName().isEmpty()*/)
            throw new IllegalArgumentException("Il nome non è stato inserito");
        if(/*user.getUsername().isEmpty() ||*/ user.getUsername().isBlank())
            throw new IllegalArgumentException("L'username non è stato inserito");
        if(user.getComune().isBlank() /*|| user.getComune().isEmpty()*/)
            throw new IllegalArgumentException("Il Comune di residenza non è stato inserito");
        if(/*user.getEmail().isEmpty() ||*/ user.getEmail().isBlank())
            throw new IllegalArgumentException("L'email non è stata inserita");
        if(!EmailValidator.isValidEmail(user.getEmail()))
            throw new IllegalArgumentException("L'email non è stata inserita correttamente. Si prega di inserire una email valida");
        if(user.getPassword().isBlank() /*|| user.getPassword().isEmpty()*/)
            throw new IllegalArgumentException("La password non è stata inserita");
        if(!PasswordValidator.isValidPassword(user.getPassword()))
            throw new IllegalArgumentException("La password non rispetta i requisiti richiesti: lunghezza 5, almeno una maiuscola, " +
                    "almeno una minuscola, almeno un numero, almeno un simbolo speciale, niente spazi vuoti");
    }

    public void EliminaUtente(Long idUtente){
        if(!repository.existsById(idUtente))
            throw new IllegalArgumentException("L'utente non esiste");
        this.repository.deleteById(idUtente);
    }

    public String LoginUtente(String username, String password){
        ConfrontoCredenzialiDB(username, password);

        User user = repository.findByUsername(username);

        return tokenProvider.createToken(user);

    }

    private void ConfrontoCredenzialiDB(String username, String password) {
        User utenteLogin = repository.findByUsername(username);
        if(utenteLogin == null)
            throw new NullPointerException("Non esiste alcun utente con l'username passato");
        if(!CheckPassword(password, utenteLogin.getUsername()))
            throw new IllegalArgumentException("Password errata");
    }


    public boolean CheckPassword(String rawPassword, String username) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, this.repository.findByUsername(username).getPassword());
    }

    public String AccessoOspite(){
        //TODO fornire soltanto un username momentaneo, non salvaere nel database
        return "";
    }

    public Ruolo GetUtente(String username) {
        return repository.findByUsername(username).getRuoloComune();
    }
    public User GetUtenteById(Long id){
        return repository.getById(id);
    }

    public Long GetIdByUsername(String username) {
        return this.repository.findByUsername(username).getId();
    }

    public void EliminaUtenteByUsername(String username) {
        User utente = this.repository.findByUsername(username);
        Long idUtente = utente.getId();
        this.repository.deleteById(idUtente);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // Restituisci un'istanza di UserDetails personalizzata, o usa l'implementazione di Spring.
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public String GetComuneByUsername(String username) {
        return this.repository.findByUsername(username).getComune();
    }
}
