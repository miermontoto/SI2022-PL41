package g41.si2022.dto;

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

}
