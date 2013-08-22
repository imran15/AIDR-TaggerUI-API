/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import qa.qcri.aidr.predictui.entities.NominalLabel;
import qa.qcri.aidr.predictui.facade.NominalLabelResourceFacade;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/label")
@Stateless
public class NominalLabelResource {

    @Context
    private UriInfo context;
    @EJB
    private NominalLabelResourceFacade labelLocal;

    public NominalLabelResource() {
    }
    
    @GET
    @Produces("application/json")
    @Path("/all")
    public Response getAllNominalLabels() {
        List<NominalLabel> labelList = labelLocal.getAllNominalLabel();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage("SUCCESS");
        response.setNominalLabels(labelList);
        return Response.ok(response).build();
    }

    
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAttribute(NominalLabel label){
        NominalLabel newLabel =  labelLocal.addNominalLabel(label);
        return Response.ok(newLabel).build();
    }
    
}
