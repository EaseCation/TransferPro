package net.easecation.transferpro;

import net.easecation.transferpro.provider.TSProServerEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class TransferProAPI {

    public static Map<String, Map<String, TSProServerEntry>> getRawServerEntries() {
        return TransferPro.getInstance().getServersSync().getServerEntries();
    }

    public static Map<String, TSProServerEntry> getRawGroupEntries(String group) {
        return getRawServerEntries().getOrDefault(group, new HashMap<>());
    }

    public static Map<String, TSProServerEntry> getGroupEntries(String group) {
        return getRawGroupEntries(group).values().stream()
                .filter(TSProServerEntry::isAlive)
                .collect(Collectors.toMap(TSProServerEntry::getServer, e -> e));
    }

    public static Map<String, Map<String, TSProServerEntry>> getServerEntries() {
        return getRawServerEntries().keySet().stream()
                .collect(Collectors.toMap(g -> g, TransferProAPI::getGroupEntries));
    }

    public static TSProServerEntry getServerEntry(String group, String server) {
        return getGroupEntries(group).get(server);
    }

    public static int getGroupPlayerCount(String group) {
        return getGroupEntries(group).values().stream()
                .mapToInt(TSProServerEntry::getPlayerCount)
                .sum();
    }

    public static int getGroupMaxPlayerCount(String group) {
        return getGroupEntries(group).values().stream()
                .mapToInt(TSProServerEntry::getMaxPlayerCount)
                .sum();
    }

    public static int getTotalPlayerCount() {
        return getRawServerEntries().keySet().stream()
                .mapToInt(TransferProAPI::getGroupPlayerCount).sum();
    }

    public static int getTotalMaxPlayerCount() {
        return getRawServerEntries().keySet().stream()
                .mapToInt(TransferProAPI::getGroupMaxPlayerCount).sum();
    }

}
