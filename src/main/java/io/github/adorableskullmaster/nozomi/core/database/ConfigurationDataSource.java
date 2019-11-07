package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.models.Configuration;

import java.sql.*;

public class ConfigurationDataSource {

    public static Boolean isSetup() {
        String sql = "SELECT COUNT(*) FROM config";
        int count = 0;
        try (Connection connection = Bot.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)
        ) {
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count == 1;
    }

    public static Configuration getConfiguration() {
        String sql = "SELECT * FROM config";
        Configuration configuration = new Configuration();

        try (Connection connection = Bot.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            resultSet.next();
            configuration.setMainChannel(resultSet.getLong(1));
            configuration.setMembersChannel(resultSet.getLong(2));
            configuration.setGovChannel(resultSet.getLong(3));
            configuration.setWarsChannel(resultSet.getLong(4));
            configuration.setVmChannel(resultSet.getLong(5));
            configuration.setBeigeChannel(resultSet.getLong(6));
            configuration.setMemberRole(resultSet.getLong(7));
            configuration.setGovRole(resultSet.getLong(8));
            configuration.setApiKeys((String[]) resultSet.getArray(9).getArray());
            configuration.setJoinText(resultSet.getString(10));

        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }

        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        //noinspection SqlWithoutWhere
        String sqlDel = "DELETE FROM config";
        String sql = "INSERT INTO config VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = Bot.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlDel);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, configuration.getMainChannel());
            preparedStatement.setLong(2, configuration.getMembersChannel());
            preparedStatement.setLong(3, configuration.getGovChannel());
            preparedStatement.setLong(4, configuration.getWarsChannel());
            preparedStatement.setLong(5, configuration.getVmChannel());
            preparedStatement.setLong(6, configuration.getBeigeChannel());
            preparedStatement.setLong(7, configuration.getMemberRole());
            preparedStatement.setLong(8, configuration.getGovRole());
            preparedStatement.setArray(9, connection.createArrayOf("text", configuration.getApiKeys()));
            preparedStatement.setString(10, configuration.getJoinText());
            preparedStatement.executeUpdate(sql);
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }
}
