package net.easecation.transferpro.api;

import net.easecation.transferpro.TransferPro;
import net.easecation.transferpro.TSProServerEntry;

import java.util.*;
import java.util.stream.Collectors;

public final class TransferProAPI {

    private final static Random random = new Random();

    /**
     * 不推荐使用Raw相关数据，里面可能包含不在运行的及配置或地址有变的无效数据
     */
    public static Map<String, Map<String, TSProServerEntry>> getRawServerEntries() {
        return TransferPro.getInstance().getServersSync().getServerEntries();
    }

    /**
     * 不推荐使用Raw相关数据，里面可能包含不在运行的及配置或地址有变的无效数据
     */
    public static Map<String, TSProServerEntry> getRawGroupEntries(String group) {
        return getRawServerEntries().getOrDefault(group, new HashMap<>());
    }

    /**
     * 根据Group获取该Group下所有服务器信息
     * 已剔除无效服务器，全部为正常运行的服务器
     * @param group 群组
     * @return 该Group下所有服务器信息
     */
    public static Map<String, TSProServerEntry> getGroupEntries(String group) {
        return getRawGroupEntries(group).values().stream()
                .filter(TSProServerEntry::isAlive)
                .collect(Collectors.toMap(TSProServerEntry::getServer, e -> e));
    }

    /**
     * 判断某个Group是否是空的
     * @param group 群组
     * @return 是否为空
     */
    public static boolean isGroupEmpty(String group) {
        return !getGroupEntries(group).isEmpty();
    }

    /**
     * 获取该环境体系下所有服务器信息
     * 已剔除无效服务器，全部为正常运行的服务器
     * @return 该Group下所有服务器信息
     */
    public static Map<String, Map<String, TSProServerEntry>> getServerEntries() {
        return getRawServerEntries().keySet().stream()
                .collect(Collectors.toMap(g -> g, TransferProAPI::getGroupEntries));
    }

    /**
     * 根据Group和Server名称精确获取某个服务器信息
     * @param group 群组
     * @param server 服务器名
     * @return 服务器信息
     */
    public static TSProServerEntry getServerEntry(String group, String server) {
        return getGroupEntries(group).get(server);
    }

    /**
     * 忽略Group，直接根据Server名称搜索，可能搜索到多个服务器
     * @param server Server名称
     * @return 服务器信息列表
     */
    public static List<TSProServerEntry> findServerEntry(String server) {
        List<TSProServerEntry> result = new ArrayList<>();
        getServerEntries().values().forEach(entries ->
                result.addAll(entries.values().stream().filter(e -> e.getServer().equals(server)).collect(Collectors.toList()))
        );
        return result;
    }

    /**
     * 根据给定的group进入随机一个未满的子服务器
     * @param group 群组
     * @return 某个随机的Group
     */
    public static TSProServerEntry randomFromGroup(String group) {
        List<TSProServerEntry> servers = getGroupEntries(group).values().stream().filter(e -> !e.isFull()).collect(Collectors.toList());
        if (servers.isEmpty()) return null;
        return servers.get(random.nextInt(servers.size()));
    }

    /**
     * 根据Group统计人数
     * @param group 群组
     * @return 所有该Group下的服务器的总在线人数
     */
    public static int getGroupPlayerCount(String group) {
        return getGroupEntries(group).values().stream()
                .mapToInt(TSProServerEntry::getPlayerCount)
                .sum();
    }

    /**
     * 根据Group统计最大人数
     * @param group 群组
     * @return 所有该Group下的服务器的总最大人数
     */
    public static int getGroupMaxPlayerCount(String group) {
        return getGroupEntries(group).values().stream()
                .mapToInt(TSProServerEntry::getMaxPlayerCount)
                .sum();
    }

    /**
     * 判断某个Group是否全部人满
     * @param group 群组
     * @return 是否已满
     */
    public static boolean isGroupAllFull(String group) {
        int count = getGroupPlayerCount(group);
        int max = getGroupMaxPlayerCount(group);
        if (count == 0 && max == 0) return true;
        else return count < max;
    }

    /**
     * 统计该环境体系内的总在线人数，包括所有Group
     * @return 总人数
     */
    public static int getTotalPlayerCount() {
        return getRawServerEntries().keySet().stream()
                .mapToInt(TransferProAPI::getGroupPlayerCount)
                .sum();
    }

    /**
     * 统计该环境体系内的总最大人数，包括所有Group
     * @return 总最大人数
     */
    public static int getTotalMaxPlayerCount() {
        return getRawServerEntries().keySet().stream()
                .mapToInt(TransferProAPI::getGroupMaxPlayerCount)
                .sum();
    }

    /**
     * 模糊匹配
     * @param arg0 可能为group或server
     * @param arg1 可能为server或null
     * @return 匹配到的服务器及匹配结果
     */
    public static ServerMatchResult match(String arg0, String arg1) {
        if (arg0 == null) return new ServerMatchResult(ServerMatchResultType.EMPTY_ARG, null);
        Map<String, TSProServerEntry> servers = getGroupEntries(arg0);
        if (!servers.isEmpty()) { //arg0 是 group
            if (arg1 != null) {
                TSProServerEntry server = servers.get(arg1);
                if (server != null) return new ServerMatchResult(ServerMatchResultType.SUCCESS, server);
                else return new ServerMatchResult(ServerMatchResultType.GROUP_NO_MATCH_SERVER, null);
            } else {
                if (isGroupAllFull(arg0)) return new ServerMatchResult(ServerMatchResultType.FULL, null);
                else {
                    TSProServerEntry server = randomFromGroup(arg0);
                    if (server != null) return new ServerMatchResult(ServerMatchResultType.SUCCESS, server);
                    else return new ServerMatchResult(ServerMatchResultType.FULL, null);
                }
            }
        } else { //arg0 是 server
            List<TSProServerEntry> found = findServerEntry(arg0);
            if (found.isEmpty()) return new ServerMatchResult(ServerMatchResultType.SERVER_FIND_FAIL, null);
            if (found.stream().mapToInt(TSProServerEntry::getPlayerCount).sum() < found.stream().mapToInt(TSProServerEntry::getMaxPlayerCount).sum())
                return new ServerMatchResult(ServerMatchResultType.FULL, null);
            else {
                TSProServerEntry server = found.stream().filter(e -> !e.isFull()).findFirst().orElse(null);
                if (server != null) return new ServerMatchResult(ServerMatchResultType.SUCCESS, server);
                else return new ServerMatchResult(ServerMatchResultType.MATCH_FAIL, null);
            }
        }
    }

}
