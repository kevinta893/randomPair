import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.DefaultListModel;
//import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;


/**
 * The utility that pairs a list of names, to itself.
 * The partnered names will not be paired to itself.
 * 
 * Use the enter key to input names in addition to the add button.
 * Use the delete key to delete names off of the list Partner A.
 * 
Copyright (c) 2014 Kevin Ta

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 * @author Kevin Ta
 *
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JMenuItem mntmExit;
	private JList<String> listA;
	private JList<String> listB;

	private JButton btnRandomize;
	private JButton btnMode;
	
	private DefaultListModel<String> nameA;
	private DefaultListModel<String> nameB;

	private Random rand = new MersenneTwister();

	private static final String NULL_NAME = "<<null>>";
	
	private static final Color BLUE = new Color (73,126,232);
	private static final Color ORANGE = new Color (232,179,73);
	
	private boolean partnerUp = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setPreferredSize(new Dimension(500, 500));
		setBounds(100, 100, 500, 500);

		Color themeColor = (partnerUp == true) ? ORANGE : BLUE;
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameA.clear();
				nameB.clear();
			}
		});
		mnFile.add(mntmClear);
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		this.listB = new JList<String>();
		nameB = new DefaultListModel<String>();
		listB.setModel(nameB);
		listB.setBorder(new BevelBorder(BevelBorder.LOWERED, themeColor, themeColor, themeColor, themeColor));
		listB.setBounds(278, 45, 196, 322);
		contentPane.add(listB);

		this.listA = new JList<String>();
		listA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() ==  KeyEvent.VK_DELETE){
					if (listA.getSelectedIndex() >= 0){
						nameA.remove(listA.getSelectedIndex());
					}
				}
			}
		});
		nameA = new DefaultListModel<String>();
		listA.setModel(nameA);
		listA.setBorder(new BevelBorder(BevelBorder.LOWERED, themeColor, themeColor, themeColor, themeColor));
		listA.setBounds(12, 45, 194, 322);
		contentPane.add(listA);

		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					addName(txtName.getText());
				}
			}
		});
		txtName.setBounds(10, 14, 128, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addName(txtName.getText());
			}
		});
		btnAdd.setBounds(148, 13, 65, 23);
		contentPane.add(btnAdd);

		this.btnRandomize = new JButton("Pair Up");
		btnRandomize.setBackground(themeColor);
		btnRandomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (partnerUp == true){
					partnerUp();
				}
				else{
					pairUp();
				}
				
			}
		});
		btnRandomize.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRandomize.setBounds(180, 398, 124, 32);
		btnRandomize.setContentAreaFilled(false);
		btnRandomize.setOpaque(true);	
		contentPane.add(btnRandomize);

		JLabel lblPartnerA = new JLabel("Partner A");
		lblPartnerA.setBounds(12, 373, 65, 14);
		contentPane.add(lblPartnerA);

		JLabel lblPartnerB = new JLabel("Partner B");
		lblPartnerB.setBounds(278, 373, 65, 14);
		contentPane.add(lblPartnerB);

		JButton btnClear = new JButton("Clear list");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameA.clear();
				nameB.clear();
			}
		});
		btnClear.setBounds(12, 405, 91, 23);
		contentPane.add(btnClear);
		
		btnMode = new JButton("->");
		btnMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//toggle the mode
				partnerUp = (partnerUp == true) ? false : true;
				
				
				
				Color themeColor = (partnerUp == true) ? ORANGE : BLUE;
				//update UI as appropriate
				if (partnerUp == true){
					themeColor = ORANGE;
					
					btnMode.setText("<->");
					btnRandomize.setText("Partner Up");
					btnRandomize.setBackground(themeColor);
					
				}
				else{
					btnMode.setText("->");
					btnRandomize.setText("Pair Up");
					btnRandomize.setBackground(themeColor);
					
				}
				
				listA.setBorder(new BevelBorder(BevelBorder.LOWERED, themeColor, themeColor, themeColor, themeColor));
				listB.setBorder(new BevelBorder(BevelBorder.LOWERED, themeColor, themeColor, themeColor, themeColor));
			}
		});
		btnMode.setBounds(216, 200, 52, 23);
		contentPane.add(btnMode);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About.showDialog();
			}
		});
		mnAbout.add(mntmAbout);

	}

	private void addName(String name){
		String entry = name.trim();

		//add a name only if the text is greater than 0 trimmed
		if (entry.length() > 0){

			boolean repeat = false;
			for (int i =0; i < nameA.getSize(); i++){
				if (nameA.get(i).equals(entry)){
					repeat = true;
				}
			}


			if (repeat == true){
				//repeated name
				JOptionPane.showMessageDialog(this, "This name is already on the list! Enter a different name.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else{
				//no repeat
				nameA.addElement(entry);
				txtName.setText(""); 					//clear text when added
			}
		}
	}

	
	
	private static final int RETRY_MAX = 5000000;
	/**
	 * Pairs up the list of names in A randomly with other members 
	 * in list A.
	 * Output is a list of names in B corresponding to the pairs on
	 * A, where there are no reflexive relations. 
	 * 
	 * Maps A -> B only.
	 */
	private void pairUp(){

		if (nameA.size() <= 1){
			JOptionPane.showMessageDialog(this, "Enter at least two items to start pair up.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			//clear the old list
			nameB.clear();


			String[] names = new String[nameA.size()];
			nameA.copyInto(names);

			//convert array to collections
			ArrayList<String> raw = new ArrayList<String>(names.length);
			for (String s : names){
				raw.add(s);
			}

			//shuffle, ensure that pairing is not the same.
			int retryCount = 0;
			boolean validShuffle = false;
			while (validShuffle == false){

				Collections.shuffle(raw, rand);
				
				//check for a valid shuffle
				validShuffle = true;
				for (int i =0; i < names.length ; i++){
					if (names[i].equals(raw.get(i))){
						//retry shuffle
						validShuffle = false;
					}
				}

				retryCount++;

				//stop when the retry count too high
				if (retryCount >= RETRY_MAX){
					int response = JOptionPane.showConfirmDialog(this, "Shuffler has tried " + RETRY_MAX + " times to pair the list but has had no success. It is likely that there is no solution for a small set of data. \n\nAbort?", "Application Deadlock", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (response == JOptionPane.YES_OPTION){
						break;
					}
					retryCount =0;
				}
			}


			//valid shuffle, display on screen
			for(String s : raw){
				nameB.addElement(s);
			}
		}



	}
	
	
	/**
	 * Pairs up every member to exactly one partner.
	 * The difference here is that this function maps A -> B and B -> A.
	 * If there is not enough names to partner up (ie, a list
	 * with an odd number of names) then that partner is given
	 * a null partner.
	 * 
	 * No reflexive partners.
	 */
	private void partnerUp(){
		if (nameA.size() <= 1){
			JOptionPane.showMessageDialog(this, "Enter at least two items to start pair up.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			//clear the old list
			nameB.clear();
			

			String[] names = new String[nameA.size()];
			nameA.copyInto(names);

			//convert array to collections
			LinkedList<String> raw = new LinkedList<String>();
			for (String s : names){
				raw.add(s);
			}

			if ((raw.size() % 2) == 1){
				//list size is odd, one of the pairs will not have a partner
				raw.add(NULL_NAME);
			}
			
			
			//shuffle, ensure that pairing is not the same.
			int retryCount = 0;
			while (raw.size() > 0){

				
				int first = rand.nextInt(raw.size());
				int second = rand.nextInt(raw.size());
				while(first == second){
					second = rand.nextInt(raw.size());
					
					retryCount++;
					
					//stop when the retry count too high
					if (retryCount >= RETRY_MAX){
						int response = JOptionPane.showConfirmDialog(this, "Shuffler has tried " + RETRY_MAX + " times to pair the list but has had no success. It is likely that there is no solution for a small set of data. \n\nAbort?", "Application Deadlock", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (response == JOptionPane.YES_OPTION){
							break;
						}
						retryCount =0;
					}
				}
				
				String strFirst = raw.get(first);
				String strSecond = raw.get(second);
				
				raw.remove(strFirst);
				raw.remove(strSecond);
				
				nameB.addElement("(" + strFirst + ", " + strSecond + ")");
			}


			
		}

	}
}
