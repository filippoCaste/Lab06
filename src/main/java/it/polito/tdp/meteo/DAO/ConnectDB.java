package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

public class ConnectDB {
	
	private static String jdbcUrl = "jdbc:mysql://localhost/meteo";
	private static HikariDataSource ds = null;

	public static Connection getConnection() {
		if(ds==null) {
			ds = new HikariDataSource();
			ds.setJdbcUrl(jdbcUrl);
			ds.setUsername("root");
			ds.setPassword("root");
		}

		try {
				return ds.getConnection();

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException("Cannot get a connection " + jdbcUrl, e);
		}
	}

}
