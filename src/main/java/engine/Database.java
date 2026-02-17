package engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    private Connection connection;

    // Constructor
    public Database(String url) throws Exception {
        connection = DriverManager.getConnection(url);
        createTable();
    }

    private void createTable() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS steps ("
                + "workflow_id TEXT, "
                + "step_key TEXT, "
                + "status TEXT, "
                + "output TEXT, "
                + "PRIMARY KEY (workflow_id, step_key)"
                + ");";

        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public synchronized void save(String workflowId, String stepKey, String status, String output) throws Exception {
        String sql = "INSERT OR REPLACE INTO steps VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, workflowId);
        ps.setString(2, stepKey);
        ps.setString(3, status);
        ps.setString(4, output);
        ps.executeUpdate();
    }

    public String find(String workflowId, String stepKey) throws Exception {
        String sql = "SELECT output FROM steps WHERE workflow_id=? AND step_key=? AND status='COMPLETED'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, workflowId);
        ps.setString(2, stepKey);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("output");
        }
        return null;
    }
}

