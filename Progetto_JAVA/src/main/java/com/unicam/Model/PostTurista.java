package com.unicam.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "postTurista")
public class PostTurista extends Contenuto{

    @Lob
    private byte[] fileData;

    public PostTurista(){
        super();
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
