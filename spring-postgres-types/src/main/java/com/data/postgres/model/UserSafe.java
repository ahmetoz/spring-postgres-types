package com.data.postgres.model;

import com.data.postgres.types.JSONBUserType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

//add class property explicitly
@TypeDef(name = "userDetailsJsonb",
        typeClass = JSONBUserType.class,
        parameters = {@Parameter(name = JSONBUserType.CLASS, value = "com.data.postgres.model.UserDetails")})
@Entity
public class UserSafe {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String code;

    @Column
    @Type(type = "userDetailsJsonb")
    private UserDetails value;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserDetails getValue() {
        return value;
    }

    public void setValue(UserDetails value) {
        this.value = value;
    }
}
