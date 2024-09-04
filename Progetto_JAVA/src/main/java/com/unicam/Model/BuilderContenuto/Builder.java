package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.Comune;
import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

public interface Builder {
    void BuildAutore(User autore);
    void BuildTitolo(String titolo);
    void BuildDescrizione(String descrizione);
    void BuildComune(String comune);
}
