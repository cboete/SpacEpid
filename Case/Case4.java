package Case;

import java.util.Random;

public class Case4 extends Case {

	
	public static Case4 generer (int taille, int tailleVoisinage){
		Case4 [][] grille = new Case4 [taille][taille];
		for (int i =0; i < taille; i++)
			for (int j =0; j < taille; j++)
				grille[i][j] = new Case4 (i,j, tailleVoisinage);

		int nbVoisins = tailleVoisinage*(tailleVoisinage+1)*2;
		for (int i =0; i < taille; i++)
			for (int j =0; j < taille; j++)
				grille[i][j].set(nbVoisins, grille[(i+taille-1)%taille][j],
								 grille[i][(j+taille+1)%taille],
						         grille[(i+taille+1)%taille][j], 
						         grille[i][(j+taille-1)%taille]);

		for (int i =0; i < taille; i++){
			for (int j =0; j < taille; j++){
				grille[i][j].genererVoisinage();
			}
		}
		return grille [0][0];
	}
	
	
	public Case4(int ligne, int colonne, int taille) {
		super(ligne, colonne, taille);
		// TODO Auto-generated constructor stub
	}
	
	public Case getRandomCase (Random r){
		return getVoisin (r.nextInt(getNbVoisins()));
	}
	
	private void genererVoisinage (){
		int index  = OUEST+1;
		index = genererQuart(index, NORD, EST);
		index = genererQuart(index, EST, SUD);
		index = genererQuart(index, SUD, OUEST);
		index = genererQuart(index, OUEST, NORD);
	}
	
	
	private int genererQuart (int index, int d1, int d2){
		Case caseCou = getVoisin(d1);
		int indexCou = index;
		for (int i = 1; i < tailleVoisinage();i++){
			voisins [indexCou++] = (caseCou = caseCou.getVoisin(d1));
		}

		caseCou = getVoisin(d1);
		for (int j = 1; j < tailleVoisinage(); j++){
			voisins[indexCou++] = (caseCou = caseCou.getVoisin(d2));
		}
				
		for (int i = 0; i+1 < tailleVoisinage(); i++){
			caseCou = voisins [index + i];
			for (int j = 0; i+j+2 < tailleVoisinage(); j++){
				voisins[indexCou++] = caseCou = caseCou.getVoisin(d2);
			}
		}
		return indexCou;
	}

	
	
	public static void main (String [] args){
		int taille = 10;
		int tailleVoisinage = 1;
		Case [][] grille = new Case [taille][taille];
		
		grille [0][0] = generer (taille, tailleVoisinage);
		((Case4) grille[0][0]).genererVoisinage();
		
		for (int i = 1; i < taille; i++){
			grille[i][0] = grille [i-1][0].getVoisin(SUD);
			((Case4) grille[0][0]).genererVoisinage();
		}

		for (int i = 0; i < taille; i++){
			for (int j = 1; j < taille; j++){
				grille[i][j] = grille [i][j-1].getVoisin(EST);
				((Case4) grille[0][0]).genererVoisinage();
			}
		}
		
		for (int i = 0; i< taille; i++){
			for (int j = 0; j < taille; j++){
				System.out.print(i + " " + j + ": " + grille [i][j]);
				for (int k = 0; k < grille[i][j].getNbVoisins(); k++){
					System.out.print (" " + k + " " + grille[i][j].getVoisin(k));
				}
				System.out.println ("\n");
			}
		}
		
	}
	
	
	
}
