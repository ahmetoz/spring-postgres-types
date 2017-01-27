package com.data.postgres.model;

import com.data.postgres.types.JSONBRawUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@TypeDef(name = "jsonbRawDefinition", typeClass = JSONBRawUserType.class)
public class UserRaw {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String code;

    @Column
    @Type(type="jsonbRawDefinition")
//    @Type(type = "com.data.postgres.types.JSONBRawUserType")
    private Object value;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
