package pe.com.tss.runakuna.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.tss.runakuna.domain.model.entities.DepartamentoArea;
import pe.com.tss.runakuna.domain.model.entities.Empleado;
import pe.com.tss.runakuna.domain.model.entities.Empresa;
import pe.com.tss.runakuna.domain.model.entities.Proyecto;
import pe.com.tss.runakuna.domain.model.entities.ReporteMarcacion;
import pe.com.tss.runakuna.domain.model.entities.ReporteMarcacionSubscriptor;
import pe.com.tss.runakuna.domain.model.entities.UnidadDeNegocio;
import pe.com.tss.runakuna.domain.model.repository.jdbc.ReporteMarcacionJdbcRepository;
import pe.com.tss.runakuna.domain.model.repository.jpa.DepartamentoAreaJpaRepository;
import pe.com.tss.runakuna.domain.model.repository.jpa.EmpleadoJpaRepository;
import pe.com.tss.runakuna.domain.model.repository.jpa.EmpresaJpaRepository;
import pe.com.tss.runakuna.domain.model.repository.jpa.ProyectoJpaRepository;
import pe.com.tss.runakuna.domain.model.repository.jpa.ReporteMarcacionJpaRepository;
import pe.com.tss.runakuna.domain.model.repository.jpa.ReporteMarcacionSubscriptorJpaRepository;
import pe.com.tss.runakuna.domain.model.repository.jpa.UnidaDeNegocioJpaRepository;
import pe.com.tss.runakuna.enums.SeverityStatusEnum;
import pe.com.tss.runakuna.view.model.ReporteMarcacionFilterViewModel;
import pe.com.tss.runakuna.view.model.ReporteMarcacionResultViewModel;
import pe.com.tss.runakuna.view.model.NotificacionViewModel;
import pe.com.tss.runakuna.view.model.ReporteMarcacionViewModel;
import pe.com.tss.runakuna.view.model.ReporteMarcacionSubscriptorViewModel;
import pe.com.tss.runakuna.exception.BusinessException;
import pe.com.tss.runakuna.exception.GenericRestException;
import pe.com.tss.runakuna.service.ReporteMarcacionService;

/**
 * Created by josediaz on 14/12/2016.
 */
@Service
public class ReporteMarcacionServiceImpl implements ReporteMarcacionService {

    @Autowired
    private EmpresaJpaRepository empresaJpaRepository;
    
    @Autowired
    private UnidaDeNegocioJpaRepository  unidaDeNegocioJpaRepository;
    
    @Autowired
    private DepartamentoAreaJpaRepository departamentoAreaJpaRepository;
    
    @Autowired
    private ProyectoJpaRepository proyectoJpaRepository;
    
    @Autowired
	Mapper mapper;
    
	@Autowired
	private EmpleadoJpaRepository empleadoJpaRepository;
	
	@Autowired
	private ReporteMarcacionJpaRepository reporteMarcacionJpaRepository;
	
	@Autowired
	private ReporteMarcacionJdbcRepository  reporteMarcacionJdbcRepository;
	
	@Autowired
	private ReporteMarcacionSubscriptorJpaRepository reporteMarcacionSubscriptorJpaRepository;
	
    private NotificacionViewModel crearActualizarMarcacion(ReporteMarcacionViewModel reporteMarcacionDto) {
    	NotificacionViewModel notificacionDto=new NotificacionViewModel();
		ReporteMarcacion entity=new ReporteMarcacion();
		entity.setSubscriptores(new ArrayList<>());
		UnidadDeNegocio unidadNegocio=null;
		DepartamentoArea departamentoArea=null;
		Proyecto proyecto=null;
		Empleado empleado=null;
		mapper.map(reporteMarcacionDto, entity);
		entity.setSubscriptores(new ArrayList<>());
		Empresa empresa=empresaJpaRepository.findOne(reporteMarcacionDto.getIdEmpresa());
		if(reporteMarcacionDto.getIdUnidadDeNegocio()!=null)
			unidadNegocio=unidaDeNegocioJpaRepository.findOne(reporteMarcacionDto.getIdUnidadDeNegocio());
		if(reporteMarcacionDto.getIdDepartamentoArea()!=null)
		   departamentoArea= departamentoAreaJpaRepository.findOne(reporteMarcacionDto.getIdDepartamentoArea());
		if(reporteMarcacionDto.getIdProyecto()!=null)
		   proyecto=proyectoJpaRepository.findOne(reporteMarcacionDto.getIdProyecto());
		if(reporteMarcacionDto.getIdJefe()!=null)
			empleado=empleadoJpaRepository.findOne(reporteMarcacionDto.getIdJefe());
		
		ReporteMarcacionSubscriptor reporteMarcacionSubscriptor=null;
		try {
			entity.setEmpresa(empresa);
			entity.setJefeProyecto(empleado);
			entity.setUnidadDeNegocio(unidadNegocio);
			entity.setDepartamentoArea(departamentoArea);
			entity.setProyecto(proyecto);
			entity.setEstado(reporteMarcacionDto.getEstado());//cod Estado
			if(reporteMarcacionDto.getSubscriptores()!=null){
				for(ReporteMarcacionSubscriptorViewModel subscriptor: reporteMarcacionDto.getSubscriptores()){
					reporteMarcacionSubscriptor =new ReporteMarcacionSubscriptor();
					reporteMarcacionSubscriptor.setReporteMarcacion(entity);
					Empleado empl=empleadoJpaRepository.findOne(subscriptor.getIdEmpleado());
					reporteMarcacionSubscriptor.setEmpleado(empl);
					entity.getSubscriptores().add(reporteMarcacionSubscriptor);
				}
			}
			if(entity.getReporteDiario()==null)
				entity.setReporteDiario(0);
			if(entity.getReporteAcumulado()==null)
				entity.setReporteAcumulado(0);
			reporteMarcacionJpaRepository.save(entity);
			reporteMarcacionJpaRepository.flush();
			notificacionDto.setCodigo(1L);
			notificacionDto.setSeverity("success");
			notificacionDto.setSummary("Runakuna Success");
			notificacionDto.setDetail("Se registro correctamente");
		} catch (Exception e) {
			notificacionDto.setCodigo(0L);
			notificacionDto.setSeverity("error");
			notificacionDto.setSummary("Runakuna Error");
			notificacionDto.setDetail("No es posible registrar, "+e.getMessage());
		}
		return notificacionDto;
    }
		
