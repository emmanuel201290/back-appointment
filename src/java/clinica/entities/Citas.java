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
 * @author em628
 */
@Entity
@Table(name = "citas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Citas.findAll", query = "SELECT c FROM Citas c"),
    @NamedQuery(name = "Citas.findById", query = "SELECT c FROM Citas c WHERE c.id = :id"),
    @NamedQuery(name = "Citas.findByIdMedico", query = "SELECT c FROM Citas c WHERE c.idMedico = :idMedico"),
    @NamedQuery(name = "Citas.findByIdUsuario", query = "SELECT c FROM Citas c WHERE c.idUsuario = :idUsuario"),
    @NamedQuery(name = "Citas.findByFechaVisita", query = "SELECT c FROM Citas c WHERE c.fechaVisita = :fechaVisita"),
    @NamedQuery(name = "Citas.findByHoraInicio", query = "SELECT c FROM Citas c WHERE c.horaInicio = :horaInicio"),
    @NamedQuery(name = "Citas.findByHoraFin", query = "SELECT c FROM Citas c WHERE c.horaFin = :horaFin"),
    @NamedQuery(name = "Citas.findByDia", query = "SELECT c FROM Citas c WHERE c.dia = :dia"),
    @NamedQuery(name = "Citas.findByFechaRegistro", query = "SELECT c FROM Citas c WHERE c.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Citas.findByMedAndUser", query = "SELECT c FROM Citas c WHERE c.idMedico = :idMedico AND c.idUsuario = :idUsuario"),
    @NamedQuery(name = "Citas.findByIdMedAndDate", query = "SELECT c FROM Citas c WHERE c.idMedico = :idMedico and c.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Citas.findByNombrePaciente", query = "SELECT c FROM Citas c WHERE c.nombrePaciente = :nombrePaciente")})
public class Citas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_medico")
    private Integer idMedico;
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Size(max = 50)
    @Column(name = "fecha_visita")
    private String fechaVisita;
    @Size(max = 30)
    @Column(name = "hora_inicio")
    private String horaInicio;
    @Size(max = 30)
    @Column(name = "hora_fin")
    private String horaFin;
    @Size(max = 10)
    @Column(name = "dia")
    private String dia;
    @Size(max = 50)
    @Column(name = "fecha_registro")
    private String fechaRegistro;
    @Size(max = 50)
    @Column(name = "nombre_paciente")
    private String nombrePaciente;

    public Citas() {
    }

    public Citas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(String fechaVisita) {
        this.fechaVisita = fechaVisita;
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

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Citas)) {
            return false;
        }
        Citas other = (Citas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clinica.entities.Citas[ id=" + id + " ]";
    }
    
}
