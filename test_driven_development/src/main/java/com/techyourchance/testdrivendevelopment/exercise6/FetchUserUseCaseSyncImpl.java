package com.techyourchance.testdrivendevelopment.exercise6;

import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.*;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointResult;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private final FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
    private final UsersCache usersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        User cachedUser = usersCache.getUser(userId);
        if (cachedUser != null) {
            return new UseCaseResult(Status.SUCCESS, cachedUser);
        }

        EndpointResult result;

        try {
            result = fetchUserHttpEndpointSync.fetchUserSync(userId);
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, usersCache.getUser(userId));
        }

        switch (result.getStatus()) {
            case SUCCESS:
                User user = new User(result.getUserId(), result.getUsername());
                usersCache.cacheUser(user);
                return new UseCaseResult(Status.SUCCESS, user);
            case AUTH_ERROR:
            case GENERAL_ERROR:
                return new UseCaseResult(Status.FAILURE, null);
            default:
                throw new RuntimeException("Invalid status: " + result);
        }
    }
}
