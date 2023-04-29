package g41.si2022.util.db;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.io.File;

import java.util.Properties;
import java.util.function.Function;

import org.apache.commons.dbutils.DbUtils;

import g41.si2022.ui.util.Dialog;
import g41.si2022.dto.DTO;
import g41.si2022.util.Pair;
import g41.si2022.util.exception.ApplicationException;

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
	private static File databaseFile = null;

	// Crea una instancia, leyendo los parametros de driver y url de application.properties
	public Database() {
		Properties prop = new Properties();
		try (FileInputStream fs = new FileInputStream(APP_PROPERTIES)) { prop.load(fs); }
		catch (IOException e) { throw new ApplicationException(e); }
		driver = prop.getProperty("datasource.driver");
		url = prop.getProperty("datasource.url");
		if (driver == null || url == null) throw new ApplicationException("Configuracion de driver y/o url no encontrada en application.properties");
		DbUtils.loadDriver(driver);
		databaseFile = new File(url.split(":")[2]);
	}

	public String getUrl() { return url; }
	public boolean exists() { return databaseFile.isFile(); }
	
	/**
	 * insertBulk. This method will insert multiple entries in one query.
	 * Note that this method's complexity is O(3*n+n^2).
	 * 
	 * @param tableName Name of the table that the data should be inserted to
	 * @param dataColumns Name of the columns that will be inserted
	 * @param data List of DTOs
	 * @param dataSuppliers Functions that will supply each value for each entry in the List of DTOs.
	 * 
	 * @return Pair containing the Query as a String and the Array of Object consisting of the values passed to the query.
	 */
	public Pair<String, Object[]> insertBulk (String tableName, String[] dataColumns, java.util.List<? extends DTO> data, 
			java.util.ArrayList<java.util.function.Function<DTO, Object>> dataSuppliers) {
		String sql = String.format("INSERT INTO %s (%s", tableName, dataColumns[0]);
		String dataFields = "(?";
		for (int i = 1 ; i < dataSuppliers.size(); i++) dataFields += ", ?";
		dataFields += ")";
		for (int i = 1 ; i < dataColumns.length ; i++) sql += String.format(", %s", dataColumns[i]);
		sql += String.format(") VALUES %s", dataFields);
		for (int i = 1 ; i < data.size(); i++) sql += String.format(", %s", dataFields);
		Object [] readyData = new Object[data.size() * dataSuppliers.size()];
		int i = 0;
		for (DTO entry : data) {
			for (Function<? super DTO, Object> sup : dataSuppliers) {
				readyData[i++] =  sup.apply(entry);
			}
		}
		this.executeUpdate(sql, readyData);
		return new Pair<String, Object[]> (sql , readyData);
	}

	/**
	 * Creación de una base de datos limpia a partir del script schema.sql en src/main/resources
	 * (si onlyOnce == true solo ejecutará el script la primera vez)
	 */
	public boolean createDatabase(boolean onlyOnce) {
		boolean create = !onlyOnce || !this.exists();
		if (create) try {executeScript(SQL_SCHEMA);} catch (NoSuchFileException e) {
			Dialog.showError("Missing " + SQL_SCHEMA);
			System.exit(1);
		}
		return create;
	}

	/**
	 * Carga de datos iniciales a partir del script data.sql en src/main/resources
	 */
	public void loadDatabase() {
		try { executeScript(SQL_LOAD); }
		catch (NoSuchFileException nsfe) {
			try {
				Process process = Runtime.getRuntime().exec("ruby src/main/resources/generator/main.rb");
				if (process.waitFor() != 0) throw new InterruptedException();
				executeScript(SQL_LOAD);
			} catch (java.io.IOException | InterruptedException ie) {
				Dialog.showWarning("No se han podido generar los datos de prueba"
					+ "\n(missing ruby or gems?)");
			}
		}
	}

	public boolean deleteDatabase() {
		if (this.exists()) return databaseFile.delete();
		return false;
	}
}
