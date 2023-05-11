package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter @Setter @Data
public class DocenciaDTO implements DTO {

    private String
        id,
        nombre,
        apellidos,
        dni,
        fecha,
        remuneracion,
        curso_id,
        docente_id;

    public DocenciaDTO(String idCurso, String idDocente, String remuneracion) {
        this.curso_id = idCurso;
        this.docente_id = idDocente;
        this.remuneracion = remuneracion;
    }

    public DocenciaDTO() {}

    @Override
    public String toString() {
        return this.nombre + " " + this.apellidos + " (" + this.remuneracion + "€)";
    }
}
