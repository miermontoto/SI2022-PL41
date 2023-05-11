package g41.si2022.coiipa;

import org.junit.Before;

public abstract class TestCase extends g41.si2022.mvc.Model {
	
	@Before
	public void setUp () {
		this.getDatabase().deleteDatabase();
		this.getDatabase().createDatabase(false);
		this.loadData();
	}
	
	public abstract void loadData();
	
}
