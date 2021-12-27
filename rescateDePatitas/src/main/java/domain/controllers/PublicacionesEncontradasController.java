package domain.controllers;

import domain.models.entities.hogares.Hogar;
import domain.models.entities.hogares.ListadoDeHogares;
import domain.models.entities.mascotas.DatosMascotaEncontrada;
import domain.models.entities.mascotas.Foto;
import domain.models.entities.mascotas.Lugar;
import domain.models.entities.mascotas.Mascota;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.PublicacionIntencionAdopcion;
import domain.models.entities.publicaciones.PublicacionMascotaEncontrada;
import domain.models.entities.rol.Rescatista;
import domain.models.repositories.RepositorioDePersonas;
import domain.models.entities.publicaciones.EstadoDePublicacion;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.factories.FactoryRepositorio;
import services.BuscadorDeHogaresDeTransito;
import services.FiltradorDeHogaresDeTransito;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

public class PublicacionesEncontradasController {
    private final RepositorioGenerico<PublicacionMascotaEncontrada> repo;
    private final UsuarioController usuarioController = UsuarioController.getInstancia();
    private final RolController rolController = RolController.getInstancia();

    public PublicacionesEncontradasController() {
        this.repo = FactoryRepositorio.get(PublicacionMascotaEncontrada.class);
    }

    public ModelAndView mostrarEncontradas(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<PublicacionMascotaEncontrada> encontradas = this.repo.buscarTodos();
        List<PublicacionMascotaEncontrada> aprobadas = new ArrayList<>();

        for (PublicacionMascotaEncontrada publicacion : encontradas) {
            if (publicacion.estaAprobada()) {
                aprobadas.add(publicacion);
            }
        }

        parametros.put("encontradas", aprobadas);
        return new ModelAndView(parametros, "encontradas.hbs");
    }

    public ModelAndView mostrarHogares(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<Hogar> hogares = request.session().attribute("hogares");
        parametros.put("hogares", hogares);

        return new ModelAndView(parametros, "registro_encontrada_asociacion.hbs");
    }

    public ModelAndView encontrada(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        List<TipoDeDocumento> tipo = new ArrayList<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        tipo.add(TipoDeDocumento.valueOf("DNI"));
        tipo.add(TipoDeDocumento.valueOf("LIBRETA_CIVICA"));
        tipo.add(TipoDeDocumento.valueOf("PASAPORTE"));
        tipo.add(TipoDeDocumento.valueOf("CEDULA"));
        tipo.add(TipoDeDocumento.valueOf("LIBRETA_ENROLAMIENTO"));

        List<String> provincias = new ArrayList<>();
        provincias.add("Buenos Aires");
        provincias.add("CABA");
        provincias.add("Córdoba");
        provincias.add("Santa Fe");

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        parametros.put("tipos", tipo);
        parametros.put("provincias", provincias);

        return new ModelAndView(parametros, "registro_encontrada.hbs");
    }

