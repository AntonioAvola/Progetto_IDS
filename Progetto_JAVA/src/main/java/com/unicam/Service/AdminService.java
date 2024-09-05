package com.unicam.Service;

import com.unicam.Model.AdminPiattaforma;
import com.unicam.Model.User;
import com.unicam.Repository.AdminRepository;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.Validator.EmailValidator;
import com.unicam.Validator.PasswordValidator;
import com.unicam.dto.RegistrazioneAdminDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository repo;

    private JwtTokenProvider tokenProvider = new JwtTokenProvider();

    @Autowired
    public AdminService(AdminRepository repo){
        this.repo = repo;
    }

    public void RegistrazioneUtente(RegistrazioneAdminDTO registrazione) {
        AdminPiattaforma admin = registrazione.ToEntity();
        ControlloCampiUse(admin);
        admin.CriptaPassword();
        PresenzaNomeEmailNelDB(admin);
        this.repo.save(admin);
        //return tokenProvider.createToken(admin);
    }

    private void PresenzaNomeEmailNelDB(AdminPiattaforma user) {
        AdminPiattaforma utentePresente = repo.findByUsername(user.getUsername());
        if (utentePresente != null)
            throw new IllegalArgumentException("L'username è già in uso");
        utentePresente = repo.findByEmail(user.getEmail());
        if(utentePresente != null)
            throw new IllegalArgumentException("L'email è già in uso");
    }

    private void ControlloCampiUse(AdminPiattaforma user) {
        if(user == null)
            throw new NullPointerException("L'utente passato è nullo");
        if(user.getName().isBlank() /*|| user.getName().isEmpty()*/)
            throw new IllegalArgumentException("Il nome non è stato inserito");
        if(/*user.getUsername().isEmpty() ||*/ user.getUsername().isBlank())
            throw new IllegalArgumentException("L'username non è stato inserito");
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

    public void LoginUtente(String username, String password) {
        ConfrontoCredenzialiDB(username, password);
        AdminPiattaforma admin = repo.findByUsername(username);
        this.repo.save(admin);

        //return tokenProvider.createToken(admin);
    }

    private void ConfrontoCredenzialiDB(String username, String password) {
        AdminPiattaforma utenteLogin = repo.findByUsername(username);
        if(utenteLogin == null)
            throw new NullPointerException("Non esiste alcun utente con l'username passato");
        if(!CheckPassword(password, utenteLogin.getUsername()))
            throw new IllegalArgumentException("Password errata");
    }

    public boolean CheckPassword(String rawPassword, String username) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, this.repo.findByUsername(username).getPassword());
    }
}
