package com.techyourchance.testdrivendevelopment.exercise7;

import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.*;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

public class FetchReputationUseCaseSync {
    private final GetReputationHttpEndpointSync getReputationHttpEndpointSync;
    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult fetchReputationSync() {
        EndpointResult result = getReputationHttpEndpointSync.getReputationSync();
        switch (result.getStatus()) {
            case SUCCESS:
                return new UseCaseResult(Status.SUCCESS, result.getReputation());
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return new UseCaseResult(Status.FAILURE, result.getReputation());
            default:
                throw new RuntimeException("Invalid Status: " + result);
        }
    }

    public enum Status {
        SUCCESS,
        FAILURE,
    }

    static class UseCaseResult {
        private final Status status;
        private final int reputation;

        public UseCaseResult(Status status, int reputation) {
            this.status = status;
            this.reputation = reputation;
        }

        public Status getStatus() {
            return status;
        }

        public long getReputation() {
            return reputation;
        }
    }
}
