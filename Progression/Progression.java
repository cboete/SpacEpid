package Progression;
 
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*; 
  
public class Progression extends JDialog implements Runnable{
	
	public static final long serialVersionUID = 0;
	
	
	JProgressBar progress;
	JFrame cadre;
	long debut;
	JTabbedPane pane;
	Table [] serveurs;
	private long moyenneExecutionJob = 0;
	private AffichageTemps ecoule, moyenne/*, restant*/;	
		
	public Progression (JFrame parent, String [] listeServeur){
		super (parent, true);
		setTitle("En cours de chargement");
		
		pane = new JTabbedPane();
		JPanel panneau = new JPanel();
		panneau.setOpaque(false);
		panneau.setLayout(new GridLayout(0, 1));
//		JLabel texte = new JLabel("Veuillez patienter pendant le chargement...");
		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setIndeterminate(false);
		panneau.add(progress); 
//		panneau.add("Center", texte);
		panneau.add(ecoule = new AffichageTemps("temps écoulé: "));
		panneau.add(moyenne = new AffichageTemps("temps execution moyen d'une tache: "));
//		panneau.add(restant = new AffichageTemps("temps restant: "));
		pane.addTab("General", panneau);
		
		serveurs = new Table [listeServeur.length];
		for (int i = 0; i < serveurs.length; i++){
			serveurs[i] = new Table ();
			pane.addTab(listeServeur[i], serveurs[i]);
		}
		
		setContentPane(pane);
		new Thread (this).start ();
		//setSize(275,85);
		//setResizable(false);
	}

	public void addJob (Job j, String serveur){
		for (int i = 0;i < serveurs.length; i++){
			if (serveur.equals(pane.getTitleAt(i+1)))
				serveurs[i].addJob(j);
		}		
		repaint();
		//pack();
	}

	public void removeJob (Job j, String serveur){
		for (int i = 0;i < serveurs.length; i++){
			if (serveur.equals(pane.getTitleAt(i+1)))
				serveurs[i].removeJob(j);
		}
		progress.setValue(progress.getValue() + 1 );
		progress.setString("" + progress.getValue() + "/" + progress.getMaximum());
		if (fini()){
			setBackground(Color.red);
			for (int i = 0; i < serveurs.length; i++)
				serveurs[i].setBackground(Color.red);
		}
		moyenneExecutionJob = moyenneExecutionJob + (j.getTime() - moyenneExecutionJob)/progress.getValue();
		repaint();
	}
	
	@Override
	public void setBackground (Color c){
		super.setBackground(c);
		if (ecoule!= null)
			ecoule.setBackground(c);
		if (moyenne != null)
			moyenne.setBackground(c);
		if (serveurs != null){
			for (int i = 0; i< serveurs.length; i++)
				serveurs[i].setBackground(c);
		}
	}
	
	@Override
	public void setVisible (boolean b){
		if (b){
			debut = System.currentTimeMillis();
			pack ();
		}
		super.setVisible(b);
	}
	
	public void setMax (int max){
		progress.setMaximum(max);
	}
	
	public void setIndeterminate (boolean b){
		progress.setIndeterminate(b);
	}

	public void run (){
		for (;progress.getValue()< progress.getMaximum();){
			try{
				Thread.sleep(1000);
				ecoule.update(System.currentTimeMillis() - debut);
				moyenne.update(moyenneExecutionJob);
			}catch(InterruptedException e){}	
		}	
	}
	
	public boolean fini (){
		return progress.getValue() == progress.getMaximum();
	}

	public class Table extends JScrollPane{
		
		JPanel content;
		
		public Table (){
			super (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			setViewportView(content = new JPanel());
			content.setLayout(new GridLayout(0, 1));
		}
		
		public void addJob (Job j){
			content.add(j);
		}

		public void removeJob (Job j){
			content.remove(j);
		}	
	}
	
	
	public class AffichageTemps extends JPanel {
		JLabel temps;
		
		public AffichageTemps (String nom){
			add (new JLabel (nom));
			add (temps = new JLabel ("0 s"));
		}
		
		public void update (long time){
			String valeur = "";
			time = time / 1000;
			valeur = (time % 60) + "s";
			time = time / 60;
			if (time > 0){
				valeur = (time % 60) + "min " + valeur;
				time = time / 60;
				if (time > 0){
					valeur = (time % 24) + "h " + valeur;
					time = time / 24;
					if (time > 0)
						valeur = time + "j " + valeur;
				}
			}
			temps.setText(valeur);
		}
	}
	
} 
  