/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.sitiosweb.resources;

import co.edu.uniandes.csw.sitiosweb.dtos.UserDTO;
import co.edu.uniandes.csw.sitiosweb.dtos.UserDetailDTO;
import co.edu.uniandes.csw.sitiosweb.ejb.UserLogic;
import co.edu.uniandes.csw.sitiosweb.entities.UserEntity;

import co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.sitiosweb.mappers.BusinessLogicExceptionMapper;
import co.edu.uniandes.csw.sitiosweb.mappers.WebApplicationExceptionMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Path("users")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class UserResource {
    private final static Logger LOGGER = Logger.getLogger(UserResource.class.getName());
    
    @Inject
    private UserLogic userLogic;
    
    @POST
    public UserDTO createUser(UserDTO user) throws BusinessLogicException{
        LOGGER.log(Level.INFO, "UserResource createUser: input: {0}", user);
        UserEntity userEntity = user.toEntity();
        UserEntity nuevoUserEntity = userLogic.createUser(userEntity);
        UserDTO nuevoEditorialDTO = new UserDTO(nuevoUserEntity);
        LOGGER.log(Level.INFO, "UserResource createUser: output: {0}", nuevoEditorialDTO);
        return nuevoEditorialDTO;    
    }

    @GET
    public List<UserDetailDTO> getUsers() {
        LOGGER.info("UserResource getUsers: input: void");
        List<UserDetailDTO> listaUsuarios = listEntity2DetailDTO(userLogic.getUsers());
        LOGGER.log(Level.INFO, "UserResource getUsers: output: {0}", listaUsuarios);
        return listaUsuarios;
    }

    @GET
    @Path("{usersId: \\d+}")
    public UserDetailDTO getUser(@PathParam("usersId") Long usersId) throws WebApplicationException {
        LOGGER.log(Level.INFO, "UserResource getUser: input: {0}", usersId);
        UserEntity userEntity = userLogic.getUser(usersId);
        if (userEntity == null) {
            throw new WebApplicationException("El recurso /users/" + usersId + " no existe.", 404);
        }
        UserDetailDTO detailDTO = new UserDetailDTO(userEntity);
        LOGGER.log(Level.INFO, "UserResource getUser: output: {0}", detailDTO);
        return detailDTO;
    }

    @PUT
    @Path("{usersId: \\d+}")
    public UserDetailDTO updateUser(@PathParam("usersId") Long usersId, UserDetailDTO user) throws WebApplicationException, BusinessLogicException {
        LOGGER.log(Level.INFO, "UserResource updateUser: input: id:{0} , user: {1}", new Object[]{usersId, user});
        user.setId(usersId);
        if (userLogic.getUser(usersId) == null) {
            throw new WebApplicationException("El recurso /users/" + usersId + " no existe.", 404);
        }
        UserDetailDTO detailDTO = new UserDetailDTO(userLogic.updateUser(usersId, user.toEntity()));
        LOGGER.log(Level.INFO, "UserResource updateUser: output: {0}", detailDTO);
        return detailDTO;
    }
    
    @DELETE
    @Path("{usersId: \\d+}")
    public void deleteUser(@PathParam("usersId") Long usersId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "UserResource deleteUser: input: {0}", usersId);
        if (userLogic.getUser(usersId) == null) {
            throw new WebApplicationException("El recurso /users/" + usersId + " no existe.", 404);
        }
        userLogic.deleteUser(usersId);
        LOGGER.info("UserResource deleteUser: output: void");
    }

    private List<UserDetailDTO> listEntity2DetailDTO(List<UserEntity> entityList) {
        List<UserDetailDTO> list = new ArrayList<>();
        for (UserEntity entity : entityList) {
            list.add(new UserDetailDTO(entity));
        }
        return list;
    }
}