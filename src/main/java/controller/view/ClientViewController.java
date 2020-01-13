package controller.view;

import service.ClientService;
import util.Constant;
import util.JsonBuilder;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.script.ScriptException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/view/client")
public class ClientViewController {

    @EJB
    private ClientService clientService;

    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject searchByFIO(@QueryParam("lName") String lName,
                                  @QueryParam("fName") String fName,
                                  @QueryParam("pName") String pName)
            throws IllegalAccessException, NoSuchFieldException, ScriptException {

        return JsonBuilder.getInstance().getView(clientService.searchByFIO(lName, fName, pName), Constant.VIEW_CLIENTS);

    }

}
