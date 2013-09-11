/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.collections.iterators.ArrayListIterator;
import qa.qcri.aidr.predictui.dto.TrainingDataDTO;

import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.facade.MiscResourceFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/misc")
@Stateless
public class MiscResource {

    @Context
    private UriInfo context;
    @EJB
    private MiscResourceFacade miscEJB;

    public MiscResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getTrainingData")
    public Response getTrainingDataByCrisisAndAttribute(@QueryParam("crisisID") int crisisID, @QueryParam("attributeID") int attribID,
                    @DefaultValue("0") @QueryParam("fromRecord") int fromRecord, @DefaultValue("100") @QueryParam("limit") int limit) {
        List<TrainingDataDTO> trainingDataList = new ArrayList();
        ResponseWrapper response = new ResponseWrapper();
        try {
            trainingDataList = miscEJB.getTraningDataByCrisisAndAttribute(crisisID, attribID, fromRecord, limit);
            response.setTrainingData(trainingDataList);
        } catch (RuntimeException e) {
            return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
        }
        return Response.ok(response).build();
    }

}