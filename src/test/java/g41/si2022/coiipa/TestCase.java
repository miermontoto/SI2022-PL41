package g41.si2022.coiipa;

import org.junit.Before;

/**
 * TestCase.<br>
 * <p>
 * All Test classes will interact with one of the {@link Model} and the {@link Database}.<br>
 * Instead of adding a new {@link Database} to each of the instances, this class extends the {@link Model},
 * giving access to the {@link Database} object as well.
 * </p> <p>
 * Any Test classes that extend this class will delete the database and recreate the schema before starting every single test.
 * If adding previous information to the tests (i.e. adding starting data to the DB), they may be added in the {@link #loadData()} method,
 * which has to be implemented by every extending class.
 * </p>
 * 
 * @author Alex // UO281827
 *
 */
public abstract class TestCase extends g41.si2022.mvc.Model {
	
	/**
	 * setUp. This method is called before every test.
	 * It will delete the database and run the schema.
	 * Note that this database contains no data whatsoever.
	 */
	@Before
	public void setUp () {
		this.getDatabase().deleteDatabase();
		this.getDatabase().createDatabase(false);
		this.loadData();
	}
	
	/**
	 * This method will be called after creating the schema before every test.
	 * Here, the user may if needed add some data to the database.
	 */
	public abstract void loadData();
	
}
