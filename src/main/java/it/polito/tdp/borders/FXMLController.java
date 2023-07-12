/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryAndNumber;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="boxNazione"
	private ComboBox<Country> boxNazione; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCalcolaConfini(ActionEvent event) {
		
		txtResult.clear();
		int anno;
		try {
			anno = Integer.parseInt(txtAnno.getText());
			if(anno<1816 || anno>2006)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			txtResult.setText("Inserisci un anno compreso tra 1816 e 2006!");
			return;
		}
		model.creaGrafo(anno);
		this.boxNazione.getItems().addAll(model.getVertici());
		List<CountryAndNumber> result = model.getCountryAndNumbers();
		for (CountryAndNumber cn : result) {
			txtResult.appendText(cn.toString() + "\n");
		}
		
	}

	@FXML
	void doSimula(ActionEvent event) {
		
		txtResult.clear();
		Country partenza = this.boxNazione.getValue();
		if(partenza == null) {
			txtResult.appendText("Errore: devi selezionare una nazione\n");
			return;
		}
		this.txtResult.appendText("Migrazioni partendo da " + partenza.getStateName() + ":\n");
		Map<Country, Integer> stanziali = model.simulaMigrazione(partenza);
		for(Country c : stanziali.keySet()) {
			int num = stanziali.get(c);
			if(num>0)
				txtResult.appendText(" > " + c.getStateAbb() + " : " + num + "\n");
		}
		txtResult.appendText("Passi di simulazione: " + model.getnPassiSim() + "\n");
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxNazione != null : "fx:id=\"boxNazione\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
	}

	public void setModel(Model model) {
		this.model = model;
		
		
	}
}
