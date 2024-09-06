package com.unicam.Security;

import com.unicam.Model.Ruolo;
import com.unicam.Model.User;
import com.unicam.Repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtenteRepository repoUente;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Override
    public void run(String... args) throws Exception {
        if(repoUente.count() == 0){
            CreateSuperADMIN();
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
}
