package com.example.nus.ui.promotion.classUtil;

public class ConcreteBuildingForAdapter {
    public int id = -1;
    public String name = "";
    public int type = -1;

    public double pos_x = -1.0;
    public double pos_y = -1.0;

    // magic numbers for different types of building
    public static final int NO_TYPE_BUILDING = -1;
    public static final int GENERAL_BUILDING = 1;
    public static final int CANTEEN_BUILDING = 2;
    public static final int LIBRARY_BUILDING = 3;
    public static final int ACTIVITY_BUILDING = 4;
    public static final int ACADEMIC_BUILDING = 5;

    public ConcreteBuildingForAdapter(int id, String name, int type, double pos_x, double pos_y) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    public ConcreteBuildingForAdapter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ConcreteBuildingForAdapter(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPos_x() {
        return pos_x;
    }

    public void setPos_x(double pos_x) {
        this.pos_x = pos_x;
    }

    public double getPos_y() {
        return pos_y;
    }

    public void setPos_y(double pos_y) {
        this.pos_y = pos_y;
    }
}
