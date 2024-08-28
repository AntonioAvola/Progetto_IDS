package com.unicam.Authorization;

import com.unicam.Model.Ruolo;
import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthorizationService {
    private static final Map<Ruolo, Set<String>> autorizzazioni = new HashMap<Ruolo, Set<String>>();

    static {
        autorizzazioni.put(Ruolo.TURISTA_AUTENTICATO, Set.of("CreaContenuto", "ProponiContest"));
        autorizzazioni.put(Ruolo.CONTRIBUTOR, Set.of("CreaContenuto", "ProponiContenuto", "ModificaContenuto"));
        autorizzazioni.put(Ruolo.ANIMATORE, Set.of("CreaEvento", "CreaContest", "ApprovaPropostaContest"));
        autorizzazioni.put(Ruolo.CURATORE, Set.of("ApprovaContenuto", "DeclinaContenuto", "GestisciContenuti"));
        autorizzazioni.put(Ruolo.CONTRIBUTOR_AUTORIZZATO, Set.of("CreaContenuto", "AggiungiContenuto", "ModificaContenuto"));
    }

    public boolean verificaPermesso(UtenteDTO utente, String azione){
        Set<String> permessiPresenti = autorizzazioni.get(utente.getRuolo());
        return permessiPresenti != null && permessiPresenti.contains(azione);
    }

}
