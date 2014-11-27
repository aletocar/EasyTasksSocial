/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.social.implementations;

import com.easytasks.social.interfaces.SocialSBLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author alejandrotocar
 */
@Stateful
public class TwitterSB implements SocialSBLocal {

    private Twitter twitter;
    private RequestToken requestToken;

    @Override
    public void post(String nombreUsuario, String message) {
        try {
            String nombreArchivo = "twitter_" + nombreUsuario + ".properties";
            File file = new File(nombreArchivo);
            Properties prop = new Properties();
            InputStream is = null;
            OutputStream os = null;

            try {
                if (file.exists()) {
                    is = new FileInputStream(file);
                    prop.load(is);
                } else {
                    throw new EJBException("No existe una configuración de Twitter para " + nombreUsuario);
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ignore) {
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ignore) {
                    }
                }
            }
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey("KCmBWRSF6uY5UvzKyyg8JyCJc")
                    .setOAuthConsumerSecret("z63CL07tVzQBAZIjNMJFaiQuUKn9FxJPz6V6VQ6esRVrTDorBX")
                    .setOAuthAccessToken(prop.getProperty("oauth.accessToken"))
                    .setOAuthAccessTokenSecret(prop.getProperty("oauth.accessTokenSecret"));
            twitter = new TwitterFactory(cb.build()).getInstance();
            twitter.updateStatus(message);
        } catch (TwitterException e) {
            throw new EJBException("Error en el twitter sb al twittear", e);
        }
    }

    @Override
    public String connect(String nombreUsuario) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("KCmBWRSF6uY5UvzKyyg8JyCJc").setOAuthConsumerSecret("z63CL07tVzQBAZIjNMJFaiQuUKn9FxJPz6V6VQ6esRVrTDorBX");
        twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            requestToken = twitter.getOAuthRequestToken();
        } catch (TwitterException ex) {
            return "No se pudo conectar con twitter";
        }
        return requestToken.getAuthorizationURL();
    }

    @Override
    public String disconnect(String nombreUsuario) {
        String nombreArchivo = "twitter_" + nombreUsuario + ".properties";
        File file = new File(nombreArchivo);
        if (file.exists()) {
            file.delete();
        }
        assert (!file.exists());//Se asegura que se haya borrado el archivo
        return "Se han borrado los datos de su cuenta. Por favor, elimine los permisos otorgados desde su configuración";
    }

    @Override
    public void ingresarPin(String nombreUsuario, String pin) throws EJBException {
        String nombreArchivo = "twitter_" + nombreUsuario + ".properties";
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
            twitter.setOAuthAccessToken(accessToken);
            File file = new File(nombreArchivo);
            Properties prop = new Properties();
            InputStream is = null;
            OutputStream os = null;

            prop.setProperty("oauth.consumerKey", "KCmBWRSF6uY5UvzKyyg8JyCJc");
            prop.setProperty("oauth.consumerSecret", "z63CL07tVzQBAZIjNMJFaiQuUKn9FxJPz6V6VQ6esRVrTDorBX");
            prop.setProperty("oauth.accessToken", accessToken.getToken());
            prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
            os = new FileOutputStream(file);
            prop.store(os, nombreArchivo);
            os.close();
        } catch (TwitterException ex) {
            throw new EJBException("Ocurrió un problema al activar su cuenta. Por favor intente nuevamente", ex);
        } catch (FileNotFoundException ex) {
            throw new EJBException("Ocurrió un problema al guardar los datos de su cuenta. Por favor intente nuevamente.", ex);
        } catch (IOException ex) {
            throw new EJBException("Ocurrió un problema al guardar los datos de su cuenta. Por favor intente nuevamente.", ex);
        }
    }
}
