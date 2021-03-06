/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.sitiosweb.test.logic;

import co.edu.uniandes.csw.sitiosweb.ejb.DeveloperLogic;
import co.edu.uniandes.csw.sitiosweb.entities.DeveloperEntity;
import co.edu.uniandes.csw.sitiosweb.entities.ProjectEntity;
import co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.sitiosweb.persistence.DeveloperPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author Nicolás Abondano nf.abondano 201812467
 */
@RunWith(Arquillian.class)
public class DeveloperLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private DeveloperLogic developerLogic;

    @PersistenceContext
    protected EntityManager em;

    @Inject
    private UserTransaction utx;

    private List<DeveloperEntity> data = new ArrayList<DeveloperEntity>();
    private List<ProjectEntity> projects = new ArrayList<>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(DeveloperEntity.class.getPackage())
                .addPackage(ProjectEntity.class.getPackage())
                .addPackage(DeveloperLogic.class.getPackage())
                .addPackage(DeveloperPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * Configuración inicial de la prueba.
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        em.createQuery("delete from ProjectEntity").executeUpdate();
        em.createQuery("delete from DeveloperEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ProjectEntity entity = factory.manufacturePojo(ProjectEntity.class);
            em.persist(entity);
            projects.add(entity);
        }

        for (int i = 0; i < 3; i++) {
            DeveloperEntity entity = factory.manufacturePojo(DeveloperEntity.class);
            entity.setPhone("3206745567");
            entity.setLeadingProjects(new ArrayList<>());
            if(i==2){
                entity.setProjects(projects);
            }
            em.persist(entity);
            data.add(entity);
        }
        


        DeveloperEntity leader = data.get(2);
        ProjectEntity entity = factory.manufacturePojo(ProjectEntity.class);
        entity.setLeader(leader);
        em.persist(entity);
        leader.getLeadingProjects().add(entity);
    }

    /**
     * Prueba para crear un Developer.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test
    public void createDeveloper() throws BusinessLogicException {

        DeveloperEntity newEntity = factory.manufacturePojo(DeveloperEntity.class);
        newEntity.setPhone("3206745567");
        DeveloperEntity result = developerLogic.createDeveloper(newEntity);
        Assert.assertNotNull(result);

        DeveloperEntity entity = em.find(DeveloperEntity.class, result.getId());
        Assert.assertEquals(entity.getId(), result.getId());
        Assert.assertEquals(entity.getName(), result.getName());
        Assert.assertEquals(entity.getLogin(), result.getLogin());
        Assert.assertEquals(entity.getEmail(), result.getEmail());
        Assert.assertEquals(entity.getPhone(), result.getPhone());
        Assert.assertEquals(entity.getImage(), result.getImage());
        Assert.assertEquals(entity.getLeader(), result.getLeader());
    }

    /**
     * Prueba para crear un Developer con email null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createDeveloperEmailNull() throws BusinessLogicException {
        DeveloperEntity newEntity = factory.manufacturePojo(DeveloperEntity.class);
        newEntity.setEmail(null);
        DeveloperEntity result = developerLogic.createDeveloper(newEntity);
    }

    /**
     * Prueba para crear un Developer con login null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createDeveloperLoginNull() throws BusinessLogicException {
        DeveloperEntity newEntity = factory.manufacturePojo(DeveloperEntity.class);
        newEntity.setLogin(null);
        DeveloperEntity result = developerLogic.createDeveloper(newEntity);
    }

    /**
     * Prueba para crear un Developer con phone null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createDeveloperPhoneNull() throws BusinessLogicException {
        DeveloperEntity newEntity = factory.manufacturePojo(DeveloperEntity.class);
        newEntity.setPhone(null);
        DeveloperEntity result = developerLogic.createDeveloper(newEntity);
    }
    
    /**
     * Prueba para crear un Developer con name null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createDeveloperNameNull() throws BusinessLogicException {
        DeveloperEntity newEntity = factory.manufacturePojo(DeveloperEntity.class);
        newEntity.setName(null);
        DeveloperEntity result = developerLogic.createDeveloper(newEntity);
    }

    /**
     * Prueba para crear un Requester con un login ya existente.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void createDeveloperConLoginExistente() throws BusinessLogicException {
        DeveloperEntity newEntity = factory.manufacturePojo(DeveloperEntity.class);
        newEntity.setLogin(data.get(0).getLogin());
        developerLogic.createDeveloper(newEntity);
    }
    
    /**
     * Prueba para consultar la lista de Developers.
     */
    @Test
    public void getDevelopersTest() {
        List<DeveloperEntity> list = developerLogic.getDevelopers();
        Assert.assertEquals(data.size(), list.size());
        for (DeveloperEntity entity : list) {
            boolean found = false;
            for (DeveloperEntity storedEntity : data) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Prueba para consultar un Developer.
     */
    @Test
    public void getDeveloperTest() {
        DeveloperEntity entity = data.get(0);
        DeveloperEntity resultEntity = developerLogic.getDeveloper(entity.getId());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getName(), resultEntity.getName());
        Assert.assertEquals(entity.getLogin(), resultEntity.getLogin());
        Assert.assertEquals(entity.getPhone(), resultEntity.getPhone());
        Assert.assertEquals(entity.getEmail(), resultEntity.getEmail());
        Assert.assertEquals(entity.getLeader(), resultEntity.getLeader());
        Assert.assertEquals(entity.getImage(), resultEntity.getImage());
    }

    @Test
    public void getDeveloperByLoginTest(){
        DeveloperEntity entity = data.get(0);
        DeveloperEntity resultEntity = developerLogic.getDeveloperByLogin(entity.getLogin());
        Assert.assertNotNull(resultEntity);
        Assert.assertEquals(entity.getId(), resultEntity.getId());
        Assert.assertEquals(entity.getName(), resultEntity.getName());
        Assert.assertEquals(entity.getLogin(), resultEntity.getLogin());
        Assert.assertEquals(entity.getPhone(), resultEntity.getPhone());
        Assert.assertEquals(entity.getEmail(), resultEntity.getEmail());
        Assert.assertEquals(entity.getLeader(), resultEntity.getLeader());
        Assert.assertEquals(entity.getImage(), resultEntity.getImage());

    }
    /**
     * Prueba para actualizar un Developer.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test
    public void updateDeveloperTest() throws BusinessLogicException {
        DeveloperEntity entity = data.get(0);
        DeveloperEntity pojoEntity = factory.manufacturePojo(DeveloperEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setPhone("3206745567");
        developerLogic.updateDeveloper(pojoEntity.getId(), pojoEntity);
        DeveloperEntity resp = em.find(DeveloperEntity.class, entity.getId());
        Assert.assertEquals(pojoEntity.getId(), resp.getId());
        Assert.assertEquals(pojoEntity.getName(), resp.getName());
        Assert.assertEquals(pojoEntity.getLogin(), resp.getLogin());
        Assert.assertEquals(pojoEntity.getPhone(), resp.getPhone());
        Assert.assertEquals(pojoEntity.getEmail(), resp.getEmail());
        Assert.assertEquals(pojoEntity.getLeader(), resp.getLeader());
        Assert.assertEquals(pojoEntity.getImage(), resp.getImage());
    }

    /**
     * Prueba para actualizar un Developer con name null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateDeveloperConNameNullTest() throws BusinessLogicException {
        DeveloperEntity entity = data.get(0);
        DeveloperEntity pojoEntity = factory.manufacturePojo(DeveloperEntity.class);
        pojoEntity.setName(null);
        pojoEntity.setId(entity.getId());
        developerLogic.updateDeveloper(pojoEntity.getId(), pojoEntity);
    }
    
    /**
     * Prueba para actualizar un Developer con login null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateDeveloperConLoginNullTest() throws BusinessLogicException {
        DeveloperEntity entity = data.get(0);
        DeveloperEntity pojoEntity = factory.manufacturePojo(DeveloperEntity.class);
        pojoEntity.setLogin(null);
        pojoEntity.setId(entity.getId());
        developerLogic.updateDeveloper(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para actualizar un Developer con email null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateDeveloperConEmailNullest() throws BusinessLogicException {
        DeveloperEntity entity = data.get(0);
        DeveloperEntity pojoEntity = factory.manufacturePojo(DeveloperEntity.class);
        pojoEntity.setEmail(null);
        pojoEntity.setId(entity.getId());
        developerLogic.updateDeveloper(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para actualizar un Developer con phone null.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateDeveloperConPhoneNullTest() throws BusinessLogicException {
        DeveloperEntity entity = data.get(0);
        DeveloperEntity pojoEntity = factory.manufacturePojo(DeveloperEntity.class);
        pojoEntity.setPhone(null);
        pojoEntity.setId(entity.getId());
        developerLogic.updateDeveloper(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para actualizar un Developer con leader false y es lider de
     * proyectos.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void updateDeveloperConLeaderTest() throws BusinessLogicException {
        DeveloperEntity entity = data.get(2);
        DeveloperEntity pojoEntity = factory.manufacturePojo(DeveloperEntity.class);
        pojoEntity.setLeader(false);
        pojoEntity.setId(entity.getId());
        developerLogic.updateDeveloper(pojoEntity.getId(), pojoEntity);
    }
    
    /**
     * Prueba para actualizar un Developer con login existente.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void uptadteRequesterConLoginExistente() throws BusinessLogicException {
        DeveloperEntity entity = data.get(0);
        DeveloperEntity entityR = data.get(1);
        DeveloperEntity pojoEntity = factory.manufacturePojo(DeveloperEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setLogin(entityR.getLogin());
        developerLogic.updateDeveloper(pojoEntity.getId(), pojoEntity);
    }

    /**
     * Prueba para eliminar un Developer
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test
    public void deleteDeveloperTest() throws BusinessLogicException {
        DeveloperEntity entity = data.get(0);
        developerLogic.deleteDeveloper(entity.getId());
        DeveloperEntity deleted = em.find(DeveloperEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    /**
     * Prueba para eliminar un Developer asociado como lider a un proyecto
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void deleteDeveloperLeadingTest() throws BusinessLogicException {
        developerLogic.deleteDeveloper(data.get(2).getId());
    }
    
        /**
     * Prueba para eliminar un Developer asociado como lider a un proyecto
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void deleteDeveloperProjectTest() throws BusinessLogicException {
        developerLogic.deleteDeveloper(data.get(2).getId());
    }

}
