package Stockage;

import java.io.PrintStream;
import java.util.Random;

public class Conteneur <E extends Rangeable>{

	private Object [] liste;
	private int nbElements;
	private int [] random;
	private int index;
	private Random rand;
	
	public Conteneur(int tailleMax) {
		liste = new Object [tailleMax];
		nbElements = 0;
		rand = new Random (System.currentTimeMillis());
		// TODO Auto-generated constructor stub
	}

	public void add (E i){
		i.setPosition(nbElements);
		liste [nbElements++] = i;
	}
	
	public E init (){
		random = new int [nbElements];
		melanger ();
		index = 1;
		return get(random[0]);
	}
	
	public E next (){
		if (index < random.length)
			return get (random [index++]);	
		return null;
	}
	
	public void switchElement (E oldOne, E newOne){
		liste [oldOne.getPosition()] = newOne;
		newOne.setPosition(oldOne.getPosition());
		oldOne.setPosition(-2);
	}
	
	@SuppressWarnings("unchecked")	
	public void remove (E i){
		nbElements--;
		liste [i.getPosition()] = liste [nbElements]; 
		((E)liste [nbElements]).setPosition(i.getPosition ());
		i.setPosition(-1);
	}
	
	@SuppressWarnings("unchecked")	
	public E get(int index){
		return (E)liste[index]; 
	}
	
	public int nbElements(){
		return nbElements;
	}
	
	private void melanger (){
		int j;
		for (int i = 0; i < random.length; i++){
			random [i] = i;
		}
			
		int temp;
		for (int i = random.length - 1; i >1; i--){
			j = rand.nextInt(i+1);
			temp = random[j];
			random [j] = random[i];
			random [i] = temp;
		}
	}
	
	public void afficher (PrintStream ps){
		
		for (int i = 0; i< nbElements; i++){
			ps.println(i + " " + liste [i] + " " + ((Rangeable)liste[i]).getPosition() + " ");
		}
		ps.println();
		
	}
	
	public static void main (String [] args){
		int [][] resultat = new int [10][10];
		Conteneur<Rangeable> T = new Conteneur<Rangeable>(10);
		
		T.random = new int [10];
		for (int i = 0; i < 10;i++){
			T.melanger();
			for (int j = 0; j < 10; j++){
				resultat [i][j] = T.random[j];
			}
		}
			
		for (int i = 0; i < 10;i++){
			for (int j = 0; j < 10; j++){
				System.out.print(resultat [i][j] + " ");
			}
			System.out.println ();
		}
	}
}
