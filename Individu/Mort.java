package Individu;

import Case.Case;
import Stockage.Conteneur;

public class Mort extends Individu {

	protected Individu newSain (Case c, ParametreHost ph){
		return null;
	}	

	protected Individu newSain (Case c, ParametreHost ph, int ep){
		return null;
	}	
	
	public Mort (){}
	
	@Override
	public void newState(Conteneur<Individu> c) {}
	
	@Override
	public void move() {}

	@Override
	public void reproduce(Conteneur<Individu> c) {}

	@Override
	public boolean die(Conteneur<Individu> c) {
		return false;
	}

	@Override
	public boolean estInfecte() {
		return false;
	}
	
	@Override
	public boolean valide (){
		return false;
	}
	
	@Override
	public boolean estMort() {
		return true;
	}
	
	public int getState (){
		return MORT;
	}
	
	public int getId (){
		return 0;
	}

}
