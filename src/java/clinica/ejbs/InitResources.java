/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.ejbs;

import clinica.interfaces.IMethodInit;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author Usuario
 */

@Stateless
@LocalBean
public class InitResources implements IMethodInit{
    final static String URL_CONTEXT = "https://api.jsonbin.io/b/61b3b51e62ed886f915dd68a";

    @Override
    public void llenarUsuario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void llenarDisponibilidad() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
