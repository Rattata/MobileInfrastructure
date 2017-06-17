package com.example.app.androidapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ryno on 17-6-2017.
 */

public class DiscogsResults {

    @SerializedName("pagination")
    @Expose
    private JSONObject pagination;


    public JSONObject getPagination() {
        return pagination;
    }

    public void setPagination(JSONObject pagination) {
        this.pagination = pagination;
    }
}
