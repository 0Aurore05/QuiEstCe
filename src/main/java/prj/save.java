package prj;
import java.util.*;
import java.lang.String;
public class save{
	private int nbL;
	private int nbC;
	private int nbT;
	private int entM;
	private String jsonP;
	private ArrayList<Integer> coches = new ArrayList<Integer>();
	
	
	public save(int nbL,int nbC,int nbT,int entM,String jsonP,ArrayList<Integer> coches) {
		this.nbL=nbL;
		this.nbC = nbC;
		this.nbT = nbT;
		this.entM = entM;
		this.jsonP = jsonP;
		this.coches = coches;
	}
	public int getL() { return nbL;}
	public int getC() { return nbC;}
	public int getT() { return nbT;}
	public int getM() { return entM;}
	public String getJ() {return jsonP;}
	public ArrayList<Integer> getCo(){ return coches;}
}