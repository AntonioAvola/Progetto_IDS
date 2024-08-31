package com.unicam.Service;

import com.unicam.Model.Comune;
import com.unicam.Repository.IComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComuneService {

    private final IComuneRepository repository;

    @Autowired
    public ComuneService(IComuneRepository repository){
        this.repository = repository;
    }

    public void AggiungiComune(Comune comune) {
        this.repository.save(comune);
    }
}
