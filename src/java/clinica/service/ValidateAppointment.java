/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.service;

import clinica.entities.Citas;
import clinicaEnum.EnumDay;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author em628
 */
@Path("validate")
public class ValidateAppointment {
    private EntityManager em;
    private String cadConnection="jdbc:mysql://localhost:3306/clinica";
    private String user="root";
    private String pass = "";
    
    String hInicio= "";
    String hFin = "";
    String idUserActivo="";
    String idMedico="";
    
    @POST
    @Produces
    @Consumes({MediaType.APPLICATION_JSON})
    public String ValidateAppointment(Citas citas){
        Enum diaEnum = null;
        String msj = null;
        
       //variables citas
        idUserActivo=citas.getIdUsuario().toString();
        int idUsuarioMed=Integer.parseInt(citas.getIdMedico().toString());
        String fechaVisita = citas.getFechaVisita();
        hInicio = (Integer.parseInt(citas.getHoraInicio().substring(0, 2))<12) ? citas.getHoraInicio()+"AM" : citas.getHoraInicio()+"PM";
        hFin = (Integer.parseInt(citas.getHoraFin().substring(0, 2))<12) ? citas.getHoraFin()+"AM" : citas.getHoraFin()+"PM";
        String nombrePaciente = citas.getNombrePaciente();
        idMedico = citas.getIdMedico().toString();
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dateFechaVisita = isoFormat.parse(fechaVisita);
            
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
            cal.setTime(dateFechaVisita);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            int year = cal.get(Calendar.YEAR);
       
            switch(day)
            {
             case 1 :
             diaEnum=EnumDay.Domingo;
             break;
   
             case 2 :
             diaEnum=EnumDay.Lunes;
             break;
             
             case 3 :
             diaEnum=EnumDay.Martes;
             break;
             
             case 4 :
             diaEnum=EnumDay.Miercoles;
             break;
                 
             case 5 :
             diaEnum=EnumDay.Jueves;
             break;
             
             case 6 :
             diaEnum=EnumDay.Viernes;
             break;
                 
             case 7 :
             diaEnum=EnumDay.Sabado;
             break;
            }
            
            //Verificando dias disponible
           msj=readDisp(idUsuarioMed,diaEnum.toString());
           if("ok".equals(msj)){
              insertCita(idUsuarioMed,idUserActivo,dateFechaVisita,hInicio,hFin,
                      diaEnum.toString(),nombrePaciente);
           }
            
        } catch (ParseException ex) {
            Logger.getLogger(ValidateAppointment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msj;
         
   }
    
    public String readDisp(int id_disponibilidad, String dia){
        ResultSet result;
        String msj="";
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(cadConnection,user,pass); 
            PreparedStatement stmt=con.prepareStatement("select * from disponibilidad where id_usuario=? and dia=?");  
            stmt.setInt(1,id_disponibilidad);
            stmt.setString(2,dia);
            ResultSet data = stmt.executeQuery();
            if(data.next()) { 
              
             //Validar que la hora este en rango disponible
              int hInicioParseBd = Integer.parseInt(data.getString("horaInicio").split(":")[0]);
              int hFinParseBd = Integer.parseInt(data.getString("horaFin").split(":")[0]);
                     
              if(Integer.parseInt(hInicio.split(":")[0])<  convertHour(hInicioParseBd,data.getString("horaInicio")) || 
                 Integer.parseInt(hFin.split(":")[0])>  convertHour(hFinParseBd,data.getString("horaFin"))  ){
                  msj = "Horas fuera de rango ["+data.getString("horaInicio")+"] - ["+data.getString("horaFin")+"]";
                  return msj;
              }
              String citasReg = validCitaReg(dia);
              if(citasReg!=null){
                  return citasReg;
              }
             
                /*String medicoDisp = countCitasByMed();
                if(medicoDisp!=null){
                    return medicoDisp;
                }*/
                
              msj= "ok";
            }else{
              msj = "medico no disponible";
            }
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
       return msj;
    }
    
    public void insertCita(int idMedico, String idUsuarioActive,Date dateFechaVisita,
                           String hInicio, String hFin, String diaEnum, String nombrePaciente){
        
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
           LocalDateTime now = LocalDateTime.now();
           String current = dtf.format(now);
        
           try {  
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(cadConnection,user,pass); 
            PreparedStatement stmt=con.prepareStatement("insert into citas values(?,?,?,?,?,?,?,?,?)");  
            stmt.setInt(1,0);
            stmt.setInt(2,idMedico);
            stmt.setString(3,idUsuarioActive);
            stmt.setString(4, ""+dateFechaVisita);
            stmt.setString(5, hInicio);
            stmt.setString(6, hFin);
            stmt.setString(7, diaEnum);
            stmt.setString(8, current);
            stmt.setString(9, nombrePaciente);
  
            int i=stmt.executeUpdate();  
            con.close();  
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
  
    public String validCitaReg(String dia){
         //Validar si el usuario ya se encuentra registrado el mismo dia
        // con mismo medico
         ResultSet result;
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(cadConnection,user,pass); 
            PreparedStatement stmt=con.prepareStatement("select * from citas where id_medico=? and id_usuario=?");  
            stmt.setInt(1,Integer.parseInt(idMedico));
            stmt.setString(2,idUserActivo);
            ResultSet data = stmt.executeQuery();
        
            if(data.getRow()!= 0) { 
             return "Tiene una cita registrada con el medico seleccionado";
            }
              
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
     public String countCitasByMed(){
        ResultSet result;
        int count=0;
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(cadConnection,user,pass); 
            PreparedStatement stmt=con.prepareStatement("select * from citas where id_medico=?");  
            stmt.setInt(1,Integer.parseInt(idMedico));
            ResultSet data = stmt.executeQuery();
            if(data.next()) { 
              count++;
            }
            if(count>=5){
                return "Este medico no puede aceptar mas citas";
            }
              
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    
    public int convertHour(int h, String completeHour){
        int horaPm=12;
        String flgPm=completeHour.substring(completeHour.length()-2, completeHour.length());
        if("PM".equals(flgPm) && h<12){
           for(int i=1; i<=h;i++){
               horaPm++;
           }
           return horaPm;
       }
       return h;
    }
}
