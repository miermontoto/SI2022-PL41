package g41.si2022.coiipa.retrasar_fechas;

public class RetrasarFechasModel extends g41.si2022.mvc.Model{
	
	public void retrasarFechaCurso(String fecha, String idCurso) {
		
		String sql = "	UPDATE curso SET start = ? WHERE id= ?";
		this.getDatabase().executeUpdate(sql, fecha, idCurso);
		
	}

}
