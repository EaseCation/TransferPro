package net.easecation.transferpro.provider;

import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.TransferPro;
import ru.nukkit.dblib.DbLib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DataProvider {

    private TransferPro plugin;
    private Connection connection;

    public DataProvider(TransferPro plugin) {
        this.plugin = plugin;
    }

    public boolean connect() throws ProviderException {
        try {
            if (connection != null && !connection.isClosed()) {
                plugin.getLogger().info("Closing connection...");
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getServer().getLogger().logException(e);
        } finally {
            connection = null;
        }
        plugin.getLogger().info("Connecting database...");
        try {
            Connection conn = DbLib.getDefaultConnection();
            if (conn != null) {
                String sql = new BufferedReader(new InputStreamReader(this.plugin.getResource("init.sql")))
                        .lines().collect(Collectors.joining("\n"));
                conn.prepareStatement(sql);
                this.connection = conn;
                plugin.getLogger().info(TextFormat.GREEN + "Database connected...");
                return true;
            } else return false;
        } catch (Exception e) {
            throw new ProviderException("Database connect failed!", e);
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



}
