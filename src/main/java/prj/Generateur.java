package prj;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.util.*;
public class Generateur {
    private int colonnes; private int lignes;
    private ArrayList<Entite> listeEntite = new ArrayList<Entite>();
//    private String pathJsonEntites;
    
    
    public ArrayList<Entite> getAllEntite(String pathJsonEntite) {
        ArrayList<Entite> ttesEntite = new ArrayList<>();
        try {
            JsonReader reader = new JsonReader(new FileReader(pathJsonEntite));
            HashMap<String, HashMap<String, ArrayList<String>>> toHash = new HashMap<String, HashMap<String, ArrayList<String>>>();
            toHash = new Gson().fromJson(reader, HashMap.class);
            Set<Map.Entry<String, HashMap<String, ArrayList<String>>>> set1 = toHash.entrySet();

            for (Map.Entry<String, HashMap<String, ArrayList<String>>> e1 : set1) {
                HashMap<String, ArrayList<String>> attributs = new HashMap<String, ArrayList<String>>();
                String numEntite = e1.getKey();

                Map<String, ArrayList<String>> map2 = e1.getValue();
                Set<Map.Entry<String, ArrayList<String>>> set2 = map2.entrySet();
                for (Map.Entry<String, ArrayList<String>> e2 : set2) {
                    String keyAtt = e2.getKey();
                    ArrayList<String> valAtt = e2.getValue();
                    attributs.put(keyAtt, valAtt);
                }
                ttesEntite.add(new Entite(attributs, numEntite));
            }

        } catch (java.io.FileNotFoundException e) {
            System.out.println("fichier JSON non trouvé");
        }
        return ttesEntite;
    }

    public Generateur(int l, int c,String path){
       // pathJsonEntites = path;
        int important = l*c;
        this.colonnes = c;
        this.lignes = l;
        if(getAllEntite(path).size()<important){
        	important = getAllEntite(path).size();
        	if(important % 6 == 0) {
        		this.colonnes = 6;
        		this.lignes = important/6;
        		System.out.println("erreur");
        	}
        	else if(important % 5 ==0) {        		this.colonnes = 5;
    		this.lignes = important/5;
        }
        	else  	if(important % 4 ==0) {
        		this.colonnes = 4;
        		this.lignes = important/4;      
        	}
        	else  	if(important % 3 ==0) {
        		this.colonnes = 3;
        		this.lignes = important/3;
            }
        	}
        this.listeEntite = entitesAffichees(getAllEntite(path));
        for(int i=1 ; i<this.colonnes*this.lignes; i++) {
        	 
        	for(int k = 0 ; k<this.lignes*this.colonnes-i;k++) {
        		System.out.println(this.lignes*this.colonnes-i-k);
        	if(Integer.parseInt(this.listeEntite.get(k).getNumDansJson()) > Integer.parseInt(this.listeEntite.get(k+1).getNumDansJson())) {
        	Collections.swap(listeEntite, k, k+1);
        	}
        	 }
        }
    }
    
    public void setColonnes(int c)
    {
        this.colonnes = c;
    }
    public void setLignes(int l)
    {
        this.lignes = l;
    }
    public int getlignes() { return lignes;}
    
    public int getcolonnes() { return colonnes;}
    
    public ArrayList<Entite> entitesAffichees(ArrayList<Entite> d) //celles qu'on choisit d'afficher (hasard) parmi toutes selon le nb de ligne/colonnes
    {
        ArrayList<Entite> e = new ArrayList<>();
        int n = this.lignes*this.colonnes;
        int i=0;
        while(i<n)
        {
            int k = new Random().nextInt(d.size());
            if (!e.contains(d.get(k))) {
            e.add(d.get(k));
            i++;
            }
        }
        return e;
    }

    public ArrayList<Entite> getListeEntites(){ return this.listeEntite; }
    
    public void replaceK(String base, String replacement) {
    	supprK(base);
    	setK(replacement);
    }

    public void setK(String key){
        for (int i =0 ; i<getListeEntites().size(); i++){
            getListeEntites().get(i).getAttributs().put(key,new ArrayList<String>());
        }
    }
    public void supprK(String key){
        for (int i =0 ; i<getListeEntites().size(); i++){
            getListeEntites().get(i).getAttributs().remove(key);
        }
    }


    public Entite getEntiteParImg(String pathImg)
    {
            for (Entite e : getListeEntites()) {
                if (e.getAttributsByKey("path").get(0).equals(pathImg)) return e;
            }
            //todo : faire un meilleur systeme d'erreur
            System.out.println("image associée à aucune entité");
            return new Entite();
    }

    public void saveJsonEntite(String nom)
    {
    	 String sauvegarde = nom+".json";
        File Del = new File(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\EntiteFile\\"+sauvegarde);
        Del.delete();
        File sauve = new File(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\EntiteFile\\"+sauvegarde);
        try {
            sauve.createNewFile();
        } catch (IOException crf) {
            crf.printStackTrace();
        }
        try {
            char g = '"';
            PrintWriter save = new PrintWriter(sauve);
            save.println("{");

            for(int i=0; i<listeEntite.size(); i++)
            {
                save.println(g+String.valueOf(i)+g+":{");
                String[] allKeys = this.listeEntite.get(0).getAttributs().keySet().toArray(new String[0]);
                for(int k=0; k<allKeys.length; k++)
                {
                    if(! listeEntite.get(i).getAttributsByKey(allKeys[k]).isEmpty()) //if à peut être enlever 
                    {                   
                    	save.print(g+allKeys[k]+g+":"+"[");
                    	final int m = i;
                    	final String n = allKeys[k];
                    	listeEntite.get(i).getAttributs().get(n).forEach( (p) -> { if(p != listeEntite.get(m).getAttributs().get(n).get(0))
                    			save.print(",");
                    			save.print(g+p+g);});
                    }
                    else save.print(g+allKeys[k]+g+":"+"[");
                    save.println("]");
                    if(k<allKeys.length-1) save.println(",");
                }
                save.print("}");
                if(i<listeEntite.size()-1) save.println(",");
            }
            save.println("}");
            save.close(); 
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }
        File DelSet = new File(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\EntiteFile\\"+"Exe"+sauvegarde);
        DelSet.delete();
        File setup = new File(System.getProperty("user.home")+"\\.Qui-est-ce-GRPMA\\records\\"+"Exe"+sauvegarde);
        try {
            setup.createNewFile();
        } catch (IOException crf) {
            crf.printStackTrace();
        }
        try {   
        	char g = '"';
        PrintWriter save = new PrintWriter(setup);
        save.println("{");
        save.println(g+"nbL"+g+":"+ lignes +"," );
        save.println(g+"nbC"+g + ":"  + colonnes +"," );
        save.println(g+ "nbT"+g+ ":" +0+",");
        save.println(g+ "entM"+g+ ":" +0+ ",");
        save.println(g+ "jsonP"+g+ ":"+g+"EntiteFile/"+sauvegarde+g+",");
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
        }
        catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }
    }

}