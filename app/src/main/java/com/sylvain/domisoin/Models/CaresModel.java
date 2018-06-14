package com.sylvain.domisoin.Models;

/**
 * Created by Sylvain on 27/03/2017.
 */

public class CaresModel {

    private String id = "";
    private String name = "";
    private String desc = "";
    private int duration = 0;

    public CaresModel() {

    }

    public CaresModel(String id, String name, String desc, int duration) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.duration = duration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
