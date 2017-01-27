package com.data.postgres.dialect;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

public class PostgresTypesDialect extends PostgreSQL94Dialect {

    public PostgresTypesDialect() {
        super();
        registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}
