package com.example.tennisproject;

public class MATCH_DTO {

    private String match_Id;
    private String opponents1;
    private String opponents2;
    private String v_Opponents;
    private String c_Umpire_Id;
    private String umpire_Id;
    private String court_Id;
    private String doubles_Flag;
    private String start_Time;
    private String end_Time;

    public String getMatch_Id() {
        return match_Id;
    }

    public void setMatch_Id(String match_Id) {
        this.match_Id = match_Id;
    }

    public String getOpponents1() {
        return opponents1;
    }

    public void setOpponents1(String opponents1) {
        this.opponents1 = opponents1;
    }

    public String getOpponents2() {
        return opponents2;
    }

    public void setOpponents2(String opponents2) {
        this.opponents2 = opponents2;
    }

    public String getV_Opponents() {
        return v_Opponents;
    }

    public void setV_Opponents(String v_Opponents) {
        this.v_Opponents = v_Opponents;
    }

    public String getC_Umpire_Id() {
        return c_Umpire_Id;
    }

    public void setC_Umpire_Id(String c_Umpire_Id) {
        this.c_Umpire_Id = c_Umpire_Id;
    }

    public String getUmpire_Id() {
        return umpire_Id;
    }

    public void setUmpire_Id(String umpire_Id) {
        this.umpire_Id = umpire_Id;
    }

    public String getCourt_Id() {
        return court_Id;
    }

    public void setCourt_Id(String court_Id) {
        this.court_Id = court_Id;
    }

    public String getDoubles_Flag() {
        return doubles_Flag;
    }

    public void setDoubles_Flag(String doubles_Flag) {
        this.doubles_Flag = doubles_Flag;
    }

    public String getStart_Time() {
        return start_Time;
    }

    public void setStart_Time(String start_Time) {
        this.start_Time = start_Time;
    }

    public String getEnd_Time() {
        return end_Time;
    }

    public void setEnd_Time(String end_Time) {
        this.end_Time = end_Time;
    }
}
