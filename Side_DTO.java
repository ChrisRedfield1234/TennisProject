package com.example.tennisproject;

public class Side_DTO {

    private String side_Id;
    private String player_Id;
    private String toss_Winner;
    public String getSide_Id() {
        return side_Id;
    }

    public void setSide_Id(String side_Id) {
        this.side_Id = side_Id;
    }

    public String getPlayer_Id() {
        return player_Id;
    }

    public void setPlayer_Id(String player_Id) {
        this.player_Id = player_Id;
    }

    public String getToss_Winner() {
        return toss_Winner;
    }

    public void setToss_Winner(String toss_Winner) {
        this.toss_Winner = toss_Winner;
    }
}
