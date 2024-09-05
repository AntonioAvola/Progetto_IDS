package com.unicam.Service.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.PuntoLogico;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import com.unicam.Repository.Contenuto.PuntoLogicoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.LuogoDTO;
import com.unicam.dto.Risposte.PuntoLogicoResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PuntoLogicoService {

    private final PuntoLogicoRepository repoPunto;
    private final UtenteRepository repoUtente;

    public PuntoLogicoService(PuntoLogicoRepository repoPunto,
                              UtenteRepository repoUtente){
        this.repoPunto = repoPunto;
        this.repoUtente = repoUtente;
    }

    public void AggiungiContenuto(PuntoLogico contenuto) {
        this.repoPunto.save(contenuto);
    }

    public void ApprovaContenuto(long id, PuntoLogico contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoPunto.save(contenuto);
        } else {
            repoPunto.delete(contenuto);
        }
    }

    public PuntoLogico GetPuntoLogicoByNome(String nome){
        return this.repoPunto.findLogicoByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public List<PuntoLogicoResponseDTO> GetPuntiLogiciByComune(String comune) {
        List<PuntoLogico> puntiPresenti = this.repoPunto.findPuntoLogicoByComune(comune);
        List<PuntoLogicoResponseDTO> punti = new ArrayList<>();
        for (PuntoLogico punto : puntiPresenti) {
            if (!(punto.getStato() == StatoContenuto.ATTESA)) {
                PuntoLogicoResponseDTO nuovo =
                        new PuntoLogicoResponseDTO(punto.getTitolo(), punto.getDescrizione(), punto.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(punto.getRiferimento()));
                punti.add(nuovo);
            }
        }
        return punti;
    }

    private LuogoDTO ConvertiInLuogoDTO(PuntoGeolocalizzato riferimento) {
        return new LuogoDTO(riferimento.getTitolo(), riferimento.getLatitudine(), riferimento.getLongitudine());
    }

    public List<PuntoLogicoResponseDTO> GetPuntiLogiciStatoByComune(String comune, StatoContenuto stato) {
        List<PuntoLogico> puntiPresenti = this.repoPunto.findPuntoLogicoByComune(comune);
        List<PuntoLogicoResponseDTO> punti = new ArrayList<>();
        for (PuntoLogico punto : puntiPresenti) {
            if (punto.getStato() == stato) {
                PuntoLogicoResponseDTO nuovo =
                        new PuntoLogicoResponseDTO(punto.getTitolo(), punto.getDescrizione(), punto.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(punto.getRiferimento()));
                punti.add(nuovo);
            }
        }
        return punti;
    }

    public void AggiungiPreferito(String nomeContenuto, Long idUtente) {
        PuntoLogico punto = this.repoPunto.findLogicoByTitolo(nomeContenuto);
        punto.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoPunto.save(punto);
    }

    public void SegnalaContenuto(String nomeContenuto, long idCreatore) {
        PuntoLogico punto = this.repoPunto.findLogicoByTitolo(nomeContenuto);
        punto.setStato(StatoContenuto.SEGNALATO);
        this.repoPunto.save(punto);
    }

    public void AccettaORifiuta(String nomeContenuto, Long idUtente, StatoContenuto stato) {
        PuntoLogico punto = this.repoPunto.findLogicoByTitolo(nomeContenuto);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoPunto.delete(punto);
        else{
            punto.setStato(stato);
            this.repoPunto.save(punto);
        }
    }
}
