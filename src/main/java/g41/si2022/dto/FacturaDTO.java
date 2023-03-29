package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter @Setter @Data
public class FacturaDTO {

    private String
        id,
        remuneracion,
        fecha_introd,
        fecha_pago,
        docencia_id,
        doc_nombre,
        doc_apellidos,
        curso_nombre;

}
