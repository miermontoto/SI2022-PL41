package g41.SI2022.util;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;

/**
 * Encapsula los datos de acceso JDBC, lectura de la configuracion
 * y scripts de base de datos para creacion y carga.
 */
public class Database extends DbUtil {
	// Localización de ficheros de configuracion y carga de bases de datos
	private static final String APP_PROPERTIES = "src/main/resources/application.properties";
	private static final String SQL_SCHEMA = "src/main/resources/schema.sql";
	private static final String SQL_LOAD = "src/main/resources/data.sql";
	// Parámetros de la base de datos leidos de application.properties (base de datos local sin usuario/password)
	private String driver;
	private String url;
	private static boolean databaseCreated = false;

	// Crea una instancia, leyendo los parametros de driver y url de application.properties
	public Database() {
		Properties prop = new Properties();
		try (FileInputStream fs = new FileInputStream(APP_PROPERTIES)) { prop.load(fs); }
		catch (IOException e) { throw new ApplicationException(e); }
		driver = prop.getProperty("datasource.driver");
		url = prop.getProperty("datasource.url");
		if (driver == null || url == null) throw new ApplicationException("Configuracion de driver y/o url no encontrada en application.properties");
		DbUtils.loadDriver(driver);
	}


	public String getUrl() {
		return url;
	}


	public boolean isCreated() {
		return databaseCreated;
	}


	/**
	 * Creación de una base de datos limpia a partir del script schema.sql en src/main/resources
	 * (si onlyOnce == true solo ejecutará el script la primera vez)
	 */
	public boolean createDatabase(boolean onlyOnce) {
		boolean create = !onlyOnce || !databaseCreated;
		if (create) {
			executeScript(SQL_SCHEMA);
			databaseCreated = true;
		}
		return create;
	}


	/**
	 * Carga de datos iniciales a partir del script data.sql en src/main/resources
	 */
	public void loadDatabase() {
		try { executeScript(SQL_LOAD); } catch (Exception e) { throw new ApplicationException(e); }
	}

}
