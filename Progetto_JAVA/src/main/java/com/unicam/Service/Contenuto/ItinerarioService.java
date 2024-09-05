package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.ItinerarioRepository;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
import com.unicam.dto.Risposte.LuogoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ItinerarioService {

    private final ItinerarioRepository repoItinerario;
    private final PuntoGeoRepository repoPunto;
    private final UtenteRepository repoUtente;

    public ItinerarioService(ItinerarioRepository repoItinerario,
                             PuntoGeoRepository repoPunto,
                             UtenteRepository repoUtente){
        this.repoItinerario = repoItinerario;
        this.repoPunto = repoPunto;
        this.repoUtente = repoUtente;
    }

    public void AggiungiContenuto(Itinerario contenuto) {
        this.repoItinerario.save(contenuto);
    }

    public List<PuntoGeolocalizzato> GetPuntiByListaNomi(List<String> nomiPunti) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for (String nome : nomiPunti) {
            punti.add(GetPuntoGeoByNome(nome.toUpperCase(Locale.ROOT)));
        }
        return punti;
    }

    public PuntoGeolocalizzato GetPuntoGeoByNome(String nome) {
        return this.repoPunto.findGeoByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public void ApprovaContenuto(long id, Itinerario contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoItinerario.save(contenuto);
        } else {
            repoItinerario.delete(contenuto);
        }
    }

    public Itinerario GetItinerarioByNome(String nome){
        return this.repoItinerario.findItinerarioByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public List<ItinerarioResponseDTO> GetItinerariByComune(String comune) {
        List<Itinerario> itinerariPresenti = this.repoItinerario.findItinerarioByComune(comune);
        List<ItinerarioResponseDTO> itinerari = new ArrayList<>();
        for (Itinerario itinerario : itinerariPresenti) {
            if (!(itinerario.getStato() == StatoContenuto.ATTESA)) {
                ItinerarioResponseDTO nuovo = new ItinerarioResponseDTO(itinerario.getTitolo(),
                        itinerario.getDescrizione(), itinerario.getAutore().getUsername());
                nuovo.setLuoghi(ConvertiInListaDiLuoghiDTO(itinerario.getPuntiDiInteresse()));
                itinerari.add(nuovo);
            }
        }
        return itinerari;
    }

    private List<LuogoDTO> ConvertiInListaDiLuoghiDTO(List<PuntoGeolocalizzato> puntiDiInteresse) {
        List<LuogoDTO> luoghi = new ArrayList<>();
        for (PuntoGeolocalizzato punto : puntiDiInteresse) {
            luoghi.add(ConvertiInLuogoDTO(punto));
        }
        return luoghi;
    }

    private LuogoDTO ConvertiInLuogoDTO(PuntoGeolocalizzato riferimento) {
        return new LuogoDTO(riferimento.getTitolo(), riferimento.getLatitudine(), riferimento.getLongitudine());
    }

    public List<ItinerarioResponseDTO> GetItinerariStatoByComune(String comune, StatoContenuto stato) {
        List<Itinerario> itinerariPresenti = this.repoItinerario.findItinerarioByComune(comune);
        List<ItinerarioResponseDTO> itinerari = new ArrayList<>();
        for (Itinerario itinerario : itinerariPresenti) {
            if (itinerario.getStato() == stato) {
                ItinerarioResponseDTO nuovo = new ItinerarioResponseDTO(itinerario.getTitolo(),
                        itinerario.getDescrizione(), itinerario.getAutore().getUsername());
                nuovo.setLuoghi(ConvertiInListaDiLuoghiDTO(itinerario.getPuntiDiInteresse()));
                itinerari.add(nuovo);
            }
        }
        return itinerari;
    }

    public void AggiungiPreferito(String nomeContenuto, Long idUtente) {
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitolo(nomeContenuto);
        itinerario.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoItinerario.save(itinerario);
    }

    public void SegnalaContenuto(String nomeContenuto, long idCreatore) {
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitolo(nomeContenuto);
        itinerario.setStato(StatoContenuto.SEGNALATO);
        this.repoItinerario.save(itinerario);
    }

    public void AccettaORifiuta(String nomeContenuto, Long idUtente, StatoContenuto stato) {
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitolo(nomeContenuto);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoItinerario.delete(itinerario);
        else {
            itinerario.setStato(stato);
            this.repoItinerario.save(itinerario);
        }
    }
}
