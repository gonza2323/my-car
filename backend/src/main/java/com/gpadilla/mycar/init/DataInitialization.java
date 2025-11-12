package com.gpadilla.mycar.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpadilla.mycar.dtos.auto.AutoCreateDto;
import com.gpadilla.mycar.dtos.caracteristicasAuto.CaracteristicasAutoCreateDto;
import com.gpadilla.mycar.dtos.cliente.ClienteCreateRequestDto;
import com.gpadilla.mycar.dtos.costoAuto.CostoAutoCreateDto;
import com.gpadilla.mycar.dtos.empleado.EmpleadoCreateRequestDto;
import com.gpadilla.mycar.dtos.geo.direccion.DireccionCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.nacionalidad.NacionalidadCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.geo.pais.PaisCreateOrUpdateDto;
import com.gpadilla.mycar.entity.*;
import com.gpadilla.mycar.entity.geo.*;
import com.gpadilla.mycar.enums.TipoDocumento;
import com.gpadilla.mycar.enums.TipoEmpleado;
import com.gpadilla.mycar.enums.TipoTelefono;
import com.gpadilla.mycar.facade.ClienteFacade;
import com.gpadilla.mycar.facade.EmpleadoFacade;
import com.gpadilla.mycar.init.geo.*;
import com.gpadilla.mycar.repository.EmpresaRepository;
import com.gpadilla.mycar.repository.UsuarioRepository;
import com.gpadilla.mycar.repository.geo.DepartamentoRepository;
import com.gpadilla.mycar.repository.geo.LocalidadRepository;
import com.gpadilla.mycar.repository.geo.PaisRepository;
import com.gpadilla.mycar.repository.geo.ProvinciaRepository;
import com.gpadilla.mycar.service.AutoService;
import com.gpadilla.mycar.service.CaracteristicasAutoService;
import com.gpadilla.mycar.service.CostoAutoService;
import com.gpadilla.mycar.service.UsuarioService;
import com.gpadilla.mycar.service.geo.DireccionService;
import com.gpadilla.mycar.service.geo.NacionalidadService;
import com.gpadilla.mycar.service.geo.PaisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final int CANT_NACIONALIDADES = 180;
    private final int CANT_CLIENTES = 100;
    private final int CANT_MODELOS_VEHICULOS = 10;
    private final int CANT_VEHICULOS = 50;
    private final ClienteFacade clienteFacade;
    private final UsuarioService usuarioService;
    private final NacionalidadService nacionalidadService;
    private final CaracteristicasAutoService caracteristicasAutoService;
    private final AutoService autoService;
    private final CostoAutoService costoAutoService;

    //-----
    private final EmpresaRepository empresaRepository;
    private final DireccionService direccionService;
    //-----

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

        List<CaracteristicasAuto> modelos = crearCaracteristicasVehiculos();
        crearVehiculos(modelos);
        crearCostoVehiculos(modelos);

        List<Nacionalidad> nacionalidades = crearNacionalidades();
        List<Pais> paises = crearPaises();
        cargarUbicacionesArgentina(paises.getFirst());
        crearEmpleados();
        List<Long> clienteIds = crearClientes(nacionalidades);
        System.out.println("creando empresa");
        crearEmpresaInicial();
        System.out.println("Datos iniciales creados.");
    }

    @Transactional
    protected List<Nacionalidad> crearNacionalidades() {
        List<Nacionalidad> nacionalidades = new ArrayList<>();
        for (int i = 0; i < CANT_NACIONALIDADES; i++) {
            nacionalidades.add(nacionalidadService.create(
                    NacionalidadCreateOrUpdateDto.builder()
                            .nombre(faker.unique().fetchFromYaml("address.country"))
                            .build()
            ));
        }
        return nacionalidades;
    }

    @Transactional
    protected List<Pais> crearPaises() {
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
        int cantEmpleados = cargarEmpleadosEspecificos();

        for (int i = 0; i < CANT_EMPLEADOS - cantEmpleados; i++) {
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

    @Transactional
    protected int cargarEmpleadosEspecificos() {
        Long localidadId = localidades.get(faker.random().nextInt(localidades.size())).getId();
        empleadoFacade.registrarEmpleado(EmpleadoCreateRequestDto.builder()
                .nombre("Admin")
                .apellido("Sistema")
                .email("admin@gmail.com")
                .tipoEmpleado(TipoEmpleado.JEFE)
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
                .build());

        localidadId = localidades.get(faker.random().nextInt(localidades.size())).getId();
        empleadoFacade.registrarEmpleado(EmpleadoCreateRequestDto.builder()
                .nombre("Pepe")
                .apellido("Argento")
                .email("pepeargento@gmail.com")
                .tipoEmpleado(TipoEmpleado.JEFE)
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
                .build());

        localidadId = localidades.get(faker.random().nextInt(localidades.size())).getId();
        empleadoFacade.registrarEmpleado(EmpleadoCreateRequestDto.builder()
                .nombre("Moni")
                .apellido("Argento")
                .email("moniargento@gmail.com")
                .tipoEmpleado(TipoEmpleado.ADMINISTRATIVO)
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
                .build());

        return 3;
    }

    @Transactional
    protected List<Long> crearClientes(List<Nacionalidad> nacionalidades) {
        List<Long> clienteIds = new ArrayList<>();
        for (int i = 0; i < CANT_CLIENTES; i++) {
            Long localidadId = localidades.get(faker.random().nextInt(localidades.size())).getId();
            Long nacionalidadId = nacionalidades.get(faker.random().nextInt(nacionalidades.size())).getId();
            ClienteCreateRequestDto clienteDto = ClienteCreateRequestDto.builder()
                    .nombre(faker.name().firstName())
                    .apellido(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .nacionalidadId(nacionalidadId)
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
            Long id = clienteFacade.registrarClientePorFormularioAdmin(clienteDto);
            clienteIds.add(id);
        }
        return clienteIds;
    }

//    //---------------
    @Transactional
    protected void crearEmpresaInicial() {
        if (empresaRepository.count() > 0) {
            return;
        }

        Localidad localidadBase = (localidades != null && !localidades.isEmpty())
                ? localidades.getFirst()
                : localidadRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Debe existir al menos una localidad para crear la empresa"));

        Direccion direccion = direccionService.create(
                DireccionCreateOrUpdateDto.builder()
                        .calle("Av. San Martín")
                        .numeracion("1450")
                        .barrio("Centro")
                        .manzanaPiso(null)
                        .casaDepartamento(null)
                        .referencia("Casa central")
                        .localidadId(localidadBase.getId())
                        .build()
        );

        ContactoTelefonico contactoT = new ContactoTelefonico();
        contactoT.setTelefono("+54 261 555 0000");
        contactoT.setTipoTelefono(TipoTelefono.CELULAR);

        ContactoCorreoElectronico contactoC = new ContactoCorreoElectronico();
        contactoC.setEmail("gimnasiosport21@gmail.com");

        Empresa empresa = Empresa.builder()
                .nombre("MyCar S.A.")
                .telefonoPrincipal(contactoT)
                .emailPrincipal(contactoC)
                .direccion(direccion)
                .eliminado(false)
                .build();

        contactoT.setEmpresa(empresa);
        contactoT.setEliminado(false);

        contactoC.setEmpresa(empresa);
        contactoC.setEliminado(false);

        empresaRepository.save(empresa);
    }
//    //---------------

    @Transactional
    protected List<CaracteristicasAuto> crearCaracteristicasVehiculos() {
        List<CaracteristicasAuto> modelos = new ArrayList<>();
        for (int i = 0; i < CANT_MODELOS_VEHICULOS; i++) {
            String[] makeAndModel = faker.vehicle().makeAndModel().split(" ", 2);
            modelos.add(caracteristicasAutoService.create(CaracteristicasAutoCreateDto.builder()
                    .marca(makeAndModel[0])
                    .modelo(makeAndModel[1])
                    .anio(faker.number().numberBetween(1999, 2025))
                    .cantidadAsientos(faker.number().numberBetween(2, 5))
                    .cantidadPuertas(faker.number().numberBetween(2, 6)).build()));
        }
        return modelos;
    }

    @Transactional
    protected List<Auto> crearVehiculos(List<CaracteristicasAuto> modelos) {
        List<Auto> vehiculos = new ArrayList<>();
        for (int i = 0; i < CANT_VEHICULOS; i++) {
            Long modeloId = modelos.get(faker.random().nextInt(modelos.size())).getId();
            vehiculos.add(autoService.create(AutoCreateDto.builder()
                    .patente(faker.vehicle().licensePlate().toUpperCase())
                    .caracteristicasAutoId(modeloId)
                    .build()));
        }
        return vehiculos;
    }

    @Transactional
    protected void crearCostoVehiculos(List<CaracteristicasAuto> modelos) {
        Faker faker = new Faker();

        LocalDate startOverall = LocalDate.now().minusMonths(18); // ~1.5 years in the past
        LocalDate endOverall = LocalDate.now().plusMonths(12);    // ~1 year in the future

        for (CaracteristicasAuto modelo : modelos) {
            int ranges = faker.number().numberBetween(2, 6); // random ranges per model
            LocalDate currentStart = startOverall;

            for (int i = 0; i < ranges; i++) {
                long remainingDays = ChronoUnit.DAYS.between(currentStart, endOverall);

                if (remainingDays <= 0) {
                    break;
                }

                long daysForRange;
                if (i == ranges - 1) {
                    daysForRange = remainingDays; // last range takes all remaining days
                } else {
                    long maxDays = remainingDays - (ranges - i - 1); // leave at least 1 day per remaining range
                    daysForRange = faker.number().numberBetween(1, maxDays);
                }

                LocalDate currentEnd = currentStart.plusDays(daysForRange - 1);
                long costoTotal = faker.number().numberBetween(25000, 70000);

                // Call your service
                costoAutoService.create(CostoAutoCreateDto.builder()
                        .caracteristicasAutoId(modelo.getId())
                        .costoTotal(costoTotal)
                        .fechaDesde(currentStart)
                        .fechaHasta(currentEnd)
                        .build());

                currentStart = currentEnd.plusDays(1);
            }
        }
    }

}
