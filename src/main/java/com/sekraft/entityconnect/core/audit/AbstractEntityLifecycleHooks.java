package com.sekraft.entityconnect.core.audit;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class AbstractEntityLifecycleHooks
{
    @PrePersist
    protected void onPrePersist()
    {
        // Default no-op
    }

    @PreUpdate
    protected void onPreUpdate()
    {
        // Default no-op
    }
}