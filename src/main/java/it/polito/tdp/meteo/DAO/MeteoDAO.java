package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), (rs.getDate("Data")).toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Citta> getAllLocalita() {
		final String sql="SELECT DISTINCT Localita FROM situazione";
		List<Citta> citta = new ArrayList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Citta c = new Citta(rs.getString("Localita"), this.getAllRilevamentiLocalita(rs.getString("Localita")));
				citta.add(c);
			}
			rs.close();
			st.close();
			conn.close();
			return citta;
			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Rilevamento> getAllRilevamentiLocalita(String localita) {
		final String sql = "SELECT s.Localita, s.Data, s.Umidita FROM situazione s WHERE s.Localita= ?";
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, localita);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rilevamento r = new Rilevamento(rs.getString("Localita"), (rs.getDate("Data")).toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
			rs.close();
			st.close();
			conn.close();
			return rilevamenti;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT s.Localita, s.Data, s.Umidita FROM situazione s WHERE s.Localita= ? AND s.Data";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), (rs.getDate("Data")).toLocalDate(), rs.getInt("Umidita"));
				if(r.getData().getMonthValue()==mese)
					rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
