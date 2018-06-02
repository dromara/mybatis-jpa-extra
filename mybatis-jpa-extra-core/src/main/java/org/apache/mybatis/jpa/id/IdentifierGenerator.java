package org.apache.mybatis.jpa.id;

public interface IdentifierGenerator {
	/**
	 * The configuration parameter holding the entity name
	 */
	String ENTITY_NAME = "entity_name";

	/**
	 * The configuration parameter holding the JPA entity name
	 */
	String JPA_ENTITY_NAME = "jpa_entity_name";

	/**
	 * Used as a key to pass the name used as {@link GeneratedValue#generator()} to  the
	 * {@link IdentifierGenerator} as it is configured.
	 */
	String GENERATOR_NAME = "GENERATOR_NAME";

	/**
	 * Generate a new identifier.
	 *
	 * @param session The session from which the request originates
	 * @param object the entity or collection (idbag) for which the id is being generated
	 *
	 * @return a new identifier
	 *
	 * @throws HibernateException Indicates trouble generating the identifier
	 */
	String generate(Object object) ;
}
