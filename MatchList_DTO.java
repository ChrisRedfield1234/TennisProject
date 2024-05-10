package com.example.tennisproject;

public class MatchList_DTO {

    private PLAYER_DTO p_dto1;

    private PLAYER_DTO p_dto2;

    private MATCH_DTO m_dto;

    public PLAYER_DTO getP_dto1() {
        return p_dto1;
    }

    public void setP_dto1(PLAYER_DTO p_dto1) {
        this.p_dto1 = p_dto1;
    }

    public PLAYER_DTO getP_dto2() {
        return p_dto2;
    }

    public void setP_dto2(PLAYER_DTO p_dto2) {
        this.p_dto2 = p_dto2;
    }

    public MATCH_DTO getM_dto() {
        return m_dto;
    }

    public void setM_dto(MATCH_DTO m_dto) {
        this.m_dto = m_dto;
    }

}
