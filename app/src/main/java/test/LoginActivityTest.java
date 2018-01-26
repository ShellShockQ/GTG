package test;

import com.gametimegiving.mobile.Activity.LoginActivity;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Detrick on 10/10/2017.
 */
public class LoginActivityTest {
    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void onResume() throws Exception {

    }

    @Test
    public void onActivityResult() throws Exception {

    }

    @Test
    public void UserIsLoggedInAlready() throws Exception {
        //Arrange
        LoginActivity testActivity = new LoginActivity();
        boolean loginStatus = testActivity.isLoggedIn();
        //Act
        //Assert
        Assert.assertEquals(loginStatus,true);
    }

    @Test
    public void onClick() throws Exception {

    }

}