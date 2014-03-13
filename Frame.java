import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.RenderingHints.Key;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;

import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * The utility that pairs a list of names, to itself.
 * The partnered names will not be paired to itself.
 * 
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
 * @author Kevin
 *
 */
public class Frame extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JMenuItem mntmExit;
	private JList<String> listA;
	private JList<String> listB;

	private DefaultListModel<String> nameA;
	private DefaultListModel<String> nameB;

	private Random rand = new MersenneTwister();
	
	
	private static final int RETRY_MAX = 5000000;
	
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
		setTitle("Pair Up");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);

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
		listB.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listB.setBounds(223, 45, 201, 322);
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
		listA.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listA.setBounds(12, 45, 201, 322);
		contentPane.add(listA);

		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					//add a name only if the text is greater than 0 trimmed
					if (txtName.getText().trim().length()>0){
						nameA.addElement(txtName.getText());
						txtName.setText(""); 					//clear text when added
					}
				}
			}
		});
		txtName.setBounds(10, 14, 128, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//add a name only if the text is greater than 0 trimmed
				if (txtName.getText().trim().length()>0){
					nameA.addElement((nameA.size() + 1) + ". " + txtName.getText());
					txtName.setText(""); 					//clear text when added
				}
			}
		});
		btnAdd.setBounds(148, 13, 65, 23);
		contentPane.add(btnAdd);

		JButton btnRandomize = new JButton("Pair Up");
		btnRandomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pairUp();
			}
		});
		btnRandomize.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRandomize.setBounds(155, 398, 124, 32);
		contentPane.add(btnRandomize);

		JLabel lblPartnerA = new JLabel("Partner A");
		lblPartnerA.setBounds(12, 373, 65, 14);
		contentPane.add(lblPartnerA);

		JLabel lblPartnerB = new JLabel("Partner B");
		lblPartnerB.setBounds(223, 373, 65, 14);
		contentPane.add(lblPartnerB);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameA.clear();
				nameB.clear();
			}
		});
		btnClear.setBounds(12, 405, 65, 23);
		contentPane.add(btnClear);
	}

	/**
	 * Pairs up the list of names in A randomly with other members 
	 * in list A.
	 * Output is a list of names in B corresponding to the pairs on
	 * A, where there are no reflexive relations.
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
				validShuffle = true;
				for (int i =0; i < names.length ; i++){
					if (names[i].equals(raw.get(i))){
						//retry shuffle
						validShuffle = false;
					}
				}
				
				retryCount++;
				
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
}
