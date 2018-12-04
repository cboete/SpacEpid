package Graphique;

import javax.swing.JLabel;
import javax.swing.JPanel;;

public class JAffichage extends JPanel {

	public static final long serialVersionUID = 0;
	
	JLabel valeur;
	
	public JAffichage (String nom){
		add (new JLabel(nom));
		add (valeur = new JLabel ("       "));
	}
	
	public void setValeur (String v){
		valeur.setText(v);
	}
}
