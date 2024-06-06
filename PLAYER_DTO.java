package com.example.tennisproject;

public class PLAYER_DTO {

    private String player_Id;
    private String group_Id;
    private String lastName;
    private String firstName;
    private String loser_Flag;

    public String getPlayer_Id() {
        return player_Id;
    }

    public void setPlayer_Id(String player_Id) {
        this.player_Id = player_Id;
    }

    public String getGroup_Id() {
        return group_Id;
    }

    public void setGroup_Id(String group_Id) {
        this.group_Id = group_Id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLoser_Flag() {
        return loser_Flag;
    }

    public void setLoser_Flag(String loser_Flag) {
        this.loser_Flag = loser_Flag;
    }

}
