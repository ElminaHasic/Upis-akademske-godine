package prodekan;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.TimeZone;

public class ProfilProdekana {
	static private JFrame frame;
	static private JLabel title = new JLabel("FAKULTET ELEKTROTEHNIKE");

	static private JButton nazadProfilProd = new JButton("Nazad");
	static public JButton logout = new JButton("Odjava");

	// Azuriranje zvanja nastavnika
	static private JLabel titleforAzurirajZvanjeNast = new JLabel("Azuriranje zvanja nastavnika");

	static private JLabel OdabirZvanja = new JLabel("Odaberite novo zvanje nastavnika:");
	static private JButton docent = new JButton("1. Docent");
	static private JButton redovni = new JButton("2. Redovni");
	static private JButton vanredni = new JButton("3. Vanredni ");
	static private JButton potvrdiAzuriranjeZvanja = new JButton("Potvrdi");
	static private String novoZvanje = "";
	static private String SifraProf = "";
	static private JLabel listaNastAzurZvanjal = new JLabel("Unesite ime nastavnika cije zvanje zelite promijeniti:");
	static private JLabel odaberiNastAzurZvanjal = new JLabel("Odaberite nastavnika cije zvanje zelite promijeniti:");
	static private JTextField searchFieldAzurZvanje = new JTextField();
	static private JComboBox<String> listaNastAzurZvanja = new JComboBox<>();

	static private JComboBox<String> podacicombobox = new JComboBox<>();
	static private JLabel podacionastpredmetima = new JLabel("Odabrani nastavnik predaje na predmetima:");

	static private final JLabel NastAzurZvanjeL = new JLabel("Nastavnik: ");
	static private JLabel NastAzurZvanjeInfo = new JLabel("");
	static private String imeNastAzurZvanje = "";
	static private String prezNastAzurZvanje = "";
	static private String novozvanje2 = "";

	private static void updateSecondComboBox(JComboBox<String> comboBox, String sifra) {
		comboBox.removeAllItems();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection dataBase = DriverManager
					.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
							+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");
			String select;

			select = "select naziv from predmet inner join nastavnikNaPredmetu on predmet.sifPred = nastavnikNaPredmetu.sifPred inner join nastavnik on \n"
					+ " nastavnik.sifNast = nastavnikNaPredmetu.sifNast \n" + " where nastavnik.sifNast = ?";
			PreparedStatement stm = dataBase.prepareStatement(select);
			int sifraa = Integer.parseInt(sifra);
			stm.setInt(1, sifraa);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String nazivPred = rs.getString("naziv");
				comboBox.addItem(nazivPred);
			}

			select = "select imeNast, prezNast, zvanje from nastavnik where sifNast = ?";
			PreparedStatement stmtupdate = dataBase.prepareStatement(select);
			stmtupdate.setInt(1, Integer.parseInt(SifraProf));
			ResultSet rs1 = stmtupdate.executeQuery();
			while (rs1.next()) {
				imeNastAzurZvanje = rs1.getString("imeNast");
				prezNastAzurZvanje = rs1.getString("prezNast");
				String zvanjeNast = rs1.getString("zvanje");
				String nast = imeNastAzurZvanje + " " + prezNastAzurZvanje + ", " + zvanjeNast;
				NastAzurZvanjeInfo.setText(nast);
			}

