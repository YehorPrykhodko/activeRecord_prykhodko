import com.google.protobuf.DescriptorProtos;

import java.sql.*;
import java.util.ArrayList;

public class Personne {

    private int id;
    private String nom;
    private String prenom;

    Personne(String n, String pren){
        nom = n;
        prenom = pren;
        id = -1;
    }

    public static ArrayList<Personne> findAll() throws SQLException {
        Connection connect = DBConnection.getConnection();
        ArrayList<Personne> res = new ArrayList<>();
        String SQLPrep = "SELECT * FROM Personne";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.execute();

        ResultSet rs = prep1.getResultSet();
        // s'il y a un resultat
        while (rs.next()) {
            Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
            p.setId(rs.getInt("id"));
            res.add(p);
            System.out.println(p.getId() + " " + p.getNom() + " " + p.getPrenom());
        }
        return res;
    }


    public static Personne findById(int id) throws SQLException {
        Connection connect = DBConnection.getConnection();
        String SQLPrep = "SELECT * FROM Personne WHERE id = ?";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setInt(1, id);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();

        Personne res = null;
        if(rs.next()){
             res = new Personne(rs.getString("nom"), rs.getString("prenom"));
            res.setId(rs.getInt("id"));
        }
        return res;
    }


    public static ArrayList<Personne> findByName(String nom) throws SQLException{
        Connection connect = DBConnection.getConnection();
        ArrayList<Personne> res = new ArrayList<>();
        String SQLPrep = "SELECT * FROM Personne WHERE nom = ?";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setString(1,nom);
        prep1.execute();

        ResultSet rs = prep1.getResultSet();
        // s'il y a un resultat
        while (rs.next()) {
            Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
            p.setId(rs.getInt("id"));
            res.add(p);
            System.out.println(p.getId() + " " + p.getNom() + " " + p.getPrenom());
        }
        return res;
    }



    public static void createTable() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String createString = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, "
                + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(createString);
    }

    public static void deleteTable() throws SQLException{
        Connection connect = DBConnection.getConnection();
        String drop = "DROP TABLE Personne";
        Statement stmt = connect.createStatement();
        stmt.executeUpdate(drop);
    }

    public void delete() throws SQLException{
        Connection connect = DBConnection.getConnection();
        PreparedStatement prep = connect.prepareStatement("DELETE FROM Personne WHERE id=?");
        prep.setInt(1, id);
        prep.execute();
        id = -1;
    }

    public void save() throws SQLException{
        Connection connect = DBConnection.getConnection();
        if (id == -1){
            String SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";
            PreparedStatement prep;
            prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, nom);
            prep.setString(2, prenom);
            prep.executeUpdate();
            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            return;
        }
        String SQLprep = "update Personne set nom=?, prenom=? where id=?;";
        PreparedStatement prep = connect.prepareStatement(SQLprep);
        prep.setString(1, nom);
        prep.setString(2, prenom);
        prep.setInt(3, id);
        prep.execute();
    }


    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
