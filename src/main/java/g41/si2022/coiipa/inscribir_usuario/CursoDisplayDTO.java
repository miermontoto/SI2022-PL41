package g41.si2022.coiipa.inscribir_usuario;

public class CursoDisplayDTO {
    private int id;
    private String nombre;
    private int plazas;
    private String start_inscr;
    private String end_inscr;

    public CursoDisplayDTO() { }

    public CursoDisplayDTO(int id, String nombre, int plazas, String start_inscr, String end_inscr) {
        this.id = id;
        this.nombre = nombre;
        this.plazas = plazas;
        this.start_inscr = start_inscr;
        this.end_inscr = end_inscr;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPlazas() {
        return plazas;
    }

    public String getStart_inscr() {
        return start_inscr;
    }

    public String getEnd_inscr() {
        return end_inscr;
    }
}
