package com.techyourchance.mockitofundamentals.exercise5;

import static com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.*;
import static com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

public class UpdateUsernameUseCaseSyncTest {

    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";

    UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
    UsersCache usersCacheMock;
    EventBusPoster eventBusPosterMock;

    UpdateUsernameUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        updateUsernameHttpEndpointSyncMock = Mockito.mock(UpdateUsernameHttpEndpointSync.class);
        usersCacheMock = Mockito.mock(UsersCache.class);
        eventBusPosterMock = Mockito.mock(EventBusPoster.class);

        SUT = new UpdateUsernameUseCaseSync(
                updateUsernameHttpEndpointSyncMock,
                usersCacheMock,
                eventBusPosterMock
        );

        success();
    }

    @Test
    public void updateUsernameSync_success_usernameAndUserIdPassedToEndpoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(updateUsernameHttpEndpointSyncMock, times(1)).updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(captures.get(0), USER_ID);
        assertEquals(captures.get(1), USERNAME);
    }

    @Test
    public void updateUsernameSync_success_userCachesCached() throws NetworkErrorException {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(usersCacheMock, times(1)).cacheUser(ac.capture());
        User capture = ac.getValue();
        assertEquals(capture.getUserId(), USER_ID);
        assertEquals(capture.getUsername(), USERNAME);
    }

    @Test
    public void updateUsernameSync_generalError_userCachesNotCached() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUsernameSync_authError_userCachesNotCached() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUsernameSync_serverError_userCachesNotCached() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUsernameSync_success_updateEventPosted() throws NetworkErrorException {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(eventBusPosterMock).postEvent(ac.capture());

        assertTrue(ac.getValue() instanceof UserDetailsChangedEvent);
    }

    @Test
    public void updateUsernameSync_generalError_noInteractionWithEventBusPoster() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_authError_noInteractionWithEventBusPoster() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_serverError_noInteractionWithEventBusPoster() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updatedUsernameSync_success_successReturned() throws NetworkErrorException {
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.SUCCESS);
    }

    @Test
    public void updatedUsernameSync_generalError_failureReturned() throws NetworkErrorException {
        generalError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void updatedUsernameSync_authError_failureReturned() throws NetworkErrorException {
        authError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void updatedUsernameSync_serverError_failureReturned() throws NetworkErrorException {
        serverError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void updatedUsernameSync_networkError_networkErrorReturned() throws NetworkErrorException {
        networkError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.NETWORK_ERROR);
    }

    private void success() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void generalError() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", ""));
    }

    private void authError() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", ""));
    }

    private void serverError() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", ""));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException())
                .when(updateUsernameHttpEndpointSyncMock).updateUsername(any(String.class), any(String.class));
    }
}