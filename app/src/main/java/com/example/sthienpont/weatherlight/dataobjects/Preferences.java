package com.example.sthienpont.weatherlight.dataobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sébastien on 22/8/15.
 */
public class Preferences implements Serializable {
    public List<String> cities;
    public String currentCity;

    public Preferences() {
        cities = new ArrayList<>();
        currentCity = "";
    }
}
