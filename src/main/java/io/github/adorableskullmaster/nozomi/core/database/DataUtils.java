package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

class DataUtils {
    static void executeIntegerBatch(List<Integer> newIntegers, String sql) {
        int count = 1;

        try (
                Connection connection = Bot.dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            for (Integer integer : newIntegers) {
                preparedStatement.setInt(1, integer);
                preparedStatement.addBatch();

                if (count == newIntegers.size())
                    System.out.println(Arrays.toString(preparedStatement.executeBatch()));

                count++;
            }
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }
}
