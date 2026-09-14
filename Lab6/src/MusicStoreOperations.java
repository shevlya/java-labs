import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicStoreOperations {

    public List<String> getAlbumsAndShortestTracks() {
        List<String> resultList = new ArrayList<>();
        String query = """
                SELECT a.album_name, MIN(c.duration) AS min_duration
                FROM album a
                JOIN composition c ON a.album_id = c.album_id
                GROUP BY a.album_id, a.album_name
                HAVING MIN(c.duration) >= 5
                """;

        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                resultList.add(formatAlbumRow(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SELECT запроса: " + e.getMessage());
        }
        return resultList;
    }

    public List<String> getAllCompositions() {
        List<String> resultList = new ArrayList<>();

        String query = """
                SELECT c.composition_id, c.composition_name, c.duration, a.album_name 
                FROM composition c
                JOIN album a ON c.album_id = a.album_id
                ORDER BY a.album_name, c.composition_id
                """;

        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                resultList.add(formatCompositionRow(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка композиций: " + e.getMessage());
        }
        return resultList;
    }

    public String addComposition(String name, int duration, int albumId) {
        String query = "INSERT INTO composition (composition_name, duration, album_id) VALUES (?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementParameters(preparedStatement, name, duration, albumId);

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int newId = resultSet.getInt(1);
                        return String.format("Композиция '%s' успешно добавлена с ID: %d", name, newId);
                    }
                }
            }
            return "Не удалось добавить композицию.";
        } catch (SQLException e) {
            return "Ошибка при добавлении: " + e.getMessage();
        }
    }

    public String updateCompositionDuration(int compositionId, int newDuration) {
        String query = "UPDATE composition SET duration = ? WHERE composition_id = ?";

        int rows = executeUpdateQuery(query, newDuration, compositionId);

        return formatModificationResult(rows,
                "Длительность композиции с ID %d успешно изменена на %d мин.",
                "Композиция с ID %d не найдена.",
                compositionId, newDuration);

    }

    public String deleteComposition(int compositionId) {
        String query = "DELETE FROM composition WHERE composition_id = ?";

        int rows = executeUpdateQuery(query, compositionId);

        return formatModificationResult(rows,
                "Композиция с ID %d успешно удалена.",
                "Композиция с ID %d не найдена.",
                compositionId);
    }

    public int getLastCompositionId() {
        return getLastIdFromTable("composition", "composition_id");
    }

    private void setPreparedStatementParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    private int executeUpdateQuery(String sql, Object... params) {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementParameters(pstmt, params);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            return 0;
        }
    }

    private String formatModificationResult(int rows, String successTemplate, String notFoundTemplate, Object... args) {
        if (rows > 0) {
            return String.format(successTemplate, args);
        } else {
            return String.format(notFoundTemplate, args);
        }
    }

    private String formatAlbumRow(ResultSet rs) throws SQLException {
        return String.format("Альбом: %-25s | Мин. длительность: %d мин.",
                rs.getString("album_name"),
                rs.getInt("min_duration"));
    }

    private String formatCompositionRow(ResultSet rs) throws SQLException {
        return String.format("ID: %-3d | Композиция: %-30s | Альбом: %-25s | Длительность: %d мин.",
                rs.getInt("composition_id"),
                rs.getString("composition_name"),
                rs.getString("album_name"),
                rs.getInt("duration"));
    }

    private int getLastIdFromTable(String tableName, String idColumn) {
        String query = String.format("SELECT MAX(%s) FROM %s", idColumn, tableName);
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении последнего ID: " + e.getMessage());
        }
        return -1;
    }

}
