/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtPorzioni"
	private TextField txtPorzioni; // Value injected by FXMLLoader

	@FXML // fx:id="txtK"
	private TextField txtK; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalorie"
	private Button btnCalorie; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="boxFood"
	private ComboBox<Food> boxFood; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();

		Integer number;
		try {
			number = Integer.parseInt(this.txtPorzioni.getText());
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Inserisci un numero");
			return;
		}

		if (number < 1) {
			this.txtResult.appendText("Inserisci un numero maggiore di 0");
			return;
		}

		this.model.creaGrafo(number);
		List<Food> cibo = this.model.getFood();
		cibo.sort(null);
		this.boxFood.getItems().addAll(cibo);
	}

	@FXML
	void doCalorie(ActionEvent event) {
		txtResult.clear();

		Food scelta = this.boxFood.getValue();

		if (scelta == null) {
			this.txtResult.appendText("Scegli un cibo");
			return;
		}

		Map<Food, Double> mappa = this.model.getNeighboursOf(scelta, 5);

		if (mappa == null) {
			this.txtResult.appendText("Nessun collegamento");
		} else {
			for (Food f : mappa.keySet()) {

				this.txtResult.appendText(String.format("Cibo: %s Calorie: %f\n", f.toString(), mappa.get(f)));
			}

		}

	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();

		Food scelta = this.boxFood.getValue();

		if (scelta == null) {
			this.txtResult.appendText("Scegli un cibo");
			return;
		}

		Integer k;
		try {
			k = Integer.parseInt(this.txtK.getText());
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Scrivi un numero tra 1 e 10");
			return;
		}
		if (k < 1 || k > 10) {
			this.txtResult.appendText("Scrivi un numero tra 1 e 10");
			return;
		}

		this.model.simula(scelta, k);

		this.txtResult.appendText("Numero totale cibi: " + this.model.getNumeroCibi() + "\n");
		this.txtResult.appendText("Tempo totale: " + this.model.getTempoTotale().toMinutes() + " Minuti\n");
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
		assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
