/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.service;

import clinica.entities.Usuario;
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
@Path("clinica.usuario")
public class UsuarioFacadeREST extends AbstractFacade<Usuario> {
    private String secretKey = "desarrollwebServiceclinicaNicasource";

    @PersistenceContext(unitName = "ClinicaBackPU")
    private EntityManager em;

    public UsuarioFacadeREST() {
        super(Usuario.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Usuario entity) {
        Encrypt encryp = new Encrypt();
        String nombre = entity.getNombre();
        String password = entity.getPassword();
        
        String passEncrypt = encryp.Encrtype(password);
        entity.setPassword(passEncrypt);
        Query q = em.createNamedQuery("Usuario.findByNombre", Usuario.class);
        q.setParameter("nombre", nombre);
        List<Usuario> bItems = q.getResultList();
       if(bItems.isEmpty()){
          super.create(entity);
        }else{
            System.out.println("Usuario ya existe");
        }
     }
    
    public void createAutomatical(Usuario entity){
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Usuario entity) {
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
    public Usuario find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("finAllByRol/{rol}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Usuario> findAllByRol(@PathParam("rol") String rol) {
        Query query = em.createNamedQuery("Usuario.findByRol");
        query.setParameter("rol", "medico");
        return query.getResultList();
    }

    @POST
    @Path("login")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> login(Usuario entity){
        String nombre = entity.getNombre();
        String password = entity.getPassword();
       
        Encrypt encryp = new Encrypt();
        String passCif = encryp.Encrtype(password);
        
        Query q = em.createNamedQuery("Usuario.findByUserAndPass",Usuario.class);
        q.setParameter("nombre", nombre);
        q.setParameter("password", passCif);
       
         List<Usuario> bItems = q.getResultList();
         if (bItems == null || bItems.isEmpty()) {
          return  null;
         }
      return q.getResultList();
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
}
