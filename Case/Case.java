package Case;

import java.util.Random;


import Individu.Individu;
import Individu.Mort;
import Stockage.Rangeable;

public class Case extends Rangeable{
	
	public static final int NORD=0, EST=1, SUD=2, OUEST=3; 
	private Individu host, hostScreenShot;
	private int ligne, colonne;
	private int tailleVoisinage;
	protected Case [] voisins;
	
	public Case (int ligne, int colonne, int tailleVoisinage){
		this.ligne = ligne;
		this.colonne = colonne;
		this.tailleVoisinage = tailleVoisinage;
		host = new Mort ();
		hostScreenShot = new Mort ();
	}

	public int tailleVoisinage (){
		return tailleVoisinage;
	}

	public boolean estVide (){
		return host.estMort ();
	}
	
	public void setIndividu (Individu i){
		host = i;
	}
	
	public Individu getIndividu (){
		return  host;
	}
	
	public void screenShot (){
		hostScreenShot = host;
	}
	
	public Individu getScreenShot (){
		return hostScreenShot;
	}
	
	public Case getVoisin(int index){
		return voisins [index];
	}

	public int getNbVoisins (){
		return voisins.length;
	}

	public void set (int nbVoisins, Case nord, Case est, Case sud, Case ouest){
		voisins = new Case [nbVoisins];
		voisins[NORD] = nord;
		voisins[SUD] = sud;
		voisins[OUEST] = ouest;
		voisins[EST] = est;
	}
	
	public Case getRandomCase (Random r){
		return voisins [r.nextInt(voisins.length)];
	}
	
	/*
	
	public double getProbaInfection (double resistance){
		double proba = 1;
		for (int i = 0; i < voisinage.length; i++){
			if (voisinage[i].estInfecte ())
				proba *= 1-(voisinage[i].host.get(ParametreHost.CONTAGION))*(1-resistance);
		}
		return 1-proba;
	}

	public double getProbaInfectionFreq (double resistance){
		double proba = 0;
		for (int i = 0; i < voisinage.length; i++){
			if (voisinage[i].estInfecte ())
				proba += voisinage[i].host.get(ParametreHost.CONTAGION);
		}
		return (1-resistance) * proba/tailleVoisinage();
	}

	
	public int getNbVoisinInfecte (){
		int nbVoisinsInfectes = 0;
		for (int i = 0; i < voisinage.length; i++){
			if (voisinage[i].estInfecte ())
				nbVoisinsInfectes++;
		}
		return nbVoisinsInfectes;
	}
	
	public Case getCaseVoisinLibre (){
		return getCaseVoisinLibre (0);
	}
	
	public Case getCaseVoisinLibre (int min){
		Vector<Case> vide = new Vector<Case> ();
		for (int i = 0; i < voisinage.length; i++){
			if (voisinage[i].estVide())
				vide.add(voisinage[i]);
		}
		
		if (vide.size () > min)
			return vide.elementAt(random.nextInt(vide.size()));
		return null;
	}
	
	public int getType (){
		return host.getType();
	}

	
	public Individu getHost (){
		return host;
	}
	
	public void setIndividu (Individu i){
		host = i;
	}
	public void setVoisinage (Vector<Case> voisins){
		voisinage = voisins.toArray(new Case[0]);
	}
	

	
	public boolean estInfecte (){
		return host.getState() == Individu.INFECTE;
	}
	*/
	public String toString (){
		return "(" + ligne + "," + colonne + ")";
	}
	/*
	public void afficher (PrintStream ps){
		ps.print (this + ":" + " ");
		for (int i = 0 ; i < voisinage.length; i++){
			ps.print (voisinage[i]);
		}
		ps.println();
	}*/
	

	
	
	
}
