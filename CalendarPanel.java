package prodekan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarPanel extends JPanel {
	private JLabel dateField;
	private JPanel calendarGrid;
	private JTextField searchMonth;
	private JTextField searchYear;
	private JComboBox<String> monthComboBox;
	private JComboBox<String> yearComboBox;
	private Calendar currentMonth;
	private JLabel title;

	public CalendarPanel(JLabel dateField) {

		this.dateField = dateField;
		this.setLayout(new BorderLayout());

		currentMonth = Calendar.getInstance();
		currentMonth.set(Calendar.DAY_OF_MONTH, 1);

		JPanel headerPanel = new JPanel();

		monthComboBox = new JComboBox<>();
		for (int month = 0; month < 12; month++) {
			String monthName = new SimpleDateFormat("MMMM").format(new GregorianCalendar(0, month, 1).getTime());
			monthComboBox.addItem(monthName);
		}
		monthComboBox.setSelectedIndex(currentMonth.get(Calendar.MONTH));
		monthComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCalendarPanel();
			}
		});

		yearComboBox = new JComboBox<>();
		int currentYear = currentMonth.get(Calendar.YEAR);
		for (int year = currentYear - 10; year <= currentYear + 10; year++) {
			yearComboBox.addItem(String.valueOf(year));
		}
		yearComboBox.setSelectedItem(String.valueOf(currentYear));
		yearComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCalendarPanel();
			}
		});

		headerPanel.add(monthComboBox);
		headerPanel.add(yearComboBox);

		this.add(headerPanel, BorderLayout.NORTH);

		JPanel titlePanel = new JPanel(new BorderLayout());

		title = new JLabel("", SwingConstants.CENTER);
		title.setFont(new Font("Serif", Font.BOLD, 18));
		titlePanel.add(title, BorderLayout.NORTH);

		calendarGrid = new JPanel(new GridLayout(0, 7));
		calendarGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		titlePanel.add(calendarGrid, BorderLayout.CENTER);

		this.add(titlePanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		this.add(buttonPanel, BorderLayout.SOUTH);
		updateCalendarPanel();
	}

	private void updateCalendarPanel() {
		calendarGrid.removeAll();

		int selectedYear = Integer.parseInt(yearComboBox.getSelectedItem().toString());
		int selectedMonth = monthComboBox.getSelectedIndex();

		currentMonth = new GregorianCalendar(selectedYear, selectedMonth, 1);

		int year = currentMonth.get(Calendar.YEAR);
		int month = currentMonth.get(Calendar.MONTH);
		int daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

		Calendar tempCalendar = new GregorianCalendar(year, month, 1);
		int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);

		String monthYearTitle = new SimpleDateFormat("MMMM yyyy").format(currentMonth.getTime());
		title.setText(monthYearTitle);

		for (String dayName : new String[] { "Ned", "Pon", "Uto", "Sri", "Čet", "Pet", "Sub" }) {
			JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
			calendarGrid.add(dayLabel);
		}

		for (int i = 1; i < firstDayOfWeek; i++) {
			calendarGrid.add(new JLabel(""));
		}

		for (int day = 1; day <= daysInMonth; day++) {
			final int selectedDay = day;
			JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
			dayLabel.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Calendar selectedDate = new GregorianCalendar(year, month, selectedDay);
					String newdata = dateFormat.format(selectedDate.getTime());
					dateField.setText(newdata.replace('/', '.'));
					((JDialog) SwingUtilities.getWindowAncestor(CalendarPanel.this)).dispose();
				}
			});
			calendarGrid.add(dayLabel);
		}

		this.revalidate();
		this.repaint();
	}

}