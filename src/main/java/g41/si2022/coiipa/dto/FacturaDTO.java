package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter @Setter @Data
public class FacturaDTO {

    private String id, remuneracion, fecha_introd, fecha_pago,
        docencia_id, doc_nombre, doc_apellidos, curso_nombre;

    public FacturaDTO() {}

    public FacturaDTO(String id, String remuneracion, String fecha_introd, String fecha_pago,
        String docencia_id, String doc_nombre, String doc_apellidos, String curso_nombre) {
        this.id = id;
        this.remuneracion = remuneracion;
        this.fecha_introd = fecha_introd;
        this.fecha_pago = fecha_pago;
        this.docencia_id = docencia_id;
        this.doc_nombre = doc_nombre;
        this.doc_apellidos = doc_apellidos;
        this.curso_nombre = curso_nombre;
    }
}
