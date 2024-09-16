package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.Contenuto.PuntoLogicoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.LuogoDTO;
import com.unicam.dto.Risposte.PuntoGeoResponseDTO;
import com.unicam.dto.Risposte.PuntoLogicoResponseDTO;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PuntoLogicoService {

    private final PuntoLogicoRepository repoPunto;
    private final PuntoGeoRepository repoGeo;
    private final UtenteRepository repoUtente;

    public PuntoLogicoService(PuntoLogicoRepository repoPunto,
                              PuntoGeoRepository repoGeo,
                              UtenteRepository repoUtente) {
        this.repoPunto = repoPunto;
        this.repoGeo = repoGeo;
        this.repoUtente = repoUtente;
    }

    /**
     * Insersci un nuovo PuntoLogico. Il nuovo punto viene salvato nel database solo se non esistono
     * punti approvati con lo stesso riferimento. Nel caso sia già presente un punto nel database
     * con lo stesso riferimento, il contenuto viene salvato solo se entrambi sono in stato di attesa.
     * Più punti logici possono avere uno stesso riferimento, Ma devono avere un titolo differente.
     * La presenza di contenuti ripetuti in attesa verrà gestita al momento dell'approvazione di uno
     * degli oggetti presenti più volte.
     *
     * @param contenuto punto da inserire nel database
     * @throws IllegalArgumentException se è gia stato approvato un punto con lo stesso riferimento
     *                                  del punto che si sta provando ad aggiungere
     */
    public void AggiungiContenuto(PuntoLogico contenuto) {
        if (this.repoPunto.existsByTitoloAndComune(contenuto.getTitolo(), contenuto.getComune())) {
            List<PuntoLogico> puntiApprovati = this.repoPunto.findByTitoloAndComune(contenuto.getTitolo(), contenuto.getComune());
            for (PuntoLogico puntoTrovato : puntiApprovati) {
                if (puntoTrovato.getRiferimento().equals(contenuto.getRiferimento())) {
                    if (puntoTrovato.getStato() == StatoContenuto.APPROVATO)
                        throw new IllegalArgumentException("Nel sistema è già presente questo punto logio per questo specifico punto");
                    throw new IllegalArgumentException("Nel sistema è già presente questo punto logico in stato di attesa o segnalato per questo punto specifico");
                }
            }
        }
        this.repoPunto.save(contenuto);
    }

    public List<PuntoLogicoResponseDTO> GetPuntiLogiciByComune(String comune) {
        List<PuntoLogico> puntiPresenti = this.repoPunto.findPuntoLogicoByComune(comune);
        List<PuntoLogicoResponseDTO> punti = new ArrayList<>();
        for (PuntoLogico punto : puntiPresenti) {
            if (punto.getStato() == StatoContenuto.APPROVATO) {
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

    public void AccettaORifiuta(String nomeContenuto, String luogo, String comune, StatoContenuto stato) {
        if (!this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA))
            throw new IllegalArgumentException("Il punto non è presente tra le richieste. " +
                    "Si prega di controllare di aver scritto bene il nome e riprovare");
        PuntoLogico punto = this.repoPunto.findByTitoloAndRiferimento(nomeContenuto, this.repoGeo.findGeoByTitoloAndComune(luogo, comune));
        if (punto == null)
            throw new IllegalArgumentException("L'avviso non esiste per il punto passato");
        if (stato == StatoContenuto.RIFIUTATO)
            this.repoPunto.delete(punto);
        else {
            punto.setStato(stato);
            this.repoPunto.save(punto);
        }
    }

    public void EliminaContenutiAttesaDoppioni(PuntoLogico punto) {
        List<PuntoLogico> puntiTrovati = new ArrayList<>();
        puntiTrovati.addAll(this.repoPunto.findByTitoloAndComuneAndRiferimentoAndStato(punto.getTitolo(),
                punto.getComune(), punto.getRiferimento(), StatoContenuto.ATTESA));
        if (!puntiTrovati.isEmpty())
            this.repoPunto.deleteAll(puntiTrovati);
    }

    public boolean ContienePuntoLogico(String titolo, String comune) {
        return this.repoPunto.existsByTitoloAndComune(titolo, comune);
    }

    public void ControllaPresenzaNome(String titolo, String nomePuntoGeo, String comune) {
        if (this.repoPunto.existsByTitoloAndComuneAndRiferimento(titolo, comune,
                this.repoGeo.findByTitoloAndComuneAndStato(nomePuntoGeo, comune, StatoContenuto.APPROVATO)))
            throw new IllegalArgumentException("Esiste già un punto logico (accettato o in attesa) con quel luogo.");
    }

    public List<PuntoLogicoResponseDTO> GetPuntiLogiciByAutore(User autore) {
        List<PuntoLogico> punti = this.repoPunto.findByAutore(autore);
        List<PuntoLogicoResponseDTO> puntiPropri = new ArrayList<>();
        if (punti != null) {
            for (PuntoLogico punto : punti) {
                PuntoLogicoResponseDTO nuovo = new PuntoLogicoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(punto.getRiferimento()));
                puntiPropri.add(nuovo);
            }
        }
        return puntiPropri;
    }

    public void EliminaPuntoLogico(String nomeAvviso, String comune, String nomeLuogo, long idUtente) {
        PuntoGeolocalizzato punto = this.repoGeo.findGeoByTitoloAndComune(nomeLuogo, comune);
        if(punto != null){
            if(this.repoPunto.existsByTitoloAndComuneAndRiferimento(nomeAvviso, comune, punto)){
                if(repoUtente.findUserById(idUtente).getRuoloComune() != Ruolo.CURATORE){
                    if(punto.getAutore().getId() == idUtente){
                        PuntoLogico avviso = this.repoPunto.findByTitoloAndRiferimento(nomeAvviso, punto);
                        this.repoPunto.delete(avviso);
                    }
                    else{
                        throw new IllegalArgumentException("Il contenuto non è stato inserito da te");
                    }
                }
                else{
                    PuntoLogico avviso = this.repoPunto.findByTitoloAndRiferimento(nomeAvviso, punto);
                    this.repoPunto.delete(avviso);
                }
            }
            else{
                throw new IllegalArgumentException("L'avviso non è presente. Controllare di aver inserito correttamente i parametri");
            }
        }
        else{
            throw new NullPointerException("Il luogo specificato non esiste. Controllare di aver inserito correttamente il luogo dell'avviso");
        }
    }
}
