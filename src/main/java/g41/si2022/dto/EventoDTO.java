package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

import lombok.Data;

@Setter @Getter @Data
public class EventoDTO {

    private String
        id,
        curso_id,
        loc,
        fecha,
        horaIni,
        duracion,
        horaFin;

    public EventoDTO() {}

    public EventoDTO(String loc, String fecha, String horaIni, String horaFin) {
        setLoc(loc);
        setFecha(fecha);
        setHoraIni(horaIni);
        setHoraFin(horaFin);
    }

    private String calcularDuracionFromFin(String horaFin) {
        return String.valueOf(LocalTime.parse(horaFin).minusMinutes(LocalTime.parse(horaIni).getMinute()).getMinute());
    }

    private String calcularFinFromDuracion(String duracion) {
        return LocalTime.parse(horaIni).plusMinutes(Integer.parseInt(duracion)).toString();
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
        if(horaIni != null) this.horaFin = calcularFinFromDuracion(duracion);
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
        if(horaIni != null) this.duracion = calcularDuracionFromFin(horaFin);
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
        if(duracion != null) this.horaFin = calcularFinFromDuracion(duracion);
        else if(horaFin != null) this.duracion = calcularDuracionFromFin(horaFin);
    }
}
