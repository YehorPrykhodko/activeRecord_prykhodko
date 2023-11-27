public class Film {

    private String titre;
    private int id, id_real;

    public Film(String tit, Personne pers){
        titre = tit;
        id_real = pers.getId();
        id = -1;
    }

    private Film(){

    }



}
