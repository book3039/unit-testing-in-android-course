package com.techyourchance.testdrivendevelopment.example10.networking;

public class PingServerSyncUseCasePractice {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
    }
    private final PingServerHttpEndpointSync pingServerHttpEndpointSync;

    public PingServerSyncUseCasePractice(PingServerHttpEndpointSync pingServerHttpEndpointSync) {
        this.pingServerHttpEndpointSync = pingServerHttpEndpointSync;
    }

    public UseCaseResult pingServerSync() {
        PingServerHttpEndpointSync.EndpointResult result = pingServerHttpEndpointSync.pingServerSync();
        switch (result) {
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return UseCaseResult.FAILURE;
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            default:
                throw new RuntimeException("invalid result: " + result);
        }
    }
}