	@Override
	public ReporteMarcacionViewModel obtenerReporteMarcacionDetalle(Long idReporteMarcacion) {
		ReporteMarcacionViewModel dto=new ReporteMarcacionViewModel();
		dto.setSubscriptores(new ArrayList<>());
		ReporteMarcacion reporteMarcacion=reporteMarcacionJpaRepository.findOne(idReporteMarcacion);
		mapper.map(reporteMarcacion, dto);
		dto.setIdEmpresa(reporteMarcacion.getEmpresa().getIdEmpresa());
		if(reporteMarcacion.getJefeProyecto()!=null)
		dto.setIdJefe(reporteMarcacion.getJefeProyecto().getIdEmpleado());
		
		if(reporteMarcacion.getJefeProyecto()!=null) {
			dto.setIdJefeProyecto(reporteMarcacion.getJefeProyecto().getIdEmpleado());
			dto.setJefeInmediato(reporteMarcacion.getJefeProyecto().getApellidoPaterno()+" "+
			reporteMarcacion.getJefeProyecto().getApellidoMaterno()+" "+reporteMarcacion.getJefeProyecto().getNombre());
		}
		
		if(reporteMarcacion.getUnidadDeNegocio()!=null)
			dto.setIdUnidadDeNegocio(reporteMarcacion.getUnidadDeNegocio().getIdUnidadDeNegocio());
		
		if(reporteMarcacion.getDepartamentoArea()!=null)
			dto.setIdDepartamentoArea(reporteMarcacion.getDepartamentoArea().getIdDepartamentoArea());
		
		if(reporteMarcacion.getProyecto()!=null)
			dto.setIdProyecto(reporteMarcacion.getProyecto().getIdProyecto());
		
		dto.setSubscriptores(reporteMarcacionJdbcRepository.obtenerSubscriptores(dto));
		return dto;
	}

	@Override
	public NotificacionViewModel eliminarReporteMarcacion(Long idReporteMarcacion) {
		NotificacionViewModel notificacion=new NotificacionViewModel();
		if (reporteMarcacionJpaRepository.findOne(idReporteMarcacion) == null) {
			throw new GenericRestException("ERROR", "The record was changed by another user. Please re-enter the screen.");
		}
		try {
			reporteMarcacionJpaRepository.delete(idReporteMarcacion);
			reporteMarcacionJpaRepository.flush();
			notificacion.setCodigo(1L);
			notificacion.setSeverity("success");
			notificacion.setSummary("Runakuna Success");
			notificacion.setDetail("Se elimino el registro correctamente");
		} catch (Exception e) {
			notificacion.setCodigo(0L);
			notificacion.setDetail("No se pudo eliminar el registro "+e.getMessage());
			throw new BusinessException(SeverityStatusEnum.ERROR.getCode(),"Runakuna Error",  e.getMessage());
		}
		
		return notificacion;
	}

	@Override
	public List<ReporteMarcacionResultViewModel> search(ReporteMarcacionFilterViewModel filterViewModel) {
		return reporteMarcacionJdbcRepository.obtenerReportes(filterViewModel);
	}

	@Override
	public ReporteMarcacionViewModel findOne(Long id) {
		return null;
	}

	@Override
	public NotificacionViewModel create(ReporteMarcacionViewModel manteinanceViewModel) {
		NotificacionViewModel notificacionDto=new NotificacionViewModel();
		notificacionDto=crearActualizarMarcacion(manteinanceViewModel);
		return notificacionDto;
	}

	@Override
	public NotificacionViewModel update(ReporteMarcacionViewModel manteinanceViewModel) {
		NotificacionViewModel notificacionDto=new NotificacionViewModel();
		notificacionDto=crearActualizarMarcacion(manteinanceViewModel);
		return notificacionDto;
	}

	@Override
	public NotificacionViewModel delete(Long id) {
		return null;
	}


	@Override
	public List<ReporteMarcacionViewModel> obtenerReportesJob(ReporteMarcacionFilterViewModel reporteMarcacionDto) {
		return reporteMarcacionJdbcRepository.obtenerReportesJob(reporteMarcacionDto);
	}



	
	
	
}
