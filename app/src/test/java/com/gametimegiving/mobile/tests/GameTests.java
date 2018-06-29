package com.gametimegiving.mobile.tests;

import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.sampledata.SampleData;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Detrick on 1/27/2018.
 */

public class GameTests {
    private Game mGame = new Game();
    @Test
    public void IfTestValueIsFalseThenIsTestRunningReturnsFalse(){
        //Arrange
        boolean input =false;
        //Act
        SampleData SDT = new SampleData();
        boolean result = SDT.IsTestRunning(input);
        //Assert
        Assert.assertEquals(result,input);

    }


    @Test
    public void WhenClearGamePledgeTotalsIsCalledThePledgesBecomeZero() {
        //Arrange
        //Assert
        //Act
        ;
       }
    @Test
    public void WhensetHometeam_pledgeToPledgeToZeroHomeTeamPledgeIsZero() {
        //Arrange
        int zeropledge = 0;
        mGame.setHometeam_pledge(zeropledge);
        //Assert
        int ActualResult = mGame.getHometeam_pledge();
        //Act
        Assert.assertEquals(zeropledge,ActualResult);
    }
    @Test
    public void WhensetHometeam_pledgeToNullHomeTeamPledgeStillReturnsZero() {
        //Arrange
        int zeropledge=0;
        mGame.setHometeam_pledge((Integer)null);
        //Assert
        int ActualResult = mGame.getHometeam_pledge();
        //Act
        Assert.assertEquals(zeropledge,ActualResult);
    }
}
