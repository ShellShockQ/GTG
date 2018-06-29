package com.gametimegiving.mobile.tests;

import com.gametimegiving.mobile.sampledata.SampleData;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Created by Detrick on 1/27/2018.
 */

public class SampleDataTest {

    @Test
    public void IfTestValueIsTrueThenIsTestRunningReturnsTrue(){
        //Arrange
        boolean input =true;
        //Act
        SampleData SDT = new SampleData();
        boolean result = SDT.IsTestRunning(input);
        //Assert
        Assert.assertEquals(result,input);

    }
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
}
