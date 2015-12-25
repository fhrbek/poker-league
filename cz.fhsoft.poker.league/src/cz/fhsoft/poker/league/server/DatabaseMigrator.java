package cz.fhsoft.poker.league.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;

import cz.fhsoft.poker.league.shared.model.v1.DataStructureVersion;

public class DatabaseMigrator {
	
	private static final int DATA_STRUCTURE_VERSION_ID = 1;

	private DatabaseMigrator() {
		// never instantiate
	}

	public static void migrateDatabase() {
		EntityManager em = ServletInitializer.getEntityManager();
		
		DataStructureVersion dataVersion = em.find(DataStructureVersion.class, DATA_STRUCTURE_VERSION_ID);
		BufferedReader is = new BufferedReader(new InputStreamReader(DatabaseMigrator.class.getResourceAsStream("/cz/fhsoft/poker/league/server/migration/versions.txt")));
		
		try {
			String version, currentVersion = dataVersion.getCurrentVersion();
			System.out.println("Current database version is " + currentVersion + ", looking for migration scripts...");
			
			while((version = is.readLine()) != null) {
				if (version.compareTo(currentVersion) > 0) {
					runMigration(em, version, dataVersion);
				}
			}
		} catch (IOException e) {
			throw new Error("Unable to read data structure version list", e);
		} finally {
			try {
				is.close();
				System.out.println("Database migration successfully completed.");
			} catch (IOException e) {
				throw new Error("Unable to close data structure version list", e);
			}
		}
	}

	private static void runMigration(EntityManager em, String version, DataStructureVersion versionInstance) {
		System.out.println("Migrating to " + version + "...");

		String scriptName = version + "_db_migrate.up.sql";

		BufferedReader is = new BufferedReader(new InputStreamReader(DatabaseMigrator.class.getResourceAsStream("/cz/fhsoft/poker/league/server/migration/" + scriptName)));
		
		try {
			String command;
			
			em.getTransaction().begin();
			while((command = is.readLine()) != null) {
				System.out.println("  Running command: " + command);
				em.createNativeQuery(command).executeUpdate();
			}
			versionInstance.setCurrentVersion(version);
			em.merge(versionInstance);
			em.flush();
			em.getTransaction().commit();
		} catch (IOException e) {
			throw new Error("Unable to apply data structure update to version " + version, e);
		} finally {
			try {
				is.close();
				System.out.println("Migration to " + version + " succeeded.");
			} catch (IOException e) {
				throw new Error("Unable to close data structure update to version" + version, e);
			}
		}
	}
}
