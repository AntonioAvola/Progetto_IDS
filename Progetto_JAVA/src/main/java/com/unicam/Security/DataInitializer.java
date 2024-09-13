package com.unicam.Security;

import com.unicam.Model.*;
import com.unicam.Repository.ComuneRepository;
import com.unicam.Repository.Contenuto.EventoRepository;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtenteRepository repoUtente;

    @Autowired
    private ComuneRepository repoComune;

    @Autowired
    private PuntoGeoRepository repoPunto;

    @Autowired
    private EventoRepository repEvento;

    @Autowired
    private ProxyOSM proxyOSM;


    private List<String> nomiComuni = new ArrayList<>();
    private List<String> luoghiRoma = new ArrayList<>();

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Override
    public void run(String... args) throws Exception {
        nomiComuni.add("CASTELFIDARDO");
        nomiComuni.add("MILANO");
        nomiComuni.add("TOLENTINO");
        nomiComuni.add("ROMA");
        luoghiRoma.add("ARCO DI TITO");
        luoghiRoma.add("PANTHEON");
        luoghiRoma.add("FONTANA DI TREVI");
        if(repoUtente.count() == 0){
            CreateSuperADMIN();
            System.out.println("---------------------------------------------------------------------");
            int i = 1;
            for(String nome: nomiComuni){
                CreateGestoreComune(nome, i);
                CreateCuratore(nome, i);
                CreateAnimatore(nome, i);
                CreateContributor(nome, i);
                System.out.println("---------------------------------------------------------------------");
                i += 1;
            }
            CreateComuneApprovato();
            System.out.println("---------------------------------------------------------------------");
            CreateComuneAttesa();
            CreatePuntoGeoApprovato(luoghiRoma);
            CreateEventoApprovato();
            CreatePuntoGeoAttesa();
        }
        nomiComuni.clear();
    }


    private void CreateSuperADMIN(){

        User adminUser = new User();
        adminUser.setName("super admin");
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@admin.com");
        adminUser.setComune("");
        adminUser.setPassword(bCryptPasswordEncoder.encode("Admin111!"));
        adminUser.setRuoloComune(Ruolo.ADMIN);

        repoUtente.save(adminUser);

        System.out.println("Admin creato con successo");

    }

    private void CreateGestoreComune(String comune, int i){
        User GestoreUser = new User();
        GestoreUser.setName("gestore comune");
        GestoreUser.setUsername("gestore"+ i);
        GestoreUser.setEmail("gestore" + i + "@gestore.com");
        GestoreUser.setPassword(bCryptPasswordEncoder.encode("Gestore111!"));
        GestoreUser.setRuoloComune(Ruolo.COMUNE);
        GestoreUser.setComune(comune);

        repoUtente.save(GestoreUser);

        System.out.println(GestoreUser.getComune() + ": RAPPRESENTANTE " + GestoreUser.getRuoloComune() + " --> creato con successo");
    }

    private void CreateCuratore(String comune, int i){

        User CuratoreUser = new User();
        CuratoreUser.setName("curatore");
        CuratoreUser.setUsername("curatore" + i);
        CuratoreUser.setEmail("curatore" + i + "@curatore.com");
        CuratoreUser.setPassword(bCryptPasswordEncoder.encode("Curatore111!"));
        CuratoreUser.setRuoloComune(Ruolo.CURATORE);
        CuratoreUser.setComune(comune);

        repoUtente.save(CuratoreUser);
        System.out.println(CuratoreUser.getComune() + ": " + CuratoreUser.getRuoloComune() + " --> creato con successo");

    }

    private void CreateAnimatore(String comune, int i){
        User AnimatoreUser = new User();
        AnimatoreUser.setName("animatore");
        AnimatoreUser.setUsername("animatore" + i);
        AnimatoreUser.setEmail("animatore" + i + "@animatore.com");
        AnimatoreUser.setPassword(bCryptPasswordEncoder.encode("Animatore111!"));
        AnimatoreUser.setRuoloComune(Ruolo.ANIMATORE);
        AnimatoreUser.setComune(comune);

        repoUtente.save(AnimatoreUser);
        System.out.println(AnimatoreUser.getComune() + ": " + AnimatoreUser.getRuoloComune() + " --> creato con successo");
    }

    private void CreateContributor(String comune, int i){

        User ContributorUser = new User();
        ContributorUser.setName("contributor");
        ContributorUser.setUsername("contributor" + i);
        ContributorUser.setEmail("contributor" + i + "@contributor.com");
        ContributorUser.setComune(comune);
        ContributorUser.setPassword(bCryptPasswordEncoder.encode("Contributor111!"));
        ContributorUser.setRuoloComune(Ruolo.CONTRIBUTOR);

        repoUtente.save(ContributorUser);

        System.out.println(ContributorUser.getComune() + ": " + ContributorUser.getRuoloComune() + " --> creato con successo");

    }

    private void CreateComuneApprovato() throws IOException {
        List<Double> coordinate = proxyOSM.getCoordinates("ROMA");
        PuntoGeolocalizzato puntoComune = new PuntoGeolocalizzato();
        puntoComune.setAutore(this.repoUtente.findUserById(14));
        puntoComune.setTitolo("COMUNE");
        puntoComune.setComune("ROMA");
        puntoComune.setDescrizione("comune");
        puntoComune.setLatitudine(coordinate.get(0));
        puntoComune.setLongitudine(coordinate.get(1));
        puntoComune.setStato(StatoContenuto.APPROVATO);
        repoPunto.save(puntoComune);

        Comune comune = new Comune();
        comune.setNome("ROMA");
        comune.setPosizione(puntoComune);
        comune.setStatoRichiesta(StatoContenuto.APPROVATO);
        repoComune.save(comune);

        System.out.println("ROMA: COMUNE aggiunto");
    }

    private void CreateComuneAttesa() throws IOException {
        List<Double> coordinate = proxyOSM.getCoordinates("MILANO");
        PuntoGeolocalizzato puntoComune = new PuntoGeolocalizzato();
        puntoComune.setAutore(this.repoUtente.findUserById(6));
        puntoComune.setTitolo("COMUNE");
        puntoComune.setComune("MILANO");
        puntoComune.setDescrizione("comune");
        puntoComune.setLatitudine(coordinate.get(0));
        puntoComune.setLongitudine(coordinate.get(1));
        puntoComune.setStato(StatoContenuto.ATTESA);
        repoPunto.save(puntoComune);

        Comune comune = new Comune();
        comune.setNome("MILANO");
        comune.setPosizione(puntoComune);
        comune.setStatoRichiesta(StatoContenuto.ATTESA);
        repoComune.save(comune);

        System.out.println("MILANO: COMUNE richiesta inviata");
    }

    private void CreatePuntoGeoApprovato(List<String> luoghiRoma) throws IOException {
        for(String luogo: luoghiRoma){
            PuntoGeolocalizzato puntoComune = new PuntoGeolocalizzato();
            puntoComune.setAutore(this.repoUtente.findUserById(15));
            puntoComune.setTitolo(luogo);

            String url = URLEncoder.encode(luogo, StandardCharsets.UTF_8);
            List<Double> coordinate = proxyOSM.getCoordinates( url + ",Roma");

            puntoComune.setComune("ROMA");
            puntoComune.setDescrizione("descrizione");
            puntoComune.setLatitudine(coordinate.get(0));
            puntoComune.setLongitudine(coordinate.get(1));
            puntoComune.setStato(StatoContenuto.APPROVATO);
            repoPunto.save(puntoComune);

            System.out.println("ROMA: " + luogo + " --> punto geolocalizzato aggiunto");
            System.out.println("---------------------------------------------------------------------");
        }
    }

    private void CreatePuntoGeoAttesa() throws IOException {
        String luogo = URLEncoder.encode("PIAZZA NAVONA", StandardCharsets.UTF_8);
        List<Double> coordinate = proxyOSM.getCoordinates( luogo + ",Roma");
        PuntoGeolocalizzato puntoComune = new PuntoGeolocalizzato();
        puntoComune.setAutore(this.repoUtente.findUserById(17));
        puntoComune.setTitolo("PIAZZA NAVONA");
        puntoComune.setComune("ROMA");
        puntoComune.setDescrizione("descrizione");
        puntoComune.setLatitudine(coordinate.get(0));
        puntoComune.setLongitudine(coordinate.get(1));
        puntoComune.setStato(StatoContenuto.ATTESA);
        repoPunto.save(puntoComune);

        System.out.println("ROMA: " + luogo + " --> punto geolocalizzato in attesa");
    }

    private void CreateEventoApprovato() throws IOException {
        Evento evento = new Evento();
        evento.setAutore(this.repoUtente.findUserById(16));
        evento.setComune("ROMA");
        evento.setTitolo("Festa della birra");
        evento.setDescrizione("Bellissima festa con tantissima birra buona");

        PuntoGeolocalizzato luogo = repoPunto.findGeoByTitoloAndComune("FONTANA DI TREVI", "ROMA");
        evento.setLuogo(luogo);

        Tempo tempo = new Tempo();
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 0, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 0, 0));
        evento.setDurata(tempo);
        evento.setStato(StatoContenuto.APPROVATO);

        repEvento.save(evento);

        System.out.println("Evento 'Festa della birra' creato con successo e associato al luogo: " + luogo.getTitolo());
    }
}
