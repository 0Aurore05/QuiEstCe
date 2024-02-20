package prj;
import java.util.ArrayList;
import java.util.Random;
import java.lang.String;

public class PartieFacile extends Partie
{
  private ArrayList<Entite> elimines = new ArrayList<Entite>(); //total des élminés
  private String triche1,triche2;
 
  
  public PartieFacile(String pathJsonEntite,int lignes, int colonnes,int tour,int entite, ArrayList<Integer> coches)
  {
    super(pathJsonEntite,lignes,colonnes,tour,entite,coches);
    if(tour>0)
    coches.forEach(e -> { elimines.add(this.getListeEntites().get(e));});
  }
  public void setElimines(ArrayList<Entite> d) { elimines.addAll(d);}
  public ArrayList<Entite> listeElimines(){ return elimines; }
  public String getT1() {return triche1;}
  public String getT2() {return triche2;}
  public String randomQ() {
	   int fk = new Random().nextInt(getListeEntites().get(0).getKeys().size()-1)+1;
	   int fa = new Random().nextInt(getListeEntites().get(0).getAttributsByKey(getListeEntites().get(0).getKeys().get(fk)).size());
	   if (eliminesParTour("Non",true,getListeEntites().get(0).getKeys().get(fk),getListeEntites().get(0).getAttributsByKey(getListeEntites().get(0).getKeys().get(fk)).get(fa)) != null && eliminesParTour("Non",false,getListeEntites().get(0).getKeys().get(fk),getListeEntites().get(0).getAttributsByKey(getListeEntites().get(0).getKeys().get(fk)).get(fa)) != null) {
		   triche2 =getListeEntites().get(0).getAttributsByKey(getListeEntites().get(0).getKeys().get(fk)).get(fa);
		   triche1 = getListeEntites().get(0).getKeys().get(fk);
		   return "Votre personnage selectionné contient-t-il "+getListeEntites().get(0).getAttributsByKey(getListeEntites().get(0).getKeys().get(fk)).get(fa)+" pour la caracteristique \n"+getListeEntites().get(0).getKeys().get(fk);
	   } else return randomQ();
	   
  }

  
  
  public boolean verifQuestion(String connecteur,String... question) {
	  {
	        assert (question.length < 5) : " Nombre de questions hors bornes \n";
	        if(connecteur.equals("ET"))
	        {
	            int compteur = 0;
	            for (int i = 0 ; i<question.length; i=i+2){
	                if(getEntite().getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
	            }
	            if (compteur>=2) {
	            	elimines.addAll(eliminesParTour(connecteur,true,question));
	            		 
	            	 return true;}
	            else { 
	            	elimines.addAll(eliminesParTour(connecteur,false,question));
	            	 
	            	 return false;}
	        }

	        else if(connecteur.equals("OU"))
	        {
	            int compteur = 1;
	            for (int i = 0 ; i<question.length; i=i+2){
	                if(getEntite().getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
	            }
	            if (compteur>=2){
	            	elimines.addAll(eliminesParTour(connecteur,true,question));
	            	 
	            	 return true;}
	            else{ 
	            	elimines.addAll(eliminesParTour(connecteur,false,question));
	            	 
	            	 return false;}
	  }

	        else
	        {
	        	if(getEntite().getAttributs().get(question[0]).contains(question[1])){
	        		elimines.addAll(eliminesParTour(connecteur,true,question));
	            	
	            	 return true;}
	            else { 
	            	 elimines.addAll(eliminesParTour(connecteur,false,question));
	            
	            	 return false;}
	        }
	    }
  }
  
  public ArrayList<Entite> eliminesParTour(String connecteur ,boolean vrai ,String... question )
  { 
	  if(vrai) {
    assert (question.length < 5) : " Nombre de questions hors bornes \n";
    getListeEntites().forEach( 
      (n) -> {
      if(connecteur.equals("ET"))
      { 
        int compteur = 0;
        for ( int i = 0 ; i<question.length; i=i+2){
          if(n.getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
        }
        if (compteur<2 && n.getBarre() == false) { 
        this.coche(n);
         listeElimines().add(n);
        } 
      }
      else if(connecteur.equals("OU"))
      {
        int compteur = 1;
        for (int i = 0 ; i<question.length; i=i+2){
          if(n.getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
        } 
        if (compteur<2 && n.getBarre() == false){ 
          this.coche(n);
          listeElimines().add(n);
        }
      }
      else if (!n.getAttributs().get(question[0]).contains(question[1]) && n.getBarre() == false)      {  
          this.coche(n);
          listeElimines().add(n);
        } 
    });
    return listeElimines();
  }
	  else{
		    assert (question.length < 5) : " Nombre de questions hors bornes \n";
		    getListeEntites().forEach( 
		      (n) -> {
		      if(connecteur.equals("ET"))
		      { 
		        int compteur = 0;
		        for ( int i = 0 ; i<question.length; i=i+2){
		          if(n.getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
		        }
		        if (compteur>=2 && n.getBarre() == false) { 
		        this.coche(n);
		         listeElimines().add(n);
		        } 
		      }
		      else if(connecteur.equals("OU"))
		      {
		        int compteur = 1;
		        for (int i = 0 ; i<question.length; i=i+2){
		          if(n.getAttributs().get(question[i]).contains(question[i+1])) compteur ++;
		        } 
		        if (compteur>=2 && n.getBarre() == false){ 
		          this.coche(n);
		          listeElimines().add(n);
		        }
		      }
		      else //seulement 1 question
		      {
		        if (n.getAttributs().get(question[0]).contains(question[1]) && n.getBarre() == false)        
		          this.coche(n);
		          listeElimines().add(n);
		        } 
		    });
		    return listeElimines();
		  
		  }
	  }
  
}