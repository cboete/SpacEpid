package Graphique;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JParametre extends JPanel {

	public static final long serialVersionUID = 0;
	
	private JTextField valeur;
	private String nom;

	
	public JParametre (String nom){
		this.nom = nom;
		add (new JLabel(nom));
		add (valeur = new JTextField(40));
	}

	public JParametre (String nom, double valeur){
		this (nom);
		this.valeur.setText("" + valeur);
	}

	public double getValeur (){
		return Double.parseDouble(valeur.getText());
	}
	
	public String toString (){
		if (valeur.getText().isEmpty())
			return nom + "\t-1";
		return nom + "\t" + valeur.getText();
	}
	
}
