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

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                resultList.add(String.format("Альбом: %-25s | Мин. длительность: %d мин.",
                        rs.getString("album_name"),
                        rs.getInt("min_duration")));
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

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                resultList.add(String.format("ID: %-3d | Композиция: %-30s | Альбом: %-25s | Длительность: %d мин.",
                        rs.getInt("composition_id"),
                        rs.getString("composition_name"),
                        rs.getString("album_name"),
                        rs.getInt("duration")));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка композиций: " + e.getMessage());
        }
        return resultList;
    }

    /**
     * Добавление композиции. Возвращает строку с результатом.
     */
    public String addComposition(String name, int duration, int albumId) {
        String query = "INSERT INTO composition (composition_name, duration, album_id) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, duration);
            pstmt.setInt(3, albumId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        return "Композиция '" + name + "' успешно добавлена с ID: " + newId;
                    }
                }
            }
            return "Не удалось добавить композицию.";
        } catch (SQLException e) {
            return "Ошибка при добавлении: " + e.getMessage();
        }
    }

    /**
     * Обновление длительности. Возвращает строку с результатом.
     */
    public String updateCompositionDuration(int compositionId, int newDuration) {
        String query = "UPDATE composition SET duration = ? WHERE composition_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, newDuration);
            pstmt.setInt(2, compositionId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                return "Длительность композиции с ID " + compositionId + " успешно изменена на " + newDuration + " мин.";
            } else {
                return "Композиция с ID " + compositionId + " не найдена.";
            }
        } catch (SQLException e) {
            return "Ошибка при обновлении: " + e.getMessage();
        }
    }

    /**
     * Удаление композиции. Возвращает строку с результатом.
     */
    public String deleteComposition(int compositionId) {
        String query = "DELETE FROM composition WHERE composition_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, compositionId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                return "Композиция с ID " + compositionId + " успешно удалена.";
            } else {
                return "Композиция с ID " + compositionId + " не найдена.";
            }
        } catch (SQLException e) {
            return "Ошибка при удалении: " + e.getMessage();
        }
    }

    /**
     * Вспомогательный метод для получения ID последней добавленной записи.
     */
    public int getLastCompositionId() {
        String query = "SELECT MAX(composition_id) FROM composition";
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
