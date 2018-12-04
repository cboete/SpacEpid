package Cinematique;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

import Case.Case;
import Case.Case4;
import Case.Case8;
import Individu.Individu;
import Individu.IndividuInfecte;
import Individu.IndividuSain;
import Individu.ParametreHost;
import Stockage.Conteneur;


public class Simulation {	
	
	private static Random random = new Random (System.currentTimeMillis());
	public static final int REPRODUCE = 0,
					        MOVE = 1,
					        DIE = 2,
					        NEWSTATE = 3;
	
	public int type = 0;
	
	//private Case [][] grille; //Case[Ligne][Colonne]
	private Case debutGrille;
	private Conteneur<Individu> population;
	private int idReplicat;
	private int tailleGrille;
	private int tailleVoisinage;
	private int typeVoisinage;
	private int StopTime [];
	private int nbTotal;
	private boolean detaille;
	private double tauxInfecte;
	private int timeMigration;
	private double [] tauxHRSain;
	private double [] tauxHRInfecte;
	private int nbCycle;
	private boolean global = false;
	private PrintStream outProgress;
	private BufferedReader inProgress;
	public boolean pause = false;
	private String tousSain, migr;
	private boolean first;
	
	private ParametreHost normal;
	private ParametreHost [] resistant = new ParametreHost[3]; 

	private void setParametre (double beta,
			double gamma,
			double rH1,
			double mu,
			double eff1,
			double eff2,
			double cR1,
			double cR2,
			double ratioEff2,
			double alphaM,
			double alphaN,
			double densiteDependance,
			double frequenceDependant,
			double succesParasite){
		
		//this.frequenceDependant = (frequenceDependant != 1);
		generer (tailleGrille, typeVoisinage, tailleVoisinage);
	
		if (frequenceDependant == 1)
			beta = beta / debutGrille.getNbVoisins();
		
		normal = new ParametreHost(ParametreHost.NORMAL);
		normal.set("H1", rH1, mu, 1.1, 
				   succesParasite, 0, rH1*(1-alphaN), mu*(1+alphaM), 1, beta, gamma);
		
		int i =0;
		resistant[i] = new ParametreHost(ParametreHost.R1);
		resistant[i].set("HR1", rH1*(1-cR1), mu, 1.1, succesParasite, 
				eff1, rH1*(1-cR1)*(1- alphaN), mu*(1 + alphaM), 1, beta, gamma);
		
		i++;
		resistant[i] = new ParametreHost(ParametreHost.R2);
		resistant[i].set("HR2", rH1, mu, 1.1, succesParasite, 
				0, rH1*(1-cR2)*(1- alphaN), mu*(1 + alphaM), 1, beta*(1-eff2*(1-ratioEff2)), 1-(1-gamma)*(1-(eff2*ratioEff2)));
		i++;
		resistant[i] = new ParametreHost(ParametreHost.R3);
		resistant[i].set("HR3", rH1*(1-cR1), mu, 1.1, succesParasite, 
				eff1, rH1*(1-cR2)*(1-cR1)*(1- alphaN), mu*(1 + alphaM), 1, beta*(1-eff2*(1-ratioEff2)), 1-(1-gamma)*(1-(eff2*ratioEff2)));

		Vector<Case> possible = getCaseVide();
		Case caseCou;
		int nbH1Sain = (int)((1-tauxInfecte)*nbTotal);
		int nbH1Infecte = (int)(tauxInfecte*nbTotal);
		
		for (i = 0; i< nbH1Sain; i++){
			caseCou = possible.remove(random.nextInt(possible.size())); 
			caseCou.setIndividu(new IndividuSain(caseCou, normal));
			population.add(caseCou.getIndividu());
		}
		
		for (i = 0; i< nbH1Infecte; i++){
			caseCou = possible.remove(random.nextInt(possible.size())); 
			caseCou.setIndividu(new IndividuInfecte(caseCou, normal));
			population.add(caseCou.getIndividu());
		}
	}
	
	public Simulation (Socket sock) throws IOException{
		this.outProgress = new PrintStream(sock.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		init (br);
	}
	
	public Simulation (BufferedReader br/*, PrintStream pw*/) throws IOException{
		//this.pw = pw;
		PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pis = new PipedInputStream(pos);
		outProgress = new PrintStream(pos);
		inProgress = new BufferedReader(new InputStreamReader(pis));
		
		init (br);
	}

	private void init (BufferedReader br)throws IOException{
		this.tailleGrille = (int)Double.parseDouble(br.readLine());
		this.typeVoisinage = (int)Double.parseDouble(br.readLine());
		
		int step = (int)Double.parseDouble(br.readLine());		
		double max = Double.parseDouble(br.readLine());
		if (step == 0)
			step = (int) max;
		this.StopTime = new int [(int)Math.ceil(max/step)];
		int index = 0;
		for (int valeur = step; valeur < max; valeur+=step)
			StopTime[index++] = valeur;
		StopTime [index] = (int)max;
	
		this.nbTotal = (int)Double.parseDouble(br.readLine());
		this.tauxInfecte = Double.parseDouble(br.readLine());
		this.timeMigration = (int)Double.parseDouble(br.readLine());
		double alphaM = Double.parseDouble(br.readLine());
		double alphaN = Double.parseDouble(br.readLine());
		double mu = Double.parseDouble(br.readLine());
		this.tailleVoisinage = (int)Double.parseDouble(br.readLine());
		double beta = Double.parseDouble(br.readLine());
		double gamma = Double.parseDouble(br.readLine());
		this.idReplicat = (int)Double.parseDouble(br.readLine());
		double rH1 = Double.parseDouble(br.readLine());
		double densiteDependance = Double.parseDouble(br.readLine());
		double frequenceDependance = Double.parseDouble(br.readLine());
		double succesParasite = Double.parseDouble(br.readLine());
		
		tauxHRSain = new double [resistant.length];
		tauxHRInfecte = new double [resistant.length];
		for (int i = 0; i < tauxHRSain.length; i++){
			tauxHRSain[i] = Double.parseDouble(br.readLine());
			tauxHRInfecte[i] = Double.parseDouble(br.readLine());
		}
		
		double cR1 = Double.parseDouble(br.readLine());
		double eff1 = Double.parseDouble(br.readLine());
		double cR2 = Double.parseDouble(br.readLine());
		double eff2 = Double.parseDouble(br.readLine());
		double ratioEff2 = Double.parseDouble(br.readLine());
		/*
		String test = br.readLine ();
		System.out.println("detaille " + test + ".");
		detaille = test.equals("d");
		*/
		//detaille = br.readLine().equals("d");
		
		for (int i = 0; i < 6; i++)
			br.readLine ();// lit les lignes pour l'investissements;
		
		detaille = br.readLine().equals("d");
		
		population = new Conteneur<Individu>((int) (tailleGrille*tailleGrille));
		setParametre (beta, gamma, rH1, mu, eff1, eff2, cR1, cR2, ratioEff2, alphaM, alphaN, densiteDependance, frequenceDependance, succesParasite);		
	}
	
	public int maxCycles (){
		return StopTime[StopTime.length -1];
	}
			
	public void sendErreur (){
		outProgress.println("ERREUR");
	}

	public void pause (int type){
		if (pause){
			this.type = type;
			try{
				synchronized (this) {
					wait();	
				}
			}catch(InterruptedException e){
				pause(type);
			}
		}
	}
	
	public void reproduce (){
		//long temps = System.currentTimeMillis();
		pause(0);
		for (Individu i = population.init();i != null; i = population.next()){
			i.reproduce(population);
		}
		//System.out.println ("rep " + (System.currentTimeMillis() -temps));
	}
	
	public void move (){
		//long temps = System.currentTimeMillis();
		pause (1);
		for (Individu i = population.init();i != null; i = population.next())
			i.move();
		//System.out.println ("move " + (System.currentTimeMillis() -temps));
	}
	
	public void die (){
		//long temps = System.currentTimeMillis();
		pause(2);
		for (int i = 0; i < population.nbElements(); i++){
			for (;population.get(i).die(population) && i < population.nbElements(););
		}
		
		//System.out.println ("die " + (System.currentTimeMillis() -temps));
	}
	
	public void updateTauxContagion (){
				
		for (int i = 0; i < population.nbElements(); i++){
			if (population.get(i).estInfecte()){
				normal.add (population.get(i));
				for (int j = 0; j < resistant.length; j++)
					resistant[j].add (population.get(i));
			}
		}
	}
	
	public void transmission (){
		pause(3);
		for (int i = 0; i < population.nbElements(); i++)
			population.get(i).newState(population);
	}
	
	public void migration (){
		Vector<Case> possible = getCaseVide();
		migr = normal.nbSain() + "\t" + normal.nbInfectes() + "\t";
		Case caseCou;
		
		int [] nbHRSain = new int [resistant.length];
		int [] nbHRInfecte = new int [resistant.length];
		int total = 0;
		
		for (int i =0; i < resistant.length; i++){
			if (tauxHRSain [i] > 1){
				nbHRSain [i] = (int) tauxHRSain [i];
				nbHRInfecte [i] = (int) tauxHRInfecte[i];
				migr += nbHRSain [i] + "\t" + nbHRInfecte [i] + "\t";
				total += nbHRSain[i] + nbHRInfecte[i];				
			}
			else{
				nbHRSain [i] = (int) Math.ceil (possible.size() * tauxHRSain [i]);
				nbHRInfecte [i] = (int) Math.ceil (possible.size() * tauxHRInfecte[i]);
				migr += nbHRSain [i] + "\t" + nbHRInfecte [i] + "\t";
				total += nbHRSain[i] + nbHRInfecte[i];
			}
		}

		if (possible.size() >= total){
			Individu newIndividu;
			
			for (int i = 0; i < resistant.length; i++){
				for (int j = 0; j< nbHRSain[i]; j++){
					caseCou = possible.remove(random.nextInt(possible.size())); 
					caseCou.setIndividu(newIndividu = new IndividuSain(caseCou, resistant[i]));
					population.add (newIndividu);
				}
				for (int j = 0; j< nbHRInfecte[i]; j++){
					caseCou = possible.remove(random.nextInt(possible.size())); 
					caseCou.setIndividu(newIndividu = new IndividuInfecte(caseCou, resistant[i]));
					population.add (newIndividu);
				}
			}
		}
	}
	
	public boolean continuer (){
		int nbInfecte=0;
		//System.out.println (idServeur + " " + nbCycle);
					
		int nbVivant = resistant.length + 1;
		if (normal.nbSain() == 0 && normal.nbInfectes() == 0)
			nbVivant --;
		nbInfecte += normal.nbInfectes();
		for (int i = 0; i < resistant.length; i++ ){
			if (resistant [i].nbSain() == 0 && resistant[i].nbInfectes()== 0)
				nbVivant--;
			nbInfecte += resistant[i].nbInfectes();
		}
		if (nbVivant < 2){
			return false;
		}
		
		if (nbInfecte == 0 && first){
			tousSain = "" + nbCycle + '\t' + normal.nbSain()+ '\t' +
					                         resistant[0].nbSain() + '\t' +
					                         resistant[1].nbSain() + '\t' +
					                         resistant[2].nbSain() + '\t';
			first = false;
		}
		return true;
	}
	
	public void lancer (){
		nbCycle = 0;
		tousSain ="-1\t-1\t-1\t-1\t-1\t";
		//int [][] casesGraphiques = new int [tailleGrille][tailleGrille];
		int index = 0;
		System.out.println ("detaille" + detaille);
		if (detaille){
			writeSpace();
			outProgress.println('%' + getAllNb());
			
			for (; StopTime[index] < timeMigration; index++){
				for (; nbCycle < StopTime[index]; nbCycle++){
					test();
					reproduce();	
					move();
					die();
					transmission();
					outProgress.println ();
					
					if (normal.nbInfectes() == 0 ){
						tousSain = "" + nbCycle + '\t' + normal.nbSain() + '\t' +
		                        resistant[0].nbSain() + '\t' +
		                        resistant[1].nbSain() + '\t' +
		                        resistant[2].nbSain() + '\t'; 
						index += StopTime.length;
						nbCycle += maxCycles();
					}
				}
				writeSpace();
				outProgress.println('%' + getAllNb());
			}
		}
		else{
			for (; StopTime[index] < timeMigration; index++){
				for (; nbCycle < StopTime[index]; nbCycle++){
					test();
					reproduce();	
					move();
					die();
					transmission();
					outProgress.println ();
					
					if (normal.nbInfectes() == 0 ){
						tousSain = "" + nbCycle + '\t' + normal.nbSain() + '\t' +
		                        resistant[0].nbSain() + '\t' +
		                        resistant[1].nbSain() + '\t' +
		                        resistant[2].nbSain() + '\t'; 
						index = StopTime.length-2;
						nbCycle += maxCycles();
						break;
					}
				}
			}
		}
			
		for (; index < StopTime.length && nbCycle < timeMigration; nbCycle++){
			test();
			reproduce();	
			move();
			die();
			transmission();
			outProgress.println ();
			
			if (normal.nbInfectes() == 0 ){
				tousSain = "" + nbCycle + '\t' + normal.nbSain() + '\t' +
                        resistant[0].nbSain() + '\t' +
                        resistant[1].nbSain() + '\t' +
                        resistant[2].nbSain() + '\t'; 
				nbCycle += maxCycles();
				index = StopTime.length-2;
				break;
			}
		}
		//writeSpace();
		//outProgress.println('%' + getAllNb());
		
		migration ();

		//writeSpace();
		//outProgress.println('%' + getAllNb());

		first = true;
		if (detaille){
			for (;index  < StopTime.length && continuer(); index++){
				for (; nbCycle < StopTime[index] && continuer(); nbCycle++){	
					test();
					reproduce();
					move();
					die();
					transmission();
					outProgress.println ();
				}
				writeSpace();
				outProgress.println('%' + getAllNb());
			}
		}
		else{
			for (;index  < StopTime.length && continuer(); index++){
				for (; nbCycle < StopTime[index] && continuer(); nbCycle++){	
					test();
					reproduce();
					move();
					die();
					transmission();
					outProgress.println ();
				}
			}
		}

		outProgress.println('#' + migr + tousSain + getAllNb());
	}
	
	
	public void writeSpace (){
		StringBuffer sb = new StringBuffer("&");
		
		sb.append (debutGrille.getIndividu().getId());
		for (Case caseCou = debutGrille.getVoisin(Case.EST); 
				caseCou != debutGrille; 
				caseCou = caseCou.getVoisin(Case.EST)){
			sb.append(' ');
			sb.append(caseCou.getIndividu().getId());
		}
		sb.append(' ');
		
		for (Case debutLigne = debutGrille.getVoisin(Case.SUD); debutLigne != debutGrille; debutLigne = debutLigne.getVoisin(Case.SUD)){
			sb.append (debutLigne.getIndividu().getId());
			//System.out.println ("debutligne " + debutLigne);
			for (Case caseCou = debutLigne.getVoisin(Case.EST); 
					caseCou != debutLigne; 
					caseCou = caseCou.getVoisin(Case.EST)){
				//System.out.println ("caseCou " + caseCou);
				sb.append(' ');
				sb.append(caseCou.getIndividu().getId());
			}
			sb.append(' ');
		}
		outProgress.println (sb.toString());
	}
	
	
	
	public String getAllNb (){
		StringBuffer res = new StringBuffer();
		res.append (normal.nbSain());
		res.append ('\t');
		res.append (normal.nbInfectes());
		for (int i = 0; i < resistant.length; i++){
			res.append ('\t');
			res.append (resistant[i].nbSain());
			res.append ('\t');
			res.append (resistant[i].nbInfectes());
		}
		res.append ('\t');
		res.append (population.nbElements());
		res.append ('\t');
		res.append (nbCycle);
		return res.toString();
	}
	
	/*
	public void lancerGlobal (){
		nbCycle = 0;
		tousSain ="-1\t-1\t-1\t-1\t-1\t";
		for (nbCycle = 0; nbCycle < timeMigration; nbCycle++){
			test();
			reproduce();
			die();
			updateTauxContagion();
			transmission();
			outProgress.println ();
			
			if (normal.nbInfectes() == 0 ){
				tousSain = "" + nbCycle + '\t' + normal.nbSain() + '\t' +
                        resistant[0].nbSain() + '\t' +
                        resistant[1].nbSain() + '\t' +
                        resistant[2].nbSain() + '\t'; 
				nbCycle += maxCycles();
			}
		}
		migration ();
		first = true;
		for (; nbCycle < maxCycles() && continuer(); nbCycle++){	
			test();
			reproduce();
			die();
			updateTauxContagion();
			transmission();
			outProgress.println ();
		}		
		
		outProgress.print(migr + tousSain + normal.nbSain() + "\t" + normal.nbInfectes());
		for (int i = 0; i < resistant.length; i++)
			outProgress.print ("\t" + resistant[i].nbSain() + "\t" + resistant[i].nbInfectes());
		outProgress.println ("\t" + population.nbElements() + "\t" + nbCycle);
		
	}*/

	private void test (){
		boolean test = false;
		for (int i = 0; i < population.nbElements(); i++){
			if (!population.get(i).valide()){
				System.out.println ("erreur individu " + i);
				test = true;
			}
		}
		if (test){
			System.out.println("normal " + normal.nbSain() + "\t" + normal.nbInfectes());
			for (int i = 0; i < resistant.length; i++){
				System.out.println("resitant" + i + "  " + resistant[i].nbSain() + "\t" + resistant[i].nbInfectes());
			}
		}
	}
	
	
	private Vector<Case> getCaseVide (){
		Vector<Case> vide = new Vector<Case> ();
		Case [] premiereLigne = new Case [tailleGrille];
		
		Case caseCou = debutGrille;
		for (int i = 0; i < tailleGrille; i++){
			premiereLigne[i] = caseCou;
			if (caseCou.estVide ())
				vide.add(caseCou);
			
			caseCou = caseCou.getVoisin(Case.EST);
		}

		for (int i = 0; i < tailleGrille; i++){
			caseCou = premiereLigne[i];
			for (int j = 1; j < tailleGrille; j++){
				caseCou = caseCou.getVoisin(Case.SUD);
				if (caseCou.estVide ())
					vide.add(caseCou);				
			}
		}
		return vide;
	}
	
	public Case [][] getGrille (){
		
		Case[][] grille = new Case [tailleGrille][tailleGrille];
		grille [0][0] = debutGrille;
		for (int i = 1; i < tailleGrille; i++)
			grille [i][0] = grille [i-1][0].getVoisin(Case.SUD);
		
		for (int i = 0; i < tailleGrille; i++){
			for (int j = 1; j < tailleGrille; j++){
				grille[i][j] = grille [i][j-1].getVoisin(Case.EST);				
			}
		}
		return grille;
	}
	
	
	public String getProgress (){
		try{
			return inProgress.readLine();
		}catch (IOException e){
			return "erreur";
		}
	}
	
	public void generer (int tailleGrille, int typeVoisinage, int tailleVoisinage){
		if (typeVoisinage == 4)
			debutGrille = Case4.generer(tailleGrille, tailleVoisinage);
		else 
			debutGrille = Case8.generer(tailleGrille, tailleVoisinage);
	}
	
	public int getNbCycles (){
		return nbCycle;
	}	

	public int getTotal (){
		return population.nbElements();
	}
	
	public int getNbHS (){
		return normal.nbSain();
	}

	public int getNbHI (){
		return normal.nbInfectes();
	}
	
	public int getNbHRS (int i){
		return resistant[i].nbSain();
	}

	public int getNbHRI (int i){
		return resistant[i].nbInfectes();
	}
	
	
	public class Transmission{
		
		
	}
	
	
}
