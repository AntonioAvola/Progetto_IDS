package com.unicam.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "postTurista")
public class PostTurista extends Contenuto{

    @Lob
    private Long fileLenght;
    private String fileName;
    private boolean isEmpty;
    private byte[] fileData;

    public Long getFileLenght() {
        return fileLenght;
    }

    public void setFileLenght(Long fileLenght) {
        this.fileLenght = fileLenght;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
