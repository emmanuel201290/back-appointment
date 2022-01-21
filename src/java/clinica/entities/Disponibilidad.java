/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinica.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "disponibilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disponibilidad.findAll", query = "SELECT d FROM Disponibilidad d")
    , @NamedQuery(name = "Disponibilidad.findByIdDisponibilidad", query = "SELECT d FROM Disponibilidad d WHERE d.idDisponibilidad = :idDisponibilidad")
    , @NamedQuery(name = "Disponibilidad.findByIdUsuario", query = "SELECT d FROM Disponibilidad d WHERE d.idUsuario = :idUsuario")
    , @NamedQuery(name = "Disponibilidad.findByIdUsuarioAndDia", query = "SELECT d FROM Disponibilidad d WHERE d.idUsuario = :idUsuario AND d.dia = :dia")    
    , @NamedQuery(name = "Disponibilidad.findByDia", query = "SELECT d FROM Disponibilidad d WHERE d.dia = :dia")
    , @NamedQuery(name = "Disponibilidad.findByHoraInicio", query = "SELECT d FROM Disponibilidad d WHERE d.horaInicio = :horaInicio")
    , @NamedQuery(name = "Disponibilidad.findByHoraFin", query = "SELECT d FROM Disponibilidad d WHERE d.horaFin = :horaFin")})
public class Disponibilidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_disponibilidad")
    private Integer idDisponibilidad;
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Size(max = 10)
    @Column(name = "dia")
    private String dia;
    @Size(max = 10)
    @Column(name = "horaInicio")
    private String horaInicio;
    @Size(max = 10)
    @Column(name = "horaFin")
    private String horaFin;

    public Disponibilidad() {
    }

    public Disponibilidad(Integer idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }

    public Integer getIdDisponibilidad() {
        return idDisponibilidad;
    }

    public void setIdDisponibilidad(Integer idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDisponibilidad != null ? idDisponibilidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Disponibilidad)) {
            return false;
        }
        Disponibilidad other = (Disponibilidad) object;
        if ((this.idDisponibilidad == null && other.idDisponibilidad != null) || (this.idDisponibilidad != null && !this.idDisponibilidad.equals(other.idDisponibilidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clinica.entities.Disponibilidad[ idDisponibilidad=" + idDisponibilidad + " ]";
    }
    
}
