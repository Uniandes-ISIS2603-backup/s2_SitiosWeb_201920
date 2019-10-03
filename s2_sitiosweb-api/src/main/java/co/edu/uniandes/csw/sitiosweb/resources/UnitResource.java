/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.sitiosweb.resources;

import co.edu.uniandes.csw.sitiosweb.dtos.UnitDTO;
import co.edu.uniandes.csw.sitiosweb.ejb.UnitLogic;
import co.edu.uniandes.csw.sitiosweb.entities.UnitEntity;
import co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException;
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
 * @author Daniel del Castillo A.
 */
@Path("units")
@Consumes("application/json")
@Produces("application/json")
@RequestScoped
public class UnitResource 
{
    // Attributes
    
    /**
     * The unit's logger.
     */
    private static final Logger LOGGER = Logger.getLogger(UnitResource.class.getName());
    
    /**
     * The unit's logic.
     */
    @Inject
    private UnitLogic unitLogic;
    
    // Methods
    
    /**
     * Creates and returns a new unit with the given one.
     * @param unit The unit to create.
     * @return The created unit.
     * @throws BusinessLogicException Logic error when the unit couldn't be created.
     */
    @POST
    public UnitDTO createResource(UnitDTO unit) throws BusinessLogicException
    {   
        LOGGER.log(Level.INFO, "UnitResource: input: {0}.", unit);
        UnitDTO unitDTO = new UnitDTO(unitLogic.createUnit(unit.toEntity()));
        LOGGER.log(Level.INFO, "UnitResource: output: {0}.", unitDTO);
        return unitDTO;
    }
    
    @GET
    public List<UnitDTO> getUnits()
    {
        LOGGER.log(Level.INFO, "UnitResource getUnits: input: void.");
        List<UnitDTO> listUnits = listEntityToDTO(unitLogic.getUnits());
        LOGGER.log(Level.INFO, "UnitResource getUnits: output: {0}.", listUnits);
        return listUnits;
    }
    
    /**
     * Finds and returns the unit with the given id.
     * @param unitId The id of the unit.
     * @return JSON {@Link UnitDTO} the unit, if it exists.
     * @throws WebApplicationException {@Link WebApplicationExceptionMapper}
     * Logic error that generates when the unit can't be found.
     */
    @GET
    @Path("{unitId: \\d+}")
    public UnitDTO getUnit(@PathParam("unitId") Long unitId) throws WebApplicationException
    {
        LOGGER.log(Level.INFO, "UnitResource getUnit: input: {0}.", unitId);
        UnitEntity unitEntity = unitLogic.getUnit(unitId);
        if(unitEntity == null)
            throw new WebApplicationException("The resource /units/" + unitId + " doesn't exist.", 404);
        UnitDTO unitDTO = new UnitDTO(unitEntity);
        LOGGER.log(Level.INFO, "UnitResource getUnit: output: {0}.", unitDTO);
        return unitDTO;
    }
    
    /**
     * Finds and updates the unit with the given id for the given unit.
     * @param unitId The id of the unit to update.
     * @param unit The unit with the new information.
     * @return JSON {@Link UnitDTO} The updated unit, if it exists.
     * @throws WebApplicationException {@Link WebApplicationExceptionMapper}
     * Logic error that generates when the unit can't be found.
     */
    @PUT
    @Path("{unitId: \\d+}")
    public UnitDTO updateUnit(@PathParam("unitId") Long unitId, UnitDTO unit) throws WebApplicationException
    {
        LOGGER.log(Level.INFO, "UnitResource updateUnit: input: unitId: {0}, unit: {1}.", new Object[]{unitId, unit});
        unit.setId(unitId);
        if(unitLogic.getUnit(unitId) == null)
            throw new WebApplicationException("The resource /units/" + unitId + " doesn't exist.", 404);
        UnitDTO unitDTO = new UnitDTO(unitLogic.updateUnit(unitId, unit.toEntity()));
        LOGGER.log(Level.INFO, "UnitResource updateUnit: output: {0}.", unitDTO);
        return unitDTO;
    }
    
    /**
     * Finds and deletes the unit with the given id.
     * @param unitId The unit to delete.
     * @throws BusinessLogicException Logic error when the unit couldn't be deleted.
     * @throws WebApplicationException {@Link WebApplicationExceptionMapper}
     * Logic error that generates when the unit can't be found.
     */
    @DELETE
    @Path("{unitId: \\d+}")
    public void deleteUnit(@PathParam("unitId") Long unitId) throws BusinessLogicException, WebApplicationException
    {
        LOGGER.log(Level.INFO, "UnitResource deleteUnit: input: {0}.", unitId);
        if(unitLogic.getUnit(unitId) == null)
            throw new WebApplicationException("The resource /units/" + unitId + " doesn't exist.", 404);
        unitLogic.deleteUnit(unitId);
        LOGGER.log(Level.INFO, "UnitResource deleteUnit: output: void.");
    }
    
    // Auxiliar methods
    
    /**
     * Transforms a list of unit entity objects into a list of unit DTO objects.
     * @param entityList The list of unit entities.
     * @return The list of unit DTOs.
     */
    private List<UnitDTO> listEntityToDTO(List<UnitEntity> entityList)
    {
        List<UnitDTO> list = new ArrayList<>();
        for(UnitEntity entity : entityList)
            list.add(new UnitDTO(entity));
        return list;
    }
}
