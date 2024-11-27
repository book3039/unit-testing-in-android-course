package com.techyourchance.testdoublesfundamentals.example4;

import static com.techyourchance.testdoublesfundamentals.example4.LoginUseCaseSync.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.techyourchance.testdoublesfundamentals.example4.authtoken.AuthTokenCache;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.EventBusPoster;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.LoggedInEvent;
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

public class LoginUseCaseSyncAnotherTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "authToken";

    LoginHttpEndpointSyncTd loginHttpEndpointSyncTd;
    AuthTokenCacheTd authTokenCacheTd;
    EventBusPosterTd eventBusPosterTd;

    LoginUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        loginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd();
        authTokenCacheTd = new AuthTokenCacheTd();
        eventBusPosterTd = new EventBusPosterTd();
        SUT = new LoginUseCaseSync(
                loginHttpEndpointSyncTd,
                authTokenCacheTd,
                eventBusPosterTd
        );
    }

    // username and password passed to the endpoint
    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(loginHttpEndpointSyncTd.username, USERNAME);
        assertEquals(loginHttpEndpointSyncTd.password, PASSWORD);
    }

    // if login succeeds - user's auth token must be cached
    @Test
    public void loginSync_success_authTokenCached() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(authTokenCacheTd.getAuthToken(), AUTH_TOKEN);
    }

    // if fails - auth token is not changed
    @Test
    public void loginSync_generalError_authTokenNotCached() {
        loginHttpEndpointSyncTd.isGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(authTokenCacheTd.getAuthToken(), "");
    }

    @Test
    public void loginSync_authError_authTokenNotCached() {
        loginHttpEndpointSyncTd.isAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(authTokenCacheTd.getAuthToken(), "");
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() {
        loginHttpEndpointSyncTd.isServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(authTokenCacheTd.getAuthToken(), "");
    }
    
    // if login succeeds - login event posted to event bus
    @Test
    public void loginSync_success_loggedInEventPosted() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertTrue(eventBusPosterTd.event instanceof LoggedInEvent);
    }

    // if fails - no login event posted
    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() {
        loginHttpEndpointSyncTd.isGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(eventBusPosterTd.interactionCount, 0);
    }
    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() {
        loginHttpEndpointSyncTd.isAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(eventBusPosterTd.interactionCount, 0);
    }
    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() {
        loginHttpEndpointSyncTd.isServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(eventBusPosterTd.interactionCount, 0);
    }

    // if login succeeds - success returned
    @Test
    public void loginSync_success_successReturned() {
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.SUCCESS);
    }

    // if fails - fail returned
    @Test
    public void loginSync_generalError_failureReturned() {
        loginHttpEndpointSyncTd.isGeneralError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.FAILURE);
    }
    @Test
    public void loginSync_authError_failureReturned() {
        loginHttpEndpointSyncTd.isAuthError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.FAILURE);
    }
    @Test
    public void loginSync_serverError_failureReturned() {
        loginHttpEndpointSyncTd.isServerError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    // if network - network error returned
    @Test
    public void loginSync_networkError_networkErrorReturned() {
        loginHttpEndpointSyncTd.isNetWorkError = true;
        UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(result, UseCaseResult.NETWORK_ERROR);
    }

    /***********************************************************************************************
     * Helper classes
     **********************************************************************************************/
    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {
        public String username;
        public String password;
        public boolean isGeneralError;
        public boolean isAuthError;
        public boolean isServerError;
        public boolean isNetWorkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            this.username = username;
            this.password = password;

            if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");
            } else if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
            } else if (isNetWorkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
            }
        }
    }
    private static class AuthTokenCacheTd implements AuthTokenCache {

        String authToken = "";

        @Override
        public void cacheAuthToken(String authToken) {
            this.authToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return authToken;
        }
    }

    private static class EventBusPosterTd implements EventBusPoster {
        public Object event;
        public int interactionCount = 0;

        @Override
        public void postEvent(Object event) {
            this.event = event;
            interactionCount++;
        }
    }
}