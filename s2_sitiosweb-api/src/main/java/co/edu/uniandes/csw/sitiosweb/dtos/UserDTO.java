package co.edu.uniandes.csw.sitiosweb.dtos;

import co.edu.uniandes.csw.sitiosweb.adapters.DateAdapter;
import co.edu.uniandes.csw.sitiosweb.entities.UserEntity;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 
 * @author Nicolás Abondano nf.abondano 201812467
 */
public class UserDTO implements Serializable {

    private Long id;
    private String login;
    private String email;
    private Long phone;
    
    /**
     * Constructor por defecto
     */
    public UserDTO() {
        
    }

    /**
     * Constructor a partir de la entidad
     *
     * @param userEntity La entidad del libro
     */
    public UserDTO(UserEntity userEntity) {
        if (userEntity != null) {
            this.id = userEntity.getId();
            this.login = userEntity.getLogin();
            this.email = userEntity.getEmail();
            this.phone = userEntity.getPhone();
        }
    }

    /**
     * Método para transformar el DTO a una entidad.
     *
     * @return La entidad del libro asociado.
     */
    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(this.getId());
        userEntity.setLogin(this.getLogin());
        userEntity.setEmail(this.getEmail());
        userEntity.setPhone(this.getPhone());
        return userEntity;
    }

  /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public Long getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(Long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}