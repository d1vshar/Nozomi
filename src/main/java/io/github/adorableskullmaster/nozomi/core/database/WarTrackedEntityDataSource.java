package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.models.WarTrackedEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarTrackedEntityDataSource {

    public static List<WarTrackedEntity> getAllTrackedEntities() {
        List<WarTrackedEntity> trackedEntities = new ArrayList<>();

        String sql = "SELECT * FROM wartracked";

        try (
                Connection connection = Bot.dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next())
                trackedEntities.add(new WarTrackedEntity(resultSet.getBoolean(1), resultSet.getInt(2)));
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }

        return trackedEntities;
    }

    public static Boolean isBeingTracked(int id, boolean nation) {

        String sql = "SELECT COUNT(*) FROM wartracked WHERE id=? AND nation=?";

        int count = 0;
        try (Connection connection = Bot.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            statement.setBoolean(1, nation);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
        return count == 1;
    }

    private void insertEntity(WarTrackedEntity warTrackedEntity) {

    }
}
