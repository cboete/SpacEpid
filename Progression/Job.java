package Progression;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Job extends JPanel{
	private String sequence;
	private JProgressBar progression;
	private long debut;
	
	public Job (String nom, int nbSequence){
		sequence = nom;
		progression = new JProgressBar(0, nbSequence);
		progression.setStringPainted(true);
		progression.setString("0/" + nbSequence);
		add (new JLabel (sequence));
		add (progression);
		debut = System.currentTimeMillis();
	}
	
	public void setDone (int i){
		progression.setValue(i);
		progression.setString(i+"/"+progression.getMaximum());
	}
	
	public long getTime (){
		return System.currentTimeMillis() - debut;
	}
}