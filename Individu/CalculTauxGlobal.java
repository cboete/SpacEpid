package Individu;

public class CalculTauxGlobal {

	private double tauxContagion;
	private int nbInfecte;
	
	public CalculTauxGlobal() {
		// TODO Auto-generated constructor stub
	}
	
	public void reset (){
		tauxContagion = 1;
		nbInfecte = 0;
	}
	
	public void add (double contagion){
		tauxContagion *= contagion;
		nbInfecte++;
	}
	
	public double getValue (){
		return tauxContagion;
	}
	
	public int nbInfectes (){
		return nbInfecte;
	}

}
