/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.social.interfaces;

import javax.ejb.EJBException;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface SocialSBLocal {
    
    void post(String nombreUsuario,String message) throws EJBException;
    String connect(String nombreUsuario);
    String disconnect(String nombreUsuario);    
    void ingresarPin(String nombreUsuario, String pin) throws EJBException;
    
}
