package com.sekraft.entityconnect.core.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import com.sekraft.entityconnect.core.interfaces.Detail;
import com.sekraft.entityconnect.core.interfaces.IdGenerationStrategy;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

public class DbSequenceIdGenerationStrategy implements IdGenerationStrategy
{
    @Override
    public boolean supports(Object entity)
    {
        return entity instanceof Detail;
    }

    @Override
    public String generateId(SharedSessionContractImplementor session, Object entity) throws Exception
    {
        Detail detail = (Detail) entity;
        String component = detail.getComponentName();

        try (Connection conn = session.getJdbcConnectionAccess().obtainConnection();
             CallableStatement stmt = conn.prepareCall("{ call generateId(?, ?) }")) {

            stmt.setString(1, component);
            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.execute();

            return stmt.getString(2);
        }
    }
}
