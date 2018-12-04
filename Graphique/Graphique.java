package Graphique;
import javax.swing.*;


import Case.Case;
import Cinematique.ListeParametre;
import Cinematique.Parametre;
import Cinematique.Simulation;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Vector;


public class Graphique extends JFrame{

	/**
	 * 
	
	private JParametre tailleGrille, typeVoisinage, tailleVoisinage, StopTime,
	           nbH1Sain, nbH1Infecte, timeMigration,
	           beta, gamma, rH1, mu, eff1, eff2, cR1, cR2,
	           alphaM, alphaN, tauxR1, tauxR2, tauxR3, succesParasite;
	 */
	private static String [] libelle = {"reproduction", "deplacement", "mort", "transmission"};
	
	private JParametre [] parametres;
	
	private JAffichage nbCycle, total, nbHS, nbHI, nbHR1S, nbHR1I, 
	                            nbHR2S, nbHR2I, nbHR3S, nbHR3I;
	
	private JButtonColor vide, hI, hS, hR1I, hR1S, hR2I, hR2S, hR3I, hR3S;
	private JPanel grille;

	private JButton init, playPause, oneStep;
	private Simulation sim;

	private BufferedReader br;	
	private PrintStream pw;
	private boolean first = true;
	
	public Graphique (){
		
		setTitle("Gama");
		setExtendedState(MAXIMIZED_BOTH);
		
		JPanel left = new JPanel ();
		left.setLayout(new GridLayout(0,1));
		parametres = new ListeParametre().getGraphique();

		for (int i = 0; i < parametres.length;i++){
			left.add (parametres[i]);
		}

		left.add (vide = new JButtonColor("vide", Color.white));
		left.add (hI = new JButtonColor("h infecte", Color.red));
		left.add (hS = new JButtonColor("h sain", Color.green));
		left.add (hR1I = new JButtonColor("h R1 infecte", Color.orange));
		left.add (hR1S = new JButtonColor("h R1 sain", Color.yellow));
		left.add (hR2I = new JButtonColor("h R2 infecte", Color.orange));
		left.add (hR2S = new JButtonColor("h R2 sain", Color.yellow));
		left.add (hR3I = new JButtonColor("h R3 infecte", Color.orange));
		left.add (hR3S = new JButtonColor("h R3 sain", Color.yellow));
		left.add (init = new JButton("charger parametre"));
		
		init.addActionListener(new Initialisation ());
		
		JTabbedPane tab = new JTabbedPane();
		tab.addTab("parametre", left);
		
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		JPanel haut = new JPanel();
		
		grille=new JPanel ();
		haut.add(playPause = new JButton("play"));
		haut.add(oneStep = new JButton("one step (Reproduce)"));
		pane.add(haut, BorderLayout.NORTH);
		pane.add(grille=new JPanel (), BorderLayout.CENTER);
		JPanel right =new JPanel();
		right.setLayout(new GridLayout(0,1));
		right.add (nbCycle = new JAffichage("nb Cycle"));
		right.add (total = new JAffichage("total"));		
		right.add (nbHS = new JAffichage("nb H sain"));
		right.add (nbHI = new JAffichage("nb H infec"));
		right.add (nbHR1S = new JAffichage("nb H R1 sain"));
		right.add (nbHR1I = new JAffichage("nb H R1 infec"));
		right.add (nbHR2S = new JAffichage("nb H R2 sain"));
		right.add (nbHR2I = new JAffichage("nb H R2 infec"));
		right.add (nbHR3S = new JAffichage("nb H R3 sain"));
		right.add (nbHR3I = new JAffichage("nb H R3 infec"));
		
		pane.add (right,BorderLayout.WEST);
		
		tab.addTab("grille", pane);
		
		playPause.addActionListener(new Play (true));
		oneStep.addActionListener(new Play (false));
		
		setContentPane(tab);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public Graphique me (){
		return this;
	}
	
	public class Initialisation extends Thread implements ActionListener{

		public Initialisation (){}
		
		public void actionPerformed (ActionEvent ae){
			grille.removeAll();
			AffichageCase.setCouleur(vide.getCouleur(),
	                 hS.getCouleur(),
	                 hI.getCouleur(),
	                 hR1S.getCouleur(),
	                 hR1I.getCouleur(),
	                 hR2S.getCouleur(),
	                 hR2I.getCouleur(),
	                 hR3S.getCouleur(),
	                 hR3I.getCouleur());
			try{
				PipedOutputStream pos = new PipedOutputStream();
				PipedInputStream pis = new PipedInputStream(pos);
				pw = new PrintStream(pos);
				br = new BufferedReader(new InputStreamReader(pis));
				start ();
				sim = new Simulation (br/*, pw*/);
				System.out.println (br.readLine());
			}catch (IOException e){}
			grille.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			
			Case [][] allCases = sim.getGrille();

			for (int i = 0; i < allCases.length; i++){
				for (int j = 0; j < allCases.length; j++){
					gbc.gridwidth = 1;
					if (j == allCases.length -1)
						gbc.gridwidth = GridBagConstraints.REMAINDER;
					grille.add(new AffichageCase(allCases[i][j], new CaseAction(allCases[i][j])), gbc);
					//grille.add(new JButton());
				}
			}
			playPause.setText("play");
			updateGrille();
		}
		
		public void run (){
			Vector<Parametre> param = new Vector<Parametre> ();
			for (int i = 0; i < parametres.length; i++){
				param.add(new Parametre(parametres[i].toString()));
				param.lastElement().next();
			}
			
			ListeParametre liste = new ListeParametre(param);
			liste.envoyer(pw);
/*
			for (int i = 0; i < 10; i++)
				pw.println ("");
			pw.println ("fin");*/
		}
	}
	
	public class CaseAction extends Thread implements ActionListener{
		
		Case objet;
		
		public CaseAction (Case o){
			objet = o;
		}
		
		public void run (){
			AffichageCase ac;
			for (int i = 0; i < grille.getComponentCount();i++){
				ac = (AffichageCase) grille.getComponent(i);
				if (ac.isVoisin(objet)){
					ac.setBackground(Color.black);
					ac.updateUI();
					ac.repaint();
					ac.setText("jkjlfsdf");
					System.out.println("test " + i);
				}
			}
			JOptionPane.showMessageDialog(me (), objet.getIndividu().etatPrecedent());
			updateGrille();
		}
		
		
		public void actionPerformed (ActionEvent e){
			start ();
		}
		
		
	}
	
	
	public class Play extends Thread implements ActionListener{
		private boolean isPlayPause;
		
		public Play (boolean isPlayPause){
			this.isPlayPause = isPlayPause;
		}
		
		public void actionPerformed (ActionEvent ae){
			if (first){
				sim.pause = !isPlayPause;
				start();
				new MiseAJour().start();
				first = false;
			}
			else{
				if (isPlayPause){
					if (sim.pause){
						sim.pause = false;
						synchronized (sim){
							sim.notify();
						}
					}
					else{
						sim.pause = true;
					}
				}
				else{
					sim.pause = true;
					synchronized (sim){
						sim.notify();
					}
				}
			}
			if (sim.pause)
				playPause.setText("play");
			else
				playPause.setText("pause");
			
			oneStep.setText(libelle[sim.type]);
		}
		
		public void run (){
			sim.lancer ();
		}
	}
	
	public void updateGrille (){
		for (int i = 0; i < grille.getComponentCount();i++){
			((AffichageCase)grille.getComponent(i)).update ();
		}
		nbCycle.setValeur("" +sim.getNbCycles());
		total.setValeur("" + sim.getTotal());
		nbHS.setValeur("" + sim.getNbHS());
		nbHI.setValeur("" + sim.getNbHI());
		nbHR1S.setValeur("" + sim.getNbHRS(0));
		nbHR1I.setValeur("" + sim.getNbHRI(0));
		nbHR2S.setValeur("" + sim.getNbHRS(1));
		nbHR2I.setValeur("" + sim.getNbHRI(1));
		nbHR3S.setValeur("" + sim.getNbHRS(2));
		nbHR3I.setValeur("" + sim.getNbHRI(2));
	}	
	
	public class MiseAJour extends Thread {
		public void run (){
/*
			try{
				System.out.println ("test br " + (br==null));
				String ligne;/*
				for (ligne = br.readLine(); ligne != null && ligne.isEmpty(); ligne = br.readLine()){
					updateGrille();
					System.out.println ("next");
				}
				System.out.println ("ligne " + ligne + ";");
			}catch (IOException e){}*/
			for (; ; ){
				updateGrille();
			}
		}
	}

	
	public class Lecteur extends Thread{
		public void run (){
			String ligne;
			for (ligne = sim.getProgress(); ligne != null && ligne.isEmpty(); ligne = sim.getProgress());			
		}
	}
	
	public static void main (String [] args){
		new Graphique().setVisible(true);
	}

}
