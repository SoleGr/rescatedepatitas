package domain.models.entities.publicaciones;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.models.entities.personas.Persona;
import org.quartz.*;

import java.util.ArrayList;
import java.util.List;

public class MatchearPublicacionesEnAdopcion implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        GestorDePublicaciones gestorDePublicaciones = GestorDePublicaciones.getInstancia();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();



        //Recupera los datos de la publicacion
        //Cuestionario preferenciasYcomodidades = gson
         //       .fromJson((String) dataMap.get("preferenciasYComodidades"), Cuestionario.class);
        Persona adoptante = gson.fromJson((String) dataMap.get("adoptante"), Persona.class);
        ArrayList<String> preguntas = gson.fromJson((String) dataMap.get("preguntas"), ArrayList.class);
        ArrayList<String> respuestas = gson.fromJson((String) dataMap.get("respuestas"), ArrayList.class);

        List<PublicacionEnAdopcion> publicacionesEnAdopcion = gestorDePublicaciones.getPublicacionesDeAdopcion();
        publicacionesEnAdopcion.stream().filter(publicacion ->
                compararpublicacion(preguntas, respuestas, publicacion))
                .forEach(publicacion -> System.out.println(publicacion.getMascota().getApodo()));

        //TODO: Hacer que devuelva el hipervinculo a la publicacion. Probablemente sera posible en la ultima entrega
        ArrayList hypervinculosPublicacionesEnAdopcion = new ArrayList<>();
        publicacionesEnAdopcion.forEach(publicacion -> hypervinculosPublicacionesEnAdopcion.add(publicacion.getMascota().getApodo()));

        //Notifica a la persona unicamente si se encontraron publicaciones coincidentes en el periodo de tiempo
        if(!hypervinculosPublicacionesEnAdopcion.isEmpty()){
            hypervinculosPublicacionesEnAdopcion.forEach(x ->
                    System.out.println(x));
            //adoptante.notificarMascotasEnAdopcion(hypervinculosPublicacionesEnAdopcion);
        }


    }

    //Compara si para cada pregunta en el cuestionario1, existe una pregunta igual con respuesta igual
    public boolean compararpublicacion(ArrayList<String> preguntas, ArrayList<String> respuestas, PublicacionEnAdopcion publicacionEnAdopcion) {
        CuestionarioContestado cuestionarioContestado2 = publicacionEnAdopcion.getCuestionario();
//        boolean answer = cuestionario1.getRespuestas().stream().allMatch(respuestaSobrePregunta -> {
//            Optional<RespuestaSobrePregunta> match = cuestionario2.getRespuestas().stream()
//                    .filter(respuesta -> respuesta.getPregunta() == respuestaSobrePregunta.getPregunta())
//                    .findAny();
//        if(!match.isPresent() || match.get().getRespuesta() != respuestaSobrePregunta.getRespuesta())
//            return false;
//        return true;
//        });
        boolean answer = true;
        if (cuestionarioContestado2.getRespuestas().isEmpty()) {return false;}
        for(int i = 0; i < preguntas.size(); i++) {
            int index = i;
            if(!cuestionarioContestado2.getRespuestas().stream().anyMatch(respuestaSobrePregunta ->
                    respuestaSobrePregunta.getPregunta().getPregunta() == preguntas.get(index) &&
                    respuestaSobrePregunta.getRespuesta() == respuestas.get(index))){
                answer = false;
                break;
            }

        }
        return answer;
    }
}
