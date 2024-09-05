package com.unicam.Service.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.PuntoLogico;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.PuntoGeoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PuntoGeoService {

    private final PuntoGeoRepository repoPunto;
    private final UtenteRepository repoUtente;

    @Autowired
    public PuntoGeoService(PuntoGeoRepository repoPunto,
                              UtenteRepository repoUtente){
        this.repoPunto = repoPunto;
        this.repoUtente = repoUtente;
    }

    public void AggiungiContenuto(PuntoGeolocalizzato contenuto) {
        this.repoPunto.save(contenuto);
    }

    public void ApprovaContenuto(long id, PuntoGeolocalizzato contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoPunto.save(contenuto);
        } else {
            repoPunto.delete(contenuto);
        }
    }

    public PuntoGeolocalizzato GetPuntoGeoByNome(String nome) {
        return this.repoPunto.findGeoByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public List<PuntoGeolocalizzato> GetPuntiByListaNomi(List<String> nomiPunti) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for (String nome : nomiPunti) {
            punti.add(GetPuntoGeoByNome(nome.toUpperCase(Locale.ROOT)));
        }
        return punti;
    }

    public List<PuntoGeoResponseDTO> GetPuntiGeoByComune(String comune) {
        List<PuntoGeolocalizzato> puntiPresenti = this.repoPunto.findPuntoGeoByComune(comune);
        List<PuntoGeoResponseDTO> punti = new ArrayList<>();
        for (PuntoGeolocalizzato punto : puntiPresenti) {
            if (!(punto.getStato() == StatoContenuto.ATTESA))
                punti.add(new PuntoGeoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getLatitudine(), punto.getLongitudine(), punto.getAutore().getUsername()));
        }
        return punti;
    }

    public List<PuntoGeoResponseDTO> GetPuntiGeoStatoByComune(String comune, StatoContenuto stato) {
        List<PuntoGeolocalizzato> puntiPresenti = this.repoPunto.findPuntoGeoByComune(comune);
        List<PuntoGeoResponseDTO> punti = new ArrayList<>();
        for (PuntoGeolocalizzato punto : puntiPresenti) {
            if (punto.getStato() == stato)
                punti.add(new PuntoGeoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getLatitudine(), punto.getLongitudine(), punto.getAutore().getUsername()));
        }
        return punti;
    }

    public void AggiuntiPreferito(String nomeContenuto, Long idUtente) {
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitolo(nomeContenuto);
        punto.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoPunto.save(punto);
    }

    public void SegnalaContenuto(String nomeContenuto, long idCreatore) {
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitolo(nomeContenuto);
        punto.setStato(StatoContenuto.SEGNALATO);
        this.repoPunto.save(punto);
    }

    public void AccettaORifiuta(String nomeContenuto, Long idUtente, StatoContenuto stato) {
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitolo(nomeContenuto);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoPunto.delete(punto);
        else{
            punto.setStato(stato);
            this.repoPunto.save(punto);
        }
    }
}
