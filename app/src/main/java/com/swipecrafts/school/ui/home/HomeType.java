package com.swipecrafts.school.ui.home;

/**
 * Created by Madhusudan Sapkota on 2/28/2018.
 */

public enum HomeType {
    TODAY(0),
    UPCOMING(1),
    PARENT_AV(2),
    CALENDAR(3),
    GALLERY(4),
    OTHERS(5);

    private final int type;

    HomeType(int type){
        this.type = type;
    }

    public int value() {
        return type;
    }

    public String displayName(){
        switch (type){
            case 0: return "Today";
            case 1: return "Upcoming";
            case 2: return "Parent Availability";
            case 3: return "Calendar";
            case 4: return "Gallery";
            default: return "Others";
        }
    }
}
