package com.unicam.Service;

import com.unicam.Authorization.AuthorizationService;
import com.unicam.Model.Contenuto;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import com.unicam.Repository.IContenutoRepository;
import com.unicam.Repository.IUtenteRepository;
import com.unicam.dto.Provvisori.SegnalazioneProvvisoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ContenutoService <T extends Contenuto> {

    private final IContenutoRepository repo;

    private final IUtenteRepository repoUser;
    private AuthorizationService autorizzazioni = new AuthorizationService();


    @Autowired
    public ContenutoService(IContenutoRepository repo, IUtenteRepository repoUser) {
        this.repoUser = repoUser;
        this.repo = repo;
    }

    public void AggiungiContenuto(T contenuto){
        repo.save(contenuto);
    }

    public void ApprovaContenuto(Long id, T contenuto, StatoContenuto nuovoStato){
        RicercaUtente(id);
        User user = repoUser.getById(id);
        VerificaAutorizzazioni(user);
        if(nuovoStato == StatoContenuto.APPROVATO){
            contenuto.setStato(nuovoStato);
            repo.save(contenuto);
        }
        else{
            repo.delete(contenuto);
        }
    }

    private void VerificaAutorizzazioni(User user) {
        if(!autorizzazioni.VerificaPermesso(user, "AggiungiContenuto")){
            throw new UnsupportedOperationException("Non hai il permesso di creare contenuti");
        }
    }

    private void RicercaUtente(Long id) {
        if(!repo.existsById(id)){
            throw new IllegalArgumentException("L'utente non esiste");
        }
    }

    public List<PuntoGeolocalizzato> GetPuntiByListaNomi(List<String> nomiPunti) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for (String nome: nomiPunti) {
            punti.add(GetPuntoByNome(nome.toUpperCase(Locale.ROOT)));
        }
        return punti;
    }

    public PuntoGeolocalizzato GetPuntoByNome(String nome){
        return this.repo.findByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public void SegnalaContenuto(SegnalazioneProvvisoriaDTO segnala) {
        this.repo.findByTitolo(segnala.getNomeContenuto()).setStato(StatoContenuto.SEGNALATO);
    }
}
