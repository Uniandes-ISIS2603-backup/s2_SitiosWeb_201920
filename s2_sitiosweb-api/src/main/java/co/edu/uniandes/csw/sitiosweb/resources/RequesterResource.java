/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.sitiosweb.resources;

import co.edu.uniandes.csw.sitiosweb.dtos.RequesterDTO;
import co.edu.uniandes.csw.sitiosweb.dtos.RequesterDetailDTO;
import co.edu.uniandes.csw.sitiosweb.ejb.RequesterLogic;
import co.edu.uniandes.csw.sitiosweb.entities.RequesterEntity;

import co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Nicolás Abondano nf.abondano 201812467
 */
@Path("requesters")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class RequesterResource {
    private final static Logger LOGGER = Logger.getLogger(RequesterResource.class.getName());
    
    @Inject
    private RequesterLogic requesterLogic;
    
    @Dependent
    private static final String NOEXISTE = " no existe.";
    
    @Dependent
    private static final String RECURSONO = "El recurso /requesters/";
    
    @POST
    public RequesterDTO createRequester(RequesterDTO requester) throws BusinessLogicException{
        LOGGER.log(Level.INFO, "RequesterResource createRequester: input: {0}", requester);
        RequesterEntity requesterEntity = requester.toEntity();
        RequesterEntity nuevoRequesterEntity = requesterLogic.createRequester(requesterEntity);
        RequesterDTO nuevoUsuerDTO = new RequesterDTO(nuevoRequesterEntity);
        LOGGER.log(Level.INFO, "Reque\"El recurso /requesters/\"sterResource createRequester: output: {0}", nuevoUsuerDTO);
        return nuevoUsuerDTO;    
    }

    @GET
    public List<RequesterDetailDTO> getRequesters() {
        LOGGER.info("RequesterResource getRequesters: input: void");
        List<RequesterDetailDTO> listaUsuarios = listEntity2DetailDTO(requesterLogic.getRequesters());
        LOGGER.log(Level.INFO, "RequesterResource getRequesters: output: {0}", listaUsuarios);
        return listaUsuarios;
    }

    @GET
    @Path("{requestersId: \\d+}")
    public RequesterDetailDTO getRequester(@PathParam("requestersId") Long requestersId) throws WebApplicationException {
        LOGGER.log(Level.INFO, "RequesterResource getRequester: input: {0}", requestersId);
        RequesterEntity requesterEntity = requesterLogic.getRequester(requestersId);
        if (requesterEntity == null) {
            throw new WebApplicationException(RECURSONO + requestersId + NOEXISTE, 404);
        }
        RequesterDetailDTO requesterDetailDTO = new RequesterDetailDTO(requesterEntity);
        LOGGER.log(Level.INFO, "RequesterResource getRequester: output: {0}", requesterDetailDTO);
        return requesterDetailDTO;
    }
    
    @GET
    @Path("/{requestersLogin}")
    public RequesterDetailDTO getRequesterByLogin(@PathParam("requestersLogin") String requestersLogin) throws WebApplicationException {
        LOGGER.log(Level.INFO, "RequesterResource getRequester: input: {0}", requestersLogin);
        RequesterEntity requesterEntity = requesterLogic.getRequesterByLogin(requestersLogin);
        if (requesterEntity == null) {
            throw new WebApplicationException(RECURSONO + requestersLogin + NOEXISTE, 404);
        }
        RequesterDetailDTO requesterDetailDTO = new RequesterDetailDTO(requesterEntity);
        LOGGER.log(Level.INFO, "RequesterResource getRequester: output: {0}", requesterDetailDTO);
        return requesterDetailDTO;
    }

    @PUT
    @Path("{requestersId: \\d+}")
    public RequesterDTO updateRequester(@PathParam("requestersId") Long requestersId, RequesterDTO requester) throws WebApplicationException, BusinessLogicException {
        LOGGER.log(Level.INFO, "RequesterResource updateRequester: input: id:{0} , requester: {1}", new Object[]{requestersId, requester});
        requester.setId(requestersId);
        if (requesterLogic.getRequester(requestersId) == null) {
            throw new WebApplicationException(RECURSONO + requestersId + NOEXISTE, 404);
        }
        RequesterDTO requesterDTO = new RequesterDTO(requesterLogic.updateRequester(requestersId, requester.toEntity()));
        LOGGER.log(Level.INFO, "RequesterResource updateRequester: output: {0}", requesterDTO);
        return requesterDTO;
    }
    
    @DELETE
    @Path("{requestersId: \\d+}")
    public void deleteRequester(@PathParam("requestersId") Long requestersId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "RequesterResource deleteRequester: input: {0}", requestersId);
        if (requesterLogic.getRequester(requestersId) == null) {
            throw new WebApplicationException(RECURSONO + requestersId + NOEXISTE, 404);
        }
        requesterLogic.deleteRequester(requestersId);
        LOGGER.info("RequesterResource deleteRequester: output: void");
    }

    private List<RequesterDetailDTO> listEntity2DetailDTO(List<RequesterEntity> entityList) {
        List<RequesterDetailDTO> list = new ArrayList<>();
        for (RequesterEntity entity : entityList) {
            list.add(new RequesterDetailDTO(entity));
        }
        return list;
    }
}
