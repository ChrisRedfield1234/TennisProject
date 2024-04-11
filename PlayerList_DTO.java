package com.example.tennisproject;

public class PlayerList_DTO {

    private PLAYER_DTO dto;

    private String group_Id;

    private String group_Name;

    public PLAYER_DTO getDto() {
        return dto;
    }

    public void setDto(PLAYER_DTO dto) {
        this.dto = dto;
    }

    public String getGroup_Id() {
        return group_Id;
    }

    public void setGroup_Id(String group_Id) {
        this.group_Id = group_Id;
    }

    public String getGroup_Name() {
        return group_Name;
    }

    public void setGroup_Name(String group_Name) {
        this.group_Name = group_Name;
    }
}
