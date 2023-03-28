package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class EventoDTO {

    private String
        id,
        curso_id,
        loc,
        fecha,
        hora,
        duracion;

    public EventoDTO(String loc, String fecha, String hora, String duracion) {
        this.loc = loc;
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
    }
}
