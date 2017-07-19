/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scankdcdiscover;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import madura.kdc.KDCDiscoverer;

/**
 *
 * @author lgerard
 */
public class InterfaceMain extends JFrame implements ActionListener{
	
	private JTextArea logs;
	private JTextArea kdc;
	private JButton go;
	private ScanKDCDiscover main;
	
	public InterfaceMain(ScanKDCDiscover in_main){
		super();
		
		main = in_main;
		
		setSize(840,350);
		setTitle("Détecteur de matériels KDC");
		
		WindowAdapter winCloser = new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
			System.exit(0);
			}
		};
		addWindowListener(winCloser);
	
		Container contenu = getContentPane();
		contenu.setLayout(null);
		
		//Logs d'activité
		logs = new JTextArea();
		logs.setVisible(true);
		logs.setText("Initialisation...");

		JScrollPane ScrollBar = new javax.swing.JScrollPane();
		ScrollBar.setPreferredSize(new Dimension(200, 200));
		ScrollBar.setViewportView(logs);
		ScrollBar.setVisible(true);
		ScrollBar.setBounds(10, 10, 400, 300);
		contenu.add(ScrollBar);
		logs.setText("");
		
		//Liste KDC
		kdc = new JTextArea();
		kdc.setVisible(true);
		kdc.setText("");
		kdc.setBackground(null);

		kdc.setBounds(420, 10, 400, 240);
		contenu.add(kdc);
		kdc.setText("Aucune détection en cours...");
		
		go = new JButton("Détecter les KDC");
        go.setBounds(420, 260, 400, 50);
        go.addActionListener(this);
		contenu.add(go);
		
		setEnabled(true);
		setVisible(true);
		this.toFront();
		this.setResizable(false);

		//this.pack();
		this.setDefaultLookAndFeelDecorated(true);
		//this.setExtendedState(this.MAXIMIZED_BOTH);
	}
	
	public void actionPerformed(ActionEvent e) {
        go.setEnabled(false);
        this.repaint();
        
        if(e.getSource().equals(go)){
			main.startDiscover();
			//main.eventOnEnded();
        }
	
    }
	
	public void setKdcText(String texte){
		kdc.setText(texte);
		this.repaint();
	}
	
	public void writeInLog(String log){
		logs.setText(log+"\n"+logs.getText());
		this.repaint();
    }
	
	public void cleanLog(){
		logs.setText("");
	}
	
	public void reactive(){
		go.setEnabled(true);
		this.repaint();
	}
}
