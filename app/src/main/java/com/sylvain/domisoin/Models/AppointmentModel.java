package com.sylvain.domisoin.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sylvain on 29/06/2017.
 */

public class AppointmentModel {
    private String id = null;
    private String description = null;
    private String author_id = null;
    private String client_id = null;
    private String type = null;
    private Date start_date = null;
    private Date end_date = null;
    private String start_date_str = null;
    private String end_date_str = null;
    private String location = null;
    private String duration = null;
    private String link = null;
    private Boolean is_validate = true;
    public AppointmentModel() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setStart_date(String start_date) {
        this.start_date_str = start_date;
        this.start_date = StringtoDate(this.start_date_str);
    }

    public String getStart_date_str() {
        return start_date_str;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date_str = end_date;
        this.end_date = StringtoDate(this.end_date_str);
    }

    public String getEnd_date_str() {
        return end_date_str;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public Date StringtoDate(String datestr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = format.parse(datestr);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setIs_validate(Boolean _is_validate) {
        is_validate = _is_validate;
    }

    public Boolean getIs_validate() {
        return is_validate;
    }

}
