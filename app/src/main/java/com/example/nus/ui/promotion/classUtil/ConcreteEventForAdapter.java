package com.example.nus.ui.promotion.classUtil;

import java.util.Calendar;
import java.util.Date;

public class ConcreteEventForAdapter{
    public int id = -1;
    public String name = "";
    public int buildingId = -1;
    public ConcreteBuildingForAdapter building = null;

    public int type = NO_TYPE_EVENT;
    public String content = "";
    public Date time = null;

    public static final int NO_TYPE_EVENT = -1;
    public static final int TYPE_LECTURE = 1;
    public static final int TYPE_SPEECH = 2;
    public static final int TYPE_JOY = 3;
    public static final int TYPE_SPORT = 4;
    public static final int TYPE_PROMOTION = 5;


    public ConcreteEventForAdapter(int id, String name, int buildingId, ConcreteBuildingForAdapter building, String content, Date time, int type) {
        this.id = id;
        this.name = name;
        this.buildingId = buildingId;
        this.building = building;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public ConcreteEventForAdapter(int id, String name, int buildingId, ConcreteBuildingForAdapter building, String content, Date time) {
        this.id = id;
        this.name = name;
        this.buildingId = buildingId;
        this.building = building;
        this.content = content;
        this.time = time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public ConcreteBuildingForAdapter getBuilding() {
        return building;
    }

    public void setBuilding(ConcreteBuildingForAdapter building) {
        this.building = building;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
