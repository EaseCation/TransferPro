package net.easecation.transferpro.api;

import net.easecation.transferpro.TransferPro;

public enum ServerMatchResultType {

    EMPTY_ARG("tspro.match.empty-arg"),
    SUCCESS("tspro.match.suc"),
    MATCH_FAIL("tspro.match.match-fail"),
    GROUP_NO_MATCH_SERVER("tspro.match.group-no-match-server"),
    SERVER_FIND_FAIL("tspro.match.server-find-fail"),
    FULL("tspro.match.full");

    private final String message;

    ServerMatchResultType(String message) {
        this.message = message;
    }

    public String getRawMessage() {
        return message;
    }

    public String getMessage() {
        return TransferPro.getInstance().getLang().translateString(message);
    }
}
