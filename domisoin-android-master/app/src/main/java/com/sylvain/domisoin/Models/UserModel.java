package com.sylvain.domisoin.Models;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Sylvain on 04/07/2017.
 */

public class UserModel {
    public String id = null;
    public String email = null;
    public String first_name = null;
    public String last_name = null;
    public String job_title = null;
    public String address = null;
    public String workphone = null;
    public String profile_img = null;
    public Boolean is_pro = null;
    public String events = null;
    public JSONArray json_event = null;
    public String lat = null;
    public String lng = null;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setWorkphone(String workphone) {
        this.workphone = workphone;
    }

    public String getWorkphone() {
        return workphone;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setIs_pro(Boolean is_pro) {
        this.is_pro = is_pro;
    }

    public Boolean getIs_pro() {
        return is_pro;
    }

    public void setEvents(String events) {
        this.events = events;
        try {
            json_event = new JSONArray(this.events);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEvents() {
        return events;
    }

    public JSONArray getJson_event() {
        return json_event;
    }

    public String getLat() {
        return this.lat;
    }

    public String getLng() {
        return this.lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}
