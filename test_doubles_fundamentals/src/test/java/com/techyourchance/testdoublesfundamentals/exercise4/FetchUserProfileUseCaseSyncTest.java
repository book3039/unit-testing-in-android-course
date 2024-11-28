package com.techyourchance.testdoublesfundamentals.exercise4;

import static com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "userId";
    public static final String FULL_NAME = "fullName";
    public static final String IMAGE_URL = "imageUrl";

    UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    UsersCacheTd usersCacheTd;

    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();

        SUT = new FetchUserProfileUseCaseSync(
                userProfileHttpEndpointSyncTd,
                usersCacheTd
        );
    }

    // userId passed to the endpoint
    @Test
    public void fetchUserProfileSync_success_userIdPassedToEndpoint() {
        SUT.fetchUserProfileSync(USER_ID);
        assertEquals(userProfileHttpEndpointSyncTd.userId, USER_ID);
    }

    // if fetching succeeds - profile should be cached
    @Test
    public void fetchUserProfileSync_success_profileCached() {
        SUT.fetchUserProfileSync(USER_ID);
        User user = usersCacheTd.getUser(USER_ID);

        assertEquals(user.getUserId(), USER_ID);
        assertEquals(user.getFullName(), FULL_NAME);
        assertEquals(user.getImageUrl(), IMAGE_URL);
    }

    // if fails - profile is not changed
    @Test
    public void fetchUserProfileSync_generalError_profileNotCached() {
        userProfileHttpEndpointSyncTd.isGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        User user = usersCacheTd.getUser(USER_ID);
        assertNull(user);
    }

    @Test
    public void fetchUserProfileSync_authError_profileNotCached() {
        userProfileHttpEndpointSyncTd.isAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        User user = usersCacheTd.getUser(USER_ID);
        assertNull(user);
    }

    @Test
    public void fetchUserProfileSync_serverError_profileNotCached() {
        userProfileHttpEndpointSyncTd.isServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        User user = usersCacheTd.getUser(USER_ID);
        assertNull(user);
    }

    // if fetching succeeds - success returned
    @Test
    public void fetchUserProfileSync_success_successReturned() {
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(result, UseCaseResult.SUCCESS);
    }

    // if fails - fail returned
    @Test
    public void fetchUserProfileSync_generalError_failureReturned() {
        userProfileHttpEndpointSyncTd.isGeneralError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void fetchUserProfileSync_authError_failureReturned() {
        userProfileHttpEndpointSyncTd.isAuthError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void fetchUserProfileSync_serverError_failureReturned() {
        userProfileHttpEndpointSyncTd.isServerError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void fetchUserProfileSync_networkError_failureReturned() {
        userProfileHttpEndpointSyncTd.isNetworkError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(result, UseCaseResult.NETWORK_ERROR);
    }

    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {
        public String userId = "";
        public boolean isGeneralError;
        public boolean isAuthError;
        public boolean isServerError;
        public boolean isNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            this.userId = userId;

            if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (isNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, FULL_NAME, IMAGE_URL);
            }
        }
    }

    private static class UsersCacheTd implements UsersCache {

        ArrayList<User> users = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser != null) {
                users.remove(existingUser);
            }
            users.add(user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for (User user : users) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }
}