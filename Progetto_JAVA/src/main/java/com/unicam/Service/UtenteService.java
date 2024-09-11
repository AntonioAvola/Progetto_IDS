package com.unicam.Service;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.*;
import com.unicam.Repository.UtenteRepository;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.Validator.EmailValidator;
import com.unicam.Validator.PasswordValidator;
import com.unicam.dto.RegistrazioneUtentiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UtenteService implements UserDetailsService {

    private final UtenteRepository repository;
    private final PuntoGeoRepository repoGeo;
    private final PuntoLogicoRepository repoLogico;
    private final ItinerarioRepository repoIt;
    private final EventoRepository repoEv;
    private final ContestRepository repoCon;


    private JwtTokenProvider tokenProvider = new JwtTokenProvider();

    @Autowired
    public UtenteService(UtenteRepository repo,
                         PuntoGeoRepository repoGeo,
                         PuntoLogicoRepository repoLogico,
                         ItinerarioRepository repoIt,
                         EventoRepository repoEv,
                         ContestRepository repoCon){
        this.repository = repo;
        this.repoGeo = repoGeo;
        this.repoLogico = repoLogico;
        this.repoIt = repoIt;
        this.repoEv = repoEv;
        this.repoCon = repoCon;
    }

    public String RegistrazioneUtente(RegistrazioneUtentiDTO registrazione){
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
        if(user.getName().isBlank())
            throw new IllegalArgumentException("Il nome non è stato inserito");
        if(user.getUsername().isBlank())
            throw new IllegalArgumentException("L'username non è stato inserito");
        if(user.getComune().isBlank())
            throw new IllegalArgumentException("Il Comune di residenza non è stato inserito");
        if(user.getEmail().isBlank())
            throw new IllegalArgumentException("L'email non è stata inserita");
        if(!EmailValidator.isValidEmail(user.getEmail()))
            throw new IllegalArgumentException("L'email non è stata inserita correttamente. Si prega di inserire una email valida");
        if(user.getPassword().isBlank())
            throw new IllegalArgumentException("La password non è stata inserita");
        if(!PasswordValidator.isValidPassword(user.getPassword()))
            throw new IllegalArgumentException("La password non rispetta i requisiti richiesti: lunghezza 5, almeno una maiuscola, " +
                    "almeno una minuscola, almeno un numero, almeno un simbolo speciale, niente spazi vuoti");
    }

    public void EliminaUtente(long idUtente){
        if(!repository.existsById(idUtente))
            throw new IllegalArgumentException("L'utente non esiste");
        //User user = this.repository.findUserById(idUtente);
        EliminaPuntiLogici(this.repository.findUserById(idUtente));
        EliminaItinerari(this.repository.findUserById(idUtente));
        EliminaEventi(this.repository.findUserById(idUtente));
        EliminaContest(this.repository.findUserById(idUtente));
        EliminaPuntiGeo(this.repository.findUserById(idUtente));
        this.repository.deleteById(idUtente);
    }

    private void AggiornaItinerari(List<Itinerario> itinerari, long idUtente) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for(Itinerario itinerario : itinerari){
            punti.addAll(itinerario.getPuntiDiInteresse());
            List<PuntoGeolocalizzato> interessi = new ArrayList<>();
            for(PuntoGeolocalizzato punto : punti){
                if(!(punto.getAutore().getId() == idUtente)){
                    interessi.add(punto);
                }
            }
            itinerario.setPuntiDiInteresse(interessi);
            this.repoIt.save(itinerario);
            punti.clear();
        }
    }

    private void EliminaContest(User user) {
        List<Contest> contestPresenti = this.repoCon.findByAutore(user);
        for (Contest contest: contestPresenti) {
            this.repoCon.delete(contest);
        }
    }

    private void EliminaEventi(User user) {
        List<Evento> eventi = this.repoEv.findByAutore(user);
        for (Evento evento: eventi) {
            this.repoEv.delete(evento);
        }
        //elimino tutti gli eventi che non sono dell'utente che si elimina, che che hanno come luogo di riferimento un suo punto
        eventi = this.repoEv.findEventoByComune(user.getComune());
        List<PuntoGeolocalizzato> geo = this.repoGeo.findByAutore(user);
        for(Evento evento : eventi){
            if(geo.contains(evento.getLuogo()))
                this.repoEv.delete(evento);
        }
    }

    private void EliminaItinerari(User user) {
        List<Itinerario> itinerari = this.repoIt.findByAutore(user);
        for (Itinerario itinerario: itinerari) {
            this.repoIt.delete(itinerario);
        }
        itinerari = this.repoIt.findItinerarioByComune(user.getComune());
        AggiornaItinerari(itinerari, user.getId());
    }

    private void EliminaPuntiLogici(User user) {
        List<PuntoLogico> punti = this.repoLogico.findByAutore(user);
        for (PuntoLogico punto: punti) {
            this.repoLogico.delete(punto);
        }
        List<PuntoGeolocalizzato> geo = this.repoGeo.findByAutore(user);
        punti = this.repoLogico.findByComune(user.getComune());
        if(geo != null){
            for(PuntoLogico punto : punti){
                if(geo.contains(punto.getRiferimento()))
                    this.repoLogico.delete(punto);
            }
        }
    }

    private void EliminaPuntiGeo(User user) {
        List<PuntoGeolocalizzato> punti = this.repoGeo.findByAutore(user);
        for (PuntoGeolocalizzato punto: punti) {
            this.repoGeo.delete(punto);
        }
    }

    public String LoginUtente(String username, String password){
        ConfrontoCredenzialiDB(username, password);

        User user = repository.findByUsername(username);
        user.setComuneVisitato(user.getComune());
        this.repository.save(user);

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

    public Ruolo GetUtente(String username) {
        return repository.findByUsername(username).getRuoloComune();
    }
    public User GetUtenteById(Long id){
        return repository.getById(id);
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

    public void AggiornaComuneVisitato(Long idUtente, String nome) {
        User utente = this.repository.getById(idUtente);
        utente.setComuneVisitato(nome.toUpperCase(Locale.ROOT));
        this.repository.save(utente);
    }
}
