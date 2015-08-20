
package com.example.sthienpont.weatherlight.dataobjects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sthienpont on 20-Aug-15.
 */
public class OpenWeather implements Serializable {

    public Coordinates coord;
    public List<Weather> weather;
    public String base;
    public Main main;
    public String visibility;
    public Wind wind;
    public Clouds clouds;
    public String dt;
    public Sys sys;
    public String id;
    public String name;
    public String cod;
}
