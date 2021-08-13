package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.models.Korisnik;
import javafx.fxml.FXML;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class KorisnikDAO {
    private Connection conn;
    private static KorisnikDAO instance;
    private PreparedStatement korisnikUpit;

    public static KorisnikDAO getInstance() {
        if(instance==null){
            instance = new KorisnikDAO();
        }
        return instance;
    }

    private KorisnikDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:"+System.getProperty("user.home")+"\\.rprProjekat\\projekat.db");
            korisnikUpit = conn.prepareStatement("SELECT id,ime,prezime,brojTelefona,email,administrator FROM Korisnik WHERE username = ? AND password = ?");

        } catch (SQLException throwables) {
            regenerisiBazu();
            try{
                korisnikUpit = conn.prepareStatement("SELECT id,ime,prezime,brojTelefona,email,administrator FROM Korisnik WHERE username = ? AND password = ?");
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println(System.getProperty("user.home"));
            }
        }
    }
    public Korisnik dajKorisnika(String username,String password){
        Korisnik povratniKorisnik = null;
        try {
            korisnikUpit.setString(1,username);
            korisnikUpit.setString(2,password);
            ResultSet rs= korisnikUpit.executeQuery();
            if(rs.next()){
             povratniKorisnik = new Korisnik(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getInt(6) == 1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return povratniKorisnik;
    }
    private void regenerisiBazu(){
        try {
            Scanner citac = new Scanner(new FileInputStream("Projekat.sql"));
            StringBuilder upit=new StringBuilder("");
            while(citac.hasNextLine()){
                upit.append(citac.nextLine());
                if(upit.toString().length()>0 && upit.toString().charAt(upit.toString().length()-1)==';'){
                    conn.createStatement().executeUpdate(upit.toString());
                    upit.setLength(0);
                }
            }
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
