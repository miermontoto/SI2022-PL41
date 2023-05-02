package g41.si2022.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Setter @Getter @Data
public class GrupoDTO implements DTO {

    private String
        nombre,
        email,
        telefono,
        id;

}
