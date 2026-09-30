import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicStoreOperations {

    public List<String> getAlbumsAndShortestTracks() {
        List<String> result = new ArrayList<>();
        String query = """
                SELECT a.album_name,
                       c.composition_name,
                       c.duration
                FROM (
                    SELECT album_id, MIN(duration) AS min_duration
                    FROM composition
                    GROUP BY album_id
                    HAVING MIN(duration) >= 5
                ) AS shortest
                JOIN album a ON a.album_id = shortest.album_id
                JOIN composition c ON c.album_id = shortest.album_id
                   AND c.duration = shortest.min_duration
                ORDER BY a.album_name
                """;
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                result.add(formatShortestCompositionRow(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
        }
        return result;
    }

    public List<String> getAllCompositions() {
        List<String> result = new ArrayList<>();
        String query = """
                SELECT c.composition_id,
                       c.composition_name,
                       c.duration,
                       a.album_name
                FROM composition c
                JOIN album a ON c.album_id = a.album_id
                ORDER BY a.album_name, c.composition_id
                """;
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                result.add(formatCompositionRow(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка композиций: " + e.getMessage());
        }
        return result;
    }

    public int addComposition(String name, int duration, int albumId) {
        String query = "INSERT INTO composition (composition_name, duration, album_id) VALUES (?, ?, ?)" ;
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, duration);
            statement.setInt(3, albumId);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Композиция не была добавлена.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Не удалось получить ID добавленной композиции");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении композиции: " + e.getMessage());
            return -1;
        }
    }

    public void updateCompositionDuration(int compositionId, int newDuration) {
        String query = "UPDATE composition SET duration = ? WHERE composition_id = ?" ;
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newDuration);
            statement.setInt(2, compositionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при изменении композиции: " + e.getMessage());
        }
    }

    public void deleteComposition(int compositionId) {
        String query = "DELETE FROM composition WHERE composition_id = ?" ;
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, compositionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении композиции: " + e.getMessage());
        }
    }

    private String formatShortestCompositionRow(ResultSet resultSet) throws SQLException {
        return String.format("%s | %s | %d мин.",
                resultSet.getString("album_name"),
                resultSet.getString("composition_name"),
                resultSet.getInt("duration")
        );
    }

    private String formatCompositionRow(ResultSet resultSet) throws SQLException {
        return String.format("ID: %d | %s | %s | %d мин.",
                resultSet.getInt("composition_id"),
                resultSet.getString("album_name"),
                resultSet.getString("composition_name"),
                resultSet.getInt("duration")
        );
    }
}
