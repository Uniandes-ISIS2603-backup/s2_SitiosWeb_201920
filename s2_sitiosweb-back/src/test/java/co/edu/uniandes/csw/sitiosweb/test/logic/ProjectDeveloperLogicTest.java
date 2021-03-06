/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.sitiosweb.test.logic;

import co.edu.uniandes.csw.sitiosweb.ejb.DeveloperLogic;
import co.edu.uniandes.csw.sitiosweb.entities.DeveloperEntity;
import co.edu.uniandes.csw.sitiosweb.ejb.DeveloperProjectLogic;
import co.edu.uniandes.csw.sitiosweb.ejb.ProjectDeveloperLogic;
import co.edu.uniandes.csw.sitiosweb.ejb.ProjectLogic;
import co.edu.uniandes.csw.sitiosweb.entities.ProjectEntity;
import co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.sitiosweb.persistence.DeveloperPersistence;
import co.edu.uniandes.csw.sitiosweb.persistence.ProjectPersistence;

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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @developer Nicolás Abondano nf.abondano 201812467
 */
@RunWith(Arquillian.class)
public class ProjectDeveloperLogicTest {

    /**
     * Factory to generate entities
     */
    private PodamFactory factory = new PodamFactoryImpl();

    /**
     * Atribute to handle projectDeveloper`s logic
     */
    @Inject
    private ProjectDeveloperLogic projectDeveloperLogic;

    /**
     * Atribute to handle developer`s logic
     */
    @Inject
    private DeveloperLogic developerLogic;

    /**
     * Entity manager to handle persistence
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Atribute to handle transactions on DB
     */
    @Inject
    private UserTransaction utx;
    
    /**
     * Project Entity to begin test
     */
    private ProjectEntity project = new ProjectEntity();
    
    /**
     * ArrayList of Developers to store DeveloperEntities for the test
     */
    private List<DeveloperEntity> data = new ArrayList<>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ProjectEntity.class.getPackage())
                .addPackage(DeveloperEntity.class.getPackage())
                .addPackage(ProjectDeveloperLogic.class.getPackage())
                .addPackage(ProjectPersistence.class.getPackage())
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

        project = factory.manufacturePojo(ProjectEntity.class);
        project.setId(1L);
        project.setDevelopers(new ArrayList<>());
        em.persist(project);

        for (int i = 0; i < 3; i++) {
            DeveloperEntity entity = factory.manufacturePojo(DeveloperEntity.class);
            inicializeDeveloper(entity);
            entity.getProjects().add(project);
            em.persist(entity);
            data.add(entity);
            project.getDevelopers().add(entity);
        }
    }

    /**
     * Prueba para asociar un proyecto a un desarrollador.
     *
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test
    public void addDeveloperTest() throws BusinessLogicException {
        DeveloperEntity newDeveloper = factory.manufacturePojo(DeveloperEntity.class);
        inicializeDeveloper(newDeveloper);
        developerLogic.createDeveloper(newDeveloper);
        DeveloperEntity developerEntity = projectDeveloperLogic.addDeveloper(project.getId(), newDeveloper.getId());
        Assert.assertNotNull(developerEntity);

        Assert.assertEquals(developerEntity.getId(), newDeveloper.getId());
        Assert.assertEquals(developerEntity.getLogin(), newDeveloper.getLogin());
        Assert.assertEquals(developerEntity.getEmail(), newDeveloper.getEmail());
        Assert.assertEquals(developerEntity.getPhone(), newDeveloper.getPhone());
        Assert.assertEquals(developerEntity.getLeader(), newDeveloper.getLeader());

        DeveloperEntity lastDeveloper = projectDeveloperLogic.getDeveloper(project.getId(), newDeveloper.getId());

        Assert.assertEquals(lastDeveloper.getLogin(), newDeveloper.getLogin());
        Assert.assertEquals(lastDeveloper.getEmail(), newDeveloper.getEmail());
        Assert.assertEquals(lastDeveloper.getPhone(), newDeveloper.getPhone());
        Assert.assertEquals(lastDeveloper.getLeader(), newDeveloper.getLeader());

    }

    /**
     * Prueba para consultar la lista de Developers de un proyecto.
     */
    @Test
    public void getDevelopersTest() {
        List<DeveloperEntity> developerEntities = projectDeveloperLogic.getDevelopers(project.getId());

        Assert.assertEquals(data.size(), developerEntities.size());

        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue(developerEntities.contains(data.get(0)));
        }
    }

    /**
     * Prueba para cpnsultar un desarrollador de un proyecto.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test
    public void getDeveloperTest() throws BusinessLogicException {
        DeveloperEntity developerEntity = data.get(0);
        System.out.println(developerEntity.getName() + " " + developerEntity.getId());
        DeveloperEntity developer = projectDeveloperLogic.getDeveloper(project.getId(), developerEntity.getId());
        Assert.assertNotNull(developer);
        System.out.println(developer.getName() + " " + developer.getId());

        Assert.assertEquals(developerEntity.getLogin(), developer.getLogin());
        Assert.assertEquals(developerEntity.getEmail(), developer.getEmail());
        Assert.assertEquals(developerEntity.getPhone(), developer.getPhone());
        Assert.assertEquals(developerEntity.getLeader(), developer.getLeader());

    }

    /**
     * Prueba para actualizar los desarrolladors de un proyecto.
     *
     * @throws co.edu.uniandes.csw.sitiosweb.exceptions.BusinessLogicException
     */
    @Test
    public void replaceDevelopersTest() throws BusinessLogicException {
        List<DeveloperEntity> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DeveloperEntity entity = factory.manufacturePojo(DeveloperEntity.class);
            inicializeDeveloper(entity);
            entity.getProjects().add(project);
            developerLogic.createDeveloper(entity);
            nuevaLista.add(entity);
        }
        projectDeveloperLogic.replaceDevelopers(project.getId(), nuevaLista);
        List<DeveloperEntity> developerEntities = projectDeveloperLogic.getDevelopers(project.getId());
        for (DeveloperEntity aNuevaLista : nuevaLista) {
            Assert.assertTrue(developerEntities.contains(aNuevaLista));
        }
    }
    
    /**
     * Prueba desasociar un desarrollador con un proyecto.
     *
     */
    @Test
    public void removeDeveloperTest() {
        for (DeveloperEntity developer : data) {
            projectDeveloperLogic.removeDeveloper(project.getId(), developer.getId());
        }
        Assert.assertTrue(projectDeveloperLogic.getDevelopers(project.getId()).isEmpty());
    }

    /**
     * Método auxiliar para inicializar un developer para que cumpla las reglas
     * de negocio
     *
     * @param developer Desarrollador a inicializar
     */
    private void inicializeDeveloper(DeveloperEntity developer) {
        developer.setLogin("dobleSapo" + developer.getId());
        developer.setPhone("3206745567");
        developer.setLeader(false);
        developer.setProjects(new ArrayList<ProjectEntity>());
    }
}
