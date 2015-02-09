/*
 * File: Calendar.java
 * Description: a calendar picker GUI
 * Author: Muhd Mirza
 * Date created: 5 Feb 2015
 */

package javaGUI.calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.GregorianCalendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Calendar extends JPanel {
	
	private static JFrame frame;
	private JFrame miniFrame;
	
	private String[] dayString;
	private String[] monthString;
	
	private GregorianCalendar gc;
	private int year;
	private int month;
	private int firstDayOfMonth;
	private int actualDays;
	private int totalDays;
	
	private JComboBox monthComboBox;
	private JComboBox yearComboBox;
	
	private DefaultTableModel dtm;
	private JTable calendarTable;
	private int tableValue;
	private String dateString;
	
	private JButton done;
	
	/*
	 * calendar workflow on first loop:
	 * - create a new GregorianCalendar object
	 * - set data in table
	 * - fill in table
	 * - set up the gui table 
	 * 
	 * calendr workflow on event handling:
	 * - set time variables using GregorianCalendar object in local scope
	 * - set data in table
	 * - fill in table
	 * - set up the gui table
	 */
	
	public Calendar() {
		super();
		
		/*
		 * declarations and initialisations of variables
		 */
		
		/*
		 * passing the data here
		 * dealing with the actual default table model messes things up
		 */
		tableValue = 0;
		
		/*
		 * Using two variables to keep track of the days and to facilitate the loop
		 * actualDays - increment this to simulate days
		 * totalDays - initialised at a later stage, this holds the total days in a selected month
		 *  
		 * actualDays goes up, totalDays goes down
		 */
		actualDays = 1;
		
		dayString = new String[7];
		dayString[0] = "Sunday";
		dayString[1] = "Monday";
		dayString[2] = "Tuesday";
		dayString[3] = "Wednesday";
		dayString[4] = "Thursday";
		dayString[5] = "Friday";
		dayString[6] = "Saturday";
		
		monthString = new String[12];
		monthString[0] = "January";
		monthString[1] = "February";
		monthString[2] = "March";
		monthString[3] = "April";
		monthString[4] = "May";
		monthString[5] = "June";
		monthString[6] = "July";
		monthString[7] = "August";
		monthString[8] = "September";
		monthString[9] = "October";
		monthString[10] = "November";
		monthString[11] = "December";
		
		/*
		 * using only GregorianCalendar.YEAR tends to up your system timing by one
		 * same as for GregorianCalendar.MONTH
		 * calling the methods by object is better 
		 */ 
 		gc = new GregorianCalendar();
		year = gc.get(GregorianCalendar.YEAR);
		month = gc.get(GregorianCalendar.MONTH);
		
		
		/*
		 * set up the GUIs
		 */
		monthComboBox = new JComboBox(new DefaultComboBoxModel(monthString));
		monthComboBox.setSelectedIndex(month); // set default to system month
		monthComboBox.setBounds(10, 10, 100, 40);
		monthComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (monthComboBox.getSelectedIndex() == 0) {
					month = GregorianCalendar.JANUARY;
				}
				
				if (monthComboBox.getSelectedIndex() == 1) {
					month = GregorianCalendar.FEBRUARY;
				}
				
				if (monthComboBox.getSelectedIndex() == 2) {
					month = GregorianCalendar.MARCH;
				}
				
				if (monthComboBox.getSelectedIndex() == 3) {
					month = GregorianCalendar.APRIL;
				}
				
				if (monthComboBox.getSelectedIndex() == 4) {
					month = GregorianCalendar.MAY;
				}
				
				if (monthComboBox.getSelectedIndex() == 5) {
					month = GregorianCalendar.JUNE;
				}
				
				if (monthComboBox.getSelectedIndex() == 6) {
					month = GregorianCalendar.JULY;
				}
				
				if (monthComboBox.getSelectedIndex() == 7) {
					month = GregorianCalendar.AUGUST;
				}
				
				if (monthComboBox.getSelectedIndex() == 8) {
					month = GregorianCalendar.SEPTEMBER;
				}
				
				if (monthComboBox.getSelectedIndex() == 9) {
					month = GregorianCalendar.OCTOBER;
				}
				
				if (monthComboBox.getSelectedIndex() == 10) {
					month = GregorianCalendar.NOVEMBER;
				}
				
				if (monthComboBox.getSelectedIndex() == 11) {
					month = GregorianCalendar.DECEMBER;
				}
				
				if (true) {
					System.out.println(monthComboBox.getSelectedIndex());	
				}
				
				
				fillTable((int) yearComboBox.getSelectedItem(), month);
			}
		});
		add(monthComboBox);
		
		yearComboBox = new JComboBox();
		for (int i = year - 80; i < year + 20; i++) { // by doing this, you are able to specify range of the JComboBox
			yearComboBox.addItem(i);
		}
		yearComboBox.setSelectedIndex(80); // year - 80   -------- year --------- year + 20
		yearComboBox.setBounds(400, 10, 100, 40);
		yearComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String temp = yearComboBox.getSelectedItem().toString();
				year = Integer.parseInt(temp);
				System.out.println(year);
		
				fillTable(year, month);
			}
		});
		add(yearComboBox);
		
		// set table model
		dtm = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		dtm.setColumnCount(7);
		dtm.setRowCount(7);
		
		// populate table's first row with days 
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(String.valueOf(dayString[i]), 0, i);
		}
		
		/*
		 *  method call here to facilitate first loop
		 *  do not put the contents of this method in the constructor
		 *  the program will loop and use the same table model 
		 *  it will either crash or give you inaccurate results
		 */
		fillTable(year, month);
		
		done = new JButton("Done"); 
		done.setFont(new Font("Tahoma", Font.PLAIN, 12));		
		done.setForeground(Color.BLACK);
		done.setBounds(420, 400, 50, 50);
		done.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (true) {
					System.out.println(tableValue + "-" + (gc.get(GregorianCalendar.MONTH) + 1) + "-" + gc.get(GregorianCalendar.YEAR));
				}
				
				JOptionPane.showMessageDialog(miniFrame, "You chose " + dateString +
											 (gc.get(GregorianCalendar.MONTH) + 1) + "-" 
											  + gc.get(GregorianCalendar.YEAR));
				
				// close window after clicking ok in alert box
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			
		});
		add(done);
	}
	
	public void fillTable(final int year, final int month) {

		// reset table
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(null, 1, i);
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(null, 2, i);
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(null, 3, i);
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(null, 4, i);
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(null, 5, i);
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(null, 6, i);
		}
		
		/*
		 * initialise a new GregorianCalendar object with parameters
		 * these parameters specify the modified / selected year, month and first day
		 * same thing, you need to do this so the program will not reuse the old table model
		 */
		gc = new GregorianCalendar(year, month, 1);
		firstDayOfMonth = gc.get(GregorianCalendar.DAY_OF_WEEK);
		totalDays = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		actualDays = 1;
		
		if (true) {
			System.out.println("==========================");
			System.out.println("Year: " + year);
			System.out.println("Month(Not based on index): " + (month + 1));
			System.out.println("First day of month: " + firstDayOfMonth);
			System.out.println("Total days: " + totalDays);
		}
		
		// fill table again
		for (int i = firstDayOfMonth - 1; i < 7; i++) {
			dtm.setValueAt(actualDays, 1, i);
			actualDays++;
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(actualDays, 2, i);
			actualDays++;
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(actualDays, 3, i);
			actualDays++;
		}
		
		for (int i = 0; i < 7; i++) {
			dtm.setValueAt(actualDays, 4, i);
			actualDays++;
		}
		
		for (int i = 0; i < 7; i++) {
			if (actualDays > totalDays) {
				break;
			}
			
			dtm.setValueAt(actualDays, 5, i);
			actualDays++;
		}
		
		for (int i = 0; i < 7; i++) {
			if (actualDays > totalDays) {
				break;
			}
			
			dtm.setValueAt(actualDays, 6, i);
			actualDays++;
		}
		
		// set up gui table
		calendarTable = new JTable(dtm);
		calendarTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				JTable temp = (JTable) e.getSource();
				int row = temp.getSelectedRow();
				int column = temp.getSelectedColumn();
				tableValue = (int) temp.getValueAt(row, column);
				
				dateString = String.valueOf(tableValue);
				
				if (row >= 1) {
					System.out.println(tableValue + "-" + (gc.get(GregorianCalendar.MONTH) + 1) + "-" + gc.get(GregorianCalendar.YEAR));
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		calendarTable.setRowHeight(40);
		calendarTable.setBounds(20, 100, 500, 280);
		calendarTable.setForeground(Color.WHITE);
		calendarTable.setBackground(Color.BLACK);
		calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		calendarTable.setCellSelectionEnabled(true);
		add(calendarTable);
	}
	
	public static void main(String[] args) {
		frame = new JFrame();
		
		JPanel calendar = new Calendar();
		
		frame.setContentPane(calendar);
		frame.setVisible(true);
		frame.setBounds(10, 10, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
