package com.techyourchance.unittestinginandroid.example12;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import android.content.Context;

@RunWith(MockitoJUnitRunner.class)
public class StringRetrieverPracticeTest {
    public static final int ID = 10;
    public static final String STRING = "string";

    // region constants

    // endregion constants

    // region helper fields
    @Mock Context contextMock;
    // endregion helper fields

    StringRetrieverPractice SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringRetrieverPractice(contextMock);

    }

    @Test
    public void getString_correctParameterPassedToContext() throws Exception {
        // Arrange
        // Act
        SUT.getString(ID);
        // Assert
        verify(contextMock).getString(ID);
    }

    @Test
    public void getString_correctResultReturned() throws Exception {
        // Arrange
        when(contextMock.getString(ID)).thenReturn(STRING);
        // Act
        String result = SUT.getString(ID);
        // Assert
        assertEquals(result, STRING);
    }

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}