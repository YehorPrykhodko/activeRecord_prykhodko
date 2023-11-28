import java.sql.*;
import java.util.ArrayList;

public class Film {





    private String titre;
    private int id, id_real;

    public Film(String tit, Personne pers){
        titre = tit;
        id_real = pers.getId();
        id = -1;
    }

    private Film(int idFilm, int idPers, String titr){
        titre = titr;
        id = idFilm;
        id_real = idPers;
    }

    public static Film findById(int idFilm) throws SQLException {
        Connection connect = DBConnection.getConnection();
        String SQLPrep = "SELECT * FROM Film WHERE id = ?";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setInt(1, idFilm);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        Film res = null;
        if(rs.next()){
            res = new Film(rs.getInt("id"), rs.getInt("id_rea"), rs.getString("titre"));
        }
        return res;
    }

    public ArrayList<Film> findByRealisateur(Personne p) throws SQLException {
        Connection connect = DBConnection.getConnection();
        ArrayList<Film> res = new ArrayList<>();
        String SQLPrep = "SELECT * FROM Film WHERE id_rea = ?";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setString(1, String.valueOf(id_real));
        prep1.execute();

        ResultSet rs = prep1.getResultSet();
        // s'il y a un resultat
        while (rs.next()) {
            Film f = new Film(rs.getInt("id"), rs.getInt("id_rea"), rs.getString("titre"));
            res.add(f);
            System.out.println(f.getId() + " " + f.getId_real() + " " + f.getTitre());
        }
        return res;
    }



    public Personne getRealisateur() throws SQLException {
        return Personne.findById(id_real);
    }


    public static void createTable() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String createString = "CREATE TABLE Film ( " + "id int(11) AUTO_INCREMENT," +
                                                      "titre varchar(40) NOT NULL," +
                                                      "id_rea int(11) DEFAULT NULL," +
                                                        "PRIMARY KEY (id))";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(createString);
    }

    public static void deleteTable() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String drop = "DROP TABLE Film";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(drop);
    }

    public void delete() throws SQLException{
        Connection connect = DBConnection.getConnection();
        PreparedStatement prep = connect.prepareStatement("DELETE FROM Film WHERE id=?");
        prep.setInt(1, id);
        prep.execute();
        id = -1;
    }

    public void save() throws SQLException, RealisateurAbsentException {
        if (id_real == -1){
            throw new RealisateurAbsentException();
        }
        Connection connect = DBConnection.getConnection();
        if (id == -1){
            String SQLPrep = "INSERT INTO Film (id_rea, titre) VALUES (?,?);";
            PreparedStatement prep;
            prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, String.valueOf(id_real));
            prep.setString(2, titre);
            prep.executeUpdate();
            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            return;
        }
        String SQLprep = "update Film set titre=?, id_rea=? where id=?;";
        PreparedStatement prep = connect.prepareStatement(SQLprep);
        prep.setString(1, titre);
        prep.setString(2, String.valueOf(id_real));
        prep.setInt(3, id);
        prep.execute();
    }


    public void setTitre(String titre) {
        this.titre = titre;
    }


    public String getTitre() {
        return titre;
    }

    public int getId() {
        return id;
    }

    public int getId_real() {
        return id_real;
    }

}
