package com.gametimegiving.mobile;

import com.gametimegiving.mobile.sampledata.SampleData;

import java.util.Date;
import java.util.List;

/**
 * Created by Detrick on 2/12/2018.
 */

public class GamesForTheDay extends Game {
   public Date getTheDate() {
      return TheDate;
   }

   public void setTheDate(Date theDate) {
      TheDate = theDate;
   }

   public GamesForTheDay getTodaysGames() {
     return  SampleData.GetListOfGamesFromSampleData();
   }

   public void setTodaysGames(List<Game> todaysGames) {

      TodaysGames = todaysGames;
   }

   private Date TheDate;
   private List<Game> TodaysGames;


}
