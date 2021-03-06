package pe.com.tss.runakuna.domain.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import pe.com.tss.runakuna.domain.model.base.AuditingEntity;

@Entity
@Table(name = "Vacacion")
@NamedQuery(name = "Vacacion.findAll", query = "SELECT v FROM Vacacion v")
public class Vacacion extends AuditingEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long idVacacion;
	private PeriodoEmpleado periodoEmpleado;
	private Empleado atendidoPor;
	private Date fechaInicio;
	private Date fechaFin;
	private Integer diasCalendarios;
	private Integer diasHabiles;
	private String estado;
	private String comentarioResolucion;
	private Integer regularizacion;
	private Integer incluidoPlanilla;
	
	private String tipo;
	private Date fechaCompra;
	private Empleado compraAutorizadaPor;
	
	
	private String mesPlanilla;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVacacion")
	public Long getIdVacacion() {
		return idVacacion;
	}
	public void setIdVacacion(Long idVacacion) {
		this.idVacacion = idVacacion;
	}
	
	@ManyToOne()
    @JoinColumn(name = "IdPeriodoEmpleado")
	public PeriodoEmpleado getPeriodoEmpleado() {
		return periodoEmpleado;
	}
	public void setPeriodoEmpleado(PeriodoEmpleado periodoEmpleado) {
		this.periodoEmpleado = periodoEmpleado;
	}
	
	@ManyToOne()
    @JoinColumn(name = "IdAtendidoPor")
	public Empleado getAtendidoPor() {
		return atendidoPor;
	}
	public void setAtendidoPor(Empleado atendidoPor) {
		this.atendidoPor = atendidoPor;
	}
	
	@Column(name = "FechaInicio")
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	@Column(name = "FechaFin")
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	@Column(name = "DiasCalendarios")
	public Integer getDiasCalendarios() {
		return diasCalendarios;
	}
	public void setDiasCalendarios(Integer diasCalendarios) {
		this.diasCalendarios = diasCalendarios;
	}
	
	@Column(name = "DiasHabiles")
	public Integer getDiasHabiles() {
		return diasHabiles;
	}
	public void setDiasHabiles(Integer diasHabiles) {
		this.diasHabiles = diasHabiles;
	}
	
	@Column(name = "Estado")
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Column(name = "ComentarioResolucion")
	public String getComentarioResolucion() {
		return comentarioResolucion;
	}
	public void setComentarioResolucion(String comentarioResolucion) {
		this.comentarioResolucion = comentarioResolucion;
	}
	public Integer getRegularizacion() {
		return regularizacion;
	}
	public void setRegularizacion(Integer regularizacion) {
		this.regularizacion = regularizacion;
	}
	public Integer getIncluidoPlanilla() {
		return incluidoPlanilla;
	}
	public void setIncluidoPlanilla(Integer incluidoPlanilla) {
		this.incluidoPlanilla = incluidoPlanilla;
	}
	
	@Column(name = "Tipo")
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Column(name = "FechaCompra")
	public Date getFechaCompra() {
		return fechaCompra;
	}
	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}
	
	@ManyToOne()
	@JoinColumn(name = "IdCompraAutorizadaPor")
	public Empleado getCompraAutorizadaPor() {
		return compraAutorizadaPor;
	}
	public void setCompraAutorizadaPor(Empleado compraAutorizadaPor) {
		this.compraAutorizadaPor = compraAutorizadaPor;
	}
	
	@Column(name = "MesPlanilla")
	public String getMesPlanilla() {
		return mesPlanilla;
	}
	
	public void setMesPlanilla(String mesPlanilla) {
		this.mesPlanilla = mesPlanilla;
	}
			
}
