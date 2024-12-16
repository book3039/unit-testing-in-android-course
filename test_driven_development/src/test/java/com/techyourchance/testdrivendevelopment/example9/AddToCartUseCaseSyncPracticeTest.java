package com.techyourchance.testdrivendevelopment.example9;

import static com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync.*;
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

import com.techyourchance.testdrivendevelopment.example9.AddToCartUseCaseSyncPractice.UseCaseResult;
import com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.example9.networking.NetworkErrorException;

@RunWith(MockitoJUnitRunner.class)
public class AddToCartUseCaseSyncPracticeTest {
    public static final String OFFER_ID = "offerId";
    public static final int AMOUNT = 4;

    // region constants

    // endregion constants

    // region helper fields
    @Mock
    AddToCartHttpEndpointSync addToCartHttpEndpointSyncMock;
    // endregion helper fields

    AddToCartUseCaseSyncPractice SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new AddToCartUseCaseSyncPractice(addToCartHttpEndpointSyncMock);
        success();
    }

    // correct parameters passed to the endpoint
    @Test
    public void addToCartPracticeSync_correctParametersPassedToEndpoint() throws Exception {
        // Arrange
        ArgumentCaptor<CartItemScheme> ac = ArgumentCaptor.forClass(CartItemScheme.class);
        // Act
        SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        verify(addToCartHttpEndpointSyncMock).addToCartSync(ac.capture());
        assertEquals(ac.getValue().getOfferId(), OFFER_ID);
        assertEquals(ac.getValue().getAmount(), AMOUNT);
    }

    // endpoint success - success returned
    @Test
    public void addToCartPracticeSync_success_successReturned() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(result, UseCaseResult.SUCCESS);
    }

    // endpoint auth error - failure returned
    @Test
    public void addToCartPracticeSync_authError_failureReturned() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(result, UseCaseResult.FAILURE);
    }

    // endpoint general error - failure returned
    @Test
    public void addToCartPracticeSync_generalError_failureReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(result, UseCaseResult.FAILURE);
    }

    // network exception - network error returned

    @Test
    public void addToCartPracticeSync_networkError_networkErrorReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(result, UseCaseResult.NETWORK_ERROR);
    }

    // region helper methods

    private void authError() throws NetworkErrorException {
        when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.AUTH_ERROR);
    }

    private void generalError() throws NetworkErrorException {
        when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.GENERAL_ERROR);
    }

    private void networkError() throws NetworkErrorException {
        when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenThrow(new NetworkErrorException());
    }

    private void success() throws NetworkErrorException {
        when(addToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.SUCCESS);
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}