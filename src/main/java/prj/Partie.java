package prj;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.*;
import java.lang.String;
public class Partie
{
    private String nomSave;
    private int colonnes; private int lignes ;
    private Entite entiteMystere;
    private static int nbsave = 0;
    private int tour;
    private ArrayList<Entite> listeEntite = new ArrayList<Entite>();
    private int countbarre;
    private String pathJsonEntite;


    public ArrayList<Entite> getAllEntite(String pathJsonEntite)
    {
        ArrayList<Entite> ttesEntite = new ArrayList<>();
        try {
            JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\"+pathJsonEntite));
            HashMap<String, HashMap<String, ArrayList<String>>> toHash = new HashMap<String, HashMap<String, ArrayList<String>>>();
            toHash = new Gson().fromJson(reader, HashMap.class);
            Set<Map.Entry<String, HashMap<String, ArrayList<String>>>> set1 = toHash.entrySet();

            for( Map.Entry<String, HashMap<String,ArrayList<String>>> e1 : set1) {
                HashMap<String, ArrayList<String>> attributs = new HashMap<String, ArrayList<String>>();
                String numEntite = e1.getKey();

                Map<String, ArrayList<String>> map2 = e1.getValue();
                Set<Map.Entry<String, ArrayList<String>>> set2 = map2.entrySet();
                for (Map.Entry<String, ArrayList<String>> e2 : set2) {
                    String keyAtt = e2.getKey();
                    ArrayList<String> valAtt = e2.getValue();
                    attributs.put(keyAtt, valAtt);
                }
                ttesEntite.add(new Entite(attributs,numEntite));
            }

        } catch(java.io.FileNotFoundException e)
        {
            System.out.println("Fichier Json non trouvé.");
        }
        return ttesEntite;
    }


   public Partie(String pathJsonEntite,int lignes, int colonnes,int tour,int entite, ArrayList<Integer> coches) {
        this.pathJsonEntite = pathJsonEntite;
        nomSave = null;
        this.lignes = lignes; this.colonnes = colonnes;
        ArrayList<Entite> ttesEntite = this.getAllEntite(pathJsonEntite);
        
        for(int i=0 ; i<lignes*colonnes; i++)
        {	int k = 0;
        	while(Integer.parseInt(ttesEntite.get(k).getNumDansJson())!=i)k++;
            listeEntite.add(ttesEntite.get(k));
        }
        for(int i=0 ; i <lignes*colonnes;i++)
        	System.out.println(listeEntite.get(i));
        this.tour = tour;
        if(tour ==0) {
        int k = new Random().nextInt(listeEntite.size());
        this.entiteMystere = listeEntite.get(k);
        countbarre = 0;}
        else {
        	this.entiteMystere= listeEntite.get(entite);
        	coches.forEach(n -> {coche(listeEntite.get(n));
        	});
        	countbarre = coches.size();
        }
    }
   
    public ArrayList<Entite> getListeEntites()
    {
        return this.listeEntite;
    }

    public boolean finpartie(){
        if(getcountbarre() == getlignes()*getcolonnes()-1){
            int i = 0;
            while(getListeEntites().get(i).getBarre() == true)
                i++;
            if (getListeEntites().get(i)==getEntite()) return true;

        }
        return false;
    }

    public int getcolonnes(){ return colonnes; }

    public int getlignes(){ return lignes; }

    public int getTour(){ return this.tour; }
    
    public void barrePlus() {countbarre++;}
    
    public void incrT(){ tour++;}
    
    public Entite getEntite(){return this.entiteMystere;}

    public int getcountbarre(){return countbarre;}

    public boolean verifQuestion(String connecteur,String... question) //quel format pour les questions ?
    {
        assert (question.length < 5) : " Nombre de questions hors bornes \n";
        if(connecteur.equals("ET"))
        {
            int compteur = 0;
            for (int i = 0 ; i<question.length; i=i+2){
                if(getEntite().getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
            }
            if (compteur>=2) return true;
            else return false;
        }

        else if(connecteur.equals("OU"))
        {
            int compteur = 1;
            for (int i = 0 ; i<question.length; i=i+2){
                if(getEntite().getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
            }
            if (compteur>=2) return true;
            else return false;
        }

        else
        {
            if(getEntite().getAttributs().get(question[0]).contains(question[1])) return true;
            else return false;
        }
    }

    public void coche(Entite E){
        if(E.getBarre())
            countbarre --;
        else
            countbarre ++;
        E.setBarre();
    }

    public void save(String nom)
    {
        if (this.nomSave == null) {
            String sauvegarde = nom+".json";
            this.nomSave = sauvegarde;
            nbsave++;
        } else {
            File Del = new File(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\"+nomSave);
            Del.delete();
        }
        File sauve = new File(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\"+nomSave);
        try {
            sauve.createNewFile();
        } catch (IOException crf) {
            crf.printStackTrace();
        }
        try {
            char g = '"';
            PrintWriter save = new PrintWriter(sauve);
            save.println("{");
            save.println(g+"nbL"+g+":"+ lignes +"," );
            save.println(g+"nbC"+g + ":"  + colonnes +"," );
            save.println(g+ "nbT"+g+ ":" +"1"+",");
            save.println(g+ "entM"+g+ ":" +entiteMystere.getNumDansJson()+ ",");
            save.println(g+ "jsonP"+g+ ":"+g+pathJsonEntite+g+",");
            ArrayList<String> entiteBarrees = new ArrayList<>();
            for(int i=0; i<listeEntite.size(); i++){
                Entite e = listeEntite.get(i);
                if(e.getBarre()) {
                    entiteBarrees.add(e.getNumDansJson());
                }
            }

            save.print(g+"coches"+g+": [");
            for(int i=0; i<entiteBarrees.size(); i++)
            {
                if(i>0) save.print(",");
                save.print(g+entiteBarrees.get(i)+g);
            }
            save.println("]");

            save.print("}");
            save.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }
    }

    public String[] getAllKeys() //avoir toutes les clés d'attributs possibles ("cheveux", "genre",...)
    {
        String[] keys = this.listeEntite.get(0).getAttributs().keySet().toArray(new String[0]);
        return keys;
    }

    public ArrayList<String> getAllChoicesByKey(String key) //ttes possibilités selon une clé(cheveux-> "roux","brun","chauve",..)
    {
        ArrayList<String> res = new ArrayList<String>();
        for(Entite e : this.listeEntite)
        {
            for(String s : e.getAttributs().get(key))
            {
                if(!res.contains(s)) res.add(s);
            }
        }
        return res;
    }
}