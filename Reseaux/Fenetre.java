package Reseaux;
import javax.swing.*;

import Progression.Progression;
import Utilitaire.MonFileChooser;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class Fenetre extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField nbCoeurs;
	JTextField serveurs;
	JCheckBox detaille;
	//BoutonFichier out, param, gama, sh;
	BoutonFichier out, param;
	
	JProgressBar progress; 
	JFrame cadre;
	long debut;
	
	String Fichiers [];
	
	public Fenetre (){
		Toolkit k = Toolkit.getDefaultToolkit();
		Dimension tailleEcran = k.getScreenSize();
		int largeurEcran = tailleEcran.width;
		int hauteurEcran = tailleEcran.height;
		
		setTitle("GAMA");
		setSize((int)(largeurEcran/1.5), hauteurEcran/4);
		setLocation(largeurEcran*3/16, hauteurEcran*3/8);

		JPanel pane = new JPanel ();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		pane.add (new JLabel ("nb coeur:"), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add (nbCoeurs = new JTextField (40), gbc);
		gbc.gridwidth = 1;

		pane.add (new JLabel ("Serveurs:"), gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add (serveurs = new JTextField (40), gbc);
		//gbc.gridwidth = 1;
		
		pane.add(detaille = new JCheckBox("Detaille"),gbc);

		pane.add (param = new BoutonFichier("Parametre", MonFileChooser.FILES_ONLY), gbc);
		pane.add (out = new BoutonFichier("dossier resultat", MonFileChooser.FILES_ONLY), gbc);
//		pane.add (sh = new BoutonFichier("fichier sh", MonFileChooser.FILES_ONLY), gbc);
		
		if (isLocale ()){
			nbCoeurs.setText("32");
			serveurs.setText("10.0.0.1;10.0.0.2;10.0.0.3");
			MonFileChooser.setDirectory(new File("."));
		}else{
			nbCoeurs.setText("1");
			serveurs.setText("127.0.0.1");
			//param.setSelection (new File ("Test.txt"));		
			//out.setSelection (new File ("resultat.txt"));		
//			sh.setSelection (new File ("/home/morgan/workspace/GamaServeur/Gama/headless/gama-headless.sh"));
			MonFileChooser.setDirectory(new File("."));
		}
		
		JButton lancer = new JButton("Lancer");
		lancer.addActionListener(this);
		pane.add (lancer, gbc);
		setContentPane(pane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
	
	public void actionPerformed(ActionEvent event){
		
		//Maillon.minSize = Integer.parseInt(min.getText());
		//Maillon.maxSize = Integer.parseInt(max.getText());
		//Maillon.generer ();
		System.out.println ("lancer");
		if (param.selection != null && 
			out.selection!=null){
			String [] listeServeurs = serveurs.getText().split(";"); 
			Progression prog = new Progression (this, listeServeurs);
			try{	
				System.out.println ("anavnt init");
				Client.init(Integer.parseInt(nbCoeurs.getText ()), detaille.isSelected(), listeServeurs,
						    param.selection, out.selection, prog);
				Client.lancer ();
				//prog.pack ();
				prog.setVisible(true);
			}catch(IOException e){
				JOptionPane.showMessageDialog(this, "Erreur de lecture ");
				e.printStackTrace();
			}
			
		}else{
			JOptionPane.showMessageDialog(this, "vous devez selectionner un fichier séquence et un dossier de sortie");
		}
		
		/*
		MonFileChooser mfc = new MonFileChooser("Fasta", "fa", "fasta", "FASTA", "FA", "txt", "TXT");
		if (mfc.showOpenDialog(this) == MonFileChooser.APPROVE_OPTION){
			String [] listeServeurs = serveurs.getText().split(";"); 
			Progression prog = new Progression (this, listeServeurs);
			File in = mfc.getSelectedFile();
			try{
				
				Client.init(Integer.parseInt(min.getText()), 
						    Integer.parseInt(max.getText()), 
						    Integer.parseInt(nbReenc.getText ()),
						    Integer.parseInt(nbCoeurs.getText ()),
						    listeServeurs,
						    in, prog);
				Client.lancer ();
				prog.setVisible(true);
			}catch(IOException e){
				JOptionPane.showMessageDialog(this, "Erreur de lecture ");
				e.printStackTrace();
			}
		}*/
	}

	public static void main (String [] args){
		new Fenetre().setVisible(true);
		//double test = 0.1;
		//System.out.println (test);
	}

	public class BoutonFichier extends JPanel implements ActionListener{
		
		JButton parcourir;
		JLabel text;
		File selection;
		int modeSelection;
		
		public BoutonFichier (String nom, int modeSelection){
			this.modeSelection = modeSelection;
			add (new JLabel (nom));
			add (parcourir = new JButton("Parcourir"));
			add (text = new JLabel (""));
			parcourir.addActionListener(this);
		}
		
		public void actionPerformed (ActionEvent ae){
			MonFileChooser mfc = new MonFileChooser("GAMA + txt + sh", "txt", "gaml", "sh");
			mfc.setFileSelectionMode(modeSelection);
			
			if (mfc.showOpenDialog(this) == MonFileChooser.APPROVE_OPTION)
				setSelection(mfc.getSelectedFile());
		}
		
		public void setSelection (File f){
			selection = f;
			text.setText(selection.getName());
		}
	}

	private static boolean isLocale (){
		boolean isLocal = false;
		try{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {  // carte reseau trouvee
	            NetworkInterface interfaceN = (NetworkInterface)interfaces.nextElement(); 
	            Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
	            while (ienum.hasMoreElements()) {  // retourne l adresse IPv4 et IPv6
	                InetAddress ia = ienum.nextElement();
	                String adress = ia.getHostAddress().toString();
	                if (adress.startsWith ("10.0"))
	                	isLocal = true;
	            }
	        }
	    }
	    catch(Exception e){}
		return isLocal;
		
	}
	
}
