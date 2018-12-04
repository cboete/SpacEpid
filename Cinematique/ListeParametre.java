package Cinematique;


import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import Graphique.JParametre;

public class ListeParametre {

	
	static private Hashtable<String, Integer> position; 	
	
	/*
	static public String getEntete (){
		return "idReplicat\t" +
		"nbDepart\t" +
		"tauxDepart\t" +
		"rH1\t" +
		"densite\t" +
		"Geog_dep\t" + 
		"mu\t" + 
		"alphaM\t" +	
		"alphaN\t" +
		"gamma\t" +
		"beta\t" +
		"succesParasite \t" +
		"investissement total\t" +
		"taux investisement\t" +
		"exposant R1\t" + 
		"exposant R2\t" +
		"cR1\t" +
		"cR2\t" + 
		"eff1\t" +
		"eff2\t" +
		"ratio_Eff2\t" +  
		"NSz\t" +
		"NTy\t" +
		"Nb_H1S_Migr\t" + 
		"Nb_H1I_Migr\t" + 
		"Nb_HR1S_Migr\t" + 
		"Nb_HR1I_Migr\t" + 
		"Nb_HR2S_Migr\t" + 
		"Nb_HR2I_Migr\t" + 
		"Nb_HR3S_Migr\t" + 
		"Nb_HR3I_Migr\t" + 
		"Cycle_Tous_Sain\t" + 
		"Nb_H1S_Tous_Sain\t" + 
		"Nb_HR1S_Tous_Sain\t" + 
		"Nb_HR2S_Tous_Sain\t" + 
		"Nb_HR3S_Tous_Sain\t" + 
		"Nb_H1S\t" + 
		"Nb_H1I\t" + 
		"Nb_HR1S\t" + 
		"Nb_HR1I\t" + 
		"Nb_HR2S\t" + 
		"Nb_HR2I\t" + 
		"Nb_HR3S\t" + 
		"Nb_HR3I\t" + 
		"total\t" +
		"cycle\t" +
		"temps";
	}*/
	
	static public String getEntete (){

		String resultat;
		
		Vector <Map.Entry<String, Integer>> temp = new Vector<Map.Entry<String, Integer>> (position.entrySet());
		String [] tab = new String [temp.size()];
		
		for (int i = 0; i < temp.size(); i++){
			tab[temp.elementAt(i).getValue()] = temp.elementAt(i).getKey();
		}

		resultat = tab [0];
		for (int i = 1; i < tab.length; i++){
			resultat += '\t' + tab [i];
		}
		
		return resultat;		
	}
	
	public String resume (){
		return "TV=" + valeurs[position.get("tailleVoisinage")] +
				" invest=" + valeurs[position.get("investissement")] + 
				" FreqDep=" + valeurs[position.get("frequenceDependant")];
		
	}
	
	
	public String toString (){
		
		String resultat = "";
		for (int i = 0; i<valeurs.length; i++){
			if (valeurs[i] == (int)valeurs[i])
				resultat += ("" +(int) valeurs [i]) + '\t';
			else
				resultat += ("" +valeurs [i]).replace(".", ",") + '\t';
		}
		return resultat;
		/*
		return valeurs[position.get("idReplicat")] + "\t" +
				valeurs[position.get("nbTotal")] + "\t" +
				valeurs[position.get("tauxInfecte")] + "\t" +				
				valeurs[position.get("rH1")] + "\t" + 
				valeurs[position.get("densiteDependant")] + "\t" +
				valeurs[position.get("frequenceDependant")] + "\t" +
				valeurs[position.get("mu")] + "\t" + 
				valeurs[position.get("alphaM")] + "\t" + 
				valeurs[position.get("alphaN")] + "\t" + 
				valeurs[position.get("gamma")] + "\t" + 
				valeurs[position.get("beta")] + "\t" + 
				valeurs[position.get("succesParasite")] + "\t" +
				investissement + "\t" + 
				ratio1 + "\t" +
				m1 + "\t" +
				m2 + "\t" +
				valeurs[position.get("cR1")] + "\t" + 
				valeurs[position.get("cR2")] + "\t" + 
				valeurs[position.get("eff1")] + "\t" + 
				valeurs[position.get("eff2")] + "\t" + 
				valeurs[position.get("ratioEff2")] + "\t" +
				valeurs[position.get("tailleVoisinage")] + "\t" + 
				valeurs[position.get("typeVoisinage")] + "\t";*/
	}
	
	private double [] valeurs;
	//private double investissement = -1, m1=-1, m2=-1, cMax1=-1, cMax2=-1, ratio1=-1;

	public ListeParametre (){
		int i = 0;
		if (position == null){
			position = new Hashtable<String, Integer>();
			
			position.put("tailleGrille", i++);
			position.put("typeVoisinage", i++);
			position.put("step", i++);
			position.put("StopTime", i++);
			position.put("nbTotal", i++);
			position.put("tauxInfecte", i++);
			position.put("TimeMigration", i++);
			position.put("alphaM", i++);
			position.put("alphaN", i++);
			position.put("mu", i++);
			position.put("tailleVoisinage", i++);
			position.put("beta", i++);
			position.put("gamma", i++);
			position.put("idReplicat", i++);
			position.put("rH1", i++);
			position.put("densiteDependant", i++);
			position.put("frequenceDependant", i++);
			position.put("succesParasite", i++);

			position.put("tauxHR1Sain", i++);
			position.put("tauxHR1Infecte", i++);
			position.put("tauxHR2Sain", i++);
			position.put("tauxHR2Infecte", i++);
			position.put("tauxHR3Sain", i++);
			position.put("tauxHR3Infecte", i++);
			position.put("cR1", i++);
			position.put("eff1", i++);
			position.put("cR2", i++);
			position.put("eff2", i++);
			position.put("ratioEff2", i++);
			position.put("investissement", i++);
			position.put("cMax1", i++);
			position.put("cMax2", i++);
			position.put("m1", i++);
			position.put("m2", i++);
			position.put("ratio1", i++);
		}
		valeurs = new double [position.keySet().size()];
		valeurs[position.get("investissement")] = -1;
		i=0;
		valeurs[i++] = 100;
		valeurs[i++] = 4;
		valeurs[i++] = 5;		
		valeurs[i++] = 10000;
		valeurs[i++] = 10;
		valeurs[i++] = 0.2;
		valeurs[i++] = 1000;
		valeurs[i++] = 0.4;
		valeurs[i++] = 0;
		valeurs[i++] = 0.01;
		valeurs[i++] = 2;
		valeurs[i++] = 0.6;
		valeurs[i++] = 0.3;
		valeurs[i++] = 1;
		valeurs[i++] = 0.02;
		valeurs[i++] = 1;
		valeurs[i++] = 1;
		valeurs[i++] = 1;
		valeurs[i++] = 0.08;
		valeurs[i++] = 0.02;
		valeurs[i++] = 0;
		valeurs[i++] = 0;
		valeurs[i++] = 0;
		valeurs[i++] = 0;
		valeurs[i++] = 0.1;
		valeurs[i++] = 1;
		valeurs[i++] = 0;
		valeurs[i++] = 0;
		valeurs[i++] = -1;
		valeurs[i++] = -1;
		valeurs[i++] = -1;
		valeurs[i++] = -1;
		valeurs[i++] = -1;
		valeurs[i++] = -1;
		valeurs[i++] = -1;		
	}
	
	public ListeParametre (Vector<Parametre> params){
		this();
		Integer indice;
		for (int i = 0; i < params.size (); i++){
			//System.out.println (params.elementAt(i).nom);
			if ((indice = position.get(params.elementAt(i).nom)) != null){
				//System.out.println (params.elementAt(i).nom + " " + indice);
				valeurs[indice] = params.elementAt(i).getValeur();
			}
		}
		if (valeurs[position.get("investissement")] != -1){
			double invest1 = investissement()* ratio1();
			valeurs[position.get("eff1")] = arrondi(invest1, 5);
			valeurs[position.get("eff2")] = arrondi((investissement()-invest1), 5);
			valeurs[position.get("cR1")] =  arrondi(cMax1() * Math.pow(invest1, m1()), 5);
			
			//valeurs[position.get("cR2")] = arrondi(cMax2() * Math.pow(investissement()-invest1, m2()), 5);
			
			valeurs[position.get("cR2")] = cMax2() * Math.pow(investissement()-invest1, m2()) * (
					valeurs[position.get("ratioEff2")]+
          		  (1-valeurs[position.get("ratioEff2")])*
          		  (valeurs[position.get("gamma")]/
          		  (1-(1-valeurs[position.get("gamma")])*(1-valeurs[position.get("eff2")]))
          				  ));
			
			System.out.println ("cr2 1" + valeurs[position.get("cR2")]);			
			

		}
	}	
	
