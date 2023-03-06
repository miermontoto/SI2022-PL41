package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter @Setter @Data
public class FacturaDTO {

    private String id, remuneracion, fecha_introd, fecha_pago, docencia_id;

    public FacturaDTO() {}

    public FacturaDTO(String id, String importe, String fecha_introd, String fecha_pago, String docencia_id) {
        this.id = id;
        this.remuneracion = importe;
        this.fecha_introd = fecha_introd;
        this.fecha_pago = fecha_pago;
        this.docencia_id = docencia_id;
    }
}