    public Response guardarEncontrada(Request request, Response response) throws Exception {
        PublicacionMascotaEncontrada publi = new PublicacionMascotaEncontrada();

        asignarAtributosA(publi, request);

        if (request.session().attribute("id") != null) {
            //Si esta logueado lo busco en el repo y lo agrego como rescatista
            RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
            Persona rescatista = repoPersonas.dameLaPersona(request.session().attribute("id"));
            publi.setRescatista(rescatista);
        } else {
            // Si NO esta logueado lo busco por HASH
            PersonaController cPersona = PersonaController.getInstancia();
            RepositorioDePersonas repoPersona = cPersona.getRepositorio();
            String cadena = request.queryParams("fnacPersona") + request.queryParams("nroDoc");
            String hashPersona = org.apache.commons.codec.digest.DigestUtils.md5Hex(cadena);
            Persona personaEncontrada = repoPersona.buscarPersona(hashPersona);

            if (personaEncontrada != null) {
                //Si encontré a la persona la seteo como rescatista
                publi.setRescatista(personaEncontrada);
            } else {
                // Si no la encontré la agrego y la seteo como rescatista
                Persona persona = new Persona();
                asignarAtributosA(persona, request);
                persona.setUsuarioTemporal(hashPersona);
                publi.setRescatista(persona);
                repoPersona.agregar(persona);
            }
        }

        this.repo.agregar(publi);

        String especie = "";
        if (request.queryParams("especie") != null) {
            especie = request.queryParams("especie");
        }

        String tamanio = "";
        if (request.queryParams("tamanio") != null) {
            tamanio = request.queryParams("tamanio");
        }

        String radio = "";
        if (request.queryParams("radio") != null) {
            radio = request.queryParams("radio");
        }

        BuscadorDeHogaresDeTransito buscadorDeHogaresDeTransito = BuscadorDeHogaresDeTransito.getInstancia();
        ListadoDeHogares listadoDeHogares = null;
        try {
            listadoDeHogares = new ListadoDeHogares();
            listadoDeHogares = buscadorDeHogaresDeTransito.listadoDeHogares(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert listadoDeHogares != null;
        List<Hogar> hogares = listadoDeHogares.hogares;

        FiltradorDeHogaresDeTransito filtrador = new FiltradorDeHogaresDeTransito();

        try {
            hogares = filtrador.filtrarPorCapacidad(hogares);
        } catch (Exception e) {
            response.redirect("/sin_hogares");
        }


        try {
            hogares = filtrador.filtrarPorAnimalAceptado(hogares, especie);
        } catch (Exception e) {
            response.redirect("/sin_hogares");
        }


        try {
            hogares = filtrador.filtrarPorTamanio(hogares, tamanio);
        } catch (Exception e) {
            response.redirect("/sin_hogares");
        }


        if (radio.equals("")) {
            //No elige radio, no le muestro hogares
            response.redirect("/publicacion_enviada");
        } else if (radio.equals("todos"))
            //elije cualquier radio, no filtro por cercania y muestro hogares
            response.redirect("/encontrada_hogares");
        else {
            //eligio un radio, filtro por radio
            hogares = filtrador.filtrarPorCercania(hogares, Integer.parseInt(radio),
                    Double.parseDouble(request.queryParams("latitud")), Double.parseDouble(request.queryParams("longitud")));

            response.redirect("/encontrada_hogares");
        }

        request.session().attribute("hogares", hogares);
        response.redirect("/encontrada_hogares");

        return response;
    }

    private void asignarAtributosA(PublicacionMascotaEncontrada publi, Request request) throws IOException {
        //Fotos
        File uploadDir = new File("rescateDePatitas/src/main/resources/public/img/fotosmascotas");
        //uploadDir.mkdir();
        Path tempFile = Files.createTempFile(uploadDir.toPath(), request.queryParams("nombre"), ".jpg");

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream input = request.raw().getPart("foto").getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        DatosMascotaEncontrada datosMascota = new DatosMascotaEncontrada();
        List<Foto> fotos = new ArrayList<>();
        Foto foto = new Foto();
        foto.setURLfoto(tempFile.toString().replace("rescateDePatitas\\src\\main\\resources\\public\\img\\fotosmascotas\\", "img/fotosmascotas/"));
        foto.setDatosMascotaEncontrada(datosMascota);
        fotos.add(foto);
        datosMascota.setFotos(fotos);
        double latitud = 0;
        double longitud = 0;

        if (request.queryParams("descripcion") != null) {
            datosMascota.setDescripcion(request.queryParams("descripcion"));
        }

        if (request.queryParams("latitud") != null) {
            latitud = Double.parseDouble(request.queryParams("latitud"));
            request.session().attribute("latitud", latitud);
        }

        if (request.queryParams("longitud") != null) {
            longitud = Double.parseDouble(request.queryParams("longitud"));
            request.session().attribute("longitud", longitud);
        }

        Lugar lugar = new Lugar();
        lugar.setLatitud(latitud);
        lugar.setLongitud(longitud);


        publi.setTipoPublicacion("Mascota encontrada");

        datosMascota.setLugar(lugar);
        publi.setDatosMascotaEncontrada(datosMascota);

    }

    private void asignarAtributosA(Persona persona, Request request) {
        if (request.queryParams("nombrePersona") != null) {
            persona.setNombre(request.queryParams("nombrePersona"));
        }

        if (request.queryParams("apellido") != null) {
            persona.setApellido(request.queryParams("apellido"));
        }

        if (request.queryParams("fnacPersona") != null) {
            persona.setFechaDeNacimiento(LocalDate.parse(request.queryParams("fnacPersona")));
        }

        if (request.queryParams("tipoDoc") != null) {
            persona.setTipoDoc(TipoDeDocumento.valueOf(request.queryParams("tipoDoc")));
        }

        if (request.queryParams("nroDoc") != null) {
            persona.setNroDoc(Integer.valueOf(request.queryParams("nroDoc")));
        }
        String pais = "";
        String provincia = "";
        String direccion = "";
        if (request.queryParams("provincia") != null) {
            provincia = request.queryParams("provincia");
        }

        if (request.queryParams("pais") != null) {
            pais = request.queryParams("pais");
        }

        if (request.queryParams("direccion") != null) {
            direccion = request.queryParams("direccion");
        }
        persona.setDireccion(direccion + "," + provincia + "," + pais);

        String cNombre = "";
        String cApellido = "";
        String cCorreo = "";
        String cNumero = "";
        Estrategia medioPreferido = Estrategia.valueOf("WHATSAPP");

        if (request.queryParams("cNombre") != null) {
            cNombre = request.queryParams("cNombre");
        }

        if (request.queryParams("cApellido") != null) {
            cApellido = request.queryParams("cApellido");
        }

        if (request.queryParams("cNumero") != null) {
            cNumero = request.queryParams("cNumero");
        }

        if (request.queryParams("cCorreo") != null) {
            cCorreo = request.queryParams("cCorreo");
        }

        if (request.queryParams("medioPreferido") != null) {
            if (request.queryParams("medioPreferido").equals("Email")) {
                medioPreferido = Estrategia.valueOf("EMAIL");
            } else {
                if (request.queryParams("medioPreferido").equals("WhatsApp")) {
                    medioPreferido = Estrategia.valueOf("WHATSAPP");
                } else medioPreferido = Estrategia.valueOf("SMS");
            }
        }

        Contacto contacto = new Contacto(cNombre, cApellido, cNumero, cCorreo, medioPreferido);
        contacto.setPersona(persona);

        List<Contacto> contactos = new ArrayList<>();
        contactos.add(contacto);

        persona.setContactos(contactos);

        Rescatista rolRescatista = new Rescatista();
        persona.addRol(rolRescatista);

    }


    public ModelAndView revisar_encontrada(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<PublicacionMascotaEncontrada> encontradas = this.repo.buscarTodos();
        List<PublicacionMascotaEncontrada> sin_revisar = new ArrayList<>();

        for (PublicacionMascotaEncontrada publicacion : encontradas) {
            if (publicacion.getEstadoDePublicacion().equals(EstadoDePublicacion.SIN_REVISAR)) {
                sin_revisar.add(publicacion);
            }
        }
        parametros.put("encontradas", sin_revisar);
        return new ModelAndView(parametros, "revisar_encontrada.hbs");
    }

    public ModelAndView revisar_publi(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        PublicacionMascotaEncontrada publicacion = this.repo.buscar(new Integer(request.params("id")));
        parametros.put("publicacion", publicacion);
        return new ModelAndView(parametros, "revisar_encontrada_publi.hbs");
    }

    public Response aprobar(Request request, Response response) {
        PublicacionMascotaEncontrada publicacion = this.repo.buscar(new Integer(request.params("id")));
        publicacion.setEstadoDePublicacion(EstadoDePublicacion.ACEPTADO);
        this.repo.modificar(publicacion);

        response.redirect("/revisar_encontrada");

        return response;
    }

    public Response rechazar(Request request, Response response) {
        PublicacionMascotaEncontrada publicacion = this.repo.buscar(new Integer(request.params("id")));
        publicacion.setEstadoDePublicacion(EstadoDePublicacion.RECHAZADO);
        this.repo.modificar(publicacion);

        response.redirect("/revisar_encontrada");

        return response;
    }

    public ModelAndView publicacionEnviada(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "publicacion_enviada.hbs");
    }

    public ModelAndView sinHogares(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "sin_hogares.hbs");
    }

