package com.example.john.focused;

import android.widget.EditText;

/**
 * Created by John on 10/7/2017.
 */

public class goal {
    private String focus;
    private String overview;
    private String why;
    private String how;

    public String getFocus(){
        return focus;
    }

    public String getOverview(){
        return overview;
    }

    public String getHow(){
        return how;
    }

    public String getWhy(){
        return why;
    }

    public void setFocus(String s){
        focus = s;
    }

    public void setOverview(String s){
        overview=s;
    }
    public void setWhy(String s){
        why=s;
    }
    public void setHow(String s){
        how=s;
    }
}
