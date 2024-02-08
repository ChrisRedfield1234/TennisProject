package com.example.tennisproject;

public class Point_DTO {
    private String match_Id;
    private String set_Id;
    private String game_Id;
    private String point_Id;
    private String v_Id;
    private String fault_Flag;
    private String wfault_Flag;
    private String ace_Flag;
    private String start_Time;
    private String end_Time;


    public String getMatch_Id() {
        return this.match_Id;
    }

    public void setMatch_Id(String match_Id){
        this.match_Id = match_Id;
    }


    public String getSet_Id() {
        return this.set_Id;
    }

    public void setSet_Id(String set_Id){
        this.set_Id = set_Id;
    }


    public String getGame_Id() {
        return this.game_Id;
    }

    public void setGame_Id(String game_Id){
        this.game_Id = game_Id;
    }


    public String getPoint_Id() {
        return this.point_Id;
    }

    public void setPoint_Id(String point_Id){
        this.point_Id = point_Id;
    }


    public String getV_Id() {
        return this.v_Id;
    }

    public void setV_Id(String v_Id){
        this.v_Id = v_Id;
    }


    public String getFault_Flag() {
        return this.fault_Flag;
    }

    public void setFault_Flag(String fault_Flag){
        this.fault_Flag = fault_Flag;
    }


    public String getWfault_Flag() {
        return this.wfault_Flag;
    }

    public void setWfault_Flag(String wfault_Flag){
        this.wfault_Flag = wfault_Flag;
    }


    public String getAce_Flag() {
        return this.ace_Flag;
    }

    public void setAce_Flag(String ace_Flag){
        this.ace_Flag = ace_Flag;
    }


    public String getStart_Time() {
        return this.start_Time;
    }

    public void setStart_Time(String start_Time){
        this.start_Time = start_Time;
    }

    public String getEnd_Time() {
        return this.end_Time;
    }

    public void setEnd_Time(String end_Time){
        this.end_Time = end_Time;
    }
}
