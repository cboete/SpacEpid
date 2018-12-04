package Cinematique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Vector;

public class Parametre {

	static private Vector<Parametre> liste;
	static private int nbParametre;
	
	static public void generer (File param) throws IOException{
		BufferedReader br = new BufferedReader (new FileReader(param));
		liste = new Vector<Parametre>();
		nbParametre =1;
		for (String ligne = br.readLine(); ligne != null; ligne = br.readLine()){
			if (ligne.length () > 2){
				liste.add (new Parametre (ligne));
				nbParametre *= liste.lastElement().nbParam();
				System.out.println ("ligne " + ligne + " " + liste.lastElement().nbParam());
			}
		}
		br.close();
		
		/* initialise les parametre different de 0 sur leur premier élément*/
		for (int i = 1; i < liste.size(); i++){
			liste.elementAt(i).next();
		}
		
	}
	
	static public int nbPossibilite (){
		return nbParametre;
	}
	
	static public ListeParametre nextTous (){
		//double [] resultat = new double [liste.size()];
		boolean next = true;
		for (int i =0; i < liste.size (); i++){
			if (next){
				if (liste.elementAt(i).next())
					next = false;
				else {
					liste.elementAt(i).reset();
					liste.elementAt(i).next();
				}
			}
		}
		
		if (!next)
			return new ListeParametre(liste);
		return null;
	}
	
	
	//private BigDecimal debut, fin, pas, valeur;
	private Vector<BigDecimal> valeurs = new Vector<BigDecimal> ();
	int indexCou = -1;
	public String nom;
	
	public Parametre (String ligne){
		ligne = ligne.replace(" ", "");
		String  [] liste = ligne.split("\t");
		nom = liste[0];
		String [] split;
		for (int i = 1; i < liste.length; i++){
			split = liste[i].split(";");
			System.out.println (liste[i]);
			if (split.length == 1){
				valeurs.add(new BigDecimal(split[0]));
			}
			else {
				BigDecimal debut = new BigDecimal (split[0]);
				BigDecimal fin = new BigDecimal (split[1]);
				BigDecimal pas = new BigDecimal (split[2]);
				
				for (BigDecimal bd = debut; bd.compareTo(fin) < 1; bd = bd.add(pas))
					valeurs.add (bd);
			}
		}
	}
	
	public boolean next (){
		if (++indexCou < valeurs.size())
			return  true;
		indexCou = -1;
		return false;
	}
	
	public int nbParam (){
		return valeurs.size();
	}
	
	public double getValeur (){
		return valeurs.elementAt(indexCou).doubleValue();
	}
	
	public void reset (){
		indexCou = -1;
	}
	
}
