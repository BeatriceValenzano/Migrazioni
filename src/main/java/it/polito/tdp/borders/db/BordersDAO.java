package it.polito.tdp.borders.db;

import it.polito.tdp.borders.model.Adiacenza;
import it.polito.tdp.borders.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BordersDAO {

	public List<Country> loadAllCountries(Map<Integer, Country> countriesMap) {

		String sql = "SELECT ccode,StateAbb,StateNme " +
				"FROM country " +
				"ORDER BY StateAbb ";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Country> list = new LinkedList<Country>();

			while (rs.next()) {

					Country c = new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
					countriesMap.put(c.getcCode(), c);
					list.add(c);
			}

			conn.close();

			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Country> getVertici(int anno, Map<Integer, Country> countriesMap) {
		
		String sql = "SELECT DISTINCT state1no "
				+ "FROM contiguity "
				+ "WHERE YEAR <= ? AND conttype = 1";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			List<Country> list = new LinkedList<Country>();

			while (rs.next()) {
				Country c = countriesMap.get(rs.getInt("state1no"));
				list.add(c);
			}

			conn.close();

			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Adiacenza> getEdges(int anno) {
		
		final String sql = "SELECT state1no, state2no "
				+ "FROM contiguity "
				+ "WHERE year <= ? AND conttype = 1 AND state1no<state2no";
		List <Adiacenza> adiacenti = new LinkedList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Adiacenza a = new Adiacenza(rs.getInt("state1no"), rs.getInt("state2no"));
				adiacenti.add(a);
			}
			conn.close();
			return adiacenti;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
