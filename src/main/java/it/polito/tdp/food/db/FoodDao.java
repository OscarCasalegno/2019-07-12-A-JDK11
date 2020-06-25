package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {

	public void listAllFoods(Map<Integer, Food> idMapFood, Map<Integer, Condiment> idMapCondiment) {
		String sql = "SELECT * FROM food";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					Food f = new Food(res.getInt("food_code"), res.getString("display_name"));
					this.getCondiments(f, idMapCondiment);
					idMapFood.put(f.getFood_code(), f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void getCondiments(Food f, Map<Integer, Condiment> idMapCondiment) {
		String sql = "SELECT condiment_code " + "from food_condiment " + "WHERE food_code= ?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, f.getFood_code());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					f.getCondiments().add(idMapCondiment.get(res.getInt("condiment_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void listAllCondiments(Map<Integer, Condiment> idMapCondiment) {
		String sql = "SELECT * FROM condiment";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					Condiment c = new Condiment(res.getInt("condiment_code"), res.getString("display_name"),
							res.getDouble("condiment_calories"), res.getDouble("condiment_saturated_fats"));
					idMapCondiment.put(c.getCondiment_code(), c);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Portion> listAllPortions(Map<Integer, Food> idMapFood) {
		String sql = "SELECT * FROM `portion`";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Portion> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"), res.getDouble("portion_amount"),
							res.getString("portion_display_name"), res.getDouble("calories"),
							res.getDouble("saturated_fats"), idMapFood.get(res.getInt("food_code"))));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Food> listFoodsByNumber(Integer number, Map<Integer, Food> idMapFood) {
		String sql = "SELECT food.food_code as code"
				+ "	FROM `portion`, food WHERE food.food_code=portion.food_code " + "	GROUP BY food.food_code "
				+ "	HAVING COUNT(DISTINCT portion.portion_id)<= ?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, number);

			List<Food> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(idMapFood.get(res.getInt("code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Food> listConnectionsOf(Food f, Map<Integer, Food> idMapFood) {
		String sql = "SELECT distinct f1.food_code, f2.food_code " + "FROM food_condiment AS f1, food_condiment AS f2 "
				+ "WHERE f1.food_code < f2.food_code AND f1.condiment_code = f2.condiment_code AND f1.food_code= ?";
		try {
			Connection conn = DBConnect.getConnection();

			List<Food> list = new ArrayList<>();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, f.getFood_code());

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(idMapFood.get(res.getInt("f2.food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
