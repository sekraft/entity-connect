package com.sekraft.entityconnect.core.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.sekraft.entityconnect.core.service.IdGenerationService;

import java.util.Arrays;

public class IdGenerator implements IdentifierGenerator
{
    private final IdGenerationService idGenerationService;

    public IdGenerator()
    {
        this.idGenerationService = new IdGenerationService(
            Arrays.asList(
                new UuidIdGenerationStrategy(),
                new DbSequenceIdGenerationStrategy()
            )
        );
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException
    {
        try
        {
            return idGenerationService.generate(session, object);
        }
        catch (Exception e)
        {
            throw new HibernateException("Failed to generate ID", e);
        }
    }
}
