package Stockage;

public class TestRangeable extends Rangeable {

	static int compteur = 0;
	
	int id;
	
	public TestRangeable() {
		// TODO Auto-generated constructor stub
		id = compteur++;
	}

	
	public String toString (){
		return "" + id;
	}
	
	
	
	public static void main (String [] args){
		
		TestRangeable [] tous = new TestRangeable[20];
		
		for (int i = 0; i < tous.length; i++)
			tous[i] = new TestRangeable();
		
		Conteneur<TestRangeable> c = new Conteneur<>(tous.length);
		
		for (int i = 0; i < 5; i++){
			c.add(tous[i]);
		}
		
		c.afficher (System.out);
		System.out.println ("fin1");

		c.remove(c.get(c.nbElements()-1));
		c.afficher (System.out);
		System.out.println ();
		
		c.switchElement(tous[3], tous[6]);
		c.afficher (System.out);
		System.out.println ();
		c.remove(tous[2]);
		c.afficher (System.out);
		System.out.println ();
		c.add(tous[7]);
		c.afficher (System.out);
		System.out.println ();
		
		int j = 10;
		for (TestRangeable r = c.init(); r!=null; r = c.next()){
			System.out.println (r);
			c.add (tous[j++]);
		}
		c.afficher (System.out);
		System.out.println ();
	}
	
}
