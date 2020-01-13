package controller.form;

import entity.Client;
import service.ClientService;
import util.AttributeValidatorUtil;
import util.Constant;
import util.JsonBuilder;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/form/client-edit")
public class ClientEditFormController {

    @EJB
    private ClientService clientService;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getById(@PathParam("id") int id) throws NoSuchFieldException, IllegalAccessException {
        Client client = clientService.get(id);
        return JsonBuilder.getInstance().getForm(client, Constant.FORM_CLIENT_EDIT);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Client client){
        String errorText = AttributeValidatorUtil.getInstance().validateEntity(client, Constant.FORM_CLIENT_EDIT);
        if (errorText != null){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorText)
                    .build();
        }
        clientService.update(client);
        return Response
                .status(Response.Status.OK)
                .entity(client.getId())
                .build();
    }

}