package com.data.postgres.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Use this class if type is not known ( like Object types on entity )
 *  also adds @class property to persisted "jsonb" object
 */
public class JSONBRawUserType implements UserType {

    //add default class name to _class property
    private static final ObjectMapper objectMapper
            = new ObjectMapper().enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, "@class");

    private static final ClassLoaderService classLoaderService = new ClassLoaderServiceImpl();

    private static final String JSONB_TYPE = "jsonb";
    private static final String CLASS = "CLASS";

    private Class jsonClassType;

    @Override
    public Class<Object> returnedClass() {
        return Object.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws
            HibernateException, SQLException {
        try {
            final String json = resultSet.getString(names[0]);
            final JsonNode root = objectMapper.readTree(json);
            final JsonNode className = root.get("@class");
            if (className == null)
                return json == null ? null : objectMapper.readValue(json, Object.class);

            final String clazz = className.asText();
            jsonClassType = classLoaderService.classForName(clazz);
            return json == null ? null : objectMapper.readValue(json, jsonClassType);
        } catch (IOException e) {
            throw new HibernateException(e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws
            HibernateException, SQLException {
        try {

            final String json = value == null ? null : objectMapper.writeValueAsString(value);

            PGobject pgo = new PGobject();
            pgo.setType(JSONB_TYPE);
            pgo.setValue(json);
            st.setObject(index, pgo);
        } catch (JsonProcessingException e) {
            throw new HibernateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object deepCopy(Object value) throws HibernateException {

        if (!(value instanceof Collection)) {
            return value;
        }

        Collection<?> collection = (Collection) value;
        Collection collectionClone = JSONBUserType.CollectionFactory.newInstance(collection.getClass());

        collectionClone.addAll(collection.stream().map(this::deepCopy).collect(Collectors.toList()));

        return collectionClone;
    }

    static final class CollectionFactory {
        @SuppressWarnings("unchecked")
        static <E, T extends Collection<E>> T newInstance(Class<T> collectionClass) {
            if (List.class.isAssignableFrom(collectionClass)) {
                return (T) new ArrayList<E>();
            } else if (Set.class.isAssignableFrom(collectionClass)) {
                return (T) new HashSet<E>();
            } else {
                throw new IllegalArgumentException("Unsupported collection type : " + collectionClass);
            }
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }

        if ((x == null) || (y == null)) {
            return false;
        }

        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        assert (x != null);
        return x.hashCode();
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object deepCopy = deepCopy(value);

        if (!(deepCopy instanceof Serializable)) {
            throw new SerializationException(String.format("%s is not serializable class", value), null);
        }

        return (Serializable) deepCopy;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
