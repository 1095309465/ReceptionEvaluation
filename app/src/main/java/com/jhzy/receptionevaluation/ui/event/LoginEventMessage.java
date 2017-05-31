package com.jhzy.receptionevaluation.ui.event;

/**
 * Created by bigyu2012 on 2017/4/24.
 */

public class LoginEventMessage {

    private  boolean loginSuccess;

    public LoginEventMessage(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}
