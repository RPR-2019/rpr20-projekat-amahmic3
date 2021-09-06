package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.models.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class KorisnikDAO {
    private Connection conn;
    private static KorisnikDAO instance;
    private PreparedStatement korisnikUpit,usernameAvailableUpit,dodajInspektoraUpit,dajInspektoreUpit,upisiIzvjestajUpit,upisiObrazovnuInstitucijuUpit,upisiIzjavuSvjedokaUpit;
    private PreparedStatement dajObrazovnuInstituciju;
    private PreparedStatement dajIzvjestajeOdInspektoraUpit;
    private PreparedStatement dajBrojIzjavaSvjedoka;
    private PreparedStatement dajObrazovnuInstitucijuID;
    private PreparedStatement dajIzjavuSvjedokaID;
    private PreparedStatement dajKorisnikaID;
    private PreparedStatement dajBrojInspektora;
    private PreparedStatement obrazovnaInstitucijaSuggestion;
    private PreparedStatement emailPostoji;
    private PreparedStatement telefonPostoji;
    private PreparedStatement dajIdOdKorisnika;
    private PreparedStatement dajUsernamoveKojiPocinju;
    private PreparedStatement azurirajInspektoraUpit;
    private PreparedStatement obrisiInspektoraUpit;

    public static KorisnikDAO getInstance() {
        if(instance==null){
            instance = new KorisnikDAO();
        }
        return instance;
    }
    private void spremiUpite() throws SQLException {
        korisnikUpit = conn.prepareStatement("SELECT id,ime,prezime,brojTelefona,email,administrator,obrisan FROM Korisnik WHERE username = ? AND password = ?");
        usernameAvailableUpit = conn.prepareStatement("SELECT ID FROM Korisnik WHERE username LIKE ?");
        dodajInspektoraUpit = conn.prepareStatement("INSERT INTO Korisnik(ime,prezime,email,brojTelefona,username,password,administrator,obrisan) VALUES(?,?,?,?,?,?,?,0)");
        dajInspektoreUpit = conn.prepareStatement("SELECT id,ime,prezime,brojTelefona,email,administrator,username,password,obrisan FROM Korisnik");
        upisiObrazovnuInstitucijuUpit = conn.prepareStatement("INSERT INTO ObrazovnaInstitucija(Naziv,Adresa,PostanskiBroj,BrojTelefona) VALUES (?,?,?,?)");
        upisiIzjavuSvjedokaUpit = conn.prepareStatement("INSERT INTO IzjavaSvjedoka(Ime,Prezime,Email,BrojTelefona,Izjava) VALUES (?,?,?,?,?)");
        upisiIzvjestajUpit = conn.prepareStatement("INSERT INTO Izvjestaj(inspektorID,obrazovnaInstitucijaID,izjavaPrvogSvjedokaID,izjavaDrugogSvjedokaID,Opis,DatumIVrijeme) VALUES" +
                "(?,?,?,?,?,?)");
        dajObrazovnuInstituciju = conn.prepareStatement("SELECT ID FROM ObrazovnaInstitucija WHERE Adresa = ? AND PostanskiBroj = ?");
        dajIzvjestajeOdInspektoraUpit = conn.prepareStatement("SELECT ID,obrazovnaInstitucijaID,IzjavaPrvogSvjedokaID,IzjavaDrugogSvjedokaID,Opis,DatumIVrijeme FROM Izvjestaj WHERE inspektorID = ?");
        dajObrazovnuInstitucijuID = conn.prepareStatement("SELECT Naziv,Adresa,PostanskiBroj,BrojTelefona FROM ObrazovnaInstitucija WHERE ID = ?");
        dajIzjavuSvjedokaID= conn.prepareStatement("SELECT Ime,Prezime,Email,BrojTelefona,Izjava FROM IzjavaSvjedoka WHERE ID = ?");
        dajBrojIzjavaSvjedoka = conn.prepareStatement("SELECT COUNT(*) FROM IzjavaSvjedoka");
        dajKorisnikaID = conn.prepareStatement("SELECT ime,prezime,brojTelefona,email,administrator,username,password,obrisan FROM Korisnik WHERE id = ?");
        dajBrojInspektora = conn.prepareStatement("SELECT COUNT(*) FROM Korisnik");
        obrazovnaInstitucijaSuggestion = conn.prepareStatement("SELECT ID, Naziv, Adresa, BrojTelefona, PostanskiBroj FROM ObrazovnaInstitucija WHERE Naziv LIKE ?");
        emailPostoji = conn.prepareStatement("SELECT ID FROM Korisnik WHERE email LIKE ? AND obrisan=0");
        telefonPostoji = conn.prepareStatement("SELECT ID FROM Korisnik WHERE brojTelefona LIKE ? AND obrisan=0");
        dajIdOdKorisnika = conn.prepareStatement("SELECT ID FROM Korisnik WHERE username = ? AND obrisan =0");
        dajUsernamoveKojiPocinju = conn.prepareStatement("SELECT username FROM Korisnik WHERE username LIKE ? AND obrisan = 0");
        azurirajInspektoraUpit = conn.prepareStatement("UPDATE Korisnik SET ime = ?, prezime = ?, brojTelefona = ?, email = ?, username = ?, password = ?, administrator =? WHERE ID = ?");
        obrisiInspektoraUpit= conn.prepareStatement("UPDATE Korisnik SET obrisan=1 WHERE ID=?");
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
                if(rs.getInt(7)!=1)
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
    public boolean provjeriUsername(String username, int id){
        boolean povratni = true;
        try {
            usernameAvailableUpit.setString(1,username);
            ResultSet rs = usernameAvailableUpit.executeQuery();
            if(rs.next() && rs.getInt(1)!=id){
                povratni=false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return povratni;
    }
    public int generisiUsername(String username){
        int povratni = 0;
        try {
            dajUsernamoveKojiPocinju.setString(1,username);
            ResultSet rs = dajUsernamoveKojiPocinju.executeQuery();
            while(rs.next()){
                 String sufiks = rs.getString(1).substring(username.length()-1);
                 int broj;
                 if(!sufiks.isEmpty() && sufiks.matches("[0-9]*") && (broj =Integer.parseInt(sufiks))>povratni){
                    povratni=broj;
                 }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return povratni+1;
    }
    public void kreirajInspektora(Korisnik inspektor){
        try{
            dodajInspektoraUpit.setString(1,inspektor.getIme());
            dodajInspektoraUpit.setString(2,inspektor.getPrezime());
            dodajInspektoraUpit.setString(3,inspektor.getEmail());
            dodajInspektoraUpit.setString(4,inspektor.getBrojTelefona());
            dodajInspektoraUpit.setString(5,inspektor.getUsername());
            dodajInspektoraUpit.setString(6,inspektor.getPassword());
            dodajInspektoraUpit.setInt(7,inspektor.isAdministrator() ? 1 : 0);
            dodajInspektoraUpit.executeUpdate();
            dajIdOdKorisnika.setString(1,inspektor.getUsername());
            ResultSet rs = dajIdOdKorisnika.executeQuery();
            if(rs.next()){
                inspektor.setId(rs.getInt(1));
            }else{
                throw new Exception("Hairs");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Korisnik> dajSveInspektore(){
        ArrayList<Korisnik> povratnaLista = new ArrayList<>();
        try {
            ResultSet rs = dajInspektoreUpit.executeQuery();
            while(rs.next()){
                if(rs.getInt(9)!=1)
                povratnaLista.add(new Korisnik(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),false,rs.getString(7),rs.getString(8)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return povratnaLista;
    }
    public void upisiIzvjestaj(Izvještaj izvještaj){
        try {
             ResultSet rs = dajBrojIzjavaSvjedoka.executeQuery();
             rs.next();
             int idSvjedoka = rs.getInt(1);
             izvještaj.getPrvi().setId(idSvjedoka+1);
            izvještaj.getDrugi().setId(idSvjedoka+2);
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

    private void upisiIzjavuSvjedoka(IzjavaSvjedoka izjavaSvjedoka) throws SQLException {
        upisiIzjavuSvjedokaUpit.setString(1,izjavaSvjedoka.getSvjedok().getIme());
        upisiIzjavuSvjedokaUpit.setString(2,izjavaSvjedoka.getSvjedok().getPrezime());
        upisiIzjavuSvjedokaUpit.setString(3,izjavaSvjedoka.getSvjedok().getEmail());
        upisiIzjavuSvjedokaUpit.setString(4,izjavaSvjedoka.getSvjedok().getBrojTelefona());
        upisiIzjavuSvjedokaUpit.setString(5,izjavaSvjedoka.getTekstIzjave());
        upisiIzjavuSvjedokaUpit.executeUpdate();
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

    public ArrayList<Izvještaj> dajSveIzvjestajeOdInspektora(Korisnik inspektor) {
        ArrayList<Izvještaj> povratnaLista = new ArrayList<>();
        try {
            dajIzvjestajeOdInspektoraUpit.setInt(1,inspektor.getId());
            ResultSet rs = dajIzvjestajeOdInspektoraUpit.executeQuery();
            ResultSet obrazovnaInstitucija,izjavaSvjedoka;
            while (rs.next()){
                dajObrazovnuInstitucijuID.setInt(1,rs.getInt(2));
                obrazovnaInstitucija = dajObrazovnuInstitucijuID.executeQuery();
                obrazovnaInstitucija.next();
                ObrazovnaInstitucija novaObrazovna = new ObrazovnaInstitucija(rs.getInt(2),obrazovnaInstitucija.getString(1),obrazovnaInstitucija.getString(2),obrazovnaInstitucija.getString(4),obrazovnaInstitucija.getString(3));

                dajIzjavuSvjedokaID.setInt(1,rs.getInt(3));
                izjavaSvjedoka = dajIzjavuSvjedokaID.executeQuery();
                IzjavaSvjedoka[] izjaveSvjedoka={null,null};
                izjaveSvjedoka[0] = kreirajIzjavu(izjavaSvjedoka,rs.getInt(3));
                dajIzjavuSvjedokaID.setInt(1,rs.getInt(4));
                izjavaSvjedoka = dajIzjavuSvjedokaID.executeQuery();
                izjaveSvjedoka[1] = kreirajIzjavu(izjavaSvjedoka,rs.getInt(4));

                povratnaLista.add(new Izvještaj(rs.getInt(1),inspektor, novaObrazovna,rs.getString(5), LocalDateTime.parse(rs.getString(6)),izjaveSvjedoka));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return povratnaLista;
    }
    private IzjavaSvjedoka kreirajIzjavu(ResultSet izjavaSvjedoka,int id){
        try {
            izjavaSvjedoka.next();
            return new IzjavaSvjedoka(id,new Svjedok(izjavaSvjedoka.getString(1),izjavaSvjedoka.getString(2),izjavaSvjedoka.getString(3),izjavaSvjedoka.getString(4)),izjavaSvjedoka.getString(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Izvještaj> dajSveIzvjestaje(){
        ArrayList<Izvještaj> povratna = new ArrayList<>();
        try {
            var resultSet = dajBrojInspektora.executeQuery();
            resultSet.next();
            var brInstuktora = resultSet.getInt(1);
            for(int i=2;i<=brInstuktora;i++){
                Korisnik k =kreirajInspektora(i);
                povratna.addAll(dajSveIzvjestajeOdInspektora(k));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return povratna;
    }
    private Korisnik kreirajInspektora(int Id){
        Korisnik povratni = null;
        try {
            dajKorisnikaID.setInt(1,Id);
            ResultSet rs = dajKorisnikaID.executeQuery();
            if(rs.next()){
                povratni = new Korisnik(Id,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5)==1,rs.getString(6),rs.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return povratni;
    }
    public ArrayList<ObrazovnaInstitucija> obrazovneInstitucijeSuggestion(String naziv){
        ArrayList<ObrazovnaInstitucija> povratna = new ArrayList<>();
        try {
            obrazovnaInstitucijaSuggestion.setString(1,naziv+"%");
            ResultSet rs = obrazovnaInstitucijaSuggestion.executeQuery();
            while(rs.next()){
                povratna.add(new ObrazovnaInstitucija(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return povratna;
    }
    public  boolean postojiLiEmail(String email, int id){
        boolean brMailova=true;
        try {
            emailPostoji.setString(1,email);
            ResultSet rs =emailPostoji.executeQuery();
            if(rs.next() && rs.getInt(1)!=id){
                brMailova=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brMailova;
    }
    public boolean postojiLiBrojTelefona(String brojTelefona, int id){
        boolean telefonValidan=true;
        try {
            telefonPostoji.setString(1,brojTelefona);
            ResultSet rs = telefonPostoji.executeQuery();
            if(rs.next() && rs.getInt(1)!=id) telefonValidan=false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return telefonValidan;
    }
    public void azurirajInspektora(Korisnik k){
        try {
            azurirajInspektoraUpit.setString(1,k.getIme());
            azurirajInspektoraUpit.setString(2,k.getPrezime());
            azurirajInspektoraUpit.setString(3,k.getBrojTelefona());
            azurirajInspektoraUpit.setString(4,k.getEmail());
            azurirajInspektoraUpit.setString(5,k.getUsername());
            azurirajInspektoraUpit.setString(6,k.getPassword());
            azurirajInspektoraUpit.setInt(7,k.isAdministrator()?1:0);
            azurirajInspektoraUpit.setInt(8,k.getId());
            azurirajInspektoraUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void obrisiInspektora(Korisnik k){
        try {
            obrisiInspektoraUpit.setInt(1,k.getId());
            obrisiInspektoraUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
