package net.easecation.transferpro.event;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import net.easecation.transferpro.provider.TSProServerEntry;

public class TSProPlayerTransferEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final TSProServerEntry target;
    private boolean success;
    private String message;

    public TSProPlayerTransferEvent(Player player, TSProServerEntry target, boolean success, String message) {
        this.player = player;
        this.target = target;
        this.success = success;
        this.message = message;
    }

    public TSProServerEntry getTarget() {
        return target;
    }

    public boolean isSuccess() {
        return success;
    }

    public TSProPlayerTransferEvent setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TSProPlayerTransferEvent setMessage(String message) {
        this.message = message;
        return this;
    }
}
