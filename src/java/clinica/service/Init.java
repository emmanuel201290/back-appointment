/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    int port=3306;
    String user="root";
    String password="";
    private String cadConnection="jdbc:mysql://localhost:3306/clinica";
    
    Encrypt encryp=new Encrypt();
    
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
               
               //Obtener todo los usuarios registrados
               int indice = countUser();
                
               for(int i=1;i<=jr.length();i++){
                   
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
                        indice++;
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
        
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/clinica",user,password); 
            PreparedStatement stmt=con.prepareStatement("insert into usuario values(?,?,?,?)");  
            stmt.setInt(1,0);
            stmt.setString(2,name);
            stmt.setString(3, passEncryp);
            stmt.setString(4, "medico");
  
            int i=stmt.executeUpdate(); 
            
            con.close();  
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertDisp(String day, String hStart, String hEnd, int key ){
         try {  
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/clinica",user,password); 
            PreparedStatement stmt=con.prepareStatement("insert into disponibilidad values(?,?,?,?,?)");  
            stmt.setInt(1,0);
            stmt.setInt(2,key+1);
            stmt.setString(3,day);
            stmt.setString(4, hStart);
            stmt.setString(5, hEnd);
  
            int i=stmt.executeUpdate();  
            con.close();  
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public int countUser(){
        ResultSet result;
        int count=1;
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(cadConnection,user,password); 
            PreparedStatement stmt=con.prepareStatement("select * from usuario ");  
            ResultSet data = stmt.executeQuery();
            
            if (data.last()) 
            {
                count= data.getRow();
            }
              
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }
}


