package Graphique;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Case.Case;


public class AffichageCase extends JButton{

	public static final long serialVersionUID = 0;
	private static Color [] couleurs = {};
	
	public static void setCouleur (Color ... couleurs){
		AffichageCase.couleurs = new Color [couleurs.length];
		
		for (int i = 0; i < couleurs.length; i++)
			AffichageCase.couleurs [i] = couleurs [i];
	}
	
	private Case objet;
	
	public AffichageCase (Case  c, ActionListener al){
		//super(c.toString());
		objet = c;
		//setBackground(couleurs [objet.getType()]);
		addActionListener(al);
		setMinimumSize(new Dimension (9,9));
	}
	
	
	
	public void update (){
		if (objet != null){
			setBackground(couleurs [objet.getIndividu().getId()]);
		}
		updateUI();
		repaint();
	}
	
	
	public boolean isVoisin (Case c){
		
		for (int i =0; i < objet.getNbVoisins(); i++){
			if (objet.getVoisin(i) == c)
				return true;
		}
		return false;
	}

	public String toString (){
		return  "" + objet;
	}
	
	private AffichageCase me (){
		return this;
	}
	
}
