package com.techyourchance.testdrivendevelopment.example10.networking;

import static com.techyourchance.testdrivendevelopment.example10.networking.PingServerSyncUseCasePractice.*;
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

import com.techyourchance.testdrivendevelopment.example10.PingServerSyncUseCase;

@RunWith(MockitoJUnitRunner.class)
public class PingServerSyncUseCasePracticeTest {

    // region constants

    // endregion constants

    // region helper fields
    @Mock PingServerHttpEndpointSync pingServerHttpEndpointSyncMock;
    // endregion helper fields

    PingServerSyncUseCasePractice SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new PingServerSyncUseCasePractice(pingServerHttpEndpointSyncMock);
        success();
    }

    @Test
    public void pingServerSync_success_successReturned() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.pingServerSync();
        // Assert
        assertEquals(result, UseCaseResult.SUCCESS);
    }

    @Test
    public void pingServerSync_generalError_failureReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.pingServerSync();
        // Assert
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void pingServerSync_networkError_failureReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.pingServerSync();
        // Assert
        assertEquals(result, UseCaseResult.FAILURE);
    }

    // region helper methods
    private void success() {
        when(pingServerHttpEndpointSyncMock.pingServerSync()).thenReturn(PingServerHttpEndpointSync.EndpointResult.SUCCESS);
    }

    private void generalError() {
        when(pingServerHttpEndpointSyncMock.pingServerSync()).thenReturn(PingServerHttpEndpointSync.EndpointResult.GENERAL_ERROR);
    }

    private void networkError() {
        when(pingServerHttpEndpointSyncMock.pingServerSync()).thenReturn(PingServerHttpEndpointSync.EndpointResult.NETWORK_ERROR);
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes
}