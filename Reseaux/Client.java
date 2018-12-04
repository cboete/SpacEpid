package Reseaux;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import Cinematique.ListeParametre;
import Cinematique.Parametre;
import Cinematique.Simulation;
import Progression.Job;
import Progression.Progression;

public class Client extends Thread{
	//*
	//*/public static final int nbCoeurs = 3;
	
	private static int nbCoeurs = 2;	
	private static Vector<Client> clients;
	private static Progression prog;
	private static int indiceSimCou=0, fini=0;
	private static String [] listeServeur;
	private static PrintStream derniereLigne;
	private static boolean detaille;
	private static File dossier;
	
	
	public static void init (int nbCoeurs, boolean  detaille, String [] serveurs, File param, File out, Progression prog) throws IOException{
		System.out.println ("debut init");
		listeServeur = serveurs;
		
		Parametre.generer(param);
		//System.out.println ("taille " + Parametre.nbPossibilite());
		clients = new Vector<Client>(Parametre.nbPossibilite());
		
		int id = 0;
		
		for (ListeParametre parametres = Parametre.nextTous (); parametres != null; parametres = Parametre.nextTous ()){
			if (parametres.isValide())
				clients.add(new Client(parametres, id++));
		}	
		out.getParentFile().mkdirs();
		derniereLigne = new PrintStream(out);
		derniereLigne.println (ListeParametre.getEntete() + "\t H1sainMigr\t H1infecteMigr \t" +
		                "R1sainMigr\t R1infecteMigr \t" +"R2sainMigr\t R2infecteMigr \t" +"R3sainMigr\t R3infecteMigr \t" +
						"cycleTousSain\t tousSainH1 \t tousSainR1 \t tousSainR2\t tousSainR3\t" +
						"nbNormalSain\t nbNormalInfectes\t nbR1Sain \t nbR1Infectes\t nbR2Sain" +
						" \t nbR2Infectes\t nbR3Sain \t nbR3Infectes \t total \t nbCycles");

		Client.detaille = detaille;
		Client.prog = prog;
		Client.nbCoeurs = nbCoeurs;
		dossier = new File (out.getParentFile(), out.getName().substring(0, out.getName().lastIndexOf('.')));
		dossier.mkdirs();
		prog.setMax(clients.size());
		System.out.println ("fin init");
	}

	private static void lancer (String ip) throws IOException{
		for (int i = 0;i< nbCoeurs && indiceSimCou < clients.size(); i++){
			System.out.println (ip + "lancer indice Sim Cou " + indiceSimCou);
			clients.elementAt(indiceSimCou++).start (InetAddress.getByName(ip));		
		}
	}

	public static void lancer () throws IOException{
		indiceSimCou = 0;

		for (int i = 0; i < listeServeur.length; i++){
			System.out.println (i + " " + listeServeur[i]);
			lancer (listeServeur[i]);
			System.out.println ("fin " + i);
		}
	}
	
	public static synchronized void startNextSequence (InetAddress serveur, Job job){
		if (job != null)
			prog.removeJob (job, serveur.getHostAddress());
		System.out.println (indiceSimCou + " " + clients.size());
		//System.out.println (fini + " " + clients.size());
		//prog.setValue (++fini);
		if (indiceSimCou<clients.size()){
			System.out.println (indiceSimCou);			
			clients.elementAt(indiceSimCou++).start (serveur);
		}
		if (prog.fini())
			derniereLigne.close();
/*		else if (fini == clients.size())
			prog.setVisible(false);*/
	}
	
	public static synchronized void writeLine (String ligne){
		//System.out.println (ligne);
		derniereLigne.println (ligne);
//		if (prog.)
		/*
		 * try {
			PrintStream ps = new PrintStream (ligne);
			ps.println (ligne);
			ps.close ();
		}catch (IOException e){}
		*/
	}
	
	private InetAddress serveur;
	private ListeParametre parametres;
	private Job job;
	private int id;
	
	public Client (ListeParametre parametres, int id){
		this.parametres = parametres;
		this.id = id;
	}
	
	public void start (InetAddress serveur){
		this.serveur = serveur;
		start ();
	}
	
	public void run (){

		try{
			//System.out.println ("test1");
			Socket sock = new Socket (serveur, 2190);
			//System.out.println ("test2");
			
			PrintStream ps = new PrintStream(sock.getOutputStream());
		/*	for (int i = 0; i < parametres.length; i++)
				ps.println (parametres[i]);
			*/

			parametres.envoyer(ps);
			if (detaille)
				ps.println ("d");
			else
				ps.println ("n");
			
			prog.addJob (job = new Job(parametres.resume(), (int)parametres.stopTime ()), serveur.getHostAddress());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String resultat;
			
			
			if (Client.detaille){
				
				PrintStream space = new PrintStream (new File (dossier, id + "_space.txt"));
				PrintStream detail = new PrintStream (new File (dossier, id + "_detail.txt"));
				
				space.println (ListeParametre.getEntete());
				space.println (parametres + "\n");

				detail.println (ListeParametre.getEntete());
				detail.println (parametres + "\n");
				detail.println ("nbNormalSain\t nbNormalInfectes\t nbR1Sain \t nbR1Infectes\t nbR2Sain" +
						" \t nbR2Infectes\t nbR3Sain \t nbR3Infectes \t total \t nbCycles");
				
				for (int value = 0; !(resultat = br.readLine()).startsWith("#"); /*value = Integer.parseInt(br.readLine())*/){
					//System.out.println (value);
					if (resultat.isEmpty())
						job.setDone(value++); // comptage du motif i fini
					else if (resultat.charAt(0) == '&')
						space.println(resultat.substring(1));
					else if (resultat.charAt(0) == '%')
						detail.println(resultat.substring(1));
					else 
						System.out.println (resultat);
				}
				space.close();
				detail.close();
			}
			else {
				for (int value = 0; !(resultat = br.readLine()).startsWith("#"); value ++/*value = Integer.parseInt(br.readLine())*/){
					//System.out.println (value);
					job.setDone(value); // comptage du motif i fini
				}				
			}
			
			writeLine(parametres + resultat.substring(1) + "\t" + job.getTime());
			br.close();
			ps.close();
			sock.close();
			
			System.out.println ("next");
			Client.startNextSequence(serveur, job);
		}catch (IOException e){System.out.println ("erreur connection");e.printStackTrace();}

	}	
	
	
	private static void delete (File f){
		if (f.isDirectory()){
			File fils [] = f.listFiles();
			for (int i =0; i < fils.length; i++)
				delete(f);
			f.delete();
		}else
			f.delete ();
	}
	
}
