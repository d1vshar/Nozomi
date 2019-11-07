package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ApplicantsDataSource {
    public static List<Integer> getStoredWars() {
        List<Integer> storedWars = new ArrayList<>();

        String sql = "SELECT * FROM applicants";

        try (
                Connection connection = Bot.dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            while (resultSet.next())
                storedWars.add(resultSet.getInt(1));
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }

        return storedWars;
    }

    public static void setStoredWars(List<Integer> newApplicants) {
        String sql = "INSERT INTO applicants(id) VALUES (?)";

        DataUtils.executeIntegerBatch(newApplicants, sql);
    }
}
