package com.i3c.itrack.ui.login;

import androidx.annotation.Nullable;

import com.i3c.itrack.config.SharedPreference;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;
    private Integer userid;
    private String iser_name;

    public LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    public LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }

    public Integer getUserID(){return userid;}

    public void setUserName(String u_name){
        this.iser_name=u_name;
    }
    public String getUserName(){return iser_name;}
    public void setUserID(Integer uid){
        this.userid=uid;
    }
}