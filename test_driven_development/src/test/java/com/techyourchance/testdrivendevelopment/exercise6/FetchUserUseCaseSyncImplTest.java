package com.techyourchance.testdrivendevelopment.exercise6;

import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.*;
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

import com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.UseCaseResult;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncImplTest {

    // region constants
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final User USER = new User(USER_ID, USER_NAME);

    // endregion constants

    // region helper fields
    @Mock
    FetchUserHttpEndpointSync fetchUserHttpEndpointSyncMock;
    @Mock
    UsersCache usersCacheMock;
    // endregion helper fields

    FetchUserUseCaseSyncImpl SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchUserUseCaseSyncImpl(fetchUserHttpEndpointSyncMock, usersCacheMock);
        userNotCached();
        success();
    }

    @Test
    public void fetchUserSync_notInCache_correctUserIdPassedToEndpoint() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(fetchUserHttpEndpointSyncMock).fetchUserSync(ac.capture());
        assertEquals(ac.getValue(), USER_ID);
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_successStatus() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertEquals(result.getStatus(), FetchUserUseCaseSync.Status.SUCCESS);
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_correctUserReturned() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertEquals(result.getUser(), USER);
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_userCached() throws Exception {
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock).cacheUser(ac.capture());
        assertEquals(ac.getValue(), USER);
    }

    @Test
    public void fetchUserSync_notCachedEndpointAuthError_failureStatus() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertEquals(result.getStatus(), FetchUserUseCaseSync.Status.FAILURE);
    }

    @Test
    public void fetchUserSync_notCachedEndpointAuthError_nullUserReturned() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notCachedEndpointAuthError_userNotCached() throws Exception {
        // Arrange
        authError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_notCachedEndpointGeneralError_failureStatus() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertEquals(result.getStatus(), FetchUserUseCaseSync.Status.FAILURE);
    }

    @Test
    public void fetchUserSync_notCachedEndpointGeneralError_nullUserReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notCachedEndpointGeneralError_userNotCached() throws Exception {
        // Arrange
        generalError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_notCachedEndpointNetworkError_networkErrorStatus() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertEquals(result.getStatus(), FetchUserUseCaseSync.Status.NETWORK_ERROR);
    }

    @Test
    public void fetchUserSync_notCachedEndpointNetworkError_nullUserReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notCachedEndpointNetworkError_userNotCached() throws Exception {
        // Arrange
        networkError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_userCached_correctUserIdPassedToCache() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock).getUser(ac.capture());
        assertEquals(ac.getValue(), USER_ID);
    }

    @Test
    public void fetchUserSync_userCached_successStatus() throws Exception {
        // Arrange
        userCached();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertEquals(result.getStatus(), FetchUserUseCaseSync.Status.SUCCESS);
    }

    @Test
    public void fetchUserSync_userCached_cachedUserReturned() throws Exception {
        // Arrange
        userCached();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertEquals(result.getUser(), USER);
    }

    @Test
    public void fetchUserSync_userCached_endpointNotPolled() throws Exception {
        // Arrange
        userCached();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(fetchUserHttpEndpointSyncMock, never()).fetchUserSync(USER_ID);
    }

    // region helper methods

    private void userNotCached() {
        when(usersCacheMock.getUser(anyString())).thenReturn(null);
    }
    private void userCached() {
        when(usersCacheMock.getUser(anyString())).thenReturn(USER);
    }

    private void authError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new EndpointResult(EndpointStatus.AUTH_ERROR, "", ""));
    }

    private void generalError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new EndpointResult(EndpointStatus.GENERAL_ERROR, "", ""));
    }

    private void networkError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenThrow(new NetworkErrorException());
    }

    private void success() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new EndpointResult(EndpointStatus.SUCCESS, USER_ID, USER_NAME));
    }


    // endregion helper methods
    // region helper classes
    // endregion helper classes
}