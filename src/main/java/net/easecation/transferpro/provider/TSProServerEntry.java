package net.easecation.transferpro.provider;

import cn.nukkit.Player;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

public class TSProServerEntry {

    private String group;

    private String server;

    private InetSocketAddress address;

    private int playerCount;
    private int maxPlayerCount;

    private int tps;

    private Timestamp lastUpdate;

    public TSProServerEntry(String group, String server, InetSocketAddress address, int playerCount, int maxPlayerCount, int tps, Timestamp lastUpdate) {
        this.group = group;
        this.server = server;
        this.address = address;
        this.playerCount = playerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.tps = tps;
        this.lastUpdate = lastUpdate;
    }

    public String getGroup() {
        return group;
    }

    public String getServer() {
        return server;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public int getTps() {
        return tps;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public TSProServerEntry update(InetSocketAddress address, int playerCount, int maxPlayerCount, int tps, Timestamp lastUpdate) {
        this.address = address;
        this.playerCount = playerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.tps = tps;
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void transfer(Player player) {
        player.transfer(address);
    }

}
