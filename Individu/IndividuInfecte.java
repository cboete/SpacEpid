package Individu;

import Case.Case;
import Stockage.Conteneur;


public class IndividuInfecte extends Individu{

	protected Individu newSain (Case c, ParametreHost ph){
		return new IndividuSain (c, ph);
	}	

	protected Individu newSain (Case c, ParametreHost ph, int ep){
		return new IndividuSain (c, ph, ep);
	}	

	public IndividuInfecte (Case location, ParametreHost p){
		super(ParametreHost.INFECTE, location, p);
	}
	
	public IndividuInfecte (Case location, ParametreHost p, int etat){
		super(ParametreHost.INFECTE, location, p, etat);
	}
	
	
	public void newState(Conteneur<Individu> c) {		
		if ((lastTirage = random.nextFloat()) <  get(ParametreHost.GUERISSON)){
			Individu newOne;
			location.setIndividu(newOne = newSain (location, parametre,INFECTE));
			c.switchElement(this, newOne);
			parametre.nbIndividu[type]--;
		}
	}
	
	public int getState (){
		return INFECTE;
	}
	
	public boolean estInfecte (){
		return true;
	}
	
	public int getId (){
		return parametre.getId()+1;
	}
}
