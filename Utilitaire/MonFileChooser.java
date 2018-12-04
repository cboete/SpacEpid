package Utilitaire;


import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MonFileChooser extends JFileChooser {

	public static final long serialVersionUID = 0;
	private static File dirCou;

	
	public MonFileChooser (){
		super();
	}
		
	public MonFileChooser (String desc, String... extensions){
		super (dirCou);
		setFileFilter(new FileNameExtensionFilter (desc, extensions));
	}
	
	public static File getDirCou (){
		return dirCou;
	}
	
	public static void setDirectory (File f){
		dirCou = f;
	}
	
	@Override
	public int showOpenDialog (Component c){
		setCurrentDirectory(dirCou);
		int res = super.showOpenDialog (c);
		if (res == APPROVE_OPTION){
			dirCou = getSelectedFile().getParentFile();
		}
		return res;
	}

	@Override
	public int showSaveDialog (Component c){
		setCurrentDirectory(dirCou);
		int res = super.showSaveDialog (c);
		if (res == APPROVE_OPTION){
			dirCou = getSelectedFile().getParentFile();
		}
		return res;
	}
	
	@Override
	public int showDialog (Component c, String title){
		setCurrentDirectory(dirCou);
		int res = super.showDialog (c, title);
		if (res == APPROVE_OPTION){
			dirCou = getSelectedFile().getParentFile();
		}
		return res;
	}

	public void setFileName (String name){
		setSelectedFile(new File(dirCou, name));
	}
	
	
	public File getSelectedFile (String extension){
		if (getSelectedFile().getName().endsWith("." + extension))
			return getSelectedFile();
		return new File (getSelectedFile().getParentFile(), getSelectedFile().getName() + "." + extension);
	}
	
	
	public static void main (String [] args){
		MonFileChooser fc = new MonFileChooser();
		if (fc.showOpenDialog(null) == APPROVE_OPTION){
			File ficIn = fc.getSelectedFile();
			File ficOut = new  File (dirCou + "\\new" + ficIn.getName());
			
			try{
				BufferedReader in = new BufferedReader(new FileReader(ficIn));
				PrintStream out = new PrintStream(ficOut);
				int index = 1;
				int compteurN;
				out.println (">contig" + (index++));
				
				for (int carCou = in.read(); carCou != -1; carCou = in.read()){
					compteurN = 0;
					for (;(char) carCou == 'n'; carCou = in.read())
						compteurN++;
					
					if (compteurN>10){
						out.println ();
						out.println (">contig" + (index++));
					}
					else{
						for (int i = 0; i < compteurN; i++)
							out.print('n');
					}
					out.print ((char) carCou);
				}
				out.close();
				in.close();
			}catch (IOException e){}
		}
		
		
		
	}
}
