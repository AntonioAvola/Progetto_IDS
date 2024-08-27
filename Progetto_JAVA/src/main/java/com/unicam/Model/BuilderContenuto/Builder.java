package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

public interface Builder {
    void BuildAutore(UtenteDTO autore);
    void BuildTitolo(String titolo);
    void BuildDescrizione(String descrizione);
}
