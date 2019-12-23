package net.easecation.transferpro.api;

import net.easecation.transferpro.TSProServerEntry;

public class ServerMatchResult {

    private final ServerMatchResultType resultType;
    private final TSProServerEntry server;

    public ServerMatchResult(ServerMatchResultType resultType, TSProServerEntry server) {
        this.resultType = resultType;
        this.server = server;
    }

    public ServerMatchResultType getResultType() {
        return resultType;
    }

    public TSProServerEntry getServer() {
        return server;
    }
}
