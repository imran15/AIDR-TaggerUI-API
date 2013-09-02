/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.predictui.util.JAXBContextResolver;
import qa.qcri.aidr.predictui.util.Lists;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/crisis")
@Stateless
public class CrisisResource {

    @Context
    private UriInfo context;
    @EJB
    private CrisisResourceFacade crisisLocalEJB;

    public CrisisResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getCrisisByID(@PathParam("id") int id) {
        Crisis crisis = null;
        try {
            crisis = crisisLocalEJB.getCrisisByID(id);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
        }
        return Response.ok(crisis).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/code/{code}")
    public Response isCrisisExists(@PathParam("code") String crisisCode) {
        Integer crisisId = crisisLocalEJB.isCrisisExists(crisisCode);
//        null value can not be correct deserialized
        if (crisisId == null){
            crisisId = 0;
        }
        //TODO: Following way of creating JSON should be chagned through a proper and automatic way
        String response = "{\"crisisCode\":\"" + crisisCode + "\", \"crisisId\":\"" + crisisId + "\"}";
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllCrisis() {
        List<Crisis> crisisList = crisisLocalEJB.getAllCrisis();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage(Config.STATUS_CODE_SUCCESS);
        response.setCrisises(crisisList);
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseWrapper getAllCrisisByUserID(@QueryParam("userID") int userID) throws Exception {
        List<Crisis> crisisList = crisisLocalEJB.getAllCrisisByUserID(userID);
        ResponseWrapper response = new ResponseWrapper();
        if (crisisList == null) {
            response.setMessage("No crisis found associated with the given user id.");
            return response;
        }
        response.setCrisises(crisisList);
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCrisis(Crisis crisis) {
        try {
            crisisLocalEJB.addCrisis(crisis);
        } catch (RuntimeException e) {
            return Response.ok("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format.").build();
        }

        return Response.ok(Config.STATUS_CODE_SUCCESS).build();

    }
}
