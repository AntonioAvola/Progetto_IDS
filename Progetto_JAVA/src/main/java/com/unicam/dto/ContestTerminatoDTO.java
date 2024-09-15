package com.unicam.dto;

import com.unicam.Model.Contest;

import java.util.List;

public class ContestTerminatoDTO {

    private String contest;
    private List<String> partecipanti;

    public ContestTerminatoDTO(String contest, List<String> partecipanti){
        this.contest = contest;
        this.partecipanti = partecipanti;
    }

    public String getContest() {
        return contest;
    }

    public List<String> getPartecipanti() {
        return partecipanti;
    }
}
