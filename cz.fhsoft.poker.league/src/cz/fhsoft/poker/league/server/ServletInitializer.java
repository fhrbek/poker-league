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
		//This is now a zombee application, not supposed to connect to the database any more...
		//entityManagerFactory = Persistence.createEntityManagerFactory("poker.league.data");
	}

	public static EntityManager getEntityManager() {
		throw new Error("This zombee application does not connect to the database any more!");
		// if(entityManager == null)
		// entityManager = entityManagerFactory.createEntityManager();
		//
		// return entityManager;
	}
}
