package data;

import db.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLNonTransientConnectionException;

public abstract class Writable {

    protected abstract PreparedStatement prepare(Connection conn) throws Exception;

    public int write() {
        return write(false);
    }

    public int write(boolean ignoreError) {
        int result = 0;
        while(true) {
            Connection conn = null;
            PreparedStatement stat = null;
            try {
                conn = DBHelper.getConnection();
                stat = prepare(conn);
                result = stat.executeUpdate();
                stat.close();
                conn.close();
            } catch (SQLNonTransientConnectionException e) {
                try {
                    Thread.sleep(100);
                } catch (Exception ee) {
                } finally {
                    continue;
                }
            } catch (Exception e) {
                if (!ignoreError) {
                    System.err.println("Caused by: " + this);
                    e.printStackTrace();
                    result = -1;
                }
            } finally {
                try {
                    if (stat != null) {
                        stat.close();
                    }
                } catch (Exception e) {
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                }
            }
            break;
        }
        return result;
    }

}
