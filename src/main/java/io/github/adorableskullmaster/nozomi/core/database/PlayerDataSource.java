package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.models.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDataSource {

    public static boolean isNationRegistered(int nid) {
        String sql = "SELECT COUNT(*) FROM players WHERE nationid=?";
        int count = 0;
        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, nid);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
        return count == 1;
    }

    public static boolean isPlayerRegistered(long discordId) {
        String sql = "SELECT COUNT(*) FROM players WHERE discordid=?";
        int count = 0;
        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, discordId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
        return count == 1;
    }

    public static void insertPlayerData(Player player) {
        String sql = "INSERT INTO players VALUES (?,?,?,?)";

        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, player.getDiscordId());
            preparedStatement.setInt(2, player.getNationId());
            preparedStatement.setBoolean(3, player.isMember());
            preparedStatement.setInt(4, player.getAllianceId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }

    public static Player getPlayerData(long discordId) {
        String sql = "SELECT * FROM players WHERE discordid=?";
        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, discordId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(resultSet.getLong("discordid"),
                        resultSet.getInt("nationid"),
                        resultSet.getBoolean("member"),
                        resultSet.getInt("alliance"));
            }
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
        return null;
    }

    public static Player getPlayerData(int nationId) {
        String sql = "SELECT * FROM players WHERE nationid=?";
        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, nationId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(resultSet.getLong("discordid"),
                        resultSet.getInt("nationid"),
                        resultSet.getBoolean("member"),
                        resultSet.getInt("alliance"));
            }
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
        return null;
    }

    public static void updatePlayerData(Player player) {
        String sql = "UPDATE players SET nationid=?,member=?,alliance=? WHERE discordid=?";

        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, player.getNationId());
            preparedStatement.setBoolean(2, player.isMember());
            preparedStatement.setInt(3, player.getAllianceId());
            preparedStatement.setLong(4, player.getDiscordId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }

    public static void deletePlayerData(long discordId) {
        String sql = "DELETE FROM players WHERE discordid=?";

        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, discordId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }
}
