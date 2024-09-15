package com.unicam.dto.Risposte;

public class VincitoriContestDTO {

    private String contest;
    private String vincitore;

    public VincitoriContestDTO(String contest,
                               String vincitore){
        this.contest = contest;
        this.vincitore = vincitore;
    }

    public String getContest() {
        return contest;
    }

    public String getVincitore() {
        return vincitore;
    }
}
