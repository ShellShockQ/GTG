package com.gametimegiving.mobile.tests;

import com.gametimegiving.mobile.Activity.LoginActivity;

import junit.framework.Assert;

import org.junit.Test;

public class LoginActivityUnitTests {
    @Test
    public void UserIsLoggedInAlready() {
        //Arrange
        LoginActivity mActivity = new LoginActivity();
        boolean loginStatus = mActivity.isLoggedIn();
        //Act
        //Assert
        Assert.assertEquals(loginStatus, true);
    }
}
