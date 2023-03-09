package g41.si2022.mvc;

import g41.si2022.util.db.Database;

public abstract class Model {

	private Database db = new Database();
	
	public Database getDatabase () {
		return this.db;
	}
}
