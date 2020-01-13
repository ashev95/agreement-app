package controller.form;


import config.property.EvaluatedCondition;
import config.valdiator.AttributeValidatorType;
import config.valdiator.JSAttributeValidator;
import entity.Client;
import service.ClientService;
import util.*;
import entity.Agreement;
import service.AgreementService;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Path("/form/agreement")
public class AgreementFormController {

    @EJB
    private AgreementService agreementService;

    @EJB
    private ClientService clientService;


    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getById(@PathParam("id") int id) throws NoSuchFieldException, IllegalAccessException {

        Agreement agreement = null;

        if (id == 0){
            agreement = new Agreement();
            agreement.setLimitDateStart(new Date());
            agreement.setConcludeDate(new Date());
        }else{
            agreement = agreementService.get(id);
        }

        return JsonBuilder.getInstance().getForm(agreement, Constant.FORM_AGREEMENT);

    }

    @Path("/calculate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject calculate(Agreement agreement) throws NoSuchFieldException, IllegalAccessException {

        AttributeValidatorUtil.getInstance().validateEntity(agreement, Constant.FORM_AGREEMENT,
                new HashSet<String>(Arrays.asList("insuranceAmount", "limitDateStart", "limitDateEnd",
                        "typeOfProperty", "yearOfConstruction", "area")));

        double areaValue = -1;

        for (EvaluatedCondition evaluatedCondition : ConfigUtil.getInstance().getAreaCondition(PropertyLoader.getInstance()).getValues()){
            HashMap<String, String> input = new HashMap<String, String>();
            input.put("0", agreement.getArea());
            JSAttributeValidator jsAttributeValidator = new JSAttributeValidator();
            jsAttributeValidator.setInput(input);
            jsAttributeValidator.setType(AttributeValidatorType.JAVASCRIPT);
            jsAttributeValidator.setScript(evaluatedCondition.getPredicate());
            jsAttributeValidator.setMessage("");
            if (jsAttributeValidator.validate()){
                areaValue = evaluatedCondition.getValue();
                break;
            }
        }

        double typeOfPropertyValue = -1;
        typeOfPropertyValue = ConfigUtil.getInstance().getTypeOfProperty(PropertyLoader.getInstance()).getValues().get(agreement.getTypeOfProperty());

        double yearOfConstructionValue = -1;

        for (EvaluatedCondition evaluatedCondition : ConfigUtil.getInstance().getYearOfConstruction(PropertyLoader.getInstance()).getValues()){
            HashMap<String, String> input = new HashMap<String, String>();
            input.put("0", agreement.getYearOfConstruction());
            JSAttributeValidator jsAttributeValidator = new JSAttributeValidator();
            jsAttributeValidator.setInput(input);
            jsAttributeValidator.setType(AttributeValidatorType.JAVASCRIPT);
            jsAttributeValidator.setScript(evaluatedCondition.getPredicate());
            jsAttributeValidator.setMessage("");
            if (jsAttributeValidator.validate()){
                yearOfConstructionValue = evaluatedCondition.getValue();
                break;
            }
        }

        int days = (int) ((agreement.getLimitDateEnd().getTime() - agreement.getLimitDateStart().getTime()) / (1000*60*60*24));

        double result = (agreement.getInsuranceAmount() / days) * areaValue * typeOfPropertyValue * yearOfConstructionValue;

        Agreement tempAgreement = new Agreement();
        try {
            tempAgreement.setSettlementDate(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            //logging
        }
        tempAgreement.setPremium(Double.toString(result));

        Set<String> attributeNames = new HashSet<String>();
        attributeNames.add("settlementDate");
        attributeNames.add("premium");

        return JsonBuilder.getInstance().getForm(tempAgreement, Constant.FORM_AGREEMENT, attributeNames);

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Agreement agreement){
        String errorText = AttributeValidatorUtil.getInstance().validateEntity(agreement, Constant.FORM_AGREEMENT);
        if (errorText != null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorText)
                    .build();
        }
        if (agreementService.getByAgreementNumber(agreement.getAgreementNumber()).size() > 0){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Договор с номером " + agreement.getAgreementNumber() + " уже существует")
                    .build();
        }
        Client refreshedClient = clientService.get(agreement.getClient().getId());
        if (refreshedClient == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Страхователь не найден")
                    .build();
        }
        agreement.setClient(refreshedClient);
        agreementService.create(agreement);
        return Response
                .status(Response.Status.CREATED)
                .entity(agreement.getId())
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Agreement agreement){
        String errorText = AttributeValidatorUtil.getInstance().validateEntity(agreement, Constant.FORM_AGREEMENT);
        if (errorText != null){
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorText)
                    .build();
        }
        List<Agreement> agreements = agreementService.getByAgreementNumber(agreement.getAgreementNumber());
        if (agreements.size() > 1 || (agreements.size() == 1 && agreements.get(0).getId() != agreement.getId())){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Договор с номером " + agreement.getAgreementNumber() + " уже существует")
                    .build();
        }
        Client refreshedClient = clientService.get(agreement.getClient().getId());
        if (refreshedClient == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Страхователь не найден")
                    .build();
        }
        agreement.setClient(refreshedClient);
        agreementService.update(agreement);
        return Response
                .status(Response.Status.OK)
                .entity(agreement.getId())
                .build();
    }

}
