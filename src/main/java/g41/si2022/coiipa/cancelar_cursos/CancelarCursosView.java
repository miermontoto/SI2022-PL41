package g41.si2022.coiipa.cancelar_cursos;

import javax.swing.JTable;

import g41.si2022.mvc.Controller;
import g41.si2022.mvc.Model;
import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import java.awt.BorderLayout;

public class CancelarCursosView extends View {

    private static final long serialVersionUID = 1L;
    private JTable tablaCursos;

    public CancelarCursosView(SwingMain main, Class<? extends Model> m, Class<? extends View> v,
            Class<? extends Controller<? extends View, ? extends Model>> c) {
        super(main, m, v, c);  
    }

    public CancelarCursosView(SwingMain main) {
        super(main, CancelarCursosModel.class, CancelarCursosView.class, CancelarCursosController.class);
    }

    @Override
    protected void initView() {
        
        this.setLayout(new BorderLayout());

        throw new UnsupportedOperationException("Unimplemented method 'initView'");
    }
    

    


}
