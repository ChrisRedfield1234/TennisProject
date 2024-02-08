package com.example.tennisproject;

public class Server_DTO {

    private String server_Id;
    private String player_Id;

    private String toss_Winner;

    public String getServer_Id() {
        return server_Id;
    }

    public void setServer_Id(String server_Id) {
        this.server_Id = server_Id;
    }

    public String getPlayer_Id(){
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
