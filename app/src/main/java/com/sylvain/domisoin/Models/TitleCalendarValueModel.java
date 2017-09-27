package com.sylvain.domisoin.Models;

import java.util.Date;

/**
 * Created by sylvain on 22/09/17.
 */

public class TitleCalendarValueModel {
    private String _title = null;
    private Date _time = null;

    public TitleCalendarValueModel() {

    }

    public TitleCalendarValueModel (String title, Date time) {
        _title = title;
        _time = time;
    }

    public void set_time(Date _time) {
        this._time = _time;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public Date get_time() {
        return _time;
    }

    public String get_title() {
        return _title;
    }
}
