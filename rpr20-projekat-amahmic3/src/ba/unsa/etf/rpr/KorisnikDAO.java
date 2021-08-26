package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.models.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class KorisnikDAO {
    private Connection conn;
    private static KorisnikDAO instance;
    private PreparedStatement korisnikUpit,usernameAvailableUpit,dodajInspektoraUpit,dajInspektoreUpit,upisiIzvjestajUpit,upisiSvjedokeUpit,upisiObrazovnuInstitucijuUpit,upisiIzjavuSvjedokaUpit;
    private PreparedStatement dajSvjedokaSaEmailom;
    private PreparedStatement dajObrazovnuInstituciju;
    private PreparedStatement dajIDIzajveSvjedoka;

    public static KorisnikDAO getInstance() {
        if(instance==null){
            instance = new KorisnikDAO();
        }
        return instance;
    }
    private void spremiUpite() throws SQLException {
        korisnikUpit = conn.prepareStatement("SELECT id,ime,prezime,brojTelefona,email,administrator FROM Korisnik WHERE username = ? AND password = ?");
        usernameAvailableUpit = conn.prepareStatement("SELECT COUNT(*) FROM Korisnik WHERE username LIKE ?");
        dodajInspektoraUpit = conn.prepareStatement("INSERT INTO Korisnik(ime,prezime,email,brojTelefona,username,password,administrator) VALUES(?,?,?,?,?,?,0)");
        dajInspektoreUpit = conn.prepareStatement("SELECT id,ime,prezime,brojTelefona,email,administrator,username,password FROM Korisnik WHERE administrator = 0");
        upisiSvjedokeUpit = conn.prepareStatement("INSERT INTO Svjedok(Ime,Prezime,BrojTelefona,Email) VALUES (?,?,?,?)");
        upisiObrazovnuInstitucijuUpit = conn.prepareStatement("INSERT INTO ObrazovnaInstitucija(Naziv,Adresa,PostanskiBroj,BrojTelefona) VALUES (?,?,?,?)");
        upisiIzjavuSvjedokaUpit = conn.prepareStatement("INSERT INTO IzjavaSvjedoka(IDSvjedoka,Izjava) VALUES (?,?)");
        upisiIzvjestajUpit = conn.prepareStatement("INSERT INTO Izvjestaj(inspektorID,obrazovnaInstitucijaID,izjavaPrvogSvjedokaID,izjavaDrugogSvjedokaID,Opis,DatumIVrijeme) VALUES" +
                "(?,?,?,?,?,?)");
        dajSvjedokaSaEmailom = conn.prepareStatement("SELECT ID FROM Svjedok WHERE Email = ?");
        dajObrazovnuInstituciju = conn.prepareStatement("SELECT ID FROM ObrazovnaInstitucija WHERE Adresa = ? AND PostanskiBroj = ?");
        dajIDIzajveSvjedoka = conn.prepareStatement("SELECT ID FROM IzjavaSvjedoka WHERE IDSvjedoka = ? AND Izjava = ?");
    }
    private KorisnikDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:"+System.getProperty("user.home")+"\\.rprProjekat\\projekat.db");
            spremiUpite();
        } catch (SQLException throwables) {
            regenerisiBazu();
            try{
                spremiUpite();
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
             povratniKorisnik = new Korisnik(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getInt(6) == 1,username,password);
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
    public boolean provjeriUsername(String username){
        boolean povratni = true;
        try {
            usernameAvailableUpit.setString(1,username);
            ResultSet rs = usernameAvailableUpit.executeQuery();
            rs.next();
            if(rs.getInt(1)!=0) povratni= false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return povratni;
    }
    public int generisiUsername(String username){
        int povratni = 1;
        try {
            usernameAvailableUpit.setString(1,username);
            ResultSet rs = usernameAvailableUpit.executeQuery();
            rs.next();
            povratni = rs.getInt(1)+1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return povratni;
    }
    public void kreirajInspektora(Korisnik inspektor){
        try{
            dodajInspektoraUpit.setString(1,inspektor.getIme());
            dodajInspektoraUpit.setString(2,inspektor.getPrezime());
            dodajInspektoraUpit.setString(3,inspektor.getEmail());
            dodajInspektoraUpit.setString(4,inspektor.getBrojTelefona());
            dodajInspektoraUpit.setString(5,inspektor.getUsername());
            dodajInspektoraUpit.setString(6,inspektor.getPassword());
            dodajInspektoraUpit.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Korisnik> dajSveInspektore(){
        ArrayList<Korisnik> povratnaLista = new ArrayList<>();
        try {
            ResultSet rs = dajInspektoreUpit.executeQuery();
            while(rs.next()){
                povratnaLista.add(new Korisnik(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),false,rs.getString(7),rs.getString(8)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return povratnaLista;
    }
    public void upisiIzvjestaj(Izvještaj izvještaj){
        try {
            upisiSvjedoka(izvještaj.getPrvi().getSvjedok());
            upisiSvjedoka(izvještaj.getDrugi().getSvjedok());
            upisiObrazovnuInstituciju(izvještaj.getObrazovnaInstitucija());
            upisiIzjavuSvjedoka(izvještaj.getPrvi());
            upisiIzjavuSvjedoka(izvještaj.getDrugi());
            upisiIzvjestajUpit.setInt(1,izvještaj.getInspektor().getId());
            upisiIzvjestajUpit.setInt(2,izvještaj.getObrazovnaInstitucija().getId());
            upisiIzvjestajUpit.setInt(3,izvještaj.getPrvi().getId());
            upisiIzvjestajUpit.setInt(4,izvještaj.getDrugi().getId());
            upisiIzvjestajUpit.setString(5,izvještaj.getTekstIzvještaja());
            upisiIzvjestajUpit.setString(6,izvještaj.getDatumIzvještaja().toString());
            upisiIzvjestajUpit.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void upisiIzjavuSvjedoka(IzjavaSvjedoka prvi) throws SQLException {
        upisiIzjavuSvjedokaUpit.setInt(1,prvi.getSvjedok().getId());
        upisiIzjavuSvjedokaUpit.setString(2,prvi.getTekstIzjave());
        upisiIzjavuSvjedokaUpit.executeUpdate();
        dajIDIzajveSvjedoka.setInt(1,prvi.getSvjedok().getId());
        dajIDIzajveSvjedoka.setString(2,prvi.getTekstIzjave());
        ResultSet rs = dajIDIzajveSvjedoka.executeQuery();
        while(rs.next()) prvi.setId(rs.getInt(1));
    }

    private void upisiObrazovnuInstituciju(ObrazovnaInstitucija obrazovnaInstitucija) throws SQLException {
        dajObrazovnuInstituciju.setString(1,obrazovnaInstitucija.getAdresa());
        dajObrazovnuInstituciju.setString(2,obrazovnaInstitucija.getPostanskiBroj());
        ResultSet rs = dajObrazovnuInstituciju.executeQuery();
        if(!rs.next()){
            upisiObrazovnuInstitucijuUpit.setString(1,obrazovnaInstitucija.getNaziv());
            upisiObrazovnuInstitucijuUpit.setString(2,obrazovnaInstitucija.getAdresa());
            upisiObrazovnuInstitucijuUpit.setString(3,obrazovnaInstitucija.getPostanskiBroj());
            upisiObrazovnuInstitucijuUpit.setString(4,obrazovnaInstitucija.getBrojTelefona());
            upisiObrazovnuInstitucijuUpit.executeUpdate();
            rs = dajObrazovnuInstituciju.executeQuery();
            rs.next();
        }
        obrazovnaInstitucija.setId(rs.getInt(1));
    }

    private void upisiSvjedoka(Svjedok svjedok) throws SQLException {
        dajSvjedokaSaEmailom.setString(1, svjedok.getEmail());
        ResultSet rs = dajSvjedokaSaEmailom.executeQuery();
        if(!rs.next()) {
            upisiSvjedokeUpit.setString(1, svjedok.getIme());
            upisiSvjedokeUpit.setString(2, svjedok.getPrezime());
            upisiSvjedokeUpit.setString(3, svjedok.getBrojTelefona());
            upisiSvjedokeUpit.setString(4, svjedok.getEmail());
            upisiSvjedokeUpit.executeUpdate();
            //dajSvjedokaSaEmailom.setString(1, svjedok.getEmail());
            rs = dajSvjedokaSaEmailom.executeQuery();
            rs.next();
        }
        svjedok.setId(rs.getInt(1));
    }
}
