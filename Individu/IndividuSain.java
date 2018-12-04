package Individu;

import Case.Case;
import Stockage.Conteneur;

public class IndividuSain extends Individu {

	protected Individu newSain (Case c, ParametreHost ph){
		return new IndividuSain (c, ph);
	}	

	protected Individu newSain (Case c, ParametreHost ph, int ep){
		return new IndividuSain (c, ph, ep);
	}	
	
	protected Individu newInfecte (Case c, ParametreHost ph){
		return new IndividuInfecte (c, ph);
	}	

	protected Individu newInfecte (Case c, ParametreHost ph, int ep){
		return new IndividuInfecte (c, ph, ep);
	}	
	
	public IndividuSain (Case location, ParametreHost p){
		super(ParametreHost.SAIN, location, p);
	}

	public IndividuSain (Case location, ParametreHost p, int etat){
		super(ParametreHost.SAIN, location, p, etat);
	}
	
	public double getProbaInfection (){
		double proba = 1;
		for (int i = 0; i < location.getNbVoisins(); i++){
			if (location.getVoisin(i).getScreenShot().estInfecte ())
				proba *= 1-(location.getVoisin(i).getScreenShot().get(ParametreHost.CONTAGION))*(1-get(ParametreHost.RESISTANCE1));
		}
		return 1-proba;
	}
	
	public void newState(Conteneur<Individu> c) {
		if ((lastTirage = random.nextFloat()) < get(ParametreHost.SUCCES_PARASITE) *  
				                                getProbaInfection()){
			Individu newOne;
			location.setIndividu(newOne = newInfecte(location, parametre, SAIN));
			c.switchElement(this, newOne);
			parametre.nbIndividu[type]--;
		}
	}
	
	public int getState (){
		return SAIN;
	}
	
	public boolean estInfecte (){
		return false;
	}
	
	public int getId (){
		return parametre.getId();
	}
	
}
