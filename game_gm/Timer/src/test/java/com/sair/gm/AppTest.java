package com.sair.gm;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        int k=0;
        for(int i=0;i<105;i++){
            k++;
            if(k==50||i==104){
                System.out.println(i+" --A");
                k=0;
            }
        }
    }
}
