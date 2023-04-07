package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import g41.si2022.util.state.FacturaState;
import g41.si2022.util.state.StateUtilities;
import lombok.Data;

@Getter @Setter @Data
public class FacturaDTO {

    private String
        id,
        remuneracion,
        pagado,
        fecha,
        docencia_id,
        doc_nombre,
        doc_apellidos,
        curso_nombre;

    private FacturaState estado;

    public void updateEstado() {
        this.estado = StateUtilities.getFacturaState(this);
    }

}
