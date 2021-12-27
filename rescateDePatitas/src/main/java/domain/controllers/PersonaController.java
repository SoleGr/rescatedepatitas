package domain.controllers;

import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rol;
import domain.models.repositories.RepositorioDePersonas;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
import domain.models.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PersonaController {
    private final RepositorioDePersonas repositorio;
    private static PersonaController instancia;

    public PersonaController(){
        DAO<Persona> dao = new DAOHibernate<>(Persona.class);
        this.repositorio = new RepositorioDePersonas(dao);
    }

    public static PersonaController getInstancia() {
        if (instancia == null) {
            instancia = new PersonaController();
        }
        return instancia;
    }

    public RepositorioDePersonas getRepositorio() {
        return repositorio;
    }

    private void asignarAtributosA(Persona persona, Request request){
        if (request.queryParams("nombre") != null) {
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

        Duenio rolDuenio = new Duenio();
        persona.addRol(rolDuenio);
        persona.setRolElegido(rolDuenio);
    }

    public ModelAndView crear(Request request, Response response){
        //TODO ver
        Map<String, Object> parametros = new HashMap<>();
        RepositorioGenerico<Rol> repoRol = FactoryRepositorio.get(Rol.class);

        parametros.put("roles", repoRol.buscarTodos());
        return new ModelAndView(parametros, "usuario.hbs");
    }

    public Response guardar(Request request, Response response){
        //TODO ver
        Persona persona = new Persona();
        asignarAtributosA(persona, request);
        this.repositorio.agregar(persona);
        response.redirect("/usuarios");
        return response;
    }

    public Response eliminar(Request request, Response response){
        //Buscar el usuario, podriamos tener un campo boolean que se llame "activo"
        //Usuario usuario = this.repositorio.buscar(Integer.parseInt(request.params("id")));
        //this.repositorio.eliminar(usuario);
        return response;
    }

}
