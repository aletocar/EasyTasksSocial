/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.social.implementations;

import com.easytasks.social.interfaces.SocialSBLocal;
import javax.ejb.EJBException;
import javax.ejb.Stateful;

/**
 *
 * @author alejandrotocar
 */
@Stateful
public class FacebookSB implements SocialSBLocal {

    @Override
    public void post(String nombreUsuario,String message) throws EJBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String connect(String nombreUsuario) {
        return "La concha de tu madre";
    }

    @Override
    public String disconnect(String nombreUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ingresarPin(String nombreUsuario,String pin) throws EJBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
