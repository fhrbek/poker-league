package cz.fhsoft.poker.league.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletInitializer implements ServletContextListener {
	
	private static EntityManagerFactory entityManagerFactory = null;

	private static EntityManager entityManager = null;

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		if(entityManager != null)
			entityManager.close();

		if(entityManagerFactory != null)
			entityManagerFactory.close();

		entityManagerFactory = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String persistenceProvider = event.getServletContext().getInitParameter("persistence.provider.class");
		String persistenceUnit = event.getServletContext().getInitParameter("persistence.unit.name");
		
		if(persistenceProvider == null)
			throw new IllegalArgumentException("Parameter 'persistence.provider.class' not defined");

		if(persistenceUnit == null)
			throw new IllegalArgumentException("Parameter 'persistence.unit.name' not defined");

		try {
			Class.forName(persistenceProvider);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unable to load persistence provider '" + persistenceProvider + "'");
		}
		
		entityManagerFactory = Persistence.createEntityManagerFactory("poker.league.data");
	}

	public static EntityManager getEntityManager() {
		if(entityManager == null)
			entityManager = entityManagerFactory.createEntityManager();

		return entityManager;
	}
}
