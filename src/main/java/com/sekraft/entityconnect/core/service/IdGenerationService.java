package com.sekraft.entityconnect.core.service;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import com.sekraft.entityconnect.core.interfaces.IdGenerationStrategy;

import java.util.List;

public class IdGenerationService
{
    private final List<IdGenerationStrategy> strategies;

    public IdGenerationService(List<IdGenerationStrategy> strategies)
    {
        this.strategies = strategies;
    }

    public String generate(SharedSessionContractImplementor session, Object entity) throws Exception
    {
        for (IdGenerationStrategy strategy : strategies)
            if (strategy.supports(entity))
                return strategy.generateId(session, entity);
        throw new IllegalArgumentException("No suitable strategy found for entity: " + entity.getClass().getSimpleName());
    }
}
