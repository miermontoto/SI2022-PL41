package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter @Setter @Data

public class DocenciaDTO {

    private String
        id,
        remuneracion,
        curso_id,
        docente_id;

}
