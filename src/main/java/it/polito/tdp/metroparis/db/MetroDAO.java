package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metroparis.model.CoppiaFermate;
import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Linea;

public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"),
						new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}

	public List<Linea> getAllLinee() {
		final String sql = "SELECT id_linea, nome, velocita, intervallo FROM linea ORDER BY nome ASC";

		List<Linea> linee = new ArrayList<Linea>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Linea f = new Linea(rs.getInt("id_linea"), rs.getString("nome"), rs.getDouble("velocita"),
						rs.getDouble("intervallo"));
				linee.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return linee;
	}

	/**
	 * Il metodo mi dice se le due fermate passate come parametro siano adiacenti.
	 * @param fp La fermata di partenza
	 * @param fa La fermata di arrivo
	 * @return *true* se le due fermate sono adiacenti
	 */
	public boolean fermateConnesse(Fermata fp, Fermata fa) {
		String sql="SELECT COUNT(*) AS C FROM connessione WHERE id_StazP=? AND id_StazA=?";
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setInt(1, fp.getIdFermata());
			st.setInt(2, fa.getIdFermata());
			ResultSet res=st.executeQuery();
			res.first();
			int numLinee=res.getInt("C");
			conn.close();
			return (numLinee>=1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Il metodo fornisce tutte le fermate che sono direttamente connesse alla fermata passata come parametro
	 * @param fp la fermata di partenza
	 * @param fermateIdMap (per risalire all'oggetto completo, noto solo l'id)
	 * @return La lista di tutte le fermate connesse direttamente.
	 */
	public List<Fermata> fermateSuccessive(Fermata fp, Map<Integer,Fermata> fermateIdMap){
		List<Fermata> result=new ArrayList<>();
		String sql="SELECT DISTINCT id_StazA FROM connessione WHERE id_StazP=?";
		
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setInt(1, fp.getIdFermata());
			ResultSet res=st.executeQuery();
			while(res.next()) {
				int idFa=res.getInt("id_StazA");
				result.add(fermateIdMap.get(idFa));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}


	/**
	 * Il metodo mi restituisce le fermate che costituiscono la connessione, come oggetti.
	 * @param fermateIdMap per risalire all'oggetto completo, noto l'id.
	 * @return una lista di coppie di fermate.
	 */
	public List<CoppiaFermate> coppieFermate(Map<Integer,Fermata> fermateIdMap) {
		List<CoppiaFermate> result=new ArrayList<>();
		String sql="SELECT DISTINCT id_StazP, id_StazA FROM connessione";
		
		try {
			Connection conn= DBConnect.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			ResultSet res=st.executeQuery();
			while(res.next()) {
				CoppiaFermate c=new CoppiaFermate(
						fermateIdMap.get(res.getInt("id_StazP")),
						fermateIdMap.get(res.getInt("id_StazA")));
				result.add(c);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