	public JParametre [] getGraphique (){
		//Set<Map.Entry<String, Integer>> temp = position.entrySet();
		Vector <Map.Entry<String, Integer>> temp = new Vector<Map.Entry<String, Integer>> (position.entrySet());
		JParametre [] tab = new JParametre [temp.size()];
		
		for (int i = 0; i < temp.size(); i++){
			tab[temp.elementAt(i).getValue()] = new JParametre(temp.elementAt(i).getKey(), valeurs[temp.elementAt(i).getValue()]);
		}
		return tab;
	}
	
	
	
	public double arrondi (double a, int nb){
		int multi = (int)Math.pow(10, nb);
		return ((double)Math.round(a*multi))/multi;
	}
	
	public boolean isValide (){
		return valeurs[position.get("rH1")] >= valeurs [position.get("mu")];
	}
	
	public void add (Parametre p){
		valeurs[position.get(p.nom)] = p.getValeur();
	}
	/*
	public void envoyer (PrintStream ps){
		for (int i = 0; i < valeurs.length; i++)
			ps.println (valeurs[i]);
	}
   */
	public void envoyer (PrintStream ps){
		for (int i = 0; i < valeurs.length; i++)
			ps.println (valeurs[i]);
	}

	
	public int stopTime (){
		return (int) valeurs[position.get("StopTime")];
	}
	
	private double investissement (){
		return valeurs[position.get("investissement")];
	}

	private double cMax1 (){
		return valeurs[position.get("cMax1")];
	}

	private double cMax2 (){
		return valeurs[position.get("cMax2")];
	}

	private double ratio1 (){
		return valeurs[position.get("ratio1")];
	}

	private double m1 (){
		return valeurs[position.get("m1")];
	}

	private double m2 (){
		return valeurs[position.get("m2")];
	}

	/*
	
	private double idReplicat;
	private double tailleGrille;
	private double tailleVoisinage;
	private double typeVoisinage;
	private double StopTime;
	private double nbH1Sain;
	private double nbH1Infecte;
	private double timeMigration;
	private double [] tauxHRSain = new double [3];
	private double [] tauxHRInfecte = new double [3];
	private double beta;
	private double gamma;
	private double rH1;
	private double mu;
	private double eff1;
	private double eff2;
	private double cR1;
	private double cR2;
	private double alpha;
	
	public ListeParametre (double [] valeurs){
		int i = 0; 
		idReplicat = valeurs [i++];
		tailleGrille =  valeurs [i++];
		tailleVoisinage =  valeurs [i++];
		typeVoisinage =  valeurs [i++];
		StopTime =  valeurs [i++];
		nbH1Sain =  valeurs [i++];
		nbH1Infecte =  valeurs [i++];
		timeMigration =  valeurs [i++];
		for (int j = 0; j < tauxHRSain.length; j++){
			tauxHRSain[j] =  valeurs [i++];
			tauxHRInfecte[j] =  valeurs [i++];
		}
		beta =  valeurs [i++];
		gamma =  valeurs [i++];
		rH1 =  valeurs [i++];
		mu =  valeurs [i++];
		eff1 =  valeurs [i++];
		eff2 =  valeurs [i++];
		cR1 =  valeurs [i++];
		cR2 =  valeurs [i++];
		alpha =  valeurs [i++];
	}
	
	
	public void envoyer (PrintStream ps){
		ps.println (idReplicat);
		ps.println (tailleGrille);
		ps.println (tailleVoisinage);
		ps.println (typeVoisinage);
		ps.println (StopTime);
		ps.println (nbH1Sain);
		ps.println (nbH1Infecte);
		ps.println (timeMigration);
		for (int j = 0; j < tauxHRSain.length; j++){
			ps.println (tauxHRSain[j]);
			ps.println (tauxHRInfecte[j]);
		}
		ps.println (beta);
		ps.println (gamma);
		ps.println (rH1);
		ps.println (mu);
		ps.println (eff1);
		ps.println (eff2);
		ps.println (cR1);
		ps.println (cR2);
		ps.println (alpha);	
	}
	

	
	public String toString (){
			return "NSi=" + tailleVoisinage +
					     "_NTy=" + typeVoisinage +
					     "_beta=" + beta + 
					     "_gamma=" + gamma + 
					     "_rh1=" + rH1 + 
					     "_mu=" + mu + 
					     "_eff1=" + eff1 + 
					     "_eff2=" + eff2 + 
					     "_cr1=" + cR1 + 
					     "_cr2=" + cR2 + 
					     "_alpha=" + alpha 
					     ;
	}
	*/

	
	
	
}
