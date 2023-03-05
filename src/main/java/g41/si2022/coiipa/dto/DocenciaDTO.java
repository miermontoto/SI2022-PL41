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
    
    public DocenciaDTO() { }

    public DocenciaDTO(String id, String remuneracion, String curso_id, String docente_id) {
        this.id = id;
        this.remuneracion = remuneracion;
        this.curso_id = curso_id;
        this.docente_id = docente_id;
    }

    public static String getSqlQuery() { return "SELECT * from docencia order by id asc"; }
}