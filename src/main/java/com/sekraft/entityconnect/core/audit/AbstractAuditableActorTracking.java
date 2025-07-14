package com.sekraft.entityconnect.core.audit;

import org.hibernate.envers.Audited;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

/**
 * Abstract base class for entities that require auditing of the actor (user) responsible for changes.
 * <p>
 *      This class provides a field to track the actor's identifier and hooks into the entity lifecycle
 *      to allow setting this information automatically during persistence operations.
 * </p>
 *
 * <p>
 *      Subclasses should implement logic to determine and set the current actor's ID, typically
 *      based on the application's security context.
 * </p>
 *
 * <p>
 * Fields:
 * <ul>
 *      <li>{@code actorId} - The identifier of the user or system actor responsible for creating or modifying the entity.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Entity Lifecycle:AbstractAuditableActorTracking
 *      <ul>
 *          <li>{@code onPrePersist()} - Invoked before the entity is persisted; intended to set the {@code actorId}.</li>
 *      </ul>
 * </p>
 *
 * @author Ashwani Singh
 * @version 1.0.0
 * @since 2025-02-26
 */
@Getter
@MappedSuperclass
public abstract class AbstractAuditableActorTracking extends AbstractEntityLifecycleHooks
{
    @Audited
    private String actorId;

    @PrePersist
    @Override
    protected void onPrePersist()
    {
        this.actorId = null; // Todo: Implement logic to set the actor ID based on the current user context
    }
}