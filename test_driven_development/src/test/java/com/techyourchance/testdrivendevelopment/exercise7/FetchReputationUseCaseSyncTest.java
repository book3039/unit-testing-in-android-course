package com.techyourchance.testdrivendevelopment.exercise7;

import static com.techyourchance.testdrivendevelopment.exercise7.FetchReputationUseCaseSync.*;
import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {
    public static final int REPUTATION_SUCCESS = 100;
    public static final int REPUTATION_ZERO = 0;

    // region constants

    // endregion constants

    // region helper fields
    @Mock GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;
    // endregion helper fields

    FetchReputationUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchReputationUseCaseSync(getReputationHttpEndpointSyncMock);

        success();
    }

    @Test
    public void fetchReputationSync_success_successReturned() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchReputationSync();
        // Assert
        assertEquals(result.getStatus(), Status.SUCCESS);
    }

    @Test
    public void fetchReputationSync_success_correctReputationReturned() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchReputationSync();
        // Assert
        assertEquals(result.getReputation(), REPUTATION_SUCCESS);
    }

    @Test
    public void fetchReputationSync_generalError_failureReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.fetchReputationSync();
        // Assert
        assertEquals(result.getStatus(), Status.FAILURE);
    }

    @Test
    public void fetchReputationSync_generalError_reputationZeroReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.fetchReputationSync();
        // Assert
        assertEquals(result.getReputation(), REPUTATION_ZERO);
    }

    @Test
    public void fetchReputationSync_networkError_failureReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchReputationSync();
        // Assert
        assertEquals(result.getStatus(), Status.FAILURE);
    }

    @Test
    public void fetchReputationSync_networkError_reputationZeroReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchReputationSync();
        // Assert
        assertEquals(result.getReputation(), REPUTATION_ZERO);
    }

    // region helper methods

    private void success() {
        when(getReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(
                new EndpointResult(
                        EndpointStatus.SUCCESS,
                        REPUTATION_SUCCESS
                )
        );
    }
    private void generalError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(
                new EndpointResult(
                        EndpointStatus.GENERAL_ERROR,
                        REPUTATION_ZERO
                )
        );
    }
    private void networkError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(
                new EndpointResult(
                        EndpointStatus.NETWORK_ERROR,
                        REPUTATION_ZERO
                )
        );
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes
}