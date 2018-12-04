package Case;

import java.util.Random;

import Individu.CalculTauxGlobal;
import Individu.ParametreHost;

public class CaseGlobal extends Case {

	
	
	
	
	public static CaseGlobal generer (int taille){
		CaseGlobal [] grille = new CaseGlobal [taille*taille];
		int index =0;
		for (int i =0; i < taille; i++){
			for (int j =0; j < taille; j++){
				grille[index] = new CaseGlobal (i,j);
				grille[index++].voisins = grille;
			}
		}
		
		return grille [0];
	}
	
	public CaseGlobal(int ligne, int colonne) {
		// TODO Auto-generated constructor stub
		super (ligne, colonne, 0);
	}
	
	public Case getRandomCase (Random r){
		Case resultat = null;
		for  (resultat = voisins [r.nextInt(voisins.length)]; resultat == this; resultat = voisins [r.nextInt(voisins.length)]);
		return resultat;
	}	
	
}
