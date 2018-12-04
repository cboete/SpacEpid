package Graphique;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Grille extends JComponent {

	public static final long serialVersionUID = 0;
	private int [][] cases;
	private Color [] couleurs;

	public Grille(Color [] couleurs) {
		// TODO Auto-generated constructor stub
		this.couleurs = couleurs;
		setPreferredSize(new Dimension(200, 200));
	}
	
	public void update (int [][] grille){
		this.cases = grille;
		repaint ();
	}
	
	@Override
	public void paint (Graphics g){
		super.paint (g);
		
		int hgap = getWidth()/cases.length;
		int vgap = getHeight()/cases.length;
		int largeurCase = hgap -1;
		int hauteurCase = vgap -1;
		
		int largeur = hgap*cases.length;
		int hauteur = vgap*cases.length;
		
		g.setColor(Color.black);
		for (int i = 0; i <= cases.length; i++ ){
			g.drawLine(i*hgap, 0, i*hgap, hauteur);
			g.drawLine(0, i*vgap, largeur, i*vgap);
		}
		
		for (int i = 0; i < cases.length; i++){
			for (int j = 0; j < cases.length; j++){
				g.setColor(couleurs[cases[i][j]]);
				g.fillRect(j*hgap+1, i*vgap+1, largeurCase, hauteurCase);
			}
		}
	}
	
	
	
	public static void main (String [] args){
		int [][] grille = new int [10][10];
		Color [] couleurs = {Color.white, Color.CYAN, Color.blue, Color.GREEN};
		JFrame fen = new JFrame();
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Grille temp = new Grille(couleurs);
		fen.setContentPane(temp);
		fen.setVisible(true);
		fen.pack();
		
		Random r = new Random();
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				grille[i][j] = r.nextInt(4);
				System.out.print (grille[i][j] + " ");
			}
			System.out.println ();
		}
		
		temp.update(grille);
	}
	
	
	
	
	

}
