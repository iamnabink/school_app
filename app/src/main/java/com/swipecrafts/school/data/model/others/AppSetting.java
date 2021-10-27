package com.swipecrafts.school.data.model.others;


import com.swipecrafts.school.data.model.api.UserType;

/**
 * Created by Madhusudan Sapkota on 4/8/2018.
 */
public class AppSetting {

    private boolean isUserLoggedIn = false;
    private UserType userType;

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }

    public void setUserType(String userType) {
        this.userType = UserType.from(userType);
    }

    public UserType getUserType() {
        return userType;
    }
}
