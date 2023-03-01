package g41.si2022.coiipa.dto;

public class CursoInscripcionDTO {
    private String nombre;
    private String plazas;
    private String start_inscr;
    private String end_inscr;

    public CursoInscripcionDTO() { }

    public CursoInscripcionDTO(String nombre, String plazas, String start_inscr, String end_inscr) {

        this.nombre = nombre;
        this.plazas = plazas;
        this.start_inscr = start_inscr;
        this.end_inscr = end_inscr;
    }


	public String getNombre() { return this.nombre; }
    public String getPlazas() { return this.plazas; }
    public String getStart_inscr() { return this.start_inscr; }
    public String getEnd_inscr() { return this.end_inscr; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPlazas(String plazas) { this.plazas = plazas; }
    public void setStart_inscr(String start_inscr) { this.start_inscr = start_inscr; }
    public void setEnd_inscr(String end_inscr) { this.end_inscr = end_inscr; }
}
