package com.techyourchance.testdrivendevelopment.example11;

import static com.techyourchance.testdrivendevelopment.example11.networking.GetCartItemsHttpEndpoint.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.techyourchance.testdrivendevelopment.example11.cart.CartItem;
import com.techyourchance.testdrivendevelopment.example11.networking.CartItemSchema;
import com.techyourchance.testdrivendevelopment.example11.networking.GetCartItemsHttpEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FetchCartItemsUseCasePracticeTest {


    // region constants
    public static final int LIMIT = 10;
    public static final int PRICE = 5;
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String ID = "id";
    // endregion constants

    // region helper fields
    @Mock GetCartItemsHttpEndpoint getCartItemsHttpEndpointMock;
    @Mock FetchCartItemsUseCasePractice.Listener listenerMock1;
    @Mock FetchCartItemsUseCasePractice.Listener listenerMock2;
    @Captor ArgumentCaptor<List<CartItem>> acListCartItem;
    // endregion helper fields

    FetchCartItemsUseCasePractice SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchCartItemsUseCasePractice(getCartItemsHttpEndpointMock);
        success();
    }

    // correct limit passed to the endpoint
    @Test
    public void fetchCartItems_correctLimitPassedToEndpoint() throws Exception {
        // Arrange
        ArgumentCaptor<Integer> acInt = ArgumentCaptor.forClass(Integer.class);
        // Act
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(getCartItemsHttpEndpointMock).getCartItems(acInt.capture(), any(Callback.class));
        assertEquals(acInt.getValue(), new Integer(LIMIT));
    }


    // success - all observers notified with correct data
    @Test
    public void fetchCartItems_success_observersNotifiedWithCorrectData() throws Exception {
        // Arrange
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(listenerMock1).onCartItemsFetched(acListCartItem.capture());
        verify(listenerMock2).onCartItemsFetched(acListCartItem.capture());

        List<List<CartItem>> captures = acListCartItem.getAllValues();
        List<CartItem> capture1 = captures.get(0);
        List<CartItem> capture2 = captures.get(1);

        assertEquals(capture1, getCartItems());
        assertEquals(capture2, getCartItems());
    }

    // success - unsubscribe observers not notified
    @Test
    public void fetchCartItems_success_unsubscribedObserversNotNotified() throws Exception {
        // Arrange
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.unregisterListener(listenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(listenerMock1).onCartItemsFetched(any(List.class));
        verifyNoMoreInteractions(listenerMock2);
    }

    // general error - observers notified of failure
    @Test
    public void fetchCartItems_generalError_observersNotifiedOfFailure() throws Exception {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(listenerMock1).onFetchCartItemsFailed();
        verify(listenerMock2).onFetchCartItemsFailed();
    }

    // network error - observers notified of failure
    @Test
    public void fetchCartItems_networkError_observersNotifiedOfFailure() throws Exception {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(listenerMock1).onFetchCartItemsFailed();
        verify(listenerMock2).onFetchCartItemsFailed();
    }

    // region helper methods
    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetCartItemsSucceeded(getCartItemSchemes());
                return null;
            }
        }).when(getCartItemsHttpEndpointMock).getCartItems(anyInt(), any(Callback.class));
    }

    private void generalError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetCartItemsFailed(FailReason.GENERAL_ERROR);
                return null;
            }
        }).when(getCartItemsHttpEndpointMock).getCartItems(anyInt(), any(Callback.class));
    }

    private void networkError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetCartItemsFailed(FailReason.NETWORK_ERROR);
                return null;
            }
        }).when(getCartItemsHttpEndpointMock).getCartItems(anyInt(), any(Callback.class));
    }

    private List<CartItemSchema> getCartItemSchemes() {
        List<CartItemSchema> schemas = new ArrayList<>();
        schemas.add(new CartItemSchema(ID, TITLE, DESCRIPTION, PRICE));
        return schemas;
    }

    private List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(ID, TITLE, DESCRIPTION, PRICE));
        return cartItems;
    }
    // endregion helper methods

    // region helper classes
    // endregion helper classes
}