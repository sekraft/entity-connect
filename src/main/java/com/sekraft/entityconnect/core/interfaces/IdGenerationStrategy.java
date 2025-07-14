package com.sekraft.entityconnect.core.interfaces;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

public interface IdGenerationStrategy
{
    boolean supports(Object entity);
    
    String generateId(SharedSessionContractImplementor session, Object entity) throws Exception;
}