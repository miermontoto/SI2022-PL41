package g41.si2022.dto;

import lombok.*;

@Getter @Setter @Data
public class CosteDTO {

    public CosteDTO(String idCurso, String idColectivo, String coste) {
        this.curso_id = idCurso;
        this.colectivo_id = idColectivo;
        this.coste = coste;
    }

    public CosteDTO() {}

    private String
        id,
        coste,
        curso_id,
        colectivo_id;
}
