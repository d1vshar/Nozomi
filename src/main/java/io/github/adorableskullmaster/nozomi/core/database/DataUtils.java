package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

class DataUtils {
    static void executeIntegerBatch(List<Integer> newIntegers, String sql) {
        int count = 1;

        try (
                Connection connection = Bot.dataSource.getConnection();
        ) {
            for (Integer integer : newIntegers) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, integer);
                preparedStatement.addBatch();

                if (count == newIntegers.size())
                    preparedStatement.executeBatch();

                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
