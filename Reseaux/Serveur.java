package Reseaux;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Cinematique.Simulation;


public class Serveur extends Thread {
	
	public static void waitConnection  (){
		System.out.println ("version du 07 08 2014");
		
		try{
			ServerSocket serv = new ServerSocket (2190);
			for (int i=0; ;i++){
				new Serveur (serv.accept (),i);
			}
		}catch(IOException e){
			e.printStackTrace();
			//waitConnection();
		}
	}
	
	private Socket sock;
	private int id;
	
	public Serveur (Socket s, int id){
		sock = s;
		this.id = id;
		start();
	}
	
	public void run (){
		Simulation sim = null;
		try{
			sim = new Simulation(sock); 
			sim.lancer ();			
			sock.close();
		}catch (IOException e){
			sim.sendErreur();
			e.printStackTrace();
		}
	}
	
	public static void main (String [] args){
		waitConnection();
	}
	
}
