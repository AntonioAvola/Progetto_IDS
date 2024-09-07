package com.unicam.Service;

import com.unicam.Model.Comune;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.IComuneRepository;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.dto.Risposte.ComuneResponseDTO;
import com.unicam.dto.Risposte.LuogoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComuneService {

    @Autowired
    private PuntoGeoService servizioPunto;
    private final IComuneRepository repository;
    private final PuntoGeoRepository repositoryPunto;

    @Autowired
    public ComuneService(IComuneRepository repository,
                         PuntoGeoRepository repositoryPunto){
        this.repository = repository;
        this.repositoryPunto = repositoryPunto;
    }

    public void AggiungiComune(Comune comune) {
        this.repository.save(comune);
    }

    public boolean ContainComune(String nome) {
        return this.repository.existsByNome(nome);
    }

    public List<Comune> GetAll() {
        return this.repository.findAll();
    }

    public List<ComuneResponseDTO> GetComuneByStato(StatoContenuto attesa) {
        return ConvertiInResponse(this.repository.findAllByStatoRichiesta(attesa));
    }

    private List<ComuneResponseDTO> ConvertiInResponse(List<Comune> allByStatoRichiesta) {
        List<ComuneResponseDTO> comuni = new ArrayList<>();
        for (Comune comune: allByStatoRichiesta) {
            ComuneResponseDTO nuovo = new ComuneResponseDTO(comune.getNome());
            nuovo.setLuogo(ConvertiInLuogoDTO(comune.getPosizione()));
            comuni.add(nuovo);
        }
        return comuni;
    }

    private LuogoDTO ConvertiInLuogoDTO(PuntoGeolocalizzato riferimento) {
        return new LuogoDTO(riferimento.getTitolo(), riferimento.getLatitudine(), riferimento.getLongitudine());
    }

    public void AccettaORifiutaComune(String nomeComune, StatoContenuto stato) {
        if(!this.repository.existsByNome(nomeComune))
            throw new NullPointerException("Non esiste la richiesta per il comune inserito. " +
                    "Si prega di controllare di aver scritto il comune in maniera corretta");
        Comune comune = this.repository.findByNome(nomeComune);
        PuntoGeolocalizzato punto = this.repositoryPunto.findGeoByTitoloAndComune("COMUNE", nomeComune);
        if(stato == StatoContenuto.RIFIUTATO) {
            this.repository.delete(comune);
            this.repositoryPunto.delete(punto);
        }
        else{
            comune.setStatoRichiesta(stato);
            punto.setStato(stato);
            this.repositoryPunto.save(punto);
            this.repository.save(comune);
            this.servizioPunto.EliminaContenutiAttesaDoppioni(punto);
        }
    }

    public Comune GetComuneByNome(String comune) {
        return this.repository.findByNome(comune);
    }

    public List<ComuneResponseDTO> GetAllPresenti() {
        List<Comune> comuni = this.repository.findAllByStatoRichiesta(StatoContenuto.APPROVATO);
        return ConvertiInResponse(comuni);
    }
}
