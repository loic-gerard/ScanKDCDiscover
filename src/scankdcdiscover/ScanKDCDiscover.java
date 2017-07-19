/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scankdcdiscover;

import java.util.Vector;
import madura.kdc.KDCDiscoverer;
import madura.debug.Log;
import madura.exec.CallbackTimer;
import madura.exec.Callback;
/**
 *
 * @author lgerard
 */
public class ScanKDCDiscover {

	private InterfaceMain interfaceMain;
	private Vector kdcs;
	private KDCDiscoverer kdcDiscoverer;
	private boolean ended = false;
	private static String version = "1.2.1";
	private CallbackTimer cb;
	
	public static void main(String[] args) {
		// TODO code application logic here
		
		//new ThreadProgram();
		new ScanKDCDiscover();
	}
	
	public ScanKDCDiscover(){
		interfaceMain = new InterfaceMain(this);
		interfaceMain.writeInLog("ScanKDCDiscover v"+version);
		Log.addOnLogCallBack(this, "eventOnLog");
	}
	
	public void startDiscover(){
		interfaceMain.cleanLog();
		Log.log("START DISCOVER");
		kdcs = new Vector();
		interfaceMain.setKdcText("Initialisation de la découverte...");
		kdcDiscoverer = new KDCDiscoverer();
		kdcDiscoverer.setOnPortDetectedCallBack(this, "eventOnPortDetected");
		kdcDiscoverer.setOnPortReservedCallBack(this, "eventOnPortReserved");
		kdcDiscoverer.setOnConnexionReadyCallBack(this, "eventOnConnexionReady");
		kdcDiscoverer.setOnDeviceErrorCallBack(this, "eventOnDeviceError");
		kdcDiscoverer.setOnDeviceReadyCallBack(this, "eventOnDeviceReady");
		kdcDiscoverer.setOnEndedCallBack(this, "eventOnEnded");
		kdcDiscoverer.start();
	}
	
	public void eventOnPortDetected(Object portName){
		String port = (String)portName;
		
		Port p = getPortFromName(port);
		if(p == null){
			p = new Port();
			p.name = port;
			p.etat = "en analyse...";
			kdcs.add(p);
		}else{
			p.etat = "en analyse...";
		}
		
		updateView();
	}
	
	public void eventOnPortReserved(Object portName){
		String port = (String)portName;
		
		Port p = getPortFromName(port);
		if(p == null){
			p = new Port();
			p.name = port;
			p.etat = "déjà réservé";
			kdcs.add(p);
		}else{
			p.etat = "déjà réservé";
		}
		
		updateView();
	}
	
	public void eventOnConnexionReady(Object portName){
		String port = (String)portName;
		
		Port p = getPortFromName(port);
		if(p == null){
			p = new Port();
			p.name = port;
			p.etat = "test KDC en cours...";
			kdcs.add(p);
		}else{
			p.etat = "test KDC en cours..";
		}
		
		updateView();
	}
	
	public void eventOnDeviceError(Object portName, Object in_erreur){
		String port = (String)portName;
		String erreur = (String)in_erreur;
		
		Port p = getPortFromName(port);
		if(p == null){
			p = new Port();
			p.name = port;
			p.etat = "erreur : "+erreur;
			kdcs.add(p);
		}else{
			p.etat = "erreur : "+erreur;
		}
		
		updateView();
	}
	
	public void eventOnDeviceReady(Object portName){
		String port = (String)portName;
		
		Port p = getPortFromName(port);
		if(p == null){
			p = new Port();
			p.name = port;
			p.etat = "PRET !";
			kdcs.add(p);
		}else{
			p.etat = "PRET !";
		}
		
		updateView();
	}
	
	public void eventOnEnded(){
		Log.log("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::EVENT ON ENDED");
		ended = true;
		updateView();
		interfaceMain.reactive();
		ended = false;
		//cb = new CallbackTimer(5000, new Callback(this, "eventOnEndedPassed"));
		//cb.start();
	}
	
	public void eventOnEndedPassed(){
		Log.log("PASSED");
		//kdcDiscoverer.closeAllOpenedDevices();
		interfaceMain.reactive();
		ended = false;
	}
	
	private Port getPortFromName(String portName){
		for(int i = 0; i < kdcs.size(); i++){
			Port p = (Port)kdcs.get(i);
			if(p.name.equals(portName)){
				return p;
			}
		}
		return null;
	}
	
	public void eventOnLog(Object in_message, Object in_classe){
		String message = (String) in_message;
		String classe = (String) in_classe;
		interfaceMain.writeInLog(message+" ("+classe+")");
	}
	
	public void updateView(){
		String texte = "";
		if(ended){
			texte = "Détection terminée ! \n";
		}else{
			texte = "Détection en cours... \n";
		}
		
		for(int i = 0; i < kdcs.size(); i++){
			Port p = (Port)kdcs.get(i);
			texte += p.name+" : "+p.etat+"\n";
		}
		interfaceMain.setKdcText(texte);
	}
	
}
