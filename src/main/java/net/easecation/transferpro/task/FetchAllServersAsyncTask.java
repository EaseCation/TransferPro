package net.easecation.transferpro.task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import net.easecation.transferpro.ServersSync;
import net.easecation.transferpro.TSProServerEntry;

import java.util.HashMap;
import java.util.Map;

public class FetchAllServersAsyncTask extends AsyncTask {

    private final ServersSync serversSync;
    TSProServerEntry[] result;

    public FetchAllServersAsyncTask(ServersSync serversSync) {
        this.serversSync = serversSync;
    }

    @Override
    public void onRun() {
        result = serversSync.getPlugin().getProvider().fetchAllServers();
    }

    @Override
    public void onCompletion(Server server) {
        if (result != null) {
            for (TSProServerEntry entry : result) {
                if (!serversSync.getServerEntries().containsKey(entry.getGroup())) serversSync.getServerEntries().put(entry.getGroup(), new HashMap<>());
                Map<String, TSProServerEntry> group = serversSync.getServerEntries().get(entry.getGroup());
                if (group.containsKey(entry.getServer())) {
                    TSProServerEntry origin = group.get(entry.getServer());
                    //if (!origin.equalAddress(entry) && origin.isAlive()) serversSync.getPlugin().getLogger().warning("Fetch Warning: " + entry.getGroup() + "-" + entry.getServer() + " fetch the different address: origin[" + origin.getAddress() + "] new[" + entry.getAddress() + "]");
                    origin.update(entry);
                } else {
                    group.put(entry.getServer(), entry);
                }
            }
        }
    }
}
