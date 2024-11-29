package com.techyourchance.mockitofundamentals.example7;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.techyourchance.mockitofundamentals.example7.LoginUseCaseSync.UseCaseResult;
import com.techyourchance.mockitofundamentals.example7.authtoken.AuthTokenCache;
import com.techyourchance.mockitofundamentals.example7.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;
import com.techyourchance.mockitofundamentals.example7.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

public class LoginUseCaseSyncAnotherTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "authToken";

    LoginHttpEndpointSync loginHttpEndpointSyncMock;
    AuthTokenCache authTokenCacheMock;
    EventBusPoster eventBusPosterMock;

    LoginUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        loginHttpEndpointSyncMock = Mockito.mock(LoginHttpEndpointSync.class);
        authTokenCacheMock = Mockito.mock(AuthTokenCache.class);
        eventBusPosterMock = Mockito.mock(EventBusPoster.class);
        SUT = new LoginUseCaseSync(
                loginHttpEndpointSyncMock,
                authTokenCacheMock,
                eventBusPosterMock
        );
        success();
    }

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(loginHttpEndpointSyncMock, times(1)).loginSync(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(captures.get(0), USERNAME);
        assertEquals(captures.get(1), PASSWORD);
    }

    @Test
    public void loginSync_success_authTokenCached() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(authTokenCacheMock).cacheAuthToken(ac.capture());
        assertEquals(ac.getValue(), AUTH_TOKEN);
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws Exception {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }


    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    @Test
    public void loginSync_success_loggedInEventPosted() throws Exception {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(eventBusPosterMock).postEvent(ac.capture());
        assertTrue(ac.getValue() instanceof LoggedInEvent);
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void loginSync_success_successReturned() throws Exception {
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.SUCCESS);
    }

    @Test
    public void loginSync_serverError_failureReturned() throws Exception {
        serverError();
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_authError_failureReturned() throws Exception {
        authError();
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_generalError_failureReturned() throws Exception {
        generalError();
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void loginSync_networkError_networkErrorReturned() throws Exception {
        networkError();
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.NETWORK_ERROR);
    }

    private void success() throws NetworkErrorException {
        when(loginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN));
    }

    private void generalError() throws Exception {
        when(loginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, ""));
    }

    private void authError() throws Exception {
        when(loginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, ""));
    }

    private void serverError() throws Exception {
        when(loginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, ""));
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(loginHttpEndpointSyncMock).loginSync(any(String.class), any(String.class));
    }
}