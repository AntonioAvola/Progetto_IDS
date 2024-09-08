package com.unicam.Security;

import com.unicam.Model.*;
import com.unicam.Repository.IComuneRepository;
import com.unicam.Repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtenteRepository repoUente;

    private IComuneRepository repoComune;

    private List<String> nomiComuni = new ArrayList<>();

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Override
    public void run(String... args) throws Exception {
        nomiComuni.add("CASTELFIDARDO");
        nomiComuni.add("SANTA VITTORIA");
        nomiComuni.add("TOLENTINO");
        if(repoUente.count() == 0){
            CreateSuperADMIN();
            System.out.println("---------------------------------------------------------------------");
            int i = 1;
            for(String nome: nomiComuni){
                CreateGestoreComune(nome, i);
                CreateCuratore(nome, i);
                CreateAnimatore(nome, i);
                System.out.println("---------------------------------------------------------------------");
                i += 1;
            }
        }
    }


    private void CreateSuperADMIN(){

        User adminUser = new User();
        adminUser.setName("super admin");
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@admin.com");
        adminUser.setComune("");
        adminUser.setPassword(bCryptPasswordEncoder.encode("Admin111!"));
        adminUser.setRuoloComune(Ruolo.ADMIN);

        repoUente.save(adminUser);

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

        repoUente.save(GestoreUser);

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

        repoUente.save(CuratoreUser);
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

        repoUente.save(AnimatoreUser);
        System.out.println(AnimatoreUser.getComune() + ": " + AnimatoreUser.getRuoloComune() + " --> creato con successo");
    }

}
