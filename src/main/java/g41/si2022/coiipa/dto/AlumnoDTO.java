package g41.si2022.coiipa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class AlumnoDTO {

    private String
        nombre,
        apellidos,
        email,
        telefono,
        id;

    public AlumnoDTO() { }

    public AlumnoDTO(String id, String nombre, String apellidos, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    public static String getSqlQuery() { return "select * from alumno order by id asc"; }
}
