package com.sekraft.entityconnect.core.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import com.sekraft.entityconnect.core.interfaces.IdGenerationStrategy;
import com.sekraft.entityconnect.core.interfaces.UuidAsPrimaryKey;

import java.util.UUID;

public class UuidIdGenerationStrategy implements IdGenerationStrategy
{
    @Override
    public boolean supports(Object entity)
    {
        return entity instanceof UuidAsPrimaryKey;
    }

    @Override
    public String generateId(SharedSessionContractImplementor session, Object entity)
    {
        return UUID.randomUUID().toString();
    }
}