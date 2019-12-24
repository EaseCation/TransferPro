package net.easecation.transferpro;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.api.event.TSProPlayerTransferEvent;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

public class TSProServerEntry {

    private String group;

    private String server;

    private InetSocketAddress address;

    private int playerCount;
    private int maxPlayerCount;

    private float tps;

    private Timestamp lastUpdate;

    public TSProServerEntry(String group, String server, InetSocketAddress address, int playerCount, int maxPlayerCount, float tps, Timestamp lastUpdate) {
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

    public float getTps() {
        return tps;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public boolean isAlive() {
        return isAlive(TransferPro.getInstance().getConfig().getInt("timeout", 10000)); //10 seconds
    }

    public boolean isAlive(long timeout) {
        return System.currentTimeMillis() - lastUpdate.getTime() <= timeout;
    }

    public boolean isFull() {
        return this.playerCount >= this.maxPlayerCount;
    }

    public boolean equalAddress(TSProServerEntry another) {
        return another != null && this.address.equals(another.address);
    }

    public TSProServerEntry update(TSProServerEntry entry) {
        return update(entry.address, entry.playerCount, entry.maxPlayerCount, entry.tps, entry.lastUpdate);
    }

    public TSProServerEntry update(InetSocketAddress address, int playerCount, int maxPlayerCount, float tps, Timestamp lastUpdate) {
        this.address = address;
        this.playerCount = playerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.tps = tps;
        this.lastUpdate = lastUpdate;
        return this;
    }

    public TSProServerEntry setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void transfer(Player player) {
        TSProPlayerTransferEvent event =
                new TSProPlayerTransferEvent(player,
                        this,
                        !isFull(),
                        isFull() ?
                                TransferPro.getInstance().getLang().translateString("tspro.transfer.full", this.group, this.server, this.playerCount, this.maxPlayerCount)
                                :
                                TransferPro.getInstance().getLang().translateString("tspro.transfer.ing", this.group, this.server)
                );
        TransferPro.getInstance().getServer().getPluginManager().callEvent(event);
        if (event.isSuccess()) {
            player.sendMessage(event.getMessage());
            player.transfer(address);
        } else {
            player.sendMessage(event.getMessage());
        }
    }

    @Override
    public String toString() {
        return TextFormat.YELLOW + "[" + group + (group.isEmpty() ? "" : "|") + server + "] " + TextFormat.RESET +
                address +
                " (" + playerCount + "/" + maxPlayerCount + ") TPS=" + tps + " LastUpdate=" + lastUpdate;
    }
}
