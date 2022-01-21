/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.service;

import clinica.entities.Citas;
import clinica.entities.Disponibilidad;
import clinicaEnum.EnumDay;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
    @EJB
    private CitasFacadeREST cfr;
    @EJB
    private DisponibilidadFacadeREST dfr;
    
    Citas c = new Citas();
    
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
    
    public String readDisp(int id_UsuarioMed, String dia){
        ResultSet result;
        String msj="";
        List<Disponibilidad> lst = dfr.findByIdUsuarioAndDia(id_UsuarioMed, dia);
        
        
        if(!lst.isEmpty()){
            int hInicioParseBd = Integer.parseInt(lst.get(0).getHoraInicio().split(":")[0]);
            int hFinParseBd = Integer.parseInt(lst.get(0).getHoraFin().split(":")[0]);
           
            if(Integer.parseInt(hInicio.split(":")[0])<  convertHour(hInicioParseBd,lst.get(0).getHoraInicio()) || 
                Integer.parseInt(hFin.split(":")[0])>  convertHour(hFinParseBd,lst.get(0).getHoraFin())){
                msj = "Horas fuera de rango ["+lst.get(0).getHoraInicio()+"] - ["+lst.get(0).getHoraFin()+"]";
                return msj;
            }
            
             String medicoDispPorPaciente = validCitaReg();
               if(medicoDispPorPaciente!=null){
                   return medicoDispPorPaciente;
               }
             
             String medicoDisp = countCitasByMed();
              if(medicoDisp!=null){
                    return medicoDisp;
                }
             
            msj="ok";

        }else{
            msj = "medico no disponible";
        }
       return msj;
    }
    
    public void insertCita(int idMedico, String idUsuarioActive,Date dateFechaVisita,
                           String hInicio, String hFin, String diaEnum, String nombrePaciente){
        
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
           LocalDateTime now = LocalDateTime.now();
           String current = dtf.format(now);
           
           c.setId(0);
           c.setIdMedico(idMedico);
           c.setIdUsuario(Integer.parseInt(idUsuarioActive));
           c.setFechaVisita(""+dateFechaVisita);
           c.setHoraInicio(hInicio);
           c.setHoraFin(hFin);
           c.setDia(diaEnum);
           c.setFechaRegistro(current);
           c.setNombrePaciente(nombrePaciente);
           cfr.create(c);
    }
  
    public String validCitaReg(){
         //Validar si el usuario ya se encuentra registrado el mismo dia
        // con mismo medico
         return cfr.findByMedAndUser(idMedico, idUserActivo);
    }
     
    public String countCitasByMed(){
        return cfr.findByIdMed(idMedico);
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
