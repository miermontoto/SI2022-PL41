package g41.si2022.ui.panels;

import g41.si2022.coiipa.estado_actividades.EstadoActividadesView;
import g41.si2022.coiipa.gestionar_facturas_empresas.GestionarFacturasEmprView;
import g41.si2022.coiipa.gestionar_facturas_profesores.GestionarFacturasProfView;
import g41.si2022.coiipa.gestionar_inscripciones.GestionarInscripcionesView;
import g41.si2022.coiipa.lista_actividades.ListaActividadesView;
import g41.si2022.coiipa.gestionar_lista_espera.GestionarListaEsperaView;
import g41.si2022.ui.SwingMain;

public class TabsSecretaria extends TabbedFrame {

    public TabsSecretaria(SwingMain main) {
        super(main);

        tabs.put("Gestionar inscripciones", new GestionarInscripcionesView(main));
        tabs.put("Estado de actividades de formación", new EstadoActividadesView(main));
        tabs.put("Gestionar facturas de profesores", new GestionarFacturasProfView(main));
        tabs.put("Gestionar facturas a empresas", new GestionarFacturasEmprView(main));
        tabs.put("Lista de actividades de formación", new ListaActividadesView(main));
        tabs.put("Gestionar la lista de espera", new GestionarListaEsperaView(main));

        addTabs();
    }
}
