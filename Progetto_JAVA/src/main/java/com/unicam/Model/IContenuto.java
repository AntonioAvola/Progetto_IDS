package com.unicam.Model;

import java.time.LocalDateTime;

public interface IContenuto {

    Long getId();
    void setId(Long id);
    String getTitolo();
    void setTitolo(String titolo);
    String getDescrizione();
    void setDescrizione(String descrizione);
    LocalDateTime getDataCreazione();
    void setDataCreazione(LocalDateTime dataCreazione);
    User getAutore();
    void setAutore(User user);
    StatoContenuto getStato();
    void setStato(StatoContenuto stato);
    @Override
    String toString();

}
