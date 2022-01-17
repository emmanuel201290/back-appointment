/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.service;

import clinica.entities.Citas;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

/**
 *
 * @author em628
 */
@Stateless
@Path("clinica.entities.citas")
public class CitasFacadeREST extends AbstractFacade<Citas> {
    @PersistenceContext(unitName = "ClinicaBackPU")
    private EntityManager em;

    public CitasFacadeREST() {
        super(Citas.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Citas entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Citas entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Citas find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Citas> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("appointment/{id}")
    @Produces({"application/json"})
    public List<Citas> findAllCurrent(@PathParam("id") String id) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String current = dtf.format(now);
    
        Query q = em.createNativeQuery("SELECT * FROM citas u where u.id_medico='"+id+"' and u.fecha_registro='"+current+"'", Citas.class);
        List<Citas> bItems =  q.getResultList();
        return bItems;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Citas> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
