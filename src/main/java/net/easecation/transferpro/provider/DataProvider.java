package net.easecation.transferpro.provider;

import net.easecation.transferpro.TSProServerEntry;
import net.easecation.transferpro.TransferPro;
import ru.nukkit.dblib.DbLib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataProvider {

    private TransferPro plugin;
    private Connection connection;

    public DataProvider(TransferPro plugin) {
        this.plugin = plugin;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                plugin.getLogger().info(plugin.getLang().translateString("tspro.provider.closing"));
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getServer().getLogger().logException(e);
        } finally {
            connection = null;
        }
    }

    public boolean connect() throws ProviderException {
        this.close();
        plugin.getLogger().info(plugin.getLang().translateString("tspro.provider.connecting"));
        try {
            Connection conn = DbLib.getDefaultConnection();
            if (conn != null) {
                String sql = new BufferedReader(new InputStreamReader(this.plugin.getResource("init.sql")))
                        .lines().collect(Collectors.joining("\n"));
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.execute();
                this.connection = conn;
                plugin.getLogger().info(plugin.getLang().translateString("tspro.provider.connected"));
                return true;
            } else return false;
        } catch (Exception e) {
            throw new ProviderException(plugin.getLang().translateString("tspro.provider.fail"), e);
        }
    }

    private Connection getConnection() throws ProviderException {
        try {
            if (connection == null || connection.isClosed()) {
                if (connect()) return connection;
            } else {
                return connection;
            }
        } catch (SQLException e) {
            throw new ProviderException("Can't connect to database!", e);
        }
        throw new ProviderException("Can't connect to database!");
    }

    public TSProServerEntry[] fetchAllServers() throws ProviderException {
        Connection connection = getConnection();
        List<TSProServerEntry> list = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tspro_servers");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                list.add(new TSProServerEntry(
                        rs.getString("group"),
                        rs.getString("server"),
                        new InetSocketAddress(rs.getString("address"), rs.getInt("port")),
                        rs.getInt("player_count"),
                        rs.getInt("max_player_count"),
                        rs.getInt("tps"),
                        rs.getTimestamp("last_update")
                ));
            }
            return list.toArray(new TSProServerEntry[0]);
        } catch (SQLException e) {
            throw new ProviderException("Exception caught when getAllServers", e);
        }
    }

    public void updateServerEntry(TSProServerEntry entry) throws ProviderException {
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO tspro_servers(`group`, server, address, port, player_count, max_player_count, tps, last_update)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, entry.getGroup());
            statement.setString(2, entry.getServer());
            statement.setString(3, entry.getAddress().getAddress().getHostAddress());
            statement.setInt(4, entry.getAddress().getPort());
            statement.setInt(5, plugin.getServer().getOnlinePlayers().size());
            statement.setInt(6, plugin.getServer().getMaxPlayers());
            statement.setFloat(7, plugin.getServer().getTicksPerSecond());
            statement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            statement.execute();
        } catch (SQLException e) {
            throw new ProviderException("Exception caught when updateServerEntry", e);
        }
    }

}
