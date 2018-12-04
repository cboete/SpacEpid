package Graphique;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

public class JButtonColor extends JButton implements ActionListener{
	
	public static final long serialVersionUID = 0;
	
	private Color couleur = Color.white;
	
	
	public JButtonColor (String nom){
		super(nom);
		setBackground(couleur);
		addActionListener(this);
	}
	
	public JButtonColor (String nom, Color c){
		super(nom);
		couleur = c;
		setBackground(couleur);
		addActionListener(this);
	}

	
	public Color getCouleur (){
		return couleur;
	}
	
	@Override
	public void actionPerformed (ActionEvent ae){
		JColorChooser chooser = new JColorChooser(couleur);
		if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, chooser, "Choix couleur", JOptionPane.OK_CANCEL_OPTION)){
			couleur = chooser.getColor();
			setBackground(couleur);
		}
	}
}
