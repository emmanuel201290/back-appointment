/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.service;

import clinica.entities.Disponibilidad;
import clinica.entities.Usuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author em628
 */

@Path("init")
public class Init {
    
    Encrypt encryp=new Encrypt();
    
    @EJB
    private UsuarioFacadeREST ufr;   
    
    @EJB
    private DisponibilidadFacadeREST dfr;
 
    @GET
    @Produces
    @Consumes({MediaType.APPLICATION_JSON})
    public void init() throws JSONException{
        BufferedReader reader;
        String line;
        StringBuffer response = new StringBuffer();
       
        try {
            URL url = new URL("https://api.jsonbin.io/b/61b3b51e62ed886f915dd68a");
           
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
 
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
         
           int status = connection.getResponseCode();
         
           if(status>299){
               reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
               while((line=reader.readLine())!=null){
                   response.append(line);
               }
               reader.close();
           }else
           {
               reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               
               while((line=reader.readLine())!=null){
                   response.append(line);
                }
              
               JSONArray jr = new JSONArray(response.toString());
                
               for(int i=0;i<jr.length();i++){
                //Obtener todo los usuarios registrados
                 int indice = countUser();
                   JSONObject jo = jr.getJSONObject(i);
                   if (jo.has("nombre")) {
                   {
                    String name = jo.getString("nombre");
                   
                    //Llenando Tabla de usuarios
                    insertUser(name);
                   
                    //Llenando tabla disponibilidad
                    JSONArray totalDisp = jo.getJSONArray("disponibilidad");
                   
                     for(int j=0; j<totalDisp.length() ; j++){
                        String dia = jo.getJSONArray("disponibilidad").getJSONObject(j).getString("dia");
                        String hStart = jo.getJSONArray("disponibilidad").getJSONObject(j).getString("horaInicio");
                        String hEnd = jo.getJSONArray("disponibilidad").getJSONObject(j).getString("horaFin");
	                insertDisp(dia, hStart, hEnd, indice);
                      }
                    }  
                  }
               }
               reader.close();
           }
          
        } catch (MalformedURLException ex) {
            Logger.getLogger(DisponibilidadFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DisponibilidadFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertUser(String name){
         String passEncryp = encryp.Encrtype("temporal123");
        
         Usuario usr = new Usuario();
         usr.setNombre(name);
         usr.setPassword(passEncryp);
         usr.setRol("medico");
         ufr.createAutomatical(usr);
    }
    
    public void insertDisp(String day, String hStart, String hEnd, int key ){
        Disponibilidad d = new Disponibilidad();
        d.setIdDisponibilidad(0);
        d.setIdUsuario(key+1);
        d.setDia(day);
        d.setHoraInicio(hStart);
        d.setHoraFin(hEnd);
        
        dfr.createAutomatical(d);
    }
    
     public int countUser(){
        ResultSet result;
        return ufr.count();
    }
}