package co.edu.uniandes.csw.sitiosweb.entities;
import co.edu.uniandes.csw.sitiosweb.podam.DateStrategy;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;
import uk.co.jemos.podam.common.PodamStringValue;

/**
 *
 * @author Andres Felipe Orozco Gonzalez af.orozcog 201730058
 */
@Entity
public class IterationEntity extends BaseEntity implements Serializable {
   
    /**
     * Atributo que representa el objetivo de la iteración
     */
    @PodamStringValue(length = 1)
    private String objetive;
   
    /**
     * Atributo que representa la fecha de validación
     */
    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date validationDate;
    
    /**
     * atributo que representa los cambios que se hicieron durante la iteración
     */
    @PodamStringValue(length = 1)
    private String changes;
    
    /**
     * atributo que representa la fecha de inicio de la iteración
     */
    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date beginDate;
    
    /**
     * atributo que representa la fecha final de la iteración
     */
    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date endDate;
    
    /**
     * asociación con la clase project
     */
    @PodamExclude
    @ManyToOne
    private ProjectEntity project;


    /**
     * @return the objetive
     */
    public String getObjetive() {
        return objetive;
    }

    /**
     * @param objetive the objetive to set
     */
    public void setObjetive(String objetive) {
        this.objetive = objetive;
    }

    /**
     * @return the validationDate
     */
    public Date getValidationDate() {
        return validationDate;
    }

    /**
     * @param validationDate the validationDate to set
     */
    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    /**
     * @return the changes
     */
    public String getChanges() {
        return changes;
    }

    /**
     * @param changes the changes to set
     */
    public void setChanges(String changes) {
        this.changes = changes;
    }

    /**
     * @return the beginDate
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param beginDate the beginDate to set
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the project
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }
    
  @Override
  public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IterationEntity other = (IterationEntity)o;
    return this.getId() == other.getId();
  }
  
        @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.changes);
        return hash;
    }


}
