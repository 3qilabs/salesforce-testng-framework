package com.salesforce.utilities;

import com.salesforce.core.ConfigurationManager;
import org.apache.commons.configuration.Configuration;

import java.sql.*;
import java.util.*;

/**
 * Database class for DatabaseUtil Created on Jan 29, 2010
 * 
 */

public class DatabaseUtil {

	/**
	 * {@link PreparedStatement#close() close PreparedStatement} if not null ignoring exception if any
	 * @param ps
	 */
	public static void close(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.clearWarnings();
				ps.clearParameters();
				ps.close();
			}
		} catch (SQLException sqle) {
		}
	}

	/**
	 * {@link Statement#close() close Statement} if not null ignoring exception if any
 
	 * @param s
	 */
	public static void close(Statement s) {
		try {
			if (s != null) {
				s.close();
			}
		} catch (SQLException sqle) {
		}
	}

	/**
	 * Utility method to close result-set and statement if not not and ignoring exception if any
	 * @param ps
	 * @param rs
	 */
	public static void close(PreparedStatement ps, ResultSet rs) {
		close(rs);
		close(ps);
	}

	/**
	 * Utility method to close result-set and statement if not not and ignoring exception if any
	 * @param st
	 * @param rs
	 */
	public static void close(Statement st, ResultSet rs) {
		close(rs);
		close(st);
	}

	/**
	 * {@link ResultSet#close() close result set} if not null ignoring exception if any
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException sqle) {
		}
	}

	/**
	 * {@link Connection#close() close connection} if not null ignoring exception if any

	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException se) {
		}
	}

	/**
	 * For default data base configuration, Creates data base connection using following properties:
	 * <ol>
	 * <li>db.driver.class</li>
	 * <li>db.connection.url</li>
	 * <li>db.user</li>
	 * <li>db.pwd</li>
	 * </ol>
	 * 
	 * @return {@link Connection} object
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		PropertyUtil props = ConfigurationManager.getBundle();
		String url = props.getString("db.connection.url");
		String driverclass = props.getString("db.driver.class");
		String user = props.getString("db.user");
		String pwd = props.getString("db.pwd");
		return getConnection(driverclass, url, user, pwd);
	}

	/**
	 * If you want to use multiple data bases in the project. provide different configuration with different prefix. For example
	 * <p>defualt</p>
	 * <ol>
	 * <li>db.driver.class</li>
	 * <li>db.connection.url</li>
	 * <li>db.user</li>
	 * <li>db.pwd</li>
	 * </ol>
	 * <p>another database configuration</p>
	 * <ol>
	 * <li>con1.db.driver.class</li>
	 * <li>con1.db.connection.url</li>
	 * <li>con1.db.user</li>
	 * <li>con1.db.pwd</li>
	 * </ol>
	 * to use another configuration: <code>{@link #getConnection(String) getConnection("con1")}
	 * @param prefix - prefix of the database configuration to be used
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(String prefix) throws Exception {
		Configuration props = StringUtil.isBlank(prefix) ? ConfigurationManager.getBundle()
				: ConfigurationManager.getBundle().subset(prefix);
		String url = props.getString("db.connection.url");
		String driverclass = props.getString("db.driver.class");
		String user = props.getString("db.user");
		String pwd = props.getString("db.pwd");
		return getConnection(driverclass, url, user, pwd);
	}

	/**
	 * @param driverCls
	 * @param url
	 * @param user
	 * @param pwd
	 * @return {@link Connection} object
	 * @throws Exception
	 */
	public static Connection getConnection(String driverCls, String url, String user, String pwd) throws Exception {
		Connection con = null;
		Class.forName(driverCls);// loads the driver
		con = DriverManager.getConnection(url, user, pwd);
		return con;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static Object[][] getData(String query) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				int colsCnt = rs.getMetaData().getColumnCount();
				Object[] cols = new Object[colsCnt];
				for (int indx = 0; indx < colsCnt; indx++) {
					cols[indx] = rs.getObject(indx + 1);
				}
				rows.add(cols);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}

		return rows.toArray(new Object[][] {});
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static Object[][] getRecordDataAsMap(String query) {
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				HashMap<String, Object> map = new LinkedHashMap<String, Object>();

				int colsCnt = rs.getMetaData().getColumnCount();
				for (int indx = 1; indx <= colsCnt; indx++) {
					map.put(rs.getMetaData().getColumnLabel(indx), (rs.getObject(indx)));
				}
				rows.add(new Object[] { map });
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}
		return rows.toArray(new Object[][] {});
	}
	
	/**
	 * use this method when you want to run query using default database configuration
	 * @param query
	 * @return
	 */
	public static List<Map<String, Object>> getRecordAsMap(String query) {
		return getRecordAsMap("", query);
	}

	/**
	 * Use this method if you have multiple database configuration. Provide prefix of the configuration to be used. 
	 * @see #getConnection(String)
	 * @param connectionPrefix
	 * @param query
	 * @return
	 */
	public static List<Map<String, Object>> getRecordAsMap(String connectionPrefix, String query) {
		ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DatabaseUtil.getConnection(connectionPrefix);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();

				int colsCnt = rs.getMetaData().getColumnCount();
				for (int indx = 1; indx <= colsCnt; indx++) {
					map.put(rs.getMetaData().getColumnLabel(indx), rs.getObject(indx));
				}
				rows.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.close(stmt, rs);
			DatabaseUtil.close(con);
		}
		return rows;
	}
}
