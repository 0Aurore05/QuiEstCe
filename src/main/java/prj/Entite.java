package prj;
import java.util.ArrayList;
import java.util.HashMap;


public class Entite {

    private boolean barre;
    private HashMap<String, ArrayList<String>> attributs = new HashMap<String, ArrayList<String>>();
    private String numDansJson;

    public Entite()
    {
        barre=false;
        numDansJson="erreur";
    }

    public Entite(HashMap<String,ArrayList<String>> attributs, String numDansJson)
    {
        this.barre = false;
        this.attributs = attributs;
        this.numDansJson = numDansJson;
    }

    public String getNumDansJson(){return this.numDansJson;}

    public boolean getBarre(){ return barre; }

    public void setBarre(){
        if (barre==false)
            barre=true;
        else
            barre=false;
    }

    public HashMap<String, ArrayList<String>> getAttributs() { return this.attributs; }
    public ArrayList<String> getKeys(){
    	 ArrayList<String> arr = new ArrayList<>(attributs.keySet());
    	 return arr;
    }
    public ArrayList<String> getAttributsByKey(String key) //key:cheveux ==>return [bruns, gris] par/ex
    {
        ArrayList<String> a = new ArrayList<String>();
        a.addAll(this.attributs.get(key));
        return a;
    }


    public void addAttribut(String key,String att) { attributs.get(key).add(att);}
}