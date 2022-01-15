package utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Slf4j
public class DBConnection {
    public void insertNewFileInfo(String title, String content) {
        String query = """
                INSERT INTO FILES (TYPE_ID, NAME, CONTENT)
                VALUES(?,?,?);
                """;
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:file:C:/Users/razma/IdeaProjects/TextEditor/src/main/resources/DataBase/myDB", "sa", "");
            PreparedStatement prepStat = conn.prepareStatement(query);
            prepStat.setInt(1, 1);
            prepStat.setString(2, title);
            prepStat.setString(3, content);
            prepStat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Problem while trying to connect DataBase");
        }
    }

    public void updateSavedFileContentAndType(String currentName, String content) {
        String query = """
                UPDATE FILES
                SET CONTENT = ?
                WHERE NAME = ?;
                """;
        String answer = "";
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:file:C:/Users/razma/IdeaProjects/TextEditor/src/main/resources/DataBase/myDB", "sa", "");
            PreparedStatement prepStat = conn.prepareStatement(query);
            prepStat.setString(1, content);
            prepStat.setString(2, currentName);
            int result = prepStat.executeUpdate();
            log.info("query updated " + result + " rows");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Problem while trying to connect DataBase");
        }
    }
}