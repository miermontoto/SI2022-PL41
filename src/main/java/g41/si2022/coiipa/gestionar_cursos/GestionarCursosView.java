package g41.si2022.coiipa.gestionar_cursos;

import javax.swing.JTable;
import java.awt.BorderLayout;
import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;
import lombok.Getter;

@Getter
public class GestionarCursosView extends Tab {
    
    private static final long serialVersionUID = 1L;
    private JTable tablaCursos;
    
    public GestionarCursosView(SwingMain main)
    {
        super(main);
        initialize();
    }

    private void initialize()
    {
        this.setLayout(new BorderLayout());
    }

    @Override
	protected void initController() { new GestionarCursosController(new GestionarCursosModel(), this); }

}
