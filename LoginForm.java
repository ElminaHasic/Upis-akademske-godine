package login;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.border.*;

import admin.HelperFunctions;
import admin.SQL;
import admin.Student;
import prodekan.ProfilProdekana;

public class LoginForm {
	static boolean checkusername = false, checkpassword = false;
	static String username, password;
	static student.Student studFrame;
	static nastavnik.Nastavnik nastavnikFrame;
	static admin.Admin adminFrame;
	static ProfilProdekana prodekanFrame;
	static JFrame frame;
	static JLabel title;
	static JLabel title2;
	static JLabel labelicon;
	static JLabel labelname;
	static JButton login;
	static JTextField usernamefl;
	static JLabel labelpassword;
	static JPasswordField passwordfl;

	public static void main(String[] args) {

		frame = new JFrame();
		frame.setLayout(null);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frame, "Da li ste sigurni za zelite zatvoriti program?",
						"Zatvaranje programa?", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					try {
						SQL.conn.commit();
						SQL.conn.close();
					} catch (Exception e) {
						System.out.println(e);
					}

					System.exit(0);
				}
			}
		});
		frame.setBounds(500, 200, 800, 450);
		frame.getContentPane().setBackground(new Color(194, 29, 43));
		title = new JLabel("FAKULTET ELEKTROTEHNIKE");
		Font font1 = new Font("Purisa", Font.BOLD, 20);
		title.setBounds(95, 50, 400, 30);
		title.setForeground(Color.WHITE);
		title.setFont(font1);

		title2 = new JLabel("Registracija predmeta");
		title2.setBounds(130, 120, 300, 30);
		title2.setForeground(Color.WHITE);
		title2.setFont(font1);

		ImageIcon imageicon = new ImageIcon("/home/elmina/Downloads/login.png");
		Image image = imageicon.getImage();
		Image resizeImage = image.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
		ImageIcon resizeIcon = new ImageIcon(resizeImage);
		labelicon = new JLabel();
		labelicon.setIcon(resizeIcon);
		labelicon.setBounds(180, 190, 150, 150);

		labelname = new JLabel("Email: ");
		labelname.setBounds(500, 125, 190, 25);
		Font font2 = new Font("PURISA", Font.BOLD, 13);
		labelname.setFont(font2);
		labelname.setForeground(Color.WHITE);

		usernamefl = new JTextField();
		usernamefl.setBounds(500, 155, 190, 25);
		usernamefl.setFont(font2);
		usernamefl.setForeground(new Color(37, 32, 141));
		usernamefl.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		labelpassword = new JLabel("Password: ");
		labelpassword.setBounds(500, 200, 190, 25);
		labelpassword.setFont(font2);
		labelpassword.setForeground(Color.WHITE);

		passwordfl = new JPasswordField();
		passwordfl.setBounds(500, 230, 190, 25);
		passwordfl.setFont(font2);
		passwordfl.setForeground(new Color(37, 32, 141));
		passwordfl.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		login = new JButton("Login");
		login.setBounds(500, 300, 190, 25);
		login.setFont(font2);
		login.setBackground(new Color(37, 32, 141));
		login.setForeground(Color.WHITE);
		login.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		loadLoginFrame();

		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				username = usernamefl.getText();
				if (username.equals("")) {
					Border newborder = new LineBorder(new Color(37, 32, 141), 3, false);
					usernamefl.setBorder(newborder);
					checkusername = true;
					JOptionPane.showMessageDialog(null, "Niste unijeli email.", "Poruka", 0);
				}

				password = passwordfl.getText();
				if (password.equals("")) {
					Border newborder = new LineBorder(new Color(37, 32, 141), 3, false);
					passwordfl.setBorder(newborder);
					checkpassword = true;
					JOptionPane.showMessageDialog(null, "Niste unijeli password.", "Poruka", 0);
				}

				if (checkusername && !username.equals("")) {
					Border newborder = new LineBorder(Color.WHITE, 3, false);
					usernamefl.setBorder(newborder);

				}
				if (checkpassword && !password.equals("")) {
					Border newborder = new LineBorder(Color.WHITE, 3, false);
					passwordfl.setBorder(newborder);

				}

				login(username, password);
			}
		});

	}

	private static void loadLoginFrame() {
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.getContentPane().setBackground(new Color(194, 29, 43));
		usernamefl.setText("");
		passwordfl.setText("");
		frame.add(title);
		frame.add(title2);
		frame.add(labelicon);
		frame.add(labelname);
		frame.add(usernamefl);
		frame.add(labelpassword);
		frame.add(passwordfl);
		frame.add(login);

		frame.setVisible(true);
	}

	private static admin.Student ucitajStudenta(int id, String email) {
		try {

			String select = "SELECT * FROM stud WHERE indeks=?";
			PreparedStatement stmt1 = SQL.conn.prepareStatement(select);
			stmt1.setInt(1, id);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				admin.Student user = new admin.Student(rs1.getString("imeStud"), rs1.getString("prezStud"), email,
						rs1.getInt("indeks"), rs1.getString("statusStud"), rs1.getInt("godStud"));
				return user;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

	}

	private static admin.Nastavnik ucitajNastavnika(int id, String email) {
		try {

			String select = "SELECT * FROM nastavnik WHERE sifNast=?";
			PreparedStatement stmt1 = SQL.conn.prepareStatement(select);
			stmt1.setInt(1, id);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				admin.Nastavnik user = new admin.Nastavnik(rs1.getString("imeNast"), rs1.getString("prezNast"), email,
						rs1.getInt("sifNast"), rs1.getString("zvanje"), rs1.getBoolean("prodekan"));
				return user;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	static void login(String username, String password) {
		try {

			String select = "SELECT * FROM korisnik WHERE email=?";
			PreparedStatement stmt1 = SQL.conn.prepareStatement(select);
			stmt1.setString(1, username);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) { // postoji korisnik
				String userPassword = rs1.getString("password");
				String status = rs1.getString("statusKorisnika");
				int id = rs1.getInt("id");
				if (userPassword.equals(password)) {
					switch (status) {
					case "student":
						Student user = ucitajStudenta(id, username);
						studFrame = new student.Student(frame, user);
						studFrame.onLogout = () -> {
							loadLoginFrame();
						};
						studFrame.goToFirstFrame();
						break;
					case "nastavnik":
						admin.Nastavnik nastavnikUser = ucitajNastavnika(id, username);
						if (!nastavnikUser.isProdekan()) {
							nastavnikFrame = new nastavnik.Nastavnik();
							nastavnikFrame.onLogout = () -> {
								loadLoginFrame();
							};
							nastavnikFrame.sifra = id;
							nastavnikFrame.nastavnikInit(frame);
						} else {
							prodekanFrame = new ProfilProdekana(frame);
							prodekanFrame.logout.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									loadLoginFrame();

								}
							});
						}
						break;
					case "admin":
						adminFrame = new admin.Admin(frame);
						adminFrame.onLogout = () -> {
							loadLoginFrame();
						};
						adminFrame.goToFirstPage();
						break;

					}

				} else {
					JOptionPane.showMessageDialog(null, "Pogresna lozinka", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else

				JOptionPane.showMessageDialog(null, "Pogresan email korisnika ili korisnik ne postoji!", "Error",
						JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
