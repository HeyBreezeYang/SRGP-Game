package com.gmdesign.rmi.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by DJL on 2017/3/7.
 *
 * @ClassName GmRmiOperate
 * @Description
 */
public class GmRmiOperate{

    public static <T> T getGameRmiServer(String rmi){
        try {
            return (T) Naming.lookup(rmi);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
