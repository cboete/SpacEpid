package Reseaux;

public class Local {

	
	public static void main (String [] args){
		
		new Fenetre().setVisible(true);
		Serveur.waitConnection();
		
	}
	
	
}
