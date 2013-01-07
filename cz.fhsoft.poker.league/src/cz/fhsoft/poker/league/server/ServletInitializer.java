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
	public void contextInitialized(ServletContextEvent arg0) {
		entityManagerFactory = Persistence.createEntityManagerFactory("poker.league.data");
	}

	public static EntityManager getEntityManager() {
		if(entityManager == null)
			entityManager = entityManagerFactory.createEntityManager();

		return entityManager;
	}
}
