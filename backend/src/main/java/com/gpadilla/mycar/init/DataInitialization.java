package com.gpadilla.mycar.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateRequestDto;
import com.gpadilla.mycar.dtos.geo.direccion.DireccionCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.pais.PaisCreateOrUpdateDto;
import com.gpadilla.mycar.entity.geo.Departamento;
import com.gpadilla.mycar.entity.geo.Localidad;
import com.gpadilla.mycar.entity.geo.Pais;
import com.gpadilla.mycar.entity.geo.Provincia;
import com.gpadilla.mycar.enums.TipoDocumento;
import com.gpadilla.mycar.enums.TipoEmpleado;
import com.gpadilla.mycar.facade.ClienteFacade;
import com.gpadilla.mycar.facade.EmpleadoFacade;
import com.gpadilla.mycar.init.geo.*;
import com.gpadilla.mycar.repository.UsuarioRepository;
import com.gpadilla.mycar.repository.geo.DepartamentoRepository;
import com.gpadilla.mycar.repository.geo.LocalidadRepository;
import com.gpadilla.mycar.repository.geo.PaisRepository;
import com.gpadilla.mycar.repository.geo.ProvinciaRepository;
import com.gpadilla.mycar.service.UsuarioService;
import com.gpadilla.mycar.service.geo.PaisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final ProvinciaRepository provinciaRepository;
    private final ObjectMapper objectMapper;
    private final PaisService paisService;
    private final PaisRepository paisRepository;
    private final LocalidadRepository localidadRepository;

    private final EmpleadoFacade empleadoFacade;
    private final Faker faker;

    private final int CANT_EMPLEADOS = 15;
    private final int CANT_CLIENTES = 100;
    private final ClienteFacade clienteFacade;
    private final UsuarioService usuarioService;

    private List<Localidad> localidades;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        crearDatosIniciales();
    }

    @Transactional
    protected void crearDatosIniciales() throws Exception {
        if (usuarioRepository.count() > 0) {
            System.out.println("Datos iniciales ya creados. Salteando creación de datos iniciales. Para forzar su creación, borrar la base de datos");
            return;
        }

        System.out.println("Creando datos iniciales...");

        List<Pais> paises = crearPaises();
        cargarUbicacionesArgentina(paises.getFirst());
        crearEmpleados();

        System.out.println("Datos iniciales creados.");
    }

    private List<Pais> crearPaises() {
        List<Pais> paises = new ArrayList<>();

        paises.add(paisService.create(new PaisCreateOrUpdateDto("Argentina")));
        paises.add(paisService.create(new PaisCreateOrUpdateDto("España")));
        paises.add(paisService.create(new PaisCreateOrUpdateDto("Brazil")));
        paises.add(paisService.create(new PaisCreateOrUpdateDto("Chile")));

        return paises;
    }

    @Transactional
    protected void cargarUbicacionesArgentina(Pais argentina) throws Exception {
        Map<Long, Provincia> provinciaMap = loadProvincias(argentina);
        Map<Long, Departamento> departamentoMap = loadDepartamentos(provinciaMap);
        this.localidades = loadLocalidades(departamentoMap);
    }

    @Transactional
    protected Map<Long, Provincia> loadProvincias(Pais argentina) throws IOException {
        InputStream is = getClass().getResourceAsStream("/data/provincias.json");
        ProvinciasWrapper wrapper = objectMapper.readValue(is, ProvinciasWrapper.class);

        Map<Long, Provincia> provinciaMap = new HashMap<>(wrapper.getProvincias().size());

        for (ProvinciaDTO dto : wrapper.getProvincias()) {
            Long id = dto.getIdAsLong();
            if (!provinciaRepository.existsById(id)) {
                Provincia provincia = new Provincia();
                provincia.setNombre(dto.getNombre());
                provincia.setPais(argentina);
                provinciaMap.put(dto.getIdAsLong(), provincia);
            }
        }
        provinciaRepository.saveAll(provinciaMap.values());

        return provinciaMap;
    }

    @Transactional
    protected Map<Long, Departamento> loadDepartamentos(Map<Long, Provincia> provinciaMap) throws IOException {
        InputStream is = getClass().getResourceAsStream("/data/departamentos.json");
        DepartamentosWrapper wrapper = objectMapper.readValue(is, DepartamentosWrapper.class);

        Map<Long, Departamento> departamentoMap = new HashMap<>(wrapper.getDepartamentos().size());

        for (DepartamentoDTO dto : wrapper.getDepartamentos()) {
            Long id = dto.getIdAsLong();
            if (!departamentoRepository.existsById(id)) {
                Long provinciaId = dto.getProvincia().getIdAsLong();
                Provincia provincia = provinciaMap.get(provinciaId);
                if (provincia == null) {
                    throw new IllegalStateException("Provincia no encontrada, id: " + provinciaId);
                }

                Departamento departamento = new Departamento();
                departamento.setNombre(dto.getNombre());
                departamento.setProvincia(provincia);
                departamentoMap.put(dto.getIdAsLong(), departamento);
            }
        }

        departamentoRepository.saveAll(departamentoMap.values());
        return departamentoMap;
    }

    @Transactional
    protected List<Localidad> loadLocalidades(Map<Long, Departamento> departamentoMap) throws IOException {
        InputStream is = getClass().getResourceAsStream("/data/localidades.json");
        LocalidadesWrapper wrapper = objectMapper.readValue(is, LocalidadesWrapper.class);

        List<Localidad> localidadesToSave = new ArrayList<>(wrapper.getLocalidades().size());
        int postalCodeCounter = 1;

        for (LocalidadDTO dto : wrapper.getLocalidades()) {
            Long id = dto.getIdAsLong();
            if (!localidadRepository.existsById(id)) {
                Long departamentoId = dto.getDepartamento().getIdAsLong();
                Departamento departamento = departamentoMap.get(departamentoId);
                if (departamento == null)
                    new IllegalStateException("Departamento no encontrado, id: " + departamentoId);

                Localidad localidad = new Localidad();
                localidad.setNombre(dto.getNombre());
                localidad.setDepartamento(departamento);

                Provincia provincia = departamento.getProvincia();
                String provinceInitial = provincia.getNombre().substring(0, 1).toUpperCase();
                String numberPart = String.format("%04d", postalCodeCounter++);
                localidad.setCodigoPostal(provinceInitial + numberPart);

                localidadesToSave.add(localidad);
            }
        }

        localidadRepository.saveAll(localidadesToSave);
        return localidadesToSave;
    }

    @Transactional
    protected void crearEmpleados() {
        for (int i = 1; i <= CANT_EMPLEADOS; i++) {
            Long localidadId = localidades.get(faker.random().nextInt(localidades.size())).getId();
            EmpleadoCreateRequestDto empleadoDto = EmpleadoCreateRequestDto.builder()
                    .nombre(faker.name().firstName())
                    .apellido(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .tipoEmpleado(faker.random().nextEnum(TipoEmpleado.class))
                    .tipoDocumento(faker.random().nextEnum(TipoDocumento.class))
                    .numeroDocumento(faker.number().digits(7))
                    .fechaNacimiento(faker.timeAndDate().birthday(18, 100))
                    .direccion(DireccionCreateOrUpdateDto.builder()
                            .calle(faker.address().streetName())
                            .numeracion(faker.address().buildingNumber())
                            .barrio(faker.address().cityName())
                            .manzanaPiso(faker.address().streetSuffix())
                            .casaDepartamento(faker.address().secondaryAddress())
                            .referencia(faker.lorem().sentence(3))
                            .localidadId(localidadId)
                            .build())
                    .build();

            empleadoFacade.registrarEmpleado(empleadoDto);
        }
    }

}
