package com.dream.mobile.server;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;

import com.dream.mobile.rmi.interfaces.DbFunctions;
import com.dream.mobile.rmi.interfaces.RemoteInterface; 

public class Server extends DbFunctions { 
	/**
	 * @author Kasun Thilina
	 * The Server.java with a custom url and port so that the LAN device can detect the server 
	 */
   public Server() {} 
   public static void main(String args[]) { 
	   try { 
	         // Instantiating the implementation class 
	         DbFunctions dbObject = new DbFunctions(); 
	    
	         // Exporting the object of implementation class (here we are exporting the remote object to the stub) 
	         RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(dbObject, 0);  
	         //the port and hostname for the server
	         int port = 1091;
	         String hostname = "0.0.0.0";

	         String bindURL = "//" + hostname + ":" + port + "/Server";
	         
	         // Binding the remote object (stub) in the registry 
	         Registry registry = LocateRegistry.createRegistry(port); 
	         
	         //To bind with custom URL
	         Naming.bind(bindURL, dbObject);
	         System.err.println("Server ready URL"+bindURL); 
	      } catch (Exception e) { 
	         System.err.println("Server exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
   }
}


/*
 * try { 
         // Instantiating the implementation class 
    	  DbFunctions dbFunctions = new DbFunctions(); 
    
         // Exporting the object of implementation class (here we are exporting the remote object to the stub) 
         RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(dbFunctions, 0);  
         int port = 1091;
         String hostname = "0.0.0.0";

         String bindLocation = "//" + hostname + ":" + port + "/Hello";
         //Naming.bind(bindLocation, obj);
         // Binding the remote object (stub) in the registry 
         Registry registry = LocateRegistry.createRegistry(60); 
         
         registry.bind("RemoteInterface", stub);  
         System.err.println("Server ready"); 
      } catch (Exception e) { 
         System.err.println("Server exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
 * 
 */