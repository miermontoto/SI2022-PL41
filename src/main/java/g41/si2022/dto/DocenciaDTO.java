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
        remuneracion,
        curso_id,
        docente_id;

    @Override
    public String toString() {
        return this.nombre + " " + this.apellidos + " (" + this.remuneracion + "€)";
    }
}
