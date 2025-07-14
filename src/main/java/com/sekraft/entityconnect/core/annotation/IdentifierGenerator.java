package com.sekraft.entityconnect.core.annotation;

import org.hibernate.annotations.IdGeneratorType;

import com.sekraft.entityconnect.core.generator.IdGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to specify a custom identifier generator for an entity field.
 * <p>
 *      This annotation should be applied to fields that require a specific strategy
 *      for generating unique identifiers. The generator type is defined by the
 *      {@link IdGenerator} class, as specified by the {@link IdGeneratorType} annotation.
 * </p>
 *
 * <p>
 *    Usage example:
 *      <pre>
 *          &#64;IdentifierGenerator
 *          private Long id;
 *      </pre>
 * </p>
 *
 * @author Ashwani Singh
 * @version 1.0.0
 * @since 2025-02-26
 * @see IdGenerator
 * @see IdGeneratorType
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@IdGeneratorType(IdGenerator.class)
public @interface IdentifierGenerator 
{

}