/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.sitiosweb.test.persistence;

import co.edu.uniandes.csw.sitiosweb.entities.DeveloperEntity;
import co.edu.uniandes.csw.sitiosweb.entities.ProjectEntity;
import co.edu.uniandes.csw.sitiosweb.entities.UserEntity;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import co.edu.uniandes.csw.sitiosweb.persistence.DeveloperPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.Before;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author Nicolás Abondano nf.abondano 201812467
 */
@RunWith(Arquillian.class)
public class DeveloperPersistenceTest {

    private static final Logger LOGGER = Logger.getLogger(DeveloperPersistenceTest.class.getName());

    @Inject
    DeveloperPersistence developerPersistence;

    @PersistenceContext
    protected EntityManager em;

    @Inject
    UserTransaction utx;

    private List<DeveloperEntity> data = new ArrayList<DeveloperEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(UserEntity.class.getPackage())
                .addPackage(DeveloperEntity.class.getPackage())
                .addPackage(ProjectEntity.class.getPackage())
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
            em.joinTransaction();
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
        em.createQuery("delete from DeveloperEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();
        for (int i = 0; i < 3; i++) {

            DeveloperEntity entity = factory.manufacturePojo(DeveloperEntity.class);

            em.persist(entity);

            data.add(entity);
        }
    }

    /**
     * Prueba para crear un Developer.
     */
    @Test
    public void createTest() {
        PodamFactory factory = new PodamFactoryImpl();
        DeveloperEntity developer = factory.manufacturePojo(DeveloperEntity.class);
        DeveloperEntity result = developerPersistence.create(developer);
        Assert.assertNotNull(result);

        DeveloperEntity entity = em.find(DeveloperEntity.class, result.getId());
        Assert.assertEquals(developer.getName(), entity.getName());
        Assert.assertEquals(developer.getLogin(), entity.getLogin());
        Assert.assertEquals(developer.getEmail(), entity.getEmail());
        Assert.assertEquals(developer.getPhone(), entity.getPhone());
        Assert.assertEquals(developer.getLeader(), entity.getLeader());
    }

    /**
     * Prueba para consultar la lista de Developers.
     */
    @Test
    public void getDevelopersTest() {
        List<DeveloperEntity> list = developerPersistence.findAll();
        Assert.assertEquals(data.size(), list.size());
        for (DeveloperEntity ent : list) {
            boolean found = false;
            for (DeveloperEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
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
        DeveloperEntity newEntity = developerPersistence.find(entity.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getName(), newEntity.getName());
        Assert.assertEquals(entity.getLogin(), newEntity.getLogin());
        Assert.assertEquals(entity.getEmail(), newEntity.getEmail());
        Assert.assertEquals(entity.getPhone(), newEntity.getPhone());
        Assert.assertEquals(entity.getLeader(), newEntity.getLeader());

    }

    /**
     * Prueba para actualizar un Developer.
     */
    @Test
    public void updateDeveloperTest() {
        DeveloperEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        DeveloperEntity newEntity = factory.manufacturePojo(DeveloperEntity.class);

        newEntity.setId(entity.getId());

        developerPersistence.update(newEntity);
        DeveloperEntity resp = em.find(DeveloperEntity.class, entity.getId());

        Assert.assertEquals(newEntity.getName(), resp.getName());
        Assert.assertEquals(newEntity.getLogin(), resp.getLogin());
        Assert.assertEquals(newEntity.getEmail(), resp.getEmail());
        Assert.assertEquals(newEntity.getPhone(), resp.getPhone());
        Assert.assertEquals(newEntity.getLeader(), resp.getLeader());

    }

    /**
     * Prueba para eliminar un Developer.
     */
    @Test
    public void deleteDeveloperTest() {
        DeveloperEntity entity = data.get(0);
        developerPersistence.delete(entity.getId());
        DeveloperEntity deleted = em.find(DeveloperEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

}
