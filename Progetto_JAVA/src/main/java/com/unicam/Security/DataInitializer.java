package com.unicam.Security;

import com.unicam.Model.*;
import com.unicam.Repository.IComuneRepository;
import com.unicam.Repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtenteRepository repoUente;

    private IComuneRepository repoComune;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Override
    public void run(String... args) throws Exception {
        if(repoUente.count() == 0){
            CreateSuperADMIN();
            CreateGestoreComune();
            CreateCuratore();
            CreateAnimatore();
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

    private void CreateGestoreComune(){
        User GestoreUser = new User();
        GestoreUser.setName("gestore comune");
        GestoreUser.setUsername("gestore");
        GestoreUser.setEmail("gestore@gestore.com");
        GestoreUser.setPassword(bCryptPasswordEncoder.encode("Gestore111!"));
        GestoreUser.setRuoloComune(Ruolo.COMUNE);
        GestoreUser.setComune("CASTELFIDARDO");

        repoUente.save(GestoreUser);

        System.out.println(GestoreUser.getComune() + ": RAPPRESENTANTE " + GestoreUser.getRuoloComune() + " --> creato con successo");
    }

    private void CreateCuratore(){

        User CuratoreUser = new User();
        CuratoreUser.setName("curatore");
        CuratoreUser.setUsername("curatore");
        CuratoreUser.setEmail("curatore@curatore.com");
        CuratoreUser.setPassword(bCryptPasswordEncoder.encode("Curatore111!"));
        CuratoreUser.setRuoloComune(Ruolo.CURATORE);
        CuratoreUser.setComune("CASTELFIDARDO");

        repoUente.save(CuratoreUser);
        System.out.println(CuratoreUser.getComune() + ": " + CuratoreUser.getRuoloComune() + " --> creato con successo");

    }

    private void CreateAnimatore(){
        User AnimatoreUser = new User();
        AnimatoreUser.setName("animatore");
        AnimatoreUser.setUsername("animatore");
        AnimatoreUser.setEmail("animatore@animatore.com");
        AnimatoreUser.setPassword(bCryptPasswordEncoder.encode("Animatore111!"));
        AnimatoreUser.setRuoloComune(Ruolo.ANIMATORE);
        AnimatoreUser.setComune("CASTELFIDARDO");

        repoUente.save(AnimatoreUser);
        System.out.println(AnimatoreUser.getComune() + ": " + AnimatoreUser.getRuoloComune() + " --> creato con successo");
    }

}
