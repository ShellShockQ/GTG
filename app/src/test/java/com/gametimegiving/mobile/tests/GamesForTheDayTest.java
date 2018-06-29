package com.gametimegiving.mobile.tests;

import com.gametimegiving.mobile.Game;
import com.gametimegiving.mobile.GamesForTheDay;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Detrick on 2/12/2018.
 */
public class GamesForTheDayTest {
    @Test
    public void GetAListOfTodaysGames(){
        //Arrange
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); //
        Date ExpectedDate = today.getTime();

        //Act
        GamesForTheDay todaysGames = new GamesForTheDay();
        List<Game> theGames = todaysGames.getTodaysGames();
        Date firstGameDate = todaysGames.getTheDate();

        //Assert
        Assert.assertEquals(ExpectedDate,firstGameDate);

    }

}