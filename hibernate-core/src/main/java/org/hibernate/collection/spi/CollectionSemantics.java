/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.collection.spi;

import java.util.Iterator;
import java.util.function.Consumer;

import org.hibernate.Incubating;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.CollectionClassification;
import org.hibernate.metamodel.mapping.PluralAttributeMapping;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.spi.NavigablePath;
import org.hibernate.sql.results.graph.DomainResultCreationState;
import org.hibernate.sql.results.graph.Fetch;
import org.hibernate.sql.results.graph.FetchParent;

/**
 * Describes the semantics of a persistent collection such that Hibernate
 * understands how to use it - create one, handle elements, etc.
 *
 * @apiNote The described collection need not be part of the "Java Collection Framework"
 *
 * @param <E> the collection element or map key type
 * @param <CE> the type of the collection
 *
 * @author Steve Ebersole
 * @author Gavin King
 *
 * @since 6.0
 */
@Incubating
public interface CollectionSemantics<CE, E> {
	/**
	 * The classification handled by this semantic
	 */
	CollectionClassification getCollectionClassification();

	/**
	 * The collection's Java type
	 */
	Class<?> getCollectionJavaType();

	/**
	 * Create a raw (unwrapped) version of the collection
	 */
	CE instantiateRaw(
			int anticipatedSize,
			CollectionPersister collectionDescriptor);

	/**
	 * Create a wrapper for the collection
	 */
	PersistentCollection<E> instantiateWrapper(
			Object key,
			CollectionPersister collectionDescriptor,
			SharedSessionContractImplementor session);

	/**
	 * Wrap a raw collection in wrapper
	 */
	PersistentCollection<E> wrap(
			CE rawCollection,
			CollectionPersister collectionDescriptor,
			SharedSessionContractImplementor session);

	/**
	 * Obtain an iterator over the collection elements
	 */
	Iterator<E> getElementIterator(CE rawCollection);

	/**
	 * Visit the elements of the collection
	 */
	void visitElements(CE rawCollection, Consumer<? super E> action);

	/**
	 * Create a producer for {@link org.hibernate.sql.results.graph.collection.CollectionInitializer}
	 * instances for the given collection semantics
	 *
	 * @see InitializerProducerBuilder
	 */
	default CollectionInitializerProducer createInitializerProducer(
			NavigablePath navigablePath,
			PluralAttributeMapping attributeMapping,
			FetchParent fetchParent,
			boolean selected,
			String resultVariable,
			DomainResultCreationState creationState) {
		return createInitializerProducer(
				navigablePath, attributeMapping, fetchParent, selected, resultVariable, null, null, creationState
		);
	}

	/**
	 * Create a producer for {@link org.hibernate.sql.results.graph.collection.CollectionInitializer}
	 * instances for the given collection semantics
	 *
	 * @see InitializerProducerBuilder
	 */
	default CollectionInitializerProducer createInitializerProducer(
			NavigablePath navigablePath,
			PluralAttributeMapping attributeMapping,
			FetchParent fetchParent,
			boolean selected,
			String resultVariable,
			Fetch indexFetch,
			Fetch elementFetch,
			DomainResultCreationState creationState) {
		return InitializerProducerBuilder.createInitializerProducer(
				navigablePath,
				attributeMapping,
				getCollectionClassification(),
				fetchParent,
				selected,
				indexFetch,
				elementFetch,
				creationState
		);
	}
}
