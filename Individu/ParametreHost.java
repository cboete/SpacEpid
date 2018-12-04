package Individu;

public class ParametreHost {


	public static final int MORT = 0, NORMAL = 1, R1 = 3, R2 = 5, R3 = 7;
	public static final int TAUX_NATALITE = 0, 
						TAUX_MORTALITE = 1,
						TAUX_DEPLACEMENT = 2,
						CONTAGION = 3,
						GUERISSON = 4,
			            SUCCES_PARASITE = 3,
			            RESISTANCE1 = 4,
			            SAIN = 0, INFECTE = 1;

	static public int somme (int type, ParametreHost... params){
		int resultat = 0;
		for (ParametreHost param:params)
			resultat += param.nbIndividu [type];
		return resultat;
	}

	static public int somme (int type, ParametreHost p1, ParametreHost [] t1){
		int resultat = p1.nbIndividu [type];
		for (int i = 0; i < t1.length; i++)
			resultat += t1[i].nbIndividu[type];
		return resultat;
	}
	
	private int id;
	private String nom;
	private double valeurs [][] = new double [2][5];
	
//	public double tauxNatalite;
//	public double tauxMortalite;
//	public double tauxDeplacement;
//	public double tauxChangementEtat;
//	public double succesParasite;
	public int  nbIndividu [];
	
	public ParametreHost (int id){
		this.id = id;
		nbIndividu = new int [2];
		nbIndividu [0] = 0;
		nbIndividu [1] = 0;
	}
	
	/*
	public void setNom (String nom){
		this.nom = nom;
	}
	*/
	
	private void set (int type, int idParam, double valeur){
		valeurs [type][idParam] = valeur;
	}
	
	public int nbSain(){
		return nbIndividu[SAIN];
	}

	public int nbInfectes(){
		return nbIndividu[INFECTE];
	}
	
	
	public void set (String nom, double nataliteSain, double mortaliteSain, double deplacementSain,
			                double succesParasite, double resistance1, 
			                double nataliteInfecte, double mortaliteInfecte, double deplacementInfecte,
			                double contagion, double guerisson) {
		set (SAIN, TAUX_NATALITE, nataliteSain);
		set (SAIN, TAUX_MORTALITE, mortaliteSain);
		set (SAIN, TAUX_DEPLACEMENT, deplacementSain);
		set (SAIN, SUCCES_PARASITE, succesParasite);
		set (SAIN, RESISTANCE1, resistance1);

		set (INFECTE, TAUX_NATALITE, nataliteInfecte);
		set (INFECTE, TAUX_MORTALITE, mortaliteInfecte);
		set (INFECTE, TAUX_DEPLACEMENT, deplacementInfecte);
		set (INFECTE, CONTAGION, contagion);
		set (INFECTE, GUERISSON, guerisson);
		//System.out.println (nom + " " + resistance1);
		this.nom = nom;
	}
	/*
	public boolean test (){
		return nom.equals("Nb_HR1S=");
	}
	*/
	public double get (int type, int idParam){
		return valeurs [type][idParam];
	}
	
	public int getId (){
		return id;
	}
	
	public void add (Individu i){}
	
	public String toString (){
		return "_" + nom + "_" + nbIndividu;
	}
}
