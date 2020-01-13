package controller.view;

import service.AgreementService;
import util.Constant;
import util.JsonBuilder;

import javax.ejb.EJB;
import javax.json.*;
import javax.script.ScriptException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/view/agreement")
public class AgreementViewController {

    @EJB
    private AgreementService agreementService;

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getAll() throws IllegalAccessException, NoSuchFieldException, ScriptException {

        return JsonBuilder.getInstance().getView(agreementService.getAll(), Constant.VIEW_AGREEMENTS);

    }

}
