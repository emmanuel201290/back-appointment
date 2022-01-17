/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.service;

import clinica.entities.Disponibilidad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Usuario
 */
@Stateless
@Path("clinica.disponibilidad")
public class DisponibilidadFacadeREST extends AbstractFacade<Disponibilidad> {

    @PersistenceContext(unitName = "ClinicaBackPU")
    private EntityManager em;

    public DisponibilidadFacadeREST() {
        super(Disponibilidad.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Disponibilidad entity) {
        
        entity.setHoraInicio((Integer.parseInt(entity.getHoraInicio().substring(0, 2))<12) ? entity.getHoraInicio()+"AM" : entity.getHoraInicio()+"PM");
        entity.setHoraFin((Integer.parseInt(entity.getHoraFin().substring(0, 2))<12) ? entity.getHoraFin()+"AM" : entity.getHoraFin()+"PM");
        
        System.out.println("hola");
        super.create(entity);
    }
    

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Disponibilidad entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Disponibilidad find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    @GET
    @Path("disponibilidadView/{id}")
    @Produces({"application/json"})
    public List<Disponibilidad> findAllCurrent(@PathParam("id") String id) {
         
        Query q = em.createNativeQuery("SELECT * FROM disponibilidad u where u.id_usuario='"+id+"'", Disponibilidad.class);
         List<Disponibilidad> bItems =  q.getResultList();
       
        return bItems;
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Disponibilidad> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Disponibilidad> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("validate")
    @Produces({MediaType.APPLICATION_JSON})
    public boolean validate(Disponibilidad entity) {
        System.out.println("-->"+entity.getDia());
        return true;
        
    }
}