			dataBase.close();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	static void goToAzurirajZvanjeNast() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setBounds(500, 200, 800, 680);
		frame.add(title);
		frame.add(nazadProfilProd);
		nazadProfilProd.setBounds(30, 600, 100, 25);
		frame.add(podacionastpredmetima);
		frame.add(titleforAzurirajZvanjeNast);
		frame.add(NastAzurZvanjeL);
		frame.add(NastAzurZvanjeInfo);
		frame.add(listaNastAzurZvanjal);
		frame.add(listaNastAzurZvanja);
		frame.add(podacicombobox);
		frame.add(searchFieldAzurZvanje);
		frame.add(odaberiNastAzurZvanjal);
		frame.add(OdabirZvanja);
		frame.add(docent);
		frame.add(redovni);
		frame.add(vanredni);
		frame.add(potvrdiAzuriranjeZvanja);

	}
	// Azuriranje preduslova za predmete

	static private JLabel titleforAzurirajPreduslove = new JLabel("Azuriranje preduslova za predmete");
	static private JLabel LOdaberiPreduslov = new JLabel("Odaberite predmete koji su preduslovi:");

	static private int sifraPredZaAzurPred = 0;
	static private JButton PotvrdaAzurPreduslova = new JButton("Dodaj preduslov");
	static private JButton BrisanjeAzurPreduslova = new JButton("Obrisi preduslov");
	static private JLabel PregledPredmetaAzurPred = new JLabel("Odaberite predmet cije preduslove zelite azurirati:");
	static private JTextField searchPredAzurPredusl = new JTextField();
	static private JComboBox<String> PreglPredZaAzurPredusl = new JComboBox<>();
	static private JComboBox<String> dynamicComboBox = new JComboBox<>();
	static private JComboBox<String> preduslovicombo = new JComboBox<>();
	static private JTextField searchPreduslovi = new JTextField();
	static private JLabel ListaPreduslova = new JLabel("Trenutni podaci za odabrani predmet:");
	static private JLabel nosiocL = new JLabel("Nosioc predmeta:");
	static private JLabel imenosiocL = new JLabel();
	static private JLabel trpreduslovi = new JLabel("Preduslovi za odabrani predmet:");
	static private JTextField searchTrenutniPreduslovi = new JTextField();
	static private DefaultListModel<String> listModel = new DefaultListModel<>();
	static private JList<String> dataList = new JList<>(listModel);
	static private JButton removeButton = new JButton("Ukloni iz liste odabranih preduslova");
	static private JScrollPane scrollPane = new JScrollPane(dataList);
	static private JButton obirisiSvePredusl = new JButton("Obrisi sve trenutne preduslove");

	static void goToAzurirajPreduslove() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setBounds(500, 100, 800, 900);
		frame.add(title);
		frame.add(nazadProfilProd);
		nazadProfilProd.setBounds(30, 820, 100, 25);
		frame.add(nosiocL);
		frame.add(imenosiocL);
		frame.add(trpreduslovi);
		frame.add(dynamicComboBox);
		frame.add(searchTrenutniPreduslovi);
		frame.add(titleforAzurirajPreduslove);
		frame.add(PregledPredmetaAzurPred);
		frame.add(searchPredAzurPredusl);
		frame.add(PreglPredZaAzurPredusl);
		frame.add(ListaPreduslova);
		frame.add(LOdaberiPreduslov);
		frame.add(searchPreduslovi);
		frame.add(preduslovicombo);
		frame.add(scrollPane);
		frame.add(removeButton);
		frame.add(PotvrdaAzurPreduslova);
		frame.add(BrisanjeAzurPreduslova);
		frame.add(obirisiSvePredusl);

	}

	// Definisanje plana realizacije nastave

	static private JLabel titleforDefPlanRealizacije = new JLabel("Definisanje plana realizacije nastave");

	static private JComboBox<String> listaNastavnika = new JComboBox<>();
	static private JComboBox<String> listaPredmeta = new JComboBox<>();
	static private JLabel pregledProfL = new JLabel("Nastavnici na predmetu:");
	static private JLabel trenutnipodaciL = new JLabel("Trenutni podaci za odabrani predmet :");
	static private JLabel pregledPred = new JLabel("Odaberite predmet:");

	static private JTextField pretraziPredmete = new JTextField();
	static private JTextField pretraziTrenutneNast = new JTextField();

	static private JTextField pretraziNastavnika = new JTextField();

	static private JButton PotvNosiocPred = new JButton("Dodaj nosioca predmeta");
	static private JButton PotvDodajNast = new JButton("Dodaj nastavnika na predmet");
	static private JButton DeleteNast = new JButton("Ukloni sve nastavnike sa predmeta");
	static private JButton UkloniNastB = new JButton("Ukloni nastavnika sa predmeta");
	static private JButton deletelist = new JButton("Ukloni iz liste odabranih nastavnika");
	static private JLabel nosilacL = new JLabel("Nosilac predmeta:");
	static private JLabel nosilacime = new JLabel("");
	static private JComboBox<String> trenutniNascombo = new JComboBox<>();
	static private int DefPlanSifPred = 0;
	static private JLabel odnastzapredL = new JLabel("Odaberite nastavnika za predmet:");

	static private DefaultListModel<String> listModel2 = new DefaultListModel<>();
	static private JList<String> dataList2 = new JList<>(listModel2);
	static private JScrollPane scpane2 = new JScrollPane(dataList2);
	static private JLabel odNosiocPred = new JLabel("Odaberite nosioca za predmet:");
	static private JTextField pretraziNosioca = new JTextField();
	static private JComboBox<String> nosiocicombo = new JComboBox();

	static void goToDefPlanRealizacije() {
		frame.setBounds(400, 100, 1070, 700);
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.add(title);
		frame.add(nazadProfilProd);
		nazadProfilProd.setBounds(30, 620, 100, 25);
		title.setBounds(320, 40, 500, 30);
		frame.add(titleforDefPlanRealizacije);
		frame.add(pregledPred);
		frame.add(pretraziPredmete);
		frame.add(listaPredmeta);
		frame.add(trenutnipodaciL);
		frame.add(pregledProfL);
		frame.add(nosilacL);
		frame.add(nosilacime);
		frame.add(pretraziTrenutneNast);
		frame.add(trenutniNascombo);
		frame.add(DeleteNast);
		frame.add(odnastzapredL);
		frame.add(pretraziNastavnika);
		frame.add(listaNastavnika);
		frame.add(scpane2);
		frame.add(deletelist);
		frame.add(PotvDodajNast);
		frame.add(UkloniNastB);
		frame.add(odNosiocPred);
		frame.add(pretraziNosioca);
		frame.add(nosiocicombo);
		frame.add(PotvNosiocPred);

	}

	// Definisanje trajanja perioda prijave
	static private JLabel DefPeriod = new JLabel("Definisanje perioda trajanja registracije predmeta");
	static private JLabel PocRegL = new JLabel("Trenutno definisan datum pocetka registracije:");
	static private JLabel PocRegInfo = new JLabel("");
	static private JLabel KrajRegL = new JLabel("Trenutno definisan datum kraja registracije:");
	static private JLabel KrajRegInfo = new JLabel("");
	static private JButton PocRegButton = new JButton("Odaberite novi datum pocetka registracije");
	static private JLabel NoviDatumPocRegL = new JLabel("Novi odabrani datum:");

	static private JLabel NoviDatumPocRegInfo = new JLabel("");
	static private JButton KrajRegButton = new JButton("Odaberite novi datum kraja registracije");
	static private JLabel NoviDatumKrajRegL = new JLabel("Novi odabrani datum:");
	static private JLabel NoviDatumKrajRegInfo = new JLabel("");
	static private JButton PotvrdiNoviDatum = new JButton("Potvrdi");
	static private int dp = 2;
	static private int dk = 2;

	static private void showCalendar(JLabel dateField) {
		JDialog calendarDialog = new JDialog(frame, "Kalendar", true);
		calendarDialog.setLayout(new BorderLayout());
		CalendarPanel calendarPanel = new CalendarPanel(dateField);
		calendarDialog.add(calendarPanel, BorderLayout.CENTER);
		calendarDialog.pack();
		calendarDialog.setVisible(true);
	}

	static void DefinisanjePerodaTrajanjaReg() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setBounds(500, 200, 730, 580);
		frame.add(title);
		frame.add(nazadProfilProd);
		nazadProfilProd.setBounds(30, 500, 100, 25);
		title.setBounds(185, 40, 500, 30);
		frame.add(DefPeriod);
		frame.add(PocRegL);
		frame.add(PocRegInfo);
		frame.add(KrajRegL);
		frame.add(KrajRegInfo);
		frame.add(PocRegButton);
		frame.add(NoviDatumPocRegL);
		frame.add(NoviDatumPocRegInfo);
		frame.add(KrajRegButton);
		frame.add(NoviDatumKrajRegL);
		frame.add(NoviDatumKrajRegInfo);
		frame.add(PotvrdiNoviDatum);

	}

	// Pregled zahtjeva studenata
	static private JLabel PreglZaht = new JLabel("Pregled zahtjeva za promjenu registrovanih predmeta");
	static private JLabel studPredzahtjeviL = new JLabel(
			"<html>Studenti su poslali zahtjev za zamjenu sljedecih predmeta, za pregled zahtjeva odaberite predmet:</html>");
	static private JTextField pretragaPredZaZahtjev = new JTextField();
	static private JComboBox<String> PredZaZahtjevCombo = new JComboBox<>();
	static private JLabel OdabPredZahtjevL = new JLabel("Odabrani predmet:");
	static private JLabel OdabPredZahtjevInfo = new JLabel("");
	static private JLabel NosilacPredZahtjevL = new JLabel("Nosioc predmeta:");
	static private JLabel NosilacPredZahtjevInfo = new JLabel("");
	static private JLabel SemestarZahtjeviL = new JLabel("Semestar:");
	static private JLabel SemestarZahtjeviInfo = new JLabel("");
	static private JLabel StudZahtjeviL = new JLabel(
			"<html>Studenti koji su poslali zahtjev za  promjenu odabranog predmeta:</html>");
	static private JTextField PretragaStudZahtjevi = new JTextField();
	static private JComboBox<String> StudZahtjeviCombo = new JComboBox<>();

	static private JLabel PodOZahtjL = new JLabel("Podaci o odabranom zahtjevu:");

	static private final JLabel ZahtPoslStudL = new JLabel("Student:");
	static private JLabel ZahtPoslStudInfo = new JLabel("");
	static private final JLabel IndeksStudZahtL = new JLabel("Indeks studenta:");
	static private JLabel IndeksStudZahtInfo = new JLabel("");
	static private JLabel PredZaZamjL = new JLabel("Student zeli zamijeniti predmet:");
	static private JLabel PredZaZamjInfo = new JLabel("");
	static private JLabel NoviPredZahtL = new JLabel("Novi predmet kojeg student zeli odabrati:");
	static private JLabel NoviPredZahtInfo = new JLabel("");
	static private JLabel OpisZahtL = new JLabel("Obrazlozenje studenta za poslani zahtjev:");
	static private JTextPane textOpis = new JTextPane();
	static private JScrollPane OpisStudZahtPane = new JScrollPane(textOpis);
	static private JLabel ZahtPrihvL = new JLabel("Zahtjev prihvacen:");
	static private JLabel ZahtPrihvInfo = new JLabel("");
	static private JLabel OdgovorZahtL = new JLabel("Odgovor na zahtjev:");

	static private JTextPane OdgovorZahtInfo = new JTextPane();
	static private JScrollPane OdgZahtPane = new JScrollPane(OdgovorZahtInfo);
	static private JButton PotvrdiOdgZahtjevB = new JButton("Potvrdi");
	static private int SifPredZaZahtjev = 0;

	static int ectsPred1 = 0;
	static private ArrayList<Integer> listasifri = new ArrayList<>();
	static private String SemestarPredZaZaht;
	static private String SemestarPredZaZaht1;
	static private String OdgovorNaZahtjev;
	static private int IndeksStudZahtjev = 0;
	static private int sifPred2 = 0;

	static void PregledZahtStud() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setBounds(500, 200, 1070, 750);
		frame.add(title);
		frame.add(nazadProfilProd);
		nazadProfilProd.setBounds(30, 670, 100, 25);
		title.setBounds(350, 40, 500, 30);
		frame.add(PreglZaht);
		frame.add(studPredzahtjeviL);
		frame.add(pretragaPredZaZahtjev);
		frame.add(PredZaZahtjevCombo);
		frame.add(OdabPredZahtjevL);
		frame.add(OdabPredZahtjevInfo);
		frame.add(NosilacPredZahtjevL);
		frame.add(NosilacPredZahtjevInfo);
		frame.add(SemestarZahtjeviL);
		frame.add(SemestarZahtjeviInfo);
		frame.add(StudZahtjeviL);
		frame.add(PretragaStudZahtjevi);
		frame.add(StudZahtjeviCombo);
		frame.add(PodOZahtjL);
		frame.add(ZahtPoslStudL);
		frame.add(ZahtPoslStudInfo);
		frame.add(IndeksStudZahtL);
		frame.add(IndeksStudZahtInfo);
		frame.add(PredZaZamjL);
		frame.add(PredZaZamjInfo);
		frame.add(NoviPredZahtL);
		frame.add(NoviPredZahtInfo);
		frame.add(OpisZahtL);
		frame.add(OpisStudZahtPane);
		frame.add(ZahtPrihvL);
		frame.add(ZahtPrihvInfo);
		frame.add(OdgovorZahtL);
		frame.add(OdgZahtPane);
		frame.add(PotvrdiOdgZahtjevB);
	}

	// Pregled studenata na predmetima
	static private JLabel PreglRegStud = new JLabel("Pregled prijavljenih studenata na predmetima");
	static private JLabel OdaberiPred = new JLabel("Odaberite predmet:");
	static private JTextField pretraziOdaberiPredCombo = new JTextField();
	static private JComboBox<String> OdaberiPredCombo = new JComboBox<>();
	static private JLabel PodOdabPredL = new JLabel("Podaci o predmetu:");
	static private JLabel nosiocPred = new JLabel("Nosilac:");
	static private JLabel imenosiocPred = new JLabel("");
	static private JLabel semestarL = new JLabel("Semestar:");
	static private JLabel semestar = new JLabel("");
	static private JLabel ectsL = new JLabel("ECTS krediti:");
	static private JLabel ects = new JLabel("");
	static private JLabel nastNaPredL = new JLabel("Nastavnici na predmetu:");

	static private JTextField pretraga = new JTextField();
	static private JComboBox<String> comboNast = new JComboBox<>();

	static private JLabel studNaPredL = new JLabel("Prijavljeni studenti:");
	static private JTextField pretragaStud = new JTextField();
	static private JComboBox<String> StudCombo = new JComboBox<>();
	static private JLabel StudInfoL = new JLabel("Odaberite studenta za dodatne informacije:");
	static private JLabel StudImeL = new JLabel("Ime:");
	static private JLabel StudImeInfo = new JLabel("");
	static private JLabel StudPrezL = new JLabel("Prezime:");
	static private JLabel StudPrezInfo = new JLabel("");
	static private JLabel StudIndexL = new JLabel("Indeks:");
	static private JLabel StudIndexInfo = new JLabel("");
	static private JLabel StudStatusL = new JLabel("Status:");
	static private JLabel StudStatusInfo = new JLabel("");
	static private JLabel StudGodStudL = new JLabel("Godina Studija:");
	static private JLabel StudGodStudInfo = new JLabel("");
	static private JLabel StudPrviPutL = new JLabel("Prvi put na predmetu:");
	static private JLabel StudPrviPutInfo = new JLabel("");
	static private int SifPredPreglStud = 0;

	static void PregledRegStud() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setBounds(500, 200, 1070, 600);
		frame.add(title);
		frame.add(nazadProfilProd);
		nazadProfilProd.setBounds(30, 520, 100, 25);
		title.setBounds(330, 35, 400, 30);
		frame.add(PreglRegStud);
		frame.add(OdaberiPred);
		frame.add(pretraziOdaberiPredCombo);
		frame.add(OdaberiPredCombo);
		frame.add(PodOdabPredL);
		frame.add(nosiocPred);
		frame.add(imenosiocPred);
		frame.add(semestarL);
		frame.add(semestar);
		frame.add(ectsL);
		frame.add(ects);
		frame.add(nastNaPredL);
		frame.add(pretraga);
		frame.add(comboNast);
		frame.add(studNaPredL);
		frame.add(pretragaStud);
		frame.add(StudCombo);
		frame.add(StudInfoL);
		frame.add(StudImeL);
		frame.add(StudImeInfo);
		frame.add(StudPrezL);
		frame.add(StudPrezInfo);
		frame.add(StudIndexL);
		frame.add(StudIndexInfo);
		frame.add(StudStatusL);
		frame.add(StudStatusInfo);
		frame.add(StudGodStudL);
		frame.add(StudGodStudInfo);
		frame.add(StudPrviPutL);
		frame.add(StudPrviPutInfo);

	}

	static private JButton azuriranjeZvanjaNastavnika = new JButton("<html>Azuriranje zvanja<br /> nastavnika</html>");
	static private JButton azuriranjePreduslova = new JButton("<html>Azuriranje preduslova<br />za predmete</html>");
	static private JButton definisanjePlanaRealizacije = new JButton(
			"<html>Definisanje plana<br /> realizacije nastave");
	static private JButton definisanjeTrajanjaRegistracije = new JButton(
			"<html>Definisanje perioda<br /> trajanja registracije<br /> predmeta</html>");
	static private JButton pregledZahtjeva = new JButton("<html>Pregled zahtjeva<br /> studenata</html>");
	static private JButton pregledPrijavljenihStudenata = new JButton(
			"<html>Pregled prijavljenih<br /> studenata na<br /> predmetima</html>");

	static private JLabel title2 = new JLabel("Odaberite zeljenu aktivnost");
	static private JLabel profilprodekana = new JLabel("Profil prodekana");

	private static void goBack() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setBounds(500, 200, 800, 455);
		title.setBounds(210, 35, 400, 30);
		logout.setBounds(20, 20, 100, 20);
		frame.add(logout);
		frame.add(title2);
		frame.add(title);
		frame.add(azuriranjeZvanjaNastavnika);
		frame.add(azuriranjePreduslova);
		frame.add(definisanjePlanaRealizacije);
		frame.add(definisanjeTrajanjaRegistracije);
		frame.add(pregledZahtjeva);
		frame.add(pregledPrijavljenihStudenata);
		frame.add(profilprodekana);
	}

	public ProfilProdekana(JFrame mainFrame) {
		frame = mainFrame;
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setBounds(500, 200, 800, 455);
		frame.getContentPane().setBackground(new Color(43, 143, 142));
		Font font1 = new Font("Purisa", Font.BOLD, 20);
		title.setBounds(210, 35, 400, 30);
		title.setForeground(new Color(194, 29, 43));
		title.setFont(font1);
		frame.add(title);
		nazadProfilProd.setBackground(Color.WHITE);
		nazadProfilProd.setForeground(new Color(194, 29, 43));

		font1 = new Font("Purisa", Font.BOLD, 16);
		title2.setBounds(260, 70, 400, 30);
		title2.setForeground(new Color(37, 32, 141));
		title2.setFont(font1);
		frame.add(title2);
		logout.setBounds(20, 20, 100, 20);
		frame.add(logout);
		nazadProfilProd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});

		azuriranjeZvanjaNastavnika.setBounds(60, 125, 200, 110);
		frame.add(azuriranjeZvanjaNastavnika);
		azuriranjePreduslova.setBounds(300, 125, 200, 110);
		frame.add(azuriranjePreduslova);
		definisanjePlanaRealizacije.setBounds(540, 125, 200, 110);
		frame.add(definisanjePlanaRealizacije);
		definisanjeTrajanjaRegistracije.setBounds(60, 250, 200, 110);
		frame.add(definisanjeTrajanjaRegistracije);
		pregledZahtjeva.setBounds(300, 250, 200, 110);
		frame.add(pregledZahtjeva);
		pregledPrijavljenihStudenata.setBounds(540, 250, 200, 110);
		frame.add(pregledPrijavljenihStudenata);

		font1 = new Font("Purisa", Font.BOLD, 14);

		azuriranjeZvanjaNastavnika.setFont(font1);
		azuriranjePreduslova.setFont(font1);
		definisanjePlanaRealizacije.setFont(font1);
		definisanjeTrajanjaRegistracije.setFont(font1);
		pregledZahtjeva.setFont(font1);
		pregledPrijavljenihStudenata.setFont(font1);

		azuriranjeZvanjaNastavnika.setForeground(new Color(194, 29, 43));
		azuriranjePreduslova.setForeground(new Color(194, 29, 43));
		definisanjePlanaRealizacije.setForeground(new Color(194, 29, 43));
		definisanjeTrajanjaRegistracije.setForeground(new Color(194, 29, 43));
		pregledZahtjeva.setForeground(new Color(194, 29, 43));
		pregledPrijavljenihStudenata.setForeground(new Color(194, 29, 43));

		azuriranjeZvanjaNastavnika.setBackground(Color.WHITE);
		azuriranjePreduslova.setBackground(Color.WHITE);
		definisanjePlanaRealizacije.setBackground(Color.WHITE);
		definisanjeTrajanjaRegistracije.setBackground(Color.WHITE);
		pregledZahtjeva.setBackground(Color.WHITE);
		pregledPrijavljenihStudenata.setBackground(Color.WHITE);

		Border newborder = new LineBorder(new Color(37, 32, 141), 4, false);
		azuriranjeZvanjaNastavnika.setBorder(newborder);
		definisanjePlanaRealizacije.setBorder(newborder);
		definisanjeTrajanjaRegistracije.setBorder(newborder);
		pregledPrijavljenihStudenata.setBorder(newborder);
		azuriranjePreduslova.setBorder(newborder);
		pregledZahtjeva.setBorder(newborder);

		azuriranjeZvanjaNastavnika.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				azuriranjeZvanjaNastavnika.setBackground(new Color(51, 173, 255));
				azuriranjeZvanjaNastavnika.setBounds(55, 120, 210, 120);
			}

			public void mouseExited(MouseEvent e) {
				azuriranjeZvanjaNastavnika.setBackground(Color.WHITE);
				azuriranjeZvanjaNastavnika.setBounds(60, 125, 200, 110);
			}
		});
		azuriranjePreduslova.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				azuriranjePreduslova.setBackground(new Color(51, 173, 255));
				azuriranjePreduslova.setBounds(295, 120, 210, 120);
			}

			public void mouseExited(MouseEvent e) {
				azuriranjePreduslova.setBackground(Color.WHITE);
				azuriranjePreduslova.setBounds(300, 125, 200, 110);
			}
		});

		definisanjePlanaRealizacije.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				definisanjePlanaRealizacije.setBackground(new Color(51, 173, 255));
				definisanjePlanaRealizacije.setBounds(535, 120, 210, 120);
			}

			public void mouseExited(MouseEvent e) {
				definisanjePlanaRealizacije.setBackground(Color.WHITE);
				definisanjePlanaRealizacije.setBounds(540, 125, 200, 110);
			}
		});

		definisanjeTrajanjaRegistracije.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				definisanjeTrajanjaRegistracije.setBackground(new Color(51, 173, 255));
				definisanjeTrajanjaRegistracije.setBounds(55, 245, 210, 120);
			}

			public void mouseExited(MouseEvent e) {
				definisanjeTrajanjaRegistracije.setBackground(Color.WHITE);
				definisanjeTrajanjaRegistracije.setBounds(60, 250, 200, 110);
			}
		});

		pregledZahtjeva.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				pregledZahtjeva.setBackground(new Color(51, 173, 255));
				pregledZahtjeva.setBounds(295, 245, 210, 120);
			}

			public void mouseExited(MouseEvent e) {
				pregledZahtjeva.setBackground(Color.WHITE);
				pregledZahtjeva.setBounds(300, 250, 200, 110);
			}
		});

		pregledPrijavljenihStudenata.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent e) {
				pregledPrijavljenihStudenata.setBackground(new Color(51, 173, 255));
				pregledPrijavljenihStudenata.setBounds(535, 245, 210, 120);
			}

			public void mouseExited(MouseEvent e) {
				pregledPrijavljenihStudenata.setBackground(Color.WHITE);
				pregledPrijavljenihStudenata.setBounds(540, 250, 200, 110);
			}
		});

		profilprodekana.setBounds(60, 370, 200, 20);
		profilprodekana.setFont(font1);
		profilprodekana.setForeground(Color.WHITE);
		frame.add(profilprodekana);

		Font font2 = new Font("Purisa", Font.BOLD, 17);
		Font font3 = new Font("Purisa", Font.BOLD, 15);
		Font font4 = new Font("Purisa", Font.BOLD, 12);

		nazadProfilProd.setFont(font3);

		// Azuriraj Zvanje Nastavnika

		azuriranjeZvanjaNastavnika.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				goToAzurirajZvanjeNast();
				titleforAzurirajZvanjeNast.setBounds(240, 110, 400, 30);
				titleforAzurirajZvanjeNast.setForeground(new Color(37, 32, 141));
				titleforAzurirajZvanjeNast.setFont(font2);

				odaberiNastAzurZvanjal.setBounds(160, 175, 500, 25);
				searchFieldAzurZvanje.setForeground(new Color(37, 32, 141));
				searchFieldAzurZvanje.setFont(font3);
				searchFieldAzurZvanje.setBackground(Color.WHITE);

				searchFieldAzurZvanje.setBounds(160, 215, 400, 25);
				odaberiNastAzurZvanjal.setFont(font3);
				odaberiNastAzurZvanjal.setForeground(new Color(37, 32, 141));

				listaNastAzurZvanja.setBounds(160, 270, 400, 30);
				listaNastAzurZvanja.setForeground(new Color(37, 32, 141));
				listaNastAzurZvanja.setFont(font3);
				listaNastAzurZvanja.setBackground(Color.WHITE);

				NastAzurZvanjeL.setBounds(160, 315, 150, 25);
				NastAzurZvanjeInfo.setBounds(260, 315, 500, 25);
				NastAzurZvanjeL.setForeground(new Color(37, 32, 141));
				NastAzurZvanjeInfo.setForeground(Color.WHITE);
				NastAzurZvanjeInfo.setFont(font3);
				NastAzurZvanjeL.setFont(font3);

				podacionastpredmetima.setBounds(160, 350, 400, 25);
				podacionastpredmetima.setForeground(new Color(37, 32, 141));
				podacionastpredmetima.setFont(font3);

				podacicombobox.setBounds(160, 393, 400, 30);
				podacicombobox.setForeground(Color.WHITE);
				podacicombobox.setFont(font3);
				podacicombobox.setBackground(new Color(43, 143, 142));

				OdabirZvanja.setFont(font3);
				OdabirZvanja.setBounds(160, 450, 500, 30);
				OdabirZvanja.setForeground(new Color(37, 32, 141));

				docent.setForeground(Color.white);
				docent.setBounds(160, 510, 115, 25);
				docent.setBackground(new Color(43, 143, 142));
				docent.setFont(font3);
				Border border2 = new LineBorder(Color.WHITE, 2, false);
				docent.setBorder(border2);
				podacicombobox.setBorder(border2);

				redovni.setForeground(Color.WHITE);
				redovni.setBounds(300, 510, 115, 25);
				redovni.setBackground(new Color(43, 143, 142));
				redovni.setFont(font3);
				redovni.setBorder(border2);

				vanredni.setForeground(Color.WHITE);
				vanredni.setBounds(440, 510, 120, 25);
				vanredni.setBackground(new Color(43, 143, 142));
				vanredni.setFont(font3);
				vanredni.setBorder(border2);

				docent.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						novoZvanje = "docent";

					}
				});

				vanredni.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						novoZvanje = "vanredni profesor";

					}
				});

				redovni.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						novoZvanje = "redovni profesor";

					}
				});

				potvrdiAzuriranjeZvanja.setFont(font3);
				potvrdiAzuriranjeZvanja.setBackground(Color.WHITE);
				potvrdiAzuriranjeZvanja.setBorder(border2);
				potvrdiAzuriranjeZvanja.setForeground(new Color(37, 32, 141));
				potvrdiAzuriranjeZvanja.setBounds(330, 585, 100, 25);

				listaNastAzurZvanja.removeAllItems();

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

					Statement stmt = dataBase.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT imeNast, prezNast, sifNast , zvanje FROM nastavnik");

					ArrayList<String> items = new ArrayList<>();
					String opcion;

					while (rs.next()) {
						String ime = rs.getString("imeNast");
						String prezime = rs.getString("prezNast");
						int sifraNast = rs.getInt("sifNast");
						String zvanjeNast = rs.getString("zvanje");
						opcion = ime + " " + prezime + ", " + zvanjeNast + " (" + sifraNast + ")";
						listaNastAzurZvanja.addItem(opcion);
					}

					dataBase.close();
					podacicombobox.removeAllItems();
					NastAzurZvanjeInfo.setText("");

				} catch (Exception exc) {
					exc.printStackTrace();
				}

				listaNastAzurZvanja.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String selected = (String) listaNastAzurZvanja.getSelectedItem();
						if (selected != null && selected.length() >= 6) {
							SifraProf = selected.substring(selected.length() - 6);
							SifraProf = SifraProf.substring(0, 5);
							updateSecondComboBox(podacicombobox, SifraProf);
						}
					}
				});

				searchFieldAzurZvanje.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ArrayList<String> items = new ArrayList<>();

						String provjera = searchFieldAzurZvanje.getText();
						if (!provjera.equals("")) {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt
										.executeQuery("SELECT imeNast, prezNast, sifNast , zvanje FROM nastavnik");
								items.clear();
								while (rs.next()) {
									String ime = rs.getString("imeNast");
									String prezime = rs.getString("prezNast");
									int sifraNast = rs.getInt("sifNast");
									String zvanjeNast = rs.getString("zvanje");
									String item = ime + " " + prezime + ", " + zvanjeNast + " (" + sifraNast + ")";
									items.add(item);
								}

								String enteredText = searchFieldAzurZvanje.getText();
								DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) listaNastAzurZvanja
										.getModel();
								if (model != null) {
									model.removeAllElements();
									for (String item : items) {
										if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
											model.addElement(item);
										}
									}
								}

							} catch (Exception exception) {
								exception.printStackTrace();
							}
						} else {

							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt
										.executeQuery("SELECT imeNast, prezNast, sifNast , zvanje FROM nastavnik");
								items.clear();
								listaNastAzurZvanja.removeAllItems();
								while (rs.next()) {
									String ime = rs.getString("imeNast");
									String prezime = rs.getString("prezNast");
									int sifraNast = rs.getInt("sifNast");
									String zvanjeNast = rs.getString("zvanje");
									String item = ime + " " + prezime + ", " + zvanjeNast + " (" + sifraNast + ")";
									items.add(item);
									listaNastAzurZvanja.addItem(item);
								}

							}

							catch (Exception exception) {
								exception.printStackTrace();
							}
							podacicombobox.removeAllItems();
							NastAzurZvanjeInfo.setText("");
						}

					}

				});

			}
		});

		potvrdiAzuriranjeZvanja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				boolean check = true;
				if (SifraProf.equals("")) {
					check = false;
					JOptionPane.showMessageDialog(null, "Neuspjesno azuriranje! Potrebno je da odaberete nastavnika.",
							"Poruka", 0);
				}

				else if (novoZvanje.equals("")) {
					check = false;
					JOptionPane.showMessageDialog(null, "Niste odabrali novo zvanje nastavnika!", "Poruka o gresci", 0);
				}

				else {

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						String update = "UPDATE nastavnik SET zvanje = ?  WHERE sifNast = ?";
						PreparedStatement stmtupdate = dataBase.prepareStatement(update);
						dataBase.setAutoCommit(false);
						dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
						stmtupdate.setString(1, novoZvanje);
						stmtupdate.setInt(2, Integer.parseInt(SifraProf));
						stmtupdate.executeUpdate();
						dataBase.commit();

						novozvanje2 = novoZvanje;
						novoZvanje = "";
						dataBase.close();
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Neuspjesno azuriranje!", "Poruka", 0);
						check = false;
					}
				}
				if (check) {
					String poruka = "Uspjesno ste azuriranje zvanje nastavnika. \n" + imeNastAzurZvanje + " "
							+ prezNastAzurZvanje + ", " + novozvanje2;
					JOptionPane.showMessageDialog(null, poruka, "Poruka", 1);
					String newItem = imeNastAzurZvanje + " " + prezNastAzurZvanje + ", " + novozvanje2 + " ("
							+ SifraProf + ")";

					int selectedIndex = listaNastAzurZvanja.getSelectedIndex();
					listaNastAzurZvanja.removeItemAt(selectedIndex);
					listaNastAzurZvanja.insertItemAt(newItem, selectedIndex);
					listaNastAzurZvanja.setSelectedIndex(selectedIndex);
					NastAzurZvanjeInfo.setText(imeNastAzurZvanje + " " + prezNastAzurZvanje + ", " + novozvanje2);
				}
			}
		});

		// Azuriranje preduslova za predmete

		azuriranjePreduslova.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToAzurirajPreduslove();

				titleforAzurirajPreduslove.setBounds(205, 75, 400, 30);
				titleforAzurirajPreduslove.setForeground(new Color(37, 32, 141));
				titleforAzurirajPreduslove.setFont(font2);

				PregledPredmetaAzurPred.setBounds(160, 125, 550, 30);
				PregledPredmetaAzurPred.setForeground(new Color(37, 32, 141));
				PregledPredmetaAzurPred.setFont(font3);

				searchPredAzurPredusl.setBounds(180, 170, 400, 25);
				searchPredAzurPredusl.setForeground(new Color(37, 32, 141));
				searchPredAzurPredusl.setFont(font3);

				PreglPredZaAzurPredusl.setBounds(180, 205, 400, 27);
				PreglPredZaAzurPredusl.setForeground(new Color(37, 32, 141));
				PreglPredZaAzurPredusl.setFont(font3);
				PreglPredZaAzurPredusl.setBackground(Color.WHITE);

				ListaPreduslova.setBounds(180, 246, 500, 20);
				ListaPreduslova.setForeground(new Color(37, 32, 141));
				ListaPreduslova.setFont(font3);

				nosiocL.setBounds(180, 275, 200, 20);
				nosiocL.setForeground(new Color(37, 32, 141));
				nosiocL.setFont(font3);

				imenosiocL.setBounds(340, 275, 300, 20);
				imenosiocL.setForeground(Color.WHITE);
				imenosiocL.setFont(font3);

				trpreduslovi.setBounds(180, 305, 300, 20);
				trpreduslovi.setForeground(new Color(37, 32, 141));
				trpreduslovi.setFont(font3);

				searchTrenutniPreduslovi.setBounds(180, 335, 400, 25);
				searchTrenutniPreduslovi.setForeground(new Color(37, 32, 141));
				searchTrenutniPreduslovi.setFont(font3);

				dynamicComboBox.setBounds(180, 370, 400, 27);
				dynamicComboBox.setForeground(Color.WHITE);
				dynamicComboBox.setFont(font3);
				Border border = new LineBorder(Color.WHITE, 1, false);
				dynamicComboBox.setBorder(border);
				dynamicComboBox.setBackground(new Color(43, 143, 142));

				LOdaberiPreduslov.setBounds(180, 480, 500, 20);
				LOdaberiPreduslov.setForeground(new Color(37, 32, 141));
				LOdaberiPreduslov.setFont(font3);

				searchPreduslovi.setBounds(180, 510, 400, 25);
				searchPreduslovi.setForeground(new Color(37, 32, 141));
				searchPreduslovi.setFont(font3);

				preduslovicombo.setBounds(180, 545, 400, 25);
				preduslovicombo.setBackground(Color.WHITE);
				preduslovicombo.setForeground(new Color(37, 32, 141));
				preduslovicombo.setFont(font3);

				dataList.setForeground(new Color(37, 32, 141));
				dataList.setFont(font3);

				scrollPane.setBounds(180, 580, 400, 100);
				scrollPane.setForeground(new Color(37, 32, 141));
				scrollPane.setFont(font3);

				removeButton.setBounds(180, 700, 400, 25);
				removeButton.setBackground(Color.WHITE);
				removeButton.setForeground(new Color(37, 32, 141));
				removeButton.setFont(font3);

				PotvrdaAzurPreduslova.setFont(font3);
				PotvrdaAzurPreduslova.setBackground(Color.WHITE);
				PotvrdaAzurPreduslova.setForeground(new Color(37, 32, 141));
				PotvrdaAzurPreduslova.setBounds(180, 740, 190, 25);

				BrisanjeAzurPreduslova.setFont(font3);
				BrisanjeAzurPreduslova.setBackground(Color.WHITE);
				BrisanjeAzurPreduslova.setForeground(new Color(37, 32, 141));
				BrisanjeAzurPreduslova.setBounds(387, 740, 190, 25);

				obirisiSvePredusl.setFont(font3);
				obirisiSvePredusl.setBackground(Color.WHITE);
				obirisiSvePredusl.setForeground(new Color(37, 32, 141));
				obirisiSvePredusl.setBounds(180, 405, 400, 25);

				PreglPredZaAzurPredusl.removeAllItems();
				sifraPredZaAzurPred = 0;
				imenosiocL.setText("");
				dataList.removeAll();
				dynamicComboBox.removeAllItems();
				preduslovicombo.removeAllItems();
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

					Statement stmt = dataBase.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT naziv, sifPred FROM predmet");
					while (rs.next()) {
						String naziv = rs.getString("naziv");
						int sifra = rs.getInt("sifPred");
						String item = naziv + " (" + sifra + ")";
						PreglPredZaAzurPredusl.addItem(item);
					}
					imenosiocL.setText("");
					preduslovicombo.removeAllItems();
					dynamicComboBox.removeAllItems();
					sifraPredZaAzurPred = 0;

					dataBase.close();

				} catch (Exception exc) {
					exc.printStackTrace();
				}

				searchPredAzurPredusl.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						ArrayList<String> items = new ArrayList<>();
						String provjera = searchPredAzurPredusl.getText();
						if (!provjera.equals("")) {
							boolean check = true;
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt.executeQuery("SELECT naziv, sifPred FROM predmet");
								items.clear();
								PreglPredZaAzurPredusl.removeAllItems();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " (" + sifra + ")";
									PreglPredZaAzurPredusl.addItem(item);
									items.add(item);
								}

								String enteredText = searchPredAzurPredusl.getText();
								DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) PreglPredZaAzurPredusl
										.getModel();
								if (model != null) {
									model.removeAllElements();
									for (String item : items) {
										if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
											model.addElement(item);
										}
									}
									if (model.getSize() > 0) {
										PreglPredZaAzurPredusl.setSelectedIndex(0);
									}
								}

								if (PreglPredZaAzurPredusl.getItemCount() == 0) {
									imenosiocL.setText("");
								}
							} catch (Exception exception) {
								exception.printStackTrace();
								check = false;
							}
							if (check) {
								if (PreglPredZaAzurPredusl.getItemCount() == 0) {
									preduslovicombo.removeAllItems();
									dynamicComboBox.removeAllItems();
								}
							}
						} else {

							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt.executeQuery("SELECT naziv, sifPred FROM predmet");
								items.clear();
								PreglPredZaAzurPredusl.removeAllItems();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " ( " + sifra + ")";
									PreglPredZaAzurPredusl.addItem(item);
									items.add(item);
								}
							} catch (Exception exception) {
								exception.printStackTrace();
							}

							sifraPredZaAzurPred = 0;
							imenosiocL.setText("");
							dynamicComboBox.removeAllItems();
							preduslovicombo.removeAllItems();
						}
					}

				});

				searchPreduslovi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						ArrayList<String> items2 = new ArrayList<>();
						String provjera = searchPreduslovi.getText();
						if (!provjera.equals("")) {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								String upit1 = "SELECT sifPred, naziv  FROM predmet WHERE predmet.sifPred <> ? ";
								PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
								stmt1.setInt(1, sifraPredZaAzurPred);
								ResultSet rs = stmt1.executeQuery();

								items2.clear();
								preduslovicombo.removeAllItems();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " (" + sifra + ")";
									items2.add(item);
								}

								String enteredText = searchPreduslovi.getText();
								DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) preduslovicombo
										.getModel();
								if (model != null) {
									model.removeAllElements();
									for (String item : items2) {
										if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
											model.addElement(item);
										}
									}
									if (model.getSize() > 0) {
										preduslovicombo.setSelectedIndex(0);
									}
								}
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						} else {

							int count = listModel.getSize();
							boolean check = true;
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								String upit1 = "SELECT sifPred, naziv  FROM predmet WHERE predmet.sifPred <> ? ";
								PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
								stmt1.setInt(1, sifraPredZaAzurPred);
								ResultSet rs = stmt1.executeQuery();
								items2.clear();
								preduslovicombo.removeAllItems();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " (" + sifra + ")";
									preduslovicombo.addItem(item);
									items2.add(item);
								}

							} catch (Exception exception) {
								exception.printStackTrace();
								check = false;
							}
							if (check) {
								if (count < listModel.getSize()) {
									listModel.removeElementAt(count);
								}
							}

						}
					}

				});

				PreglPredZaAzurPredusl.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JComboBox<String> combo = (JComboBox<String>) e.getSource();
						Object unos = combo.getSelectedItem();
						String selected = (String) unos;
						if (selected != null && selected.length() >= 7) {
							String sifrapred = selected.substring(selected.length() - 8);
							sifrapred = sifrapred.substring(0, 7);
							sifraPredZaAzurPred = Integer.parseInt(sifrapred);
						}

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String upit2 = "select imeNast, prezNast from nastavnik inner join predmet on nastavnik.sifNast=predmet.nosilac \n"
									+ "where sifPred = ?";
							PreparedStatement st2 = dataBase.prepareStatement(upit2);
							st2.setInt(1, sifraPredZaAzurPred);
							ResultSet rs2 = st2.executeQuery();
							String ime;
							String prez;
							imenosiocL.setText("");
							while (rs2.next()) {
								ime = rs2.getString("imeNast");
								prez = rs2.getString("prezNast");
								String nast = ime + " " + prez;
								imenosiocL.setText(nast);
							}

							String upit1 = "SELECT sifPreduslova, naziv  FROM predmet inner join preduslovi on predmet.sifPred = preduslovi.sifPreduslova WHERE preduslovi.sifPred = ?";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, sifraPredZaAzurPred);
							ResultSet rs = stmt1.executeQuery();

							dynamicComboBox.removeAllItems();
							while (rs.next()) {
								int sifraPreduslova = rs.getInt("sifPreduslova");
								String naziv = rs.getString("naziv");
								String opcion = naziv + " (" + sifraPreduslova + ")";
								dynamicComboBox.addItem(opcion);
							}

							upit1 = "SELECT sifPred, naziv  FROM predmet WHERE predmet.sifPred <> ? ";
							stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, sifraPredZaAzurPred);
							rs = stmt1.executeQuery();

							preduslovicombo.removeAllItems();
							while (rs.next()) {
								String naziv = rs.getString("naziv");
								int sifra = rs.getInt("sifPred");
								String item = naziv + " (" + sifra + ")";
								preduslovicombo.addItem(item);
							}
							listModel.clear();

							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

				searchTrenutniPreduslovi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							ArrayList<String> items = new ArrayList<>();

							String enteredText = searchTrenutniPreduslovi.getText();

							String upit1 = "SELECT sifPreduslova, naziv  FROM predmet inner join preduslovi on predmet.sifPred = preduslovi.sifPreduslova WHERE preduslovi.sifPred = ?";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, sifraPredZaAzurPred);
							ResultSet rs = stmt1.executeQuery();

							while (rs.next()) {
								String naziv = rs.getString("naziv");
								int sifra = rs.getInt("sifPreduslova");
								String item = naziv + " (" + sifra + ")";
								items.add(item);
							}

							rs.close();
							stmt1.close();
							dataBase.close();

							DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
							for (String item : items) {
								model.addElement(item);
							}

							dynamicComboBox.setModel(model);
							if (!enteredText.isEmpty()) {
								for (int i = 0; i < model.getSize(); i++) {
									String item = model.getElementAt(i);
									if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
										dynamicComboBox.setSelectedIndex(i);
										break;
									}
								}
							}
						} catch (Exception exception) {
							exception.printStackTrace();
						}

					}
				});

				preduslovicombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String selectedItem = (String) preduslovicombo.getSelectedItem();
						if (selectedItem != null && !listModel.contains(selectedItem)) {
							{
								listModel.addElement(selectedItem);
							}

						}
					}
				});

			}
		});

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = dataList.getSelectedIndex();
				if (selectedIndex != -1) {
					listModel.removeElementAt(selectedIndex);
				} else {
					JOptionPane.showMessageDialog(null,
							"Potrebno je da selektujete predmet koji zelite ukloniti iz liste odabranih preduslova!",
							"Poruka", 0);
				}
			}
		});

		PotvrdaAzurPreduslova.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (sifraPredZaAzurPred == 0) {
					JOptionPane.showMessageDialog(null, "Niste odabrali predmet za koji zelite azurirati preduslove!",
							"Poruka", 0);
				} else if (dataList.getModel().getSize() == 0) {

					JOptionPane.showMessageDialog(null, "Niste odabrali niti jedan preduslov!", "Poruka", 0);

				} else {

					ArrayList<Integer> listaSifri = new ArrayList<>();
					Enumeration<String> elements = listModel.elements();
					while (elements.hasMoreElements()) {
						String element = elements.nextElement();
						String sifrapreduslova = element.substring(element.length() - 8);
						sifrapreduslova = sifrapreduslova.substring(0, 7);
						int sifraZaPreduslov = Integer.parseInt(sifrapreduslova);
						listaSifri.add(sifraZaPreduslov);
					}

					boolean check = true;
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						String insert = "INSERT IGNORE INTO preduslovi VALUES(? , ?)";

						for (int i = 0; i < listaSifri.size(); ++i) {
							PreparedStatement stinsert = dataBase.prepareStatement(insert);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							stinsert.setInt(1, sifraPredZaAzurPred);
							stinsert.setInt(2, listaSifri.get(i));
							stinsert.executeUpdate();
						}

						String upit1 = "SELECT sifPreduslova, naziv  FROM predmet inner join preduslovi on predmet.sifPred = preduslovi.sifPreduslova WHERE preduslovi.sifPred = ?";
						PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
						stmt1.setInt(1, sifraPredZaAzurPred);
						ResultSet rs = stmt1.executeQuery();
						Statement stmt = dataBase.createStatement();
						dynamicComboBox.removeAllItems();
						while (rs.next()) {
							int sifraPreduslova = rs.getInt("sifPreduslova");
							String naziv = rs.getString("naziv");
							String opcion = naziv + " (" + sifraPreduslova + ")";
							dynamicComboBox.addItem(opcion);
						}
						dataBase.commit();
						dataBase.close();
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Nije moguce dodati preduslov!", "Poruka", 0);
						check = false;
					}
					if (check) {
						JOptionPane.showMessageDialog(null, "Uspjesno ste azurirali preduslove za predmet!", "Poruka",
								1);
						listModel.clear();
					}
				}
			}
		});

		BrisanjeAzurPreduslova.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (sifraPredZaAzurPred == 0) {
					JOptionPane.showMessageDialog(null, "Niste odabrali predmet za koji zelite azurirati preduslove!",
							"Poruka", 0);
				} else if (dataList.getModel().getSize() == 0) {

					JOptionPane.showMessageDialog(null, "Niste odabrali niti jedan preduslov!", "Poruka", 0);

				} else {

					boolean check = true;
					ArrayList<Integer> listaSifri = new ArrayList<>();

					Enumeration<String> elements = listModel.elements();
					while (elements.hasMoreElements()) {
						String element = elements.nextElement();
						String sifrapreduslova = element.substring(element.length() - 8);
						sifrapreduslova = sifrapreduslova.substring(0, 7);
						int sifraZaPreduslov = Integer.parseInt(sifrapreduslova);
						listaSifri.add(sifraZaPreduslov);
					}

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						String delete = "DELETE FROM preduslovi WHERE sifPred = ? AND sifPreduslova= ?";

						for (int i = 0; i < listaSifri.size(); ++i) {
							PreparedStatement stdelete = dataBase.prepareStatement(delete);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							stdelete.setInt(1, sifraPredZaAzurPred);
							stdelete.setInt(2, listaSifri.get(i));
							stdelete.executeUpdate();
						}

						String upit1 = "SELECT sifPreduslova, naziv  FROM predmet inner join preduslovi on predmet.sifPred = preduslovi.sifPreduslova WHERE preduslovi.sifPred = ?";
						PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
						stmt1.setInt(1, sifraPredZaAzurPred);
						ResultSet rs = stmt1.executeQuery();
						Statement stmt = dataBase.createStatement();
						dynamicComboBox.removeAllItems();
						while (rs.next()) {
							int sifraPreduslova = rs.getInt("sifPreduslova");
							String naziv = rs.getString("naziv");
							String opcion = naziv + " (" + sifraPreduslova + ")";
							dynamicComboBox.addItem(opcion);
						}
						dataBase.commit();
						dataBase.close();
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Nije moguce obrisati preduslov!", "Poruka", 0);
						check = false;
					}
					if (check) {

						listModel.clear();
						JOptionPane.showMessageDialog(null, "Uspjesno ste azurirali preduslove za predmet!", "Poruka",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}

			}
		});

		obirisiSvePredusl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean check = true;
				if (sifraPredZaAzurPred != 0) {
					if (dynamicComboBox.getItemCount() == 0) {
						JOptionPane.showMessageDialog(null, "Odabrani predmet trenutno nema definisane preduslove!",
								"Poruka", 0);
					} else {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String delete = "DELETE FROM preduslovi WHERE sifPred = ? ";
							PreparedStatement stdelete = dataBase.prepareStatement(delete);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							stdelete.setInt(1, sifraPredZaAzurPred);
							stdelete.executeUpdate();
							dataBase.commit();
							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Nije moguce obrisati preduslove!", "Poruka", 0);
							check = false;
						}

						if (check) {

							dynamicComboBox.removeAllItems();
							JOptionPane.showMessageDialog(null, "Uspjesno ste obrisali sve preduslove za predmet!",
									"Poruka", 1);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Niste odabrali predmet za azuriranje preduslova!", "Poruka",
							0);
				}

			}
		});

		// Definisanje plana realizacije nastave

		deletelist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = dataList2.getSelectedIndex();
				if (selectedIndex != -1) {
					listModel2.removeElementAt(selectedIndex);
				} else {
					JOptionPane.showMessageDialog(null,
							"Potrebno je da selektujete nastavnika kojeg zelite ukloniti iz liste.", "Poruka", 0);
				}
			}
		});

		Calendar kalendar = Calendar.getInstance();
		int trenutnaGodina = kalendar.get(Calendar.YEAR);
		int narednaGod = trenutnaGodina + 1;
		String akGod = "" + trenutnaGodina + "/" + narednaGod;

		DeleteNast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean check = true;
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

					String delete = "delete from nastavnikNaPredmetu where sifPred = ? AND akGod = ?";

					PreparedStatement st = dataBase.prepareStatement(delete);
					dataBase.setAutoCommit(false);
					dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					st.setInt(1, DefPlanSifPred);
					st.setString(2, akGod);
					st.executeUpdate();

					String update = "update predmet set nosilac = null where sifPred = ?";
					PreparedStatement stupdate = dataBase.prepareStatement(update);
					stupdate.setInt(1, DefPlanSifPred);
					stupdate.executeUpdate();
					trenutniNascombo.removeAllItems();
					dataBase.commit();
					dataBase.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Nije moguce ukloniti nastavnike sa predmeta!", "Poruka", 0);
					check = false;
				}
				if (check) {
					nosilacime.setText("");
					JOptionPane.showMessageDialog(null, "Uspjesno ste uklonili nastavnike sa predmeta!", "Poruka", 1);
				}
			}
		});

		PotvNosiocPred.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean check = true;
				if (DefPlanSifPred == 0) {
					JOptionPane.showMessageDialog(null, "Niste odabrali predmet za kojeg zelite definisati nosioca!",
							"Poruka", 0);
					check = false;
				} else {

					if (nosiocicombo.getSelectedIndex() == -1) {
						JOptionPane.showMessageDialog(null, "Niste odabrali nastavnika za nosioca predmeta.", "Poruka",
								0);
						check = false;
					} else {

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String update = "UPDATE predmet SET nosilac = ? WHERE sifPred = ?";

							int sifNosioc = 0;
							Object unos = nosiocicombo.getSelectedItem();
							String selected = (String) unos;
							if (selected != null && selected.length() >= 5) {
								String sif = selected.substring(selected.length() - 6);
								sif = sif.substring(0, 5);
								sifNosioc = Integer.parseInt(sif);
							}

							PreparedStatement stupdate = dataBase.prepareStatement(update);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							stupdate.setInt(1, sifNosioc);
							stupdate.setInt(2, DefPlanSifPred);
							stupdate.executeUpdate();

							if (sifNosioc != 0) {
								String insert = "insert ignore into nastavnikNaPredmetu values(?, ?, ?)";
								PreparedStatement insertSt = dataBase.prepareStatement(insert);
								insertSt.setInt(1, sifNosioc);
								insertSt.setInt(2, DefPlanSifPred);
								insertSt.setString(3, akGod);
								insertSt.executeUpdate();
							}

							String select = "select imeNast, prezNast from nastavnik where sifNast= ?";
							stupdate = dataBase.prepareStatement(select);
							stupdate.setInt(1, sifNosioc);
							ResultSet rs = stupdate.executeQuery();
							while (rs.next()) {
								String ime = rs.getString("imeNast");
								String prez = rs.getString("prezNast");
								nosilacime.setText(ime + " " + prez);

							}

							String upit1 = "select imeNast, prezNast from nastavnik inner join nastavnikNaPredmetu on nastavnik.sifNast= nastavnikNaPredmetu.sifNast \n"
									+ "where sifPred = ? AND akGod = ?";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, DefPlanSifPred);
							stmt1.setString(2, akGod);
							rs = stmt1.executeQuery();
							trenutniNascombo.removeAllItems();
							while (rs.next()) {
								String imenast = rs.getString("imeNast");
								String prezime = rs.getString("prezNast");
								String opcion = imenast + " " + prezime;
								trenutniNascombo.addItem(opcion);
							}

							dataBase.commit();
							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Nije moguce definisati ko je nosilac predmeta!",
									"Poruka", 0);
							check = false;
						}
					}
				}
				if (check) {
					JOptionPane.showMessageDialog(null, "Uspjesno ste dodali nastavnika nosioca predmeta!", "Poruka",
							1);
				}
			}
		});

		PotvDodajNast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean check = true;

				if (DefPlanSifPred == 0) {
					check = false;
					JOptionPane.showMessageDialog(null, "Niste odabrali predmet", "Poruka", 0);
				}

				else {
					if (listModel2.getSize() == 0) {
						check = false;
						JOptionPane.showMessageDialog(null, "Niste odabrali niti jednog nastavnika.", "Poruka", 0);
					} else {

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String insert = "INSERT IGNORE INTO nastavnikNaPredmetu VALUES( ?, ?, ?)";
							ArrayList<Integer> lista = new ArrayList<>();

							Enumeration<String> elements = listModel2.elements();
							while (elements.hasMoreElements()) {
								String element = elements.nextElement();
								String sifra = element.substring(element.length() - 6);
								sifra = sifra.substring(0, 5);
								int sifraZaProf = Integer.parseInt(sifra);
								lista.add(sifraZaProf);
							}

							PreparedStatement stinsert = dataBase.prepareStatement(insert);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							for (int i = 0; i < lista.size(); ++i) {
								stinsert.setInt(1, lista.get(i));
								stinsert.setInt(2, DefPlanSifPred);
								stinsert.setString(3, akGod);
								stinsert.executeUpdate();
							}

							String upit1 = "select imeNast, prezNast from nastavnik inner join nastavnikNaPredmetu on nastavnik.sifNast= nastavnikNaPredmetu.sifNast \n"
									+ "where sifPred = ? AND akGod = ?";

							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, DefPlanSifPred);
							stmt1.setString(2, akGod);
							ResultSet rs = stmt1.executeQuery();
							trenutniNascombo.removeAllItems();
							while (rs.next()) {
								String imenast = rs.getString("imeNast");
								String prezime = rs.getString("prezNast");
								String opcion = imenast + " " + prezime;
								trenutniNascombo.addItem(opcion);
							}

							dataBase.commit();
							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Nije moguce dodati nastavnika na predmet!", "Poruka",
									0);
							check = false;
						}
					}
				}
				if (check) {
					listModel2.removeAllElements();
					JOptionPane.showMessageDialog(null, "Uspjesno ste dodali nastavnika na predmet!", "Poruka", 1);
				}
			}
		});

		UkloniNastB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean check = true;
				if (DefPlanSifPred == 0) {
					check = false;
					JOptionPane.showMessageDialog(null, "Niste odabrali predmet", "Poruka", 0);
				} else {
					if (listModel2.getSize() == 0) {
						check = false;
						JOptionPane.showMessageDialog(null, "Niste odabrali niti jednog nastavnika.", "Poruka", 0);
					} else {

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String delete = "delete from nastavnikNaPredmetu where sifNast = ? and sifPred=? and akGod= ?";
							String akGod;
							Calendar kalendar = Calendar.getInstance();
							int trenutnaGodina = kalendar.get(Calendar.YEAR);
							int narednaGod = trenutnaGodina + 1;
							akGod = "" + trenutnaGodina + "/" + narednaGod;
							ArrayList<Integer> lista = new ArrayList<>();

							Enumeration<String> elements = listModel2.elements();
							while (elements.hasMoreElements()) {
								String element = elements.nextElement();
								String sifra = element.substring(element.length() - 6);
								sifra = sifra.substring(0, 5);
								int sifraZaProf = Integer.parseInt(sifra);
								lista.add(sifraZaProf);
							}

							PreparedStatement st = dataBase.prepareStatement(delete);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							for (int i = 0; i < lista.size(); ++i) {
								st.setInt(1, lista.get(i));
								st.setInt(2, DefPlanSifPred);
								st.setString(3, akGod);
								st.executeUpdate();
							}

							String upit1 = "select imeNast, prezNast from nastavnik inner join nastavnikNaPredmetu on nastavnik.sifNast= nastavnikNaPredmetu.sifNast \n"
									+ "where sifPred = ? AND akGod = ?";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, DefPlanSifPred);
							stmt1.setString(2, akGod);
							ResultSet rs = stmt1.executeQuery();
							trenutniNascombo.removeAllItems();
							while (rs.next()) {
								String imenast = rs.getString("imeNast");
								String prezime = rs.getString("prezNast");
								String opcion = imenast + " " + prezime;
								trenutniNascombo.addItem(opcion);
							}

							String update = "update predmet set nosilac = null where sifPred = ? AND nosilac = ?";
							st = dataBase.prepareStatement(update);
							for (int i = 0; i < lista.size(); ++i) {
								st.setInt(2, DefPlanSifPred);
								st.setInt(1, lista.get(i));
								st.executeUpdate();
							}

							// ??
							String provjera = "select nosilac from predmet where sifPred = ?";
							PreparedStatement st2 = dataBase.prepareStatement(provjera);
							st2.setInt(1, DefPlanSifPred);
							String Trnosilac = "provjera";
							rs = st2.executeQuery();
							while (rs.next()) {
								int nosilac = rs.getInt("nosilac");
								Trnosilac = Integer.toString(nosilac);
							}
							if (Trnosilac.equals(null)) {
								nosilacime.setText("");
							}

							lista.clear();
							dataBase.commit();
							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Nije moguce uklonit nastavnika sa predmeta!", "Poruka",
									0);
							check = false;
						}
					}
				}
				if (check) {
					listModel2.removeAllElements();
					JOptionPane.showMessageDialog(null, "Uspjesno ste uklonili nastavnika sa predmeta!", "Poruka", 1);
				}
			}
		});

		definisanjePlanaRealizacije.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToDefPlanRealizacije();

				titleforDefPlanRealizacije.setBounds(320, 100, 500, 25);
				titleforDefPlanRealizacije.setFont(font2);
				titleforDefPlanRealizacije.setForeground(new Color(37, 32, 141));

				pregledPred.setBounds(90, 175, 300, 30);
				pregledPred.setFont(font3);
				pregledPred.setForeground(new Color(37, 32, 141));

				pretraziPredmete.setBounds(90, 210, 400, 25);
				pretraziPredmete.setFont(font4);
				pretraziPredmete.setForeground(new Color(37, 32, 141));

				listaPredmeta.setBounds(90, 245, 400, 30);
				listaPredmeta.setForeground(new Color(37, 32, 141));
				listaPredmeta.setFont(font3);
				listaPredmeta.setBackground(Color.WHITE);

				trenutnipodaciL.setBounds(90, 283, 500, 25);
				trenutnipodaciL.setForeground(new Color(37, 32, 141));
				trenutnipodaciL.setFont(font2);

				nosilacL.setBounds(90, 321, 300, 25);
				nosilacL.setForeground(new Color(37, 32, 141));
				nosilacL.setFont(font2);

				nosilacime.setBounds(300, 321, 500, 25);
				nosilacime.setForeground(Color.white);
				nosilacime.setFont(font2);

				pregledProfL.setBounds(90, 359, 500, 25);
				pregledProfL.setForeground(new Color(37, 32, 141));
				pregledProfL.setFont(font2);

				pretraziTrenutneNast.setBounds(90, 397, 400, 25);
				pretraziTrenutneNast.setForeground(new Color(37, 32, 141));
				pretraziTrenutneNast.setFont(font3);

				trenutniNascombo.setBounds(90, 440, 400, 30);
				trenutniNascombo.setForeground(new Color(37, 32, 141));
				trenutniNascombo.setFont(font3);
				trenutniNascombo.setBackground(Color.WHITE);

				DeleteNast.setBounds(90, 478, 400, 25);
				DeleteNast.setFont(font3);
				DeleteNast.setBackground(Color.WHITE);
				DeleteNast.setForeground(new Color(37, 32, 141));

				odnastzapredL.setBounds(590, 175, 400, 25);
				odnastzapredL.setForeground(new Color(37, 32, 141));
				odnastzapredL.setFont(font2);

				pretraziNastavnika.setBounds(590, 210, 400, 25);
				pretraziNastavnika.setForeground(new Color(37, 32, 141));
				pretraziNastavnika.setFont(font3);

				listaNastavnika.setBounds(590, 245, 400, 30);
				listaNastavnika.setForeground(new Color(37, 32, 141));
				listaNastavnika.setFont(font3);
				listaNastavnika.setBackground(Color.WHITE);

				scpane2.setBounds(590, 280, 400, 70);
				scpane2.setForeground(new Color(37, 32, 141));
				scpane2.setFont(font2);

				dataList2.setForeground(new Color(37, 32, 141));
				dataList2.setFont(font2);

				deletelist.setBounds(590, 360, 400, 25);
				deletelist.setForeground(new Color(37, 32, 141));
				deletelist.setFont(font3);
				deletelist.setBackground(Color.WHITE);

				PotvDodajNast.setBounds(590, 395, 400, 25);
				PotvDodajNast.setFont(font3);
				PotvDodajNast.setBackground(Color.WHITE);
				PotvDodajNast.setForeground(new Color(37, 32, 141));

				UkloniNastB.setBounds(590, 430, 400, 25);
				UkloniNastB.setFont(font3);
				UkloniNastB.setBackground(Color.WHITE);
				UkloniNastB.setForeground(new Color(37, 32, 141));

				odNosiocPred.setBounds(590, 465, 400, 25);
				odNosiocPred.setForeground(new Color(37, 32, 141));
				odNosiocPred.setFont(font2);

				pretraziNosioca.setBounds(590, 500, 400, 25);
				pretraziNosioca.setForeground(new Color(37, 32, 141));
				pretraziNosioca.setFont(font3);

				nosiocicombo.setBounds(590, 535, 400, 30);
				nosiocicombo.setForeground(new Color(37, 32, 141));
				nosiocicombo.setFont(font3);
				nosiocicombo.setBackground(Color.WHITE);

				PotvNosiocPred.setBounds(590, 570, 400, 25);
				PotvNosiocPred.setFont(font3);
				PotvNosiocPred.setBackground(Color.WHITE);
				PotvNosiocPred.setForeground(new Color(37, 32, 141));

				listaNastavnika.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String selectedItem = (String) listaNastavnika.getSelectedItem();
						if (selectedItem != null & !listModel2.contains(selectedItem)) {

							listModel2.addElement(selectedItem);
						}
					}
				});

				listaPredmeta.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JComboBox<String> combo = (JComboBox<String>) e.getSource();
						Object unos = combo.getSelectedItem();
						String selected = (String) unos;
						if (selected != null && selected.length() >= 7) {
							String sifrapred = selected.substring(selected.length() - 8);
							sifrapred = sifrapred.substring(0, 7);
							DefPlanSifPred = Integer.parseInt(sifrapred);
						}

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String upit1 = "select imeNast, prezNast from nastavnik inner join nastavnikNaPredmetu on nastavnik.sifNast= nastavnikNaPredmetu.sifNast \n"
									+ "where sifPred = ? AND akGod = ?";

							String akGod;
							Calendar kalendar = Calendar.getInstance();
							int trenutnaGodina = kalendar.get(Calendar.YEAR);
							int narednaGod = trenutnaGodina + 1;
							akGod = "" + trenutnaGodina + "/" + narednaGod;

							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, DefPlanSifPred);
							stmt1.setString(2, akGod);
							ResultSet rs = stmt1.executeQuery();

							String upit2 = "select imeNast, prezNast from nastavnik inner join predmet on nastavnik.sifNast=predmet.nosilac \n"
									+ "where sifPred = ?";
							PreparedStatement st2 = dataBase.prepareStatement(upit2);
							st2.setInt(1, DefPlanSifPred);
							ResultSet rs2 = st2.executeQuery();
							String ime;
							String prez;
							int count = 0;
							while (rs2.next()) {
								ime = rs2.getString("imeNast");
								prez = rs2.getString("prezNast");
								String nast = ime + " " + prez;
								nosilacime.setText(nast);
								++count;
							}
							if (count == 0) {
								nosilacime.setText("");
							}

							trenutniNascombo.removeAllItems();
							while (rs.next()) {
								String imenast = rs.getString("imeNast");
								String prezime = rs.getString("prezNast");
								String opcion = imenast + " " + prezime;
								trenutniNascombo.addItem(opcion);
							}
							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

				pretraziTrenutneNast.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							ArrayList<String> items = new ArrayList<>();

							String upit1 = "select imeNast, prezNast from nastavnik inner join nastavnikNaPredmetu on nastavnik.sifNast= nastavnikNaPredmetu.sifNast \n"
									+ "where sifPred = ? AND akGod = ?";

							String akGod;
							Calendar kalendar = Calendar.getInstance();
							int trenutnaGodina = kalendar.get(Calendar.YEAR);
							int narednaGod = trenutnaGodina + 1;
							akGod = "" + trenutnaGodina + "/" + narednaGod;

							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, DefPlanSifPred);
							stmt1.setString(2, akGod);
							ResultSet rs = stmt1.executeQuery();

							items.clear();
							while (rs.next()) {
								String ime = rs.getString("imeNast");
								String prezime = rs.getString("prezNast");
								String item = ime + " " + prezime;
								items.add(item);
							}

							String enteredText = pretraziTrenutneNast.getText();
							DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) trenutniNascombo
									.getModel();
							if (model != null) {
								model.removeAllElements();
								for (String item : items) {
									if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
										model.addElement(item);
									}
								}
								if (model.getSize() > 0) {
									trenutniNascombo.setSelectedIndex(0);
								}
							}
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}

				});

				pretraziNastavnika.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						boolean provjera = true;

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							ArrayList<String> items = new ArrayList<>();

							String upit1 = "select imeNast, prezNast, sifNast from nastavnik";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							ResultSet rs = stmt1.executeQuery();

							items.clear();
							while (rs.next()) {
								String ime = rs.getString("imeNast");
								String prezime = rs.getString("prezNast");
								int sif = rs.getInt("sifNast");
								String item = ime + " " + prezime + " (" + sif + ")";
								items.add(item);
							}

							String searchKeyword = pretraziNastavnika.getText();
							if (!searchKeyword.isEmpty()) {
								ArrayList<String> filteredItems = new ArrayList<>();
								for (String originalItem : items) {
									if (originalItem.toLowerCase().contains(searchKeyword.toLowerCase())) {
										filteredItems.add(originalItem);
									}
								}
								listaNastavnika
										.setModel(new DefaultComboBoxModel<>(filteredItems.toArray(new String[0])));

							} else {
								listaNastavnika.setModel(new DefaultComboBoxModel<>(items.toArray(new String[0])));
							}

						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}

				});

				pretraziNosioca.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							ArrayList<String> items = new ArrayList<>();

							String upit1 = "select imeNast, prezNast, sifNast from nastavnik";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							ResultSet rs = stmt1.executeQuery();

							items.clear();
							while (rs.next()) {
								String ime = rs.getString("imeNast");
								String prezime = rs.getString("prezNast");
								int sifra = rs.getInt("sifNast");
								String item = ime + " " + prezime + " (" + sifra + ")";
								items.add(item);
							}

							String enteredText = pretraziNosioca.getText();
							DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) nosiocicombo.getModel();
							if (model != null) {
								model.removeAllElements();
								for (String item : items) {
									if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
										model.addElement(item);
									}
								}
								if (model.getSize() > 0) {
									nosiocicombo.setSelectedIndex(0);
								}
							}
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}

				});

				listaNastavnika.removeAllItems();
				nosiocicombo.removeAllItems();
				listModel2.removeAllElements();
				trenutniNascombo.removeAllItems();
				nosilacime.setText("");
				DefPlanSifPred = 0;
				listaPredmeta.removeAllItems();

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

					Statement stmt = dataBase.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT imeNast, prezNast, sifNast FROM nastavnik");
					while (rs.next()) {
						String ime = rs.getString("imeNast");
						String prezime = rs.getString("prezNast");
						int sifraNast = rs.getInt("sifNast");
						String opcion = ime + " " + prezime + " (" + sifraNast + ")";
						listaNastavnika.addItem(opcion);
						nosiocicombo.addItem(opcion);

					}
					nosiocicombo.setSelectedIndex(-1);
					listModel2.removeAllElements();
					trenutniNascombo.removeAllItems();
					rs = stmt.executeQuery("SELECT naziv, sifPred FROM predmet");
					while (rs.next()) {
						String naziv = rs.getString("naziv");
						int sifraPred = rs.getInt("sifPred");
						String opcion = naziv + " (" + sifraPred + ")";
						listaPredmeta.addItem(opcion);

					}
					nosilacime.setText("");
					trenutniNascombo.removeAllItems();
					DefPlanSifPred = 0;
					dataBase.close();
				} catch (Exception exc) {
					exc.printStackTrace();
				}

				pretraziPredmete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String provjera = pretraziPredmete.getText();
						if (!provjera.equals("")) {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								ArrayList<String> items = new ArrayList<>();
								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt.executeQuery("SELECT sifPred, naziv FROM predmet");
								items.clear();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " (" + sifra + ")";
									items.add(item);
								}

								String enteredText = pretraziPredmete.getText();
								DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) listaPredmeta
										.getModel();
								if (model != null) {
									model.removeAllElements();
									for (String item : items) {
										if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
											model.addElement(item);
										}
									}
									if (model.getSize() > 0) {
										listaPredmeta.setSelectedIndex(0);
									}
								}
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						} else {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								ArrayList<String> items = new ArrayList<>();
								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt.executeQuery("SELECT sifPred, naziv FROM predmet");
								items.clear();
								listaPredmeta.removeAllItems();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " (" + sifra + ")";
									items.add(item);
									listaPredmeta.addItem(item);
								}

							} catch (Exception exception) {
								exception.printStackTrace();
							}

							nosilacime.setText("");
							trenutniNascombo.removeAllItems();
						}

						if (listaPredmeta.getItemCount() == 0) {
							trenutniNascombo.removeAllItems();
							nosilacime.setText("");
						}
					}

				});

			}
		});

		// Definisanje trajanja registracije
		definisanjeTrajanjaRegistracije.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefinisanjePerodaTrajanjaReg();

				DefPeriod.setBounds(90, 95, 600, 30);
				DefPeriod.setForeground(new Color(37, 32, 141));
				DefPeriod.setFont(font2);

				PocRegL.setBounds(50, 160, 500, 25);
				PocRegL.setForeground(new Color(37, 32, 141));
				PocRegL.setFont(font2);

				PocRegInfo.setBounds(535, 160, 200, 25);
				PocRegInfo.setForeground(Color.WHITE);
				PocRegInfo.setFont(font2);

				KrajRegL.setBounds(50, 195, 500, 25);
				KrajRegL.setForeground(new Color(37, 32, 141));
				KrajRegL.setFont(font2);
				KrajRegInfo.setBounds(535, 195, 200, 25);
				KrajRegInfo.setForeground(Color.white);
				KrajRegInfo.setFont(font2);

				Border border = new LineBorder(new Color(37, 32, 141), 2, false);

				PocRegButton.setBounds(145, 265, 400, 30);
				PocRegButton.setForeground(new Color(37, 32, 141));
				PocRegButton.setFont(font3);
				PocRegButton.setBackground(Color.WHITE);
				PocRegButton.setBorder(border);

				NoviDatumPocRegL.setBounds(145, 305, 300, 25);
				NoviDatumPocRegL.setForeground(new Color(37, 32, 141));
				NoviDatumPocRegL.setFont(font2);

				NoviDatumPocRegInfo.setBounds(365, 305, 200, 25);
				NoviDatumPocRegInfo.setForeground(Color.WHITE);
				NoviDatumPocRegInfo.setFont(font2);

				KrajRegButton.setBounds(145, 350, 400, 30);
				KrajRegButton.setForeground(new Color(37, 32, 141));
				KrajRegButton.setFont(font3);
				KrajRegButton.setBackground(Color.WHITE);
				KrajRegButton.setBorder(border);

				NoviDatumKrajRegL.setBounds(145, 390, 300, 25);
				NoviDatumKrajRegL.setForeground(new Color(37, 32, 141));
				NoviDatumKrajRegL.setFont(font2);

				NoviDatumKrajRegInfo.setBounds(365, 390, 200, 25);
				NoviDatumKrajRegInfo.setForeground(Color.WHITE);
				NoviDatumKrajRegInfo.setFont(font2);

				PotvrdiNoviDatum.setBounds(270, 445, 150, 30);
				PotvrdiNoviDatum.setForeground(new Color(37, 32, 141));
				PotvrdiNoviDatum.setFont(font3);
				PotvrdiNoviDatum.setBackground(Color.WHITE);
				PotvrdiNoviDatum.setBorder(border);

				NoviDatumPocRegInfo.setText("");
				NoviDatumKrajRegInfo.setText("");
				dp = 2;
				dk = 2;
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");
					Statement stmt = dataBase.createStatement();
					String upit = "SELECT pocetakReg, krajReg FROM registracija where akGod =?";
					Calendar kalendar = Calendar.getInstance();
					int trenutnaGodina = kalendar.get(Calendar.YEAR);
					int narednaGod = trenutnaGodina + 1;
					String akGod = "" + trenutnaGodina + "/" + narednaGod;
					PreparedStatement st = dataBase.prepareStatement(upit);
					st.setString(1, akGod);
					ResultSet rs = st.executeQuery();
					while (rs.next()) {
						String pocetak = rs.getString("pocetakReg");
						String kraj = rs.getString("krajReg");
						pocetak = pocetak.replace('/', '.');
						kraj = kraj.replace('/', '.');
						PocRegInfo.setText(pocetak);
						KrajRegInfo.setText(kraj);
					}
					dataBase.close();
				} catch (Exception exc) {
					exc.printStackTrace();
				}

			}
		});
		PocRegButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalendar(NoviDatumPocRegInfo);
			}
		});

		KrajRegButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalendar(NoviDatumKrajRegInfo);
			}
		});

		PotvrdiNoviDatum.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String newdata = NoviDatumPocRegInfo.getText();
				if (!newdata.equals("")) {
					dp = 1;
				}

				String newdata2 = NoviDatumKrajRegInfo.getText();
				if (!newdata2.equals("")) {
					dk = 1;
				}
				if (dp == 1 || dk == 1) {

					Calendar kalendar = Calendar.getInstance();
					int trenutnaGodina = kalendar.get(Calendar.YEAR);
					int narednaGod = trenutnaGodina + 1;
					String akGod = "" + trenutnaGodina + "/" + narednaGod;

					boolean check = true;
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						String upit = new String();
						if (dp == 1) {
							upit = "INSERT INTO registracija (akGod, pocetakReg) VALUES (?, ?) ON DUPLICATE KEY UPDATE pocetakReg = VALUES(pocetakReg)";
							PreparedStatement st = dataBase.prepareStatement(upit);
							st.setString(1, akGod);
							newdata = newdata.replace('.', '/');
							st.setString(2, newdata);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							st.executeUpdate();
							dataBase.commit();
						}
						if (dk == 1) {
							upit = "INSERT INTO registracija (akGod, krajReg) VALUES (?, ?) ON DUPLICATE KEY UPDATE krajReg = VALUES(krajReg)";
							PreparedStatement st = dataBase.prepareStatement(upit);
							st.setString(1, akGod);
							newdata2 = newdata2.replace('.', '/');
							st.setString(2, newdata2);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							st.executeUpdate();
							dataBase.commit();
						}

						dataBase.close();
					} catch (Exception exc) {
						exc.printStackTrace();
						check = false;
					}
					if (check) {

						JOptionPane.showMessageDialog(null, "Uspjesno izvrseno azuriranje!", "Poruka", 1);

						if (dp == 1) {
							newdata = newdata.replace('/', '.');
							PocRegInfo.setText(newdata);
						}
						if (dk == 1) {
							newdata2 = newdata2.replace('/', '.');
							KrajRegInfo.setText(newdata2);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Niste odabrali novi datum za azuriranje!", "Poruka", 0);
				}

			}
		});

		// Pregled zahtjeva

		pregledZahtjeva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PregledZahtStud();

				PreglZaht.setBounds(240, 80, 550, 30);
				PreglZaht.setForeground(new Color(37, 32, 141));
				PreglZaht.setFont(font2);

				studPredzahtjeviL.setBounds(50, 160, 450, 70);
				studPredzahtjeviL.setForeground(new Color(37, 32, 141));
				studPredzahtjeviL.setFont(font3);

				pretragaPredZaZahtjev.setBounds(50, 245, 400, 25);
				pretragaPredZaZahtjev.setForeground(new Color(37, 32, 141));
				pretragaPredZaZahtjev.setFont(font2);

				PredZaZahtjevCombo.setBounds(50, 285, 400, 30);
				PredZaZahtjevCombo.setForeground(new Color(37, 32, 141));
				PredZaZahtjevCombo.setFont(font3);
				PredZaZahtjevCombo.setBackground(Color.WHITE);

				OdabPredZahtjevL.setBounds(50, 330, 300, 25);
				OdabPredZahtjevL.setForeground(new Color(37, 32, 141));
				OdabPredZahtjevL.setFont(font2);

				OdabPredZahtjevInfo.setBounds(245, 330, 400, 25);
				OdabPredZahtjevInfo.setForeground(Color.WHITE);
				OdabPredZahtjevInfo.setFont(font2);

				NosilacPredZahtjevL.setBounds(50, 365, 200, 25);
				NosilacPredZahtjevL.setForeground(new Color(37, 32, 141));
				NosilacPredZahtjevL.setFont(font2);

				NosilacPredZahtjevInfo.setBounds(235, 365, 300, 25);
				NosilacPredZahtjevInfo.setForeground(Color.WHITE);
				NosilacPredZahtjevInfo.setFont(font2);

				SemestarZahtjeviL.setBounds(50, 410, 200, 25);
				SemestarZahtjeviL.setForeground(new Color(37, 32, 141));
				SemestarZahtjeviL.setFont(font2);

				SemestarZahtjeviInfo.setBounds(150, 410, 200, 25);
				SemestarZahtjeviInfo.setForeground(Color.WHITE);
				SemestarZahtjeviInfo.setFont(font2);

				StudZahtjeviL.setBounds(50, 450, 450, 50);
				StudZahtjeviL.setForeground(new Color(37, 32, 141));
				StudZahtjeviL.setFont(font2);

				PretragaStudZahtjevi.setBounds(50, 515, 400, 25);
				PretragaStudZahtjevi.setForeground(new Color(37, 32, 141));
				PretragaStudZahtjevi.setFont(font2);

				Border border = new LineBorder(Color.WHITE, 2, false);
				StudZahtjeviCombo.setBounds(50, 555, 400, 30);
				StudZahtjeviCombo.setForeground(Color.WHITE);
				StudZahtjeviCombo.setFont(font3);
				StudZahtjeviCombo.setBackground(new Color(43, 143, 142));
				StudZahtjeviCombo.setBorder(border);

				PodOZahtjL.setBounds(670, 160, 400, 25);
				PodOZahtjL.setForeground(new Color(37, 32, 141));
				PodOZahtjL.setFont(font2);

				ZahtPoslStudL.setBounds(600, 200, 100, 25);
				ZahtPoslStudL.setForeground(new Color(37, 32, 141));
				ZahtPoslStudL.setFont(font2);

				ZahtPoslStudInfo.setBounds(690, 200, 300, 25);
				ZahtPoslStudInfo.setForeground(Color.WHITE);
				ZahtPoslStudInfo.setFont(font2);

				IndeksStudZahtL.setBounds(600, 235, 200, 25);
				IndeksStudZahtL.setForeground(new Color(37, 32, 141));
				IndeksStudZahtL.setFont(font2);

				IndeksStudZahtInfo.setBounds(775, 235, 200, 25);
				IndeksStudZahtInfo.setForeground(Color.WHITE);
				IndeksStudZahtInfo.setFont(font2);

				PredZaZamjL.setBounds(600, 270, 400, 25);
				PredZaZamjL.setForeground(new Color(37, 32, 141));
				PredZaZamjL.setFont(font2);

				PredZaZamjInfo.setBounds(600, 305, 500, 25);
				PredZaZamjInfo.setForeground(Color.WHITE);
				PredZaZamjInfo.setFont(font2);

				NoviPredZahtL.setBounds(600, 340, 500, 25);
				NoviPredZahtL.setForeground(new Color(37, 32, 141));
				NoviPredZahtL.setFont(font2);

				NoviPredZahtInfo.setBounds(600, 375, 500, 25);
				NoviPredZahtInfo.setForeground(Color.WHITE);
				NoviPredZahtInfo.setFont(font2);

				OpisZahtL.setBounds(600, 410, 500, 25);
				OpisZahtL.setForeground(new Color(37, 32, 141));
				OpisZahtL.setFont(font2);

				OpisStudZahtPane.setBounds(600, 440, 400, 60);
				textOpis.setForeground(Color.WHITE);
				textOpis.setFont(font3);
				textOpis.setBackground(new Color(43, 143, 142));
				textOpis.setBorder(border);

				ZahtPrihvL.setBounds(600, 510, 400, 25);
				ZahtPrihvL.setForeground(new Color(37, 32, 141));
				ZahtPrihvL.setFont(font2);

				ZahtPrihvInfo.setBounds(800, 510, 50, 25);
				ZahtPrihvInfo.setForeground(Color.WHITE);
				ZahtPrihvInfo.setFont(font2);

				OdgovorZahtL.setBounds(600, 545, 400, 25);
				OdgovorZahtL.setForeground(new Color(37, 32, 141));
				OdgovorZahtL.setFont(font2);

				OdgZahtPane.setBounds(600, 580, 400, 60);

				OdgovorZahtInfo.setForeground(Color.WHITE);
				OdgovorZahtInfo.setFont(font3);
				OdgovorZahtInfo.setBackground(new Color(43, 143, 142));
				OdgovorZahtInfo.setBorder(border);

				PotvrdiOdgZahtjevB.setBounds(170, 605, 150, 30);
				PotvrdiOdgZahtjevB.setForeground(new Color(37, 32, 141));
				PotvrdiOdgZahtjevB.setFont(font2);
				PotvrdiOdgZahtjevB.setBackground(Color.WHITE);

				PredZaZahtjevCombo.removeAllItems();
				OdabPredZahtjevInfo.setText("");
				NosilacPredZahtjevInfo.setText("");
				SemestarZahtjeviInfo.setText("");
				StudZahtjeviCombo.removeAllItems();
				ZahtPoslStudInfo.setText("");
				IndeksStudZahtInfo.setText("");
				PredZaZamjInfo.setText("");
				NoviPredZahtInfo.setText("");
				textOpis.setText("");
				ZahtPrihvInfo.setText("");
				sifPred2 = 0;
				SifPredZaZahtjev = 0;
				IndeksStudZahtjev = 0;
				OdgovorZahtInfo.setText("");
				OdgovorNaZahtjev = "";

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");
					Statement stmt = dataBase.createStatement();
					ResultSet rs = stmt.executeQuery(
							"select distinct naziv, sifPred from predmet inner join zahtjevi on predmet.sifPred= zahtjevi.sifPred1  where zahtjevi.odgovor is null");
					while (rs.next()) {
						int sifPred = rs.getInt("sifPred");
						String naziv = rs.getString("naziv");
						String opcion = naziv + " (" + sifPred + ")";
						PredZaZahtjevCombo.addItem(opcion);
					}

					OdabPredZahtjevInfo.setText("");
					NosilacPredZahtjevInfo.setText("");
					SemestarZahtjeviInfo.setText("");
					StudZahtjeviCombo.removeAllItems();
					PredZaZamjInfo.setText("");
					sifPred2 = 0;
					SifPredZaZahtjev = 0;
					IndeksStudZahtjev = 0;
					OdgovorZahtInfo.setText("");
					OdgovorNaZahtjev = "";
					ZahtPrihvInfo.setText("");

					dataBase.close();
				} catch (Exception exc) {
					exc.printStackTrace();
				}

				pretragaPredZaZahtjev.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String provjera = pretragaPredZaZahtjev.getText();
						if (!provjera.equals("")) {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								ArrayList<String> items = new ArrayList<>();
								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt.executeQuery(
										"select distinct naziv, sifPred from predmet inner join zahtjevi on predmet.sifPred= zahtjevi.sifPred1 where zahtjevi.odgovor is null");
								items.clear();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " (" + sifra + ")";
									items.add(item);
								}
								String enteredText = pretragaPredZaZahtjev.getText();
								DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) PredZaZahtjevCombo
										.getModel();
								if (model != null) {
									model.removeAllElements();
									for (String item : items) {
										if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
											model.addElement(item);
										}
									}
									if (model.getSize() > 0) {
										PredZaZahtjevCombo.setSelectedIndex(0);
									}
								}

								if (PredZaZahtjevCombo.getItemCount() == 0) {
									OdabPredZahtjevInfo.setText("");
									NosilacPredZahtjevInfo.setText("");
									SemestarZahtjeviInfo.setText("");
									StudZahtjeviCombo.removeAllItems();
									ZahtPoslStudInfo.setText("");
									IndeksStudZahtInfo.setText("");
									PredZaZamjInfo.setText("");
									NoviPredZahtInfo.setText("");
									textOpis.setText("");
									ZahtPrihvInfo.setText("");
									sifPred2 = 0;
									SifPredZaZahtjev = 0;
									IndeksStudZahtjev = 0;
									OdgovorZahtInfo.setText("");
								}

							} catch (Exception exception) {
								exception.printStackTrace();
							}
						} else {

							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								ArrayList<String> items = new ArrayList<>();
								Statement stmt = dataBase.createStatement();
								ResultSet rs = stmt.executeQuery(
										"select distinct naziv, sifPred from predmet inner join zahtjevi on predmet.sifPred= zahtjevi.sifPred1 where zahtjevi.odgovor is null");
								items.clear();
								PredZaZahtjevCombo.removeAllItems();
								while (rs.next()) {
									String naziv = rs.getString("naziv");
									int sifra = rs.getInt("sifPred");
									String item = naziv + " (" + sifra + ")";
									PredZaZahtjevCombo.addItem(item);
									items.add(item);
								}

							} catch (Exception exception) {
								exception.printStackTrace();
							}

							OdabPredZahtjevInfo.setText("");
							NosilacPredZahtjevInfo.setText("");
							SemestarZahtjeviInfo.setText("");
							StudZahtjeviCombo.removeAllItems();
							ZahtPoslStudInfo.setText("");
							IndeksStudZahtInfo.setText("");
							PredZaZamjInfo.setText("");
							NoviPredZahtInfo.setText("");
							textOpis.setText("");
							ZahtPrihvInfo.setText("");
							sifPred2 = 0;
							SifPredZaZahtjev = 0;
							IndeksStudZahtjev = 0;
							OdgovorZahtInfo.setText("");

						}
					}

				});

				PredZaZahtjevCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JComboBox<String> combo = (JComboBox<String>) e.getSource();
						Object unos = combo.getSelectedItem();
						String selected = (String) unos;
						if (selected != null && selected.length() >= 7) {
							String sifrapred = selected.substring(selected.length() - 8);
							sifrapred = sifrapred.substring(0, 7);
							SifPredZaZahtjev = Integer.parseInt(sifrapred);
						}

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String upit1 = "select naziv, nosilac, semestar, ECTS from predmet  where sifPred = ?";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, SifPredZaZahtjev);
							ResultSet rs = stmt1.executeQuery();
							while (rs.next()) {
								String naziv = rs.getString("naziv");
								SemestarPredZaZaht1 = rs.getString("semestar");
								OdabPredZahtjevInfo.setText(naziv);
								PredZaZamjInfo.setText(naziv);
								SemestarZahtjeviInfo.setText(SemestarPredZaZaht1);
								ectsPred1 = rs.getInt("ECTS");
								int nosilacSif = 0;
								nosilacSif = rs.getInt("nosilac");
								if (nosilacSif != 0) {

									upit1 = "select imeNast, prezNast from nastavnik where sifNast=?";
									stmt1 = dataBase.prepareStatement(upit1);
									stmt1.setInt(1, nosilacSif);
									ResultSet rs2 = stmt1.executeQuery();
									while (rs2.next()) {
										String ime = rs2.getString("imeNast");
										String prez = rs2.getString("prezNast");
										NosilacPredZahtjevInfo.setText(ime + " " + prez);
									}
								} else {
									NosilacPredZahtjevInfo.setText("Nije definisano");
								}
							}

							StudZahtjeviCombo.removeAllItems();
							upit1 = "select imeStud, prezStud, stud.indeks from stud inner join zahtjevi on stud.indeks= zahtjevi.indeks \n"
									+ "where sifPred1 = ? and zahtjevi.odgovor is null";
							stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, SifPredZaZahtjev);
							ResultSet rs2 = stmt1.executeQuery();
							while (rs2.next()) {
								String ime = rs2.getString("imeStud");
								String prez = rs2.getString("prezStud");
								int indeks = rs2.getInt("stud.indeks");
								String student = ime + " " + prez + " (" + indeks + ")";
								StudZahtjeviCombo.addItem(student);
							}
							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						ZahtPoslStudInfo.setText("");
						IndeksStudZahtInfo.setText("");
						NoviPredZahtInfo.setText("");
						textOpis.setText("");
						ZahtPrihvInfo.setText("");
						sifPred2 = 0;
						IndeksStudZahtjev = 0;
						OdgovorZahtInfo.setText("");

					}
				});

				PretragaStudZahtjevi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String provjera = PretragaStudZahtjevi.getText();
						if (!provjera.equals("")) {

							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								ArrayList<String> items = new ArrayList<>();
								String upit1 = "  select imeStud, prezStud, stud.indeks from stud inner join zahtjevi on stud.indeks= zahtjevi.indeks \n"
										+ "where sifPred1 = ? and zahtjevi.odgovor is null";
								PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
								stmt1.setInt(1, SifPredZaZahtjev);
								ResultSet rs2 = stmt1.executeQuery();
								items.clear();
								while (rs2.next()) {
									String ime = rs2.getString("imeStud");
									String prez = rs2.getString("prezStud");
									int indeks = rs2.getInt("stud.indeks");
									items.add(ime + " " + prez + " (" + indeks + ")");
								}

								String enteredText = PretragaStudZahtjevi.getText();
								DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) StudZahtjeviCombo
										.getModel();
								if (model != null) {
									model.removeAllElements();
									for (String item : items) {
										if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
											model.addElement(item);
										}
									}
									if (model.getSize() > 0) {
										StudZahtjeviCombo.setSelectedIndex(0);
									}
								}

								if (StudZahtjeviCombo.getItemCount() == 0) {

									ZahtPoslStudInfo.setText("");
									IndeksStudZahtInfo.setText("");
									NoviPredZahtInfo.setText("");
									textOpis.setText("");
									ZahtPrihvInfo.setText("");
									sifPred2 = 0;
									IndeksStudZahtjev = 0;
									OdgovorZahtInfo.setText("");

								}

							} catch (Exception exception) {
								exception.printStackTrace();
							}
						} else {

							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								Connection dataBase = DriverManager.getConnection(
										"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
												+ TimeZone.getDefault().getID(),
										"sql7630078", "wgpv7L1rUF");

								ArrayList<String> items = new ArrayList<>();
								String upit1 = "  select imeStud, prezStud, stud.indeks from stud inner join zahtjevi on stud.indeks= zahtjevi.indeks \n"
										+ "where sifPred1 = ? and zahtjevi.odgovor is null";
								PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
								stmt1.setInt(1, SifPredZaZahtjev);
								ResultSet rs2 = stmt1.executeQuery();
								items.clear();
								StudZahtjeviCombo.removeAllItems();
								while (rs2.next()) {
									String ime = rs2.getString("imeStud");
									String prez = rs2.getString("prezStud");
									int indeks = rs2.getInt("stud.indeks");
									items.add(ime + " " + prez + " (" + indeks + ")");
									StudZahtjeviCombo.addItem(ime + " " + prez + " (" + indeks + ")");
								}

							} catch (Exception exception)

							{
								exception.printStackTrace();
							}

							ZahtPoslStudInfo.setText("");
							IndeksStudZahtInfo.setText("");
							NoviPredZahtInfo.setText("");
							textOpis.setText("");
							ZahtPrihvInfo.setText("");
							sifPred2 = 0;
							IndeksStudZahtjev = 0;
							OdgovorZahtInfo.setText("");
						}

					}

				});

				StudZahtjeviCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JComboBox<String> combo = (JComboBox<String>) e.getSource();
						Object unos = combo.getSelectedItem();
						String selected = (String) unos;
						int indeksStud = 0;
						if (selected != null && selected.length() >= 5) {
							String ind = selected.substring(selected.length() - 6);
							ind = ind.substring(0, 5);
							IndeksStudZahtjev = Integer.parseInt(ind);
							indeksStud = Integer.parseInt(ind);
						}
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							String upit1 = "select imeStud, prezStud, godStud from stud  where indeks = ?";
							PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, indeksStud);
							int GodStud = 0;
							ResultSet rs = stmt1.executeQuery();
							while (rs.next()) {
								String ime = rs.getString("imeStud");
								String prez = rs.getString("prezStud");
								GodStud = rs.getInt("godStud");
								ZahtPoslStudInfo.setText(ime + " " + prez);
								IndeksStudZahtInfo.setText("" + indeksStud);
							}

							upit1 = "select naziv, opis, pitanje,  sifPred2 from predmet inner join zahtjevi on predmet.sifPred= zahtjevi"
									+ ".sifPred2  where indeks=? AND sifPred1=?";
							stmt1 = dataBase.prepareStatement(upit1);
							stmt1.setInt(1, indeksStud);
							stmt1.setInt(2, SifPredZaZahtjev);

							rs = stmt1.executeQuery();
							while (rs.next()) {
								NoviPredZahtInfo.setText(rs.getString("naziv"));
								String opis = rs.getString("opis");
								String pitanje = rs.getString("pitanje");
								String poruka = opis + "\n" + pitanje;
								sifPred2 = rs.getInt("sifPred2");
								textOpis.setText(poruka);

							}

							if (GodStud > 1) {
								upit1 = "select sifPred from registrovaniPredmeti where indeks = ?";
								stmt1 = dataBase.prepareStatement(upit1);
								stmt1.setInt(1, indeksStud);
								rs = stmt1.executeQuery();
								listasifri.clear();
								while (rs.next()) {
									listasifri.add(rs.getInt("sifPred"));
								}
								boolean provjeri = listasifri.contains(SifPredZaZahtjev);
								if (provjeri == false) {
									ZahtPrihvInfo.setText("Ne");
									OdgovorZahtInfo.setText(
											"Postovani, niste registrovani na predmet koji pokusavate zamijeniti.");
									OdgovorNaZahtjev = "Postovani, niste registrovani na predmet koji pokusavate zamijeniti.";
								} else {

									String upit2 = "select indeks from registrovaniPredmeti where sifPred = ? AND prviPut = 1";
									PreparedStatement stupit2 = dataBase.prepareStatement(upit2);
									stupit2.setInt(1, SifPredZaZahtjev);
									ArrayList<Integer> listaindeksi = new ArrayList<>();
									ResultSet rsindeksi = stupit2.executeQuery();
									while (rsindeksi.next()) {
										listaindeksi.add(rsindeksi.getInt("indeks"));
									}
									boolean provjeri2 = listaindeksi.contains(indeksStud);
									if (!provjeri2) {
										ZahtPrihvInfo.setText("Ne");
										OdgovorZahtInfo.setText(
												"Postovani, nije moguce zamijeniti predmet koji ne slusate prvi put.");
										OdgovorNaZahtjev = "Postovani, nije moguce zamijeniti predmet koji ne slusate prvi put.";
									} else {
										String upit3 = "select sifPreduslova from preduslovi where sifPred = ?";
										PreparedStatement stupit3 = dataBase.prepareStatement(upit3);
										stupit3.setInt(1, sifPred2);
										ArrayList<Integer> listapreduslovi = new ArrayList<>();
										ResultSet rspreduslovi = stupit3.executeQuery();
										while (rspreduslovi.next()) {
											listapreduslovi.add(rspreduslovi.getInt("sifPreduslova"));
										}
										String upit4 = "select sifPred from ispiti where indeks = ? AND ocjena > 5 ";
										PreparedStatement stupit4 = dataBase.prepareStatement(upit4);
										stupit4.setInt(1, indeksStud);
										ArrayList<Integer> listapolozeni = new ArrayList<>();
										ResultSet rspolozeni = stupit4.executeQuery();
										while (rspolozeni.next()) {
											listapolozeni.add(rspolozeni.getInt("sifPred"));
										}

										if (listapolozeni.size() == 0) {
											ZahtPrihvInfo.setText("Ne");
											OdgovorZahtInfo.setText(
													"Postovani, ne mozete registrovati predmet za koji nemate polozene sve preduslove.");
											OdgovorNaZahtjev = "Postovani, ne mozete registrovati predmet za koji nemate polozene sve preduslove.";

										} else {

											boolean polozeno = true;
											for (int i = 0; i < listapreduslovi.size(); ++i) {
												polozeno = listapolozeni.contains(listapreduslovi.get(i));
												if (polozeno == false) {
													break;
												}
											}

											if (!polozeno) {
												ZahtPrihvInfo.setText("Ne");
												OdgovorZahtInfo.setText(
														"Postovani, ne mozete registrovati predmet za koji nemate polozene sve preduslove.");
												OdgovorNaZahtjev = "Postovani, ne mozete registrovati predmet za koji nemate polozene sve preduslove.";

											} else {
												String upit6 = "select semestar from predmet where sifPred = ? ";
												PreparedStatement stupit6 = dataBase.prepareStatement(upit6);
												stupit6.setInt(1, sifPred2);
												ResultSet rsSemestar = stupit6.executeQuery();
												while (rsSemestar.next()) {
													SemestarPredZaZaht = rsSemestar.getString("semestar");
												}

												int ukupnoECTS = 0;
												String upit5 = "select ECTS from predmet inner join registrovaniPredmeti on predmet.sifPred = registrovaniPredmeti.sifPred where indeks = ? and semestar = ? ";
												PreparedStatement stupit5 = dataBase.prepareStatement(upit5);
												stupit5.setInt(1, indeksStud);
												stupit5.setString(2, SemestarPredZaZaht);
												ResultSet rsECTS = stupit5.executeQuery();
												while (rsECTS.next()) {
													ukupnoECTS = ukupnoECTS + rsECTS.getInt("ECTS");
												}
												if (SemestarPredZaZaht1.equals(SemestarPredZaZaht)) {
													upit5 = "select ECTS from  predmet where sifPred = ? ";
													stupit5 = dataBase.prepareStatement(upit5);
													stupit5.setInt(1, SifPredZaZahtjev);
													rsECTS = stupit5.executeQuery();
													while (rsECTS.next()) {
														ukupnoECTS = ukupnoECTS - rsECTS.getInt("ECTS");
													}
												}
												upit5 = "select ECTS from predmet where sifPred = ? ";
												stupit5 = dataBase.prepareStatement(upit5);
												stupit5.setInt(1, sifPred2);
												rsECTS = stupit5.executeQuery();
												while (rsECTS.next()) {
													ukupnoECTS = ukupnoECTS + rsECTS.getInt("ECTS");
												}

												if (ukupnoECTS > 30) {
													if (GodStud < 4) {
														ZahtPrihvInfo.setText("Ne");
														OdgovorZahtInfo.setText(
																"Postovani, ne mozete registrovati vise od  30 ECTS kredita po jednom semestru.");
														OdgovorNaZahtjev = "Postovani, ne mozete registrovati vise od  30 ECTS kredita po jednom semestru.";
													} else {
														ZahtPrihvInfo.setText("Da");
														OdgovorZahtInfo.setText(
																"Postovani, spjesno ste izvrsili zamjenu predmeta.");
														OdgovorNaZahtjev = "Postovani, uspjesno ste izvrsili zamjenu predmeta.";
													}
												} else {
													ZahtPrihvInfo.setText("Da");
													OdgovorZahtInfo.setText("Uspjesno ste izvrsili zamjenu predmeta.");
													OdgovorNaZahtjev = "Postovani, uspjesno ste izvrsili zamjenu predmeta.";

												}

											}
										}
									}

								}

							} else {
								ZahtPrihvInfo.setText("Ne");
								OdgovorZahtInfo
										.setText("Studenti prve godine ne mogu mijenjati registrovane predmete.");
							}

							dataBase.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

			}
		});

		PotvrdiOdgZahtjevB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (SifPredZaZahtjev == 0 || IndeksStudZahtjev == 0) {
					JOptionPane.showMessageDialog(null, "Niste odabrali zahtjev.", "Poruka", 0);
				} else {
					boolean check = true;
					String provjera = ZahtPrihvInfo.getText();
					if (!provjera.equals("Da")) {

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");
							String update = "update zahtjevi set odgovor = ? where indeks = ? AND sifPred1 = ?";
							PreparedStatement updateSt = dataBase.prepareStatement(update);
							updateSt.setString(1, OdgovorNaZahtjev);
							updateSt.setInt(2, IndeksStudZahtjev);
							updateSt.setInt(3, SifPredZaZahtjev);
							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							updateSt.executeUpdate();
							dataBase.commit();
							dataBase.close();
						} catch (Exception exc) {
							exc.printStackTrace();
							check = false;
						}

					} else {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");
							String update = "update zahtjevi set odgovor = ? where indeks = ? AND sifPred1 = ?";
							PreparedStatement updateSt = dataBase.prepareStatement(update);
							updateSt.setString(1, OdgovorNaZahtjev);
							updateSt.setInt(2, IndeksStudZahtjev);
							updateSt.setInt(3, SifPredZaZahtjev);

							String update2 = "update registrovaniPredmeti set sifPred =  ? where sifPred = ? AND indeks = ?";
							PreparedStatement updateSt2 = dataBase.prepareStatement(update2);
							updateSt2.setInt(1, sifPred2);
							updateSt2.setInt(2, SifPredZaZahtjev);
							updateSt2.setInt(3, IndeksStudZahtjev);

							dataBase.setAutoCommit(false);
							dataBase.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
							updateSt.executeUpdate();
							updateSt2.executeUpdate();
							dataBase.commit();
							dataBase.close();
						} catch (Exception exc) {
							exc.printStackTrace();
							check = false;
						}

					}

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						Statement stmt = dataBase.createStatement();
						ResultSet rs = stmt.executeQuery(
								"select distinct naziv, sifPred from predmet inner join zahtjevi on predmet.sifPred= zahtjevi.sifPred1  where zahtjevi.odgovor is null");
						PredZaZahtjevCombo.removeAllItems();
						while (rs.next()) {
							int sifPred = rs.getInt("sifPred");
							String naziv = rs.getString("naziv");
							String opcion = naziv + " (" + sifPred + ")";
							PredZaZahtjevCombo.addItem(opcion);
						}

						dataBase.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					OdabPredZahtjevInfo.setText("");
					NosilacPredZahtjevInfo.setText("");
					SemestarZahtjeviInfo.setText("");
					StudZahtjeviCombo.removeAllItems();
					ZahtPoslStudInfo.setText("");
					IndeksStudZahtInfo.setText("");
					PredZaZamjInfo.setText("");
					NoviPredZahtInfo.setText("");
					textOpis.setText("");
					ZahtPrihvInfo.setText("");
					sifPred2 = 0;
					SifPredZaZahtjev = 0;
					IndeksStudZahtjev = 0;
					OdgovorZahtInfo.setText("");
					if (check) {
						JOptionPane.showMessageDialog(null, "Uspjesno ste odgovorili na zahtjev.", "Poruka", 1);
					}
				}

			}
		});

		// Pregled registrovanih studenata
		pregledPrijavljenihStudenata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PregledRegStud();

				OdaberiPredCombo.removeAllItems();
				boolean check = true;

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");
					Statement stmt = dataBase.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT sifPred, naziv FROM predmet");
					while (rs.next()) {
						int sifPred = rs.getInt("sifPred");
						String naziv = rs.getString("naziv");
						String opcion = naziv + " (" + sifPred + ")";
						OdaberiPredCombo.addItem(opcion);
					}
					dataBase.close();
				} catch (Exception exc) {
					exc.printStackTrace();
					check = false;
				}
				if (check) {

					imenosiocPred.setText("");
					semestar.setText("");
					ects.setText("");
					comboNast.removeAllItems();
					StudCombo.removeAllItems();
					StudImeInfo.setText("");
					StudPrezInfo.setText("");
					StudIndexInfo.setText("");
					StudStatusInfo.setText("");
					StudGodStudInfo.setText("");
					StudPrviPutInfo.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "Trenutno nije moguce pregledati podatke", "Poruka", 1);
				}

				pretraziOdaberiPredCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection dataBase = DriverManager.getConnection(
									"jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
											+ TimeZone.getDefault().getID(),
									"sql7630078", "wgpv7L1rUF");

							ArrayList<String> items = new ArrayList<>();
							Statement stmt = dataBase.createStatement();
							ResultSet rs = stmt.executeQuery("SELECT sifPred, naziv FROM predmet");
							items.clear();
							while (rs.next()) {
								String naziv = rs.getString("naziv");
								int sifra = rs.getInt("sifPred");
								String item = naziv + " (" + sifra + ")";
								items.add(item);
							}

							String enteredText = pretraziOdaberiPredCombo.getText();
							DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) OdaberiPredCombo
									.getModel();
							if (model != null) {
								model.removeAllElements();
								for (String item : items) {
									if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
										model.addElement(item);
									}
								}
								if (model.getSize() > 0) {
									OdaberiPredCombo.setSelectedIndex(0);
								}
							}
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}

				});

			}
		});

		PreglRegStud.setBounds(260, 73, 500, 30);
		PreglRegStud.setForeground(new Color(37, 32, 141));
		PreglRegStud.setFont(font2);

		OdaberiPred.setBounds(50, 135, 300, 25);
		OdaberiPred.setForeground(new Color(37, 32, 141));
		OdaberiPred.setFont(font2);

		pretraziOdaberiPredCombo.setBounds(50, 170, 400, 25);
		pretraziOdaberiPredCombo.setForeground(new Color(37, 32, 141));
		pretraziOdaberiPredCombo.setFont(font3);

		OdaberiPredCombo.setBounds(50, 205, 400, 30);
		OdaberiPredCombo.setForeground(new Color(37, 32, 141));
		OdaberiPredCombo.setFont(font2);

		PodOdabPredL.setBounds(50, 240, 400, 25);
		PodOdabPredL.setForeground(new Color(37, 32, 141));
		PodOdabPredL.setFont(font2);

		nosiocPred.setBounds(50, 275, 100, 25);
		nosiocPred.setForeground(new Color(37, 32, 141));
		nosiocPred.setFont(font2);

		imenosiocPred.setBounds(143, 275, 240, 25);
		imenosiocPred.setForeground(Color.WHITE);
		imenosiocPred.setFont(font2);

		semestarL.setBounds(50, 310, 200, 25);
		semestarL.setForeground(new Color(37, 32, 141));
		semestarL.setFont(font2);
		semestar.setBounds(160, 310, 180, 25);
		semestar.setFont(font2);
		semestar.setForeground(Color.WHITE);

		ectsL.setBounds(50, 345, 150, 25);
		ectsL.setForeground(new Color(37, 32, 141));
		ectsL.setFont(font2);
		ects.setBounds(185, 345, 140, 25);
		ects.setForeground(Color.WHITE);
		ects.setFont(font2);

		nastNaPredL.setBounds(50, 380, 400, 25);
		nastNaPredL.setForeground(new Color(37, 32, 141));
		nastNaPredL.setFont(font3);

		pretraga.setBounds(50, 415, 400, 25);
		pretraga.setForeground(new Color(37, 32, 141));
		pretraga.setFont(font2);

		comboNast.setBounds(50, 450, 400, 28);
		comboNast.setForeground(Color.WHITE);
		comboNast.setBackground(new Color(43, 143, 142));
		comboNast.setFont(font2);
		Border border = new LineBorder(Color.white, 1, false);
		comboNast.setBorder(border);

		studNaPredL.setBounds(600, 135, 400, 25);
		studNaPredL.setForeground(new Color(37, 32, 141));
		studNaPredL.setFont(font2);

		pretragaStud.setBounds(600, 170, 400, 25);
		pretragaStud.setForeground(new Color(37, 32, 141));
		pretragaStud.setFont(font3);

		StudCombo.setBounds(600, 205, 400, 28);
		StudCombo.setForeground(Color.WHITE);
		StudCombo.setBackground(new Color(43, 143, 142));
		StudCombo.setFont(font2);
		StudCombo.setBorder(border);

		StudInfoL.setBounds(600, 240, 500, 25);
		StudInfoL.setForeground(new Color(37, 32, 141));
		StudInfoL.setFont(font2);

		StudImeL.setBounds(600, 275, 100, 25);
		StudImeL.setForeground(new Color(37, 32, 141));
		StudImeL.setFont(font2);

		StudImeInfo.setBounds(672, 275, 240, 25);
		StudImeInfo.setForeground(Color.WHITE);
		StudImeInfo.setFont(font2);

		StudPrezL.setBounds(600, 310, 200, 25);
		StudPrezL.setForeground(new Color(37, 32, 141));
		StudPrezL.setFont(font2);
		StudPrezInfo.setBounds(685, 310, 180, 25);
		StudPrezInfo.setForeground(Color.WHITE);
		StudPrezInfo.setFont(font2);

		StudIndexL.setBounds(600, 345, 150, 25);
		StudIndexL.setForeground(new Color(37, 32, 141));
		StudIndexL.setFont(font2);

		StudIndexInfo.setBounds(677, 345, 140, 25);
		StudIndexInfo.setForeground(Color.WHITE);
		StudIndexInfo.setFont(font2);

		StudStatusL.setBounds(600, 380, 200, 25);
		StudStatusL.setForeground(new Color(37, 32, 141));
		StudStatusL.setFont(font2);

		StudStatusInfo.setBounds(677, 380, 200, 25);
		StudStatusInfo.setForeground(Color.WHITE);
		StudStatusInfo.setFont(font2);

		StudGodStudL.setBounds(600, 415, 300, 25);
		StudGodStudL.setForeground(new Color(37, 32, 141));
		StudGodStudL.setFont(font2);

		StudGodStudInfo.setBounds(755, 415, 50, 25);
		StudGodStudInfo.setForeground(Color.WHITE);
		StudGodStudInfo.setFont(font2);

		StudPrviPutL.setBounds(600, 450, 300, 25);
		StudPrviPutL.setForeground(new Color(37, 32, 141));
		StudPrviPutL.setFont(font2);

		StudPrviPutInfo.setBounds(830, 450, 50, 25);
		StudPrviPutInfo.setForeground(Color.WHITE);
		StudPrviPutInfo.setFont(font2);

		pretraziOdaberiPredCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String provjera = pretraziOdaberiPredCombo.getText();
				if (!provjera.equals("")) {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						ArrayList<String> items = new ArrayList<>();
						Statement stmt = dataBase.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT sifPred, naziv FROM predmet");
						items.clear();
						while (rs.next()) {
							String naziv = rs.getString("naziv");
							int sifra = rs.getInt("sifPred");
							String item = naziv + " (" + sifra + ")";
							items.add(item);
						}

						String enteredText = pretraziOdaberiPredCombo.getText();
						DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) OdaberiPredCombo.getModel();
						if (model != null) {
							model.removeAllElements();
							for (String item : items) {
								if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
									model.addElement(item);
								}
							}
							if (model.getSize() > 0) {
								OdaberiPredCombo.setSelectedIndex(0);
							}
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else {

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						ArrayList<String> items = new ArrayList<>();
						Statement stmt = dataBase.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT sifPred, naziv FROM predmet");
						items.clear();
						while (rs.next()) {
							String naziv = rs.getString("naziv");
							int sifra = rs.getInt("sifPred");
							String item = naziv + " (" + sifra + ")";
							items.add(item);
						}

					} catch (Exception exception) {
						exception.printStackTrace();
					}

					imenosiocPred.setText("");
					semestar.setText("");
					ects.setText("");
					comboNast.removeAllItems();
					StudCombo.removeAllItems();
					StudImeInfo.setText("");
					StudPrezInfo.setText("");
					StudIndexInfo.setText("");
					StudStatusInfo.setText("");
					StudGodStudInfo.setText("");
					StudPrviPutInfo.setText("");
				}

				if (OdaberiPredCombo.getItemCount() == 0) {
					imenosiocPred.setText("");
					semestar.setText("");
					ects.setText("");
					comboNast.removeAllItems();
					StudCombo.removeAllItems();
					StudImeInfo.setText("");
					StudPrezInfo.setText("");
					StudIndexInfo.setText("");
					StudStatusInfo.setText("");
					StudGodStudInfo.setText("");
					StudPrviPutInfo.setText("");
				}
			}

		});

		pretraga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

					ArrayList<String> items = new ArrayList<>();

					String upit3 = "select imeNast, prezNast from nastavnik inner join nastavnikNaPredmetu on nastavnik.sifNast= nastavnikNaPredmetu.sifNast \n"
							+ "where sifPred = ?";
					PreparedStatement st = dataBase.prepareStatement(upit3);
					st.setInt(1, SifPredPreglStud);
					ResultSet rs = st.executeQuery();
					items.clear();
					while (rs.next()) {
						String ime = rs.getString("imeNast");
						String prez = rs.getString("prezNast");
						String nastavnik = ime + " " + prez;
						items.add(nastavnik);

					}

					String enteredText = pretraga.getText();
					DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboNast.getModel();
					if (model != null) {
						model.removeAllElements();
						for (String item : items) {
							if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
								model.addElement(item);
							}
						}
						if (model.getSize() > 0) {
							comboNast.setSelectedIndex(0);
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});

		pretragaStud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String provjera = pretragaStud.getText();
				if (!provjera.equals("")) {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						ArrayList<String> items = new ArrayList<>();
						String upit4 = "select imeStud, prezStud, stud.indeks from stud  inner join registrovaniPredmeti on stud.indeks=registrovaniPredmeti.indeks \n"
								+ "where sifPred = ?";
						PreparedStatement st = dataBase.prepareStatement(upit4);
						st.setInt(1, SifPredPreglStud);
						ResultSet rs = st.executeQuery();
						items.clear();
						while (rs.next()) {
							String ime = rs.getString("imeStud");
							String prez = rs.getString("prezStud");
							int ind = rs.getInt("stud.indeks");
							String student = ime + " " + prez + " (" + ind + ")";
							items.add(student);
						}

						String enteredText = pretragaStud.getText();
						DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) StudCombo.getModel();
						if (model != null) {
							model.removeAllElements();
							for (String item : items) {
								if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
									model.addElement(item);
								}
							}
							if (model.getSize() > 0) {
								StudCombo.setSelectedIndex(0);
							}
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else {

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection dataBase = DriverManager
								.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
										+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

						ArrayList<String> items = new ArrayList<>();
						String upit4 = "select imeStud, prezStud, stud.indeks from stud  inner join registrovaniPredmeti on stud.indeks=registrovaniPredmeti.indeks \n"
								+ "where sifPred = ?";
						PreparedStatement st = dataBase.prepareStatement(upit4);
						st.setInt(1, SifPredPreglStud);
						ResultSet rs = st.executeQuery();
						items.clear();
						StudCombo.removeAllItems();
						while (rs.next()) {
							String ime = rs.getString("imeStud");
							String prez = rs.getString("prezStud");
							int ind = rs.getInt("stud.indeks");
							String student = ime + " " + prez + " (" + ind + ")";
							items.add(student);
							StudCombo.addItem(student);
						}

					} catch (Exception exception) {
						exception.printStackTrace();
					}
					StudImeInfo.setText("");
					StudPrezInfo.setText("");
					StudIndexInfo.setText("");
					StudStatusInfo.setText("");
					StudGodStudInfo.setText("");
					StudPrviPutInfo.setText("");

				}

				if (StudCombo.getItemCount() == 0) {
					StudImeInfo.setText("");
					StudPrezInfo.setText("");
					StudIndexInfo.setText("");
					StudStatusInfo.setText("");
					StudGodStudInfo.setText("");
					StudPrviPutInfo.setText("");
				}

			}

		});

		OdaberiPredCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				Object unos = combo.getSelectedItem();
				String selected = (String) unos;
				if (selected != null && selected.length() >= 7) {
					String sifrapred = selected.substring(selected.length() - 8);
					sifrapred = sifrapred.substring(0, 7);
					SifPredPreglStud = Integer.parseInt(sifrapred);
				}

				boolean check = true;

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

					String upit1 = "select imeNast, prezNast from nastavnik inner join predmet on predmet.nosilac = nastavnik.sifNast where sifPred = ?";
					PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
					stmt1.setInt(1, SifPredPreglStud);
					ResultSet rs = stmt1.executeQuery();
					imenosiocPred.setText("");
					while (rs.next()) {
						String imenast = rs.getString("imeNast");
						String prezime = rs.getString("prezNast");
						String nastavnik = imenast + " " + prezime;
						imenosiocPred.setText(nastavnik);
					}

					String upit2 = "select semestar, ECTS from predmet where sifPred = ?";
					PreparedStatement st = dataBase.prepareStatement(upit2);
					st.setInt(1, SifPredPreglStud);
					rs = st.executeQuery();
					ects.setText("");
					semestar.setText("");
					while (rs.next()) {
						String s = rs.getString("semestar");
						String krediti = rs.getString("ECTS");
						semestar.setText(s);
						ects.setText(krediti);
					}

					String upit3 = "select imeNast, prezNast from nastavnik inner join nastavnikNaPredmetu on nastavnik.sifNast= nastavnikNaPredmetu.sifNast \n"
							+ "where sifPred = ?";
					st = dataBase.prepareStatement(upit3);
					st.setInt(1, SifPredPreglStud);
					rs = st.executeQuery();
					comboNast.removeAllItems();
					while (rs.next()) {
						String ime = rs.getString("imeNast");
						String prez = rs.getString("prezNast");
						String nastavnik = ime + " " + prez;
						comboNast.addItem(nastavnik);

					}

					String upit4 = "select imeStud, prezStud, stud.indeks from stud  inner join registrovaniPredmeti on stud.indeks=registrovaniPredmeti.indeks \n"
							+ "where sifPred = ?";
					st = dataBase.prepareStatement(upit4);
					st.setInt(1, SifPredPreglStud);
					rs = st.executeQuery();
					StudCombo.removeAllItems();
					while (rs.next()) {
						String ime = rs.getString("imeStud");
						String prez = rs.getString("prezStud");
						int ind = rs.getInt("stud.indeks");
						String student = ime + " " + prez + " (" + ind + ")";
						StudCombo.addItem(student);
					}

					dataBase.close();
				} catch (Exception ex) {
					check = false;
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Nije moguce pregledati podatke za odabrani predmet!", "Poruka",
							JOptionPane.ERROR_MESSAGE);
				}

				if (check) {
					StudImeInfo.setText("");
					StudPrezInfo.setText("");
					StudIndexInfo.setText("");
					StudStatusInfo.setText("");
					StudGodStudInfo.setText("");
					StudPrviPutInfo.setText("");
				}

			}
		});

		StudCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int sifStudenta = 0;
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				Object unos = combo.getSelectedItem();
				String selected = (String) unos;
				if (selected != null && selected.length() >= 5) {
					String sifrapred = selected.substring(selected.length() - 6);
					sifrapred = sifrapred.substring(0, 5);
					sifStudenta = Integer.parseInt(sifrapred);
				}

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection dataBase = DriverManager
							.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7630078?serverTimezone="
									+ TimeZone.getDefault().getID(), "sql7630078", "wgpv7L1rUF");

					String upit1 = "select imeStud, prezStud, stud.indeks, statusStud, godStud, prviPut from stud inner join registrovaniPredmeti on stud.indeks=registrovaniPredmeti.indeks \n"
							+ "where stud.indeks=?";
					PreparedStatement stmt1 = dataBase.prepareStatement(upit1);
					stmt1.setInt(1, sifStudenta);
					ResultSet rs = stmt1.executeQuery();
					while (rs.next()) {
						String ime = rs.getString("imeStud");
						String prezime = rs.getString("prezStud");
						int ind = rs.getInt("stud.indeks");
						String s = rs.getString("statusStud");
						int god = rs.getInt("godStud");
						int prvi = rs.getInt("prviPut");
						StudImeInfo.setText(ime);
						StudPrezInfo.setText(prezime);
						StudIndexInfo.setText(String.valueOf(ind));
						StudStatusInfo.setText(s);
						StudGodStudInfo.setText("" + god);
						if (prvi == 1) {
							StudPrviPutInfo.setText("Da");
						} else {
							StudPrviPutInfo.setText("Ne");
						}

					}

					dataBase.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Nije moguce pregledati podatke za studenta!", "Poruka",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

	}
}
