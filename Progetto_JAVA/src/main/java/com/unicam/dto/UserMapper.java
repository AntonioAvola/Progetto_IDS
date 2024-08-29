package com.unicam.dto;

import com.unicam.Model.User;

public class UserMapper {

    //Metodo per convertire da User a UtenteDTO
    public static UtenteDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UtenteDTO(user.getUsername(), user.getComune(), user.getRuolo());
    }

    // Metodo per convertire da UtenteDTO a User
    public static User toEntity(UtenteDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setComune(dto.getNomeComune());
        user.setRuolo(dto.getRuolo());
        // Nota: campi come 'name', 'email' e 'password' non sono presenti nel DTO e dovrebbero essere gestiti separatamente.
        return user;
    }
}
