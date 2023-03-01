package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
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
}
