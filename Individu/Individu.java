package Individu;

import java.util.Random;
import java.util.Vector;



import Case.Case;
import Stockage.Conteneur;
import Stockage.Rangeable;


public abstract class Individu extends Rangeable {

	protected static Random random = new Random(System.currentTimeMillis());
	protected static final int MORT = 0, 
							   INFECTE = 1,
							   SAIN = 2;
	
	//private String state; generer par la classe à laquelle il appartient
	protected Case location; // location sur la grille
	protected Vector<Case> destinationPossible;
	protected ParametreHost parametre;
	protected int etatPrecedent;
	protected Case localisationPrecedente;
	protected float lastTirage;
	protected int type;
	
	protected Individu (){}
	
	protected Individu (int type, Case location, ParametreHost p){
		this.location = location;
		parametre = p;
		parametre.nbIndividu[this.type = type]++;
	}

	protected Individu (int type, Case location, ParametreHost p, int etat){
		this (type, location, p);
		etatPrecedent = etat;
	}
	
	abstract public void newState (Conteneur<Individu> c);
	abstract public boolean estInfecte ();
	abstract public int getState ();
	abstract public int getId ();
	abstract protected Individu newSain (Case c, ParametreHost ph);
	abstract protected Individu newSain (Case c, ParametreHost ph, int ep);
	
	public boolean valide (){
		return (location.getIndividu() == this);	
	}
	
	public void move() {
		if ((lastTirage = random.nextFloat()) < get(ParametreHost.TAUX_DEPLACEMENT)){
			Case newPos = location.getRandomCase(random);
			if (newPos.estVide ()){
				location.setIndividu(new Mort ());
				location = newPos; 
				newPos.setIndividu(this);
			}
		}
	}

	public void reproduce(Conteneur<Individu> c) {
		localisationPrecedente = location;
		etatPrecedent = getState ();
		
		if ((lastTirage = random.nextFloat()) < get(ParametreHost.TAUX_NATALITE)){
			Case newPos = location.getRandomCase(random);
			if (newPos.estVide()){
				Individu fils;
				newPos.setIndividu(fils = newSain (newPos, parametre, MORT));
				fils.localisationPrecedente = location;
				c.add(fils);
			}
		}
	}
	
	public boolean die(Conteneur<Individu> c) {
		// TODO Auto-generated method stub
		boolean resultat;
		if (resultat = (lastTirage = random.nextFloat()) < get(ParametreHost.TAUX_MORTALITE)){
			location.setIndividu(new Mort ());
			parametre.nbIndividu[type]--;
			c.remove(this);
		}
		else{
			location.screenShot();
		}
		return resultat;
	}
	/*
	public int getType (){
		return parametre.getId();
	}
	*/
	public double get (int idParam){
		return parametre.get(type, idParam);
	}
	
	public boolean estMort (){
		return false;
	}

	public String etatPrecedent (){
		return "" + etatPrecedent + " " + localisationPrecedente + " " + lastTirage;
		
	}
	
}
