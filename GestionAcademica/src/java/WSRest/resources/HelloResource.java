/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WSRest.resources;

//import javax.ws.rs.ApplicationPath;
import Entidad.TipoEvaluacion;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
*/
/**
 *  WSRest.Resourse HelloResource
 *
 * @author alvar
 */


//@ApplicationPath("rest")
@Path("/hello")
public class HelloResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TipoEvaluacion sayHello() {
        TipoEvaluacion tpoEvl = new TipoEvaluacion();
        tpoEvl.setTpoEvlNom("Nombre");
        return tpoEvl;
    }
}
