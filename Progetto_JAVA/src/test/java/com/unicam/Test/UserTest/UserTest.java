package com.unicam.test.UserTest;


import com.unicam.Model.User;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.UtenteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserTest {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private DataInitializer dataInitializer;



    @Before
    public void setUp() throws Exception{
        dataInitializer.run();
    }


    @Test
    public void TestRegistrazioneSuccesso() {
        User user = new User();
        user.setName("Antonio");
        user.setUsername("anto200");
        user.setPassword("Root111!");
    }

    @Test
    public void TestLogin() {

    }

    @Test
    public void TestEliminaAccount(){

    }

    @Test
    public void TestVisitaComune() {

    }
}