    public ModelAndView mostrarPublicacionEncontrada(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);

        PublicacionMascotaEncontrada publicacion = this.repo.buscar(new Integer(request.params("id")));
        parametros.put("publicacion", publicacion);
        return new ModelAndView(parametros, "encontrada_publicacion.hbs");
    }

    public ModelAndView datosEnviados(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "datos_enviados.hbs");
    }

    public Response enviarDatos(Request request, Response response) {
        int publicacionId = Integer.parseInt(request.params("id"));
        PublicacionMascotaEncontrada publicacion = this.repo.buscar(publicacionId);

        if (request.session().attribute("id") != null) {
            RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
            Persona duenio = repoPersonas.dameLaPersona(request.session().attribute("id"));

            duenio.encontreMiMascotaPerdida(publicacion);

        } else {
            String nombre = "";
            if (request.queryParams("nombre") != null) {
                nombre = request.queryParams("nombre");
            }

            String email = "";
            if (request.queryParams("email") != null) {
                email = request.queryParams("email");
            }

            String numero = "";
            if (request.queryParams("numero") != null) {
                numero = request.queryParams("numero");
            }

            Estrategia medioPreferido = Estrategia.valueOf("EMAIL");
            if (request.queryParams("medioPreferido") != null) {
                if (request.queryParams("medioPreferido").equals("Email")) {
                    medioPreferido = Estrategia.valueOf("EMAIL");
                } else {
                    if (request.queryParams("medioPreferido").equals("WhatsApp")) {
                        medioPreferido = Estrategia.valueOf("WHATSAPP");
                    } else medioPreferido = Estrategia.valueOf("SMS");
                }
            }

            Contacto contacto = new Contacto(nombre, "", numero, email, medioPreferido);
            Persona persona = new Persona();
            persona.setNombre(nombre);
            contacto.setPersona(persona);
            Persona rescatista = publicacion.getRescatista();
            rescatista.notificarContactosRescatista(contacto);
        }

        response.redirect("/datos_enviados");
        return response;
    }

    public Response enviarDatosLog(Request request, Response response) {
        PublicacionMascotaEncontrada publicacion = this.repo.buscar(new Integer(request.params("id")));

        RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
        Persona duenio = repoPersonas.dameLaPersona(request.session().attribute("id"));

        duenio.encontreMiMascotaPerdida(publicacion);

        response.redirect("/datos_enviados");
        return response;
    }

    public ModelAndView mascotaEncontradaQR(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);
        List<TipoDeDocumento> tipo = new ArrayList<>();

        tipo.add(TipoDeDocumento.valueOf("DNI"));
        tipo.add(TipoDeDocumento.valueOf("LIBRETA_CIVICA"));
        tipo.add(TipoDeDocumento.valueOf("PASAPORTE"));
        tipo.add(TipoDeDocumento.valueOf("CEDULA"));
        tipo.add(TipoDeDocumento.valueOf("LIBRETA_ENROLAMIENTO"));

        List<String> provincias = new ArrayList<>();
        provincias.add("Buenos Aires");
        provincias.add("CABA");
        provincias.add("Córdoba");
        provincias.add("Santa Fe");

        parametros.put("tipos", tipo);
        parametros.put("provincias", provincias);


        request.session().attribute("idMascota",request.params("id"));
        return new ModelAndView(parametros, "registro_encontrada_qr.hbs");
    }

    public Response enviarMensajeEncontradaQR(Request request, Response response){
        int id = Integer.parseInt(request.session().attribute("idMascota"));
        Mascota mascota = MascotaController.getInstancia().getRepositorio().buscar(id);

        DatosMascotaEncontrada datosMascota = new DatosMascotaEncontrada();
        double latitud = 0;
        double longitud = 0;

        if (request.queryParams("descripcion") != null) {
            datosMascota.setDescripcion(request.queryParams("descripcion"));
        }

        if (request.queryParams("latitud") != null) {
            latitud = Double.parseDouble(request.queryParams("latitud"));
            request.session().attribute("latitud", latitud);
        }

        if (request.queryParams("longitud") != null) {
            longitud = Double.parseDouble(request.queryParams("longitud"));
            request.session().attribute("longitud", longitud);
        }

        Lugar lugar = new Lugar();
        lugar.setLatitud(latitud);
        lugar.setLongitud(longitud);

        datosMascota.setLugar(lugar);

        if (request.session().attribute("id") != null) {
            Persona rescatista = RepositorioDePersonas.getInstancia().dameLaPersona(request.session().attribute("id"));
            mascota.getPersona().notificarContactos(mascota,rescatista.getContactos(),datosMascota);

        } else {
            // Si NO esta logueado lo busco por HASH
            PersonaController cPersona = PersonaController.getInstancia();
            RepositorioDePersonas repoPersona = cPersona.getRepositorio();
            String cadena = request.queryParams("fnacPersona") + request.queryParams("nroDoc");
            String hashPersona = org.apache.commons.codec.digest.DigestUtils.md5Hex(cadena);
            Persona personaEncontrada = repoPersona.buscarPersona(hashPersona);

            if (personaEncontrada != null) {
                //Si encontré a la persona la seteo como rescatista
                Rescatista rescatista = new Rescatista();
                personaEncontrada.addRol(rescatista);
                mascota.setRescatista(rescatista);
            } else {
                // Si no la encontré la agrego y la seteo como rescatista
                Persona persona = new Persona();
                asignarAtributosA(persona, request);
                persona.setUsuarioTemporal(hashPersona);
                repoPersona.agregar(persona);
                mascota.getPersona().notificarContactos(mascota,persona.getContactos(),datosMascota);
            }

        }

        response.redirect("/ok");
        return response;
    }
}
