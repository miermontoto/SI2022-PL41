package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

import java.time.LocalTime;

@Setter @Getter @Data
public class SesionDTO implements DTO {

    private String
        id,
        curso_id,
        loc,
        fecha,
        horaIni,
        duracion,
        horaFin;

    public SesionDTO() {}

    public SesionDTO(String loc, String fecha, String horaIni, String horaFin) {
        setLoc(loc);
        setFecha(fecha);
        setHoraIni(horaIni);
        setHoraFin(horaFin);
    }

    private String calcularDuracionFromFin(String horaFin) {
        LocalTime ini = LocalTime.parse(horaIni);
        LocalTime fin = LocalTime.parse(horaFin);
        LocalTime diff = fin.minusHours(ini.getHour()).minusMinutes(ini.getMinute());
        return String.valueOf(diff.getHour() * 60 + diff.getMinute());
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

    public void setHora_ini(String horaIni) {
        setHoraIni(horaIni);
    }

    public void setHora_fin(String horaFin) {
        setHoraFin(horaFin);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof SesionDTO)) return false;
        SesionDTO other = (SesionDTO) o;
        return
            (id == null ? other.id == null : id.equals(other.id)) &&
            (curso_id == null ? other.curso_id == null : curso_id.equals(other.curso_id)) &&
            (loc == null ? other.loc == null : loc.equals(other.loc)) &&
            (fecha == null ? other.fecha == null : fecha.equals(other.fecha)) &&
            (horaIni == null ? other.horaIni == null : horaIni.equals(other.horaIni)) &&
            (duracion == null ? other.duracion == null : duracion.equals(other.duracion)) &&
            (horaFin == null ? other.horaFin == null : horaFin.equals(other.horaFin));
    }

    @Override
    public int hashCode() {
        return
            (id == null ? 0 : id.hashCode()) +
            (curso_id == null ? 0 : curso_id.hashCode()) +
            (loc == null ? 0 : loc.hashCode()) +
            (fecha == null ? 0 : fecha.hashCode()) +
            (horaIni == null ? 0 : horaIni.hashCode()) +
            (duracion == null ? 0 : duracion.hashCode()) +
            (horaFin == null ? 0 : horaFin.hashCode());
    }
}
