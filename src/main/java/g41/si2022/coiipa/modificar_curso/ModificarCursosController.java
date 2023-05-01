package g41.si2022.coiipa.modificar_curso;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

import g41.si2022.dto.CosteDTO;
import g41.si2022.dto.DocenciaDTO;
import g41.si2022.dto.SesionDTO;
import g41.si2022.mvc.*;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.components.table.RowAppendableJTable;
import g41.si2022.ui.util.Dialog;
import g41.si2022.ui.util.EventDialog;
import g41.si2022.util.Pair;
import g41.si2022.util.Util;
import g41.si2022.util.exception.ApplicationException;
import g41.si2022.util.state.CursoState;

public class ModificarCursosController extends Controller<ModificarCursosView, ModificarCursosModel> {

    private boolean dirtyInfo = false;
    private boolean dirtyCostes = false;
    private boolean dirtySesiones = false;
    private boolean dirtyProf = false;

    private LinkedList<SesionDTO> sesiones;
    private LocalDate start;
    private LocalDate end;

    public ModificarCursosController(ModificarCursosModel model, ModificarCursosView view) {
        super(view, model);
    }

    @Override
    public void initVolatileData() {
        clear();
        updateTable();
    }

    @Override
    public void initNonVolatileData() {
        getView().getTableCursos().addMouseListener(new MouseAdapter() { // NO VALE ADDLISTSELECTIONLISTENER AAAA
            @Override
            public void mouseReleased(MouseEvent evt) { handleSelect(); }
        });
        getView().getBtnRemoveSesion().addActionListener(e -> handleRemoveSession());
        getView().getBtnAddSesion().addActionListener(e -> handleAddSession());
        getView().getBtnGuardar().addActionListener(e -> handleGuardar());
        getView().getTableSesiones().getModel().addTableModelListener(e -> dirtySesiones = true);
        getView().getTablaCostes().getModel().addTableModelListener(e -> dirtyCostes = true);
        getView().getTableSesiones().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent evt) { handleSelectSesion(); }
        });

        KeyAdapter dirtyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { dirtyInfo = true; }
        };

        getView().getTxtNombre().addKeyListener(dirtyAdapter);
        getView().getTxtDescripcion().addKeyListener(dirtyAdapter);
        getView().getTxtPlazas().addKeyListener(dirtyAdapter);
    }

    private void handleSelectSesion() {
        JTable table = getView().getTableSesiones();
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        if(row == -1) return;

        getView().getBtnRemoveSesion().setEnabled(getView().getMain().getToday().isBefore(LocalDate.parse(sesiones.get(row).getFecha())));
    }

    private void handleGuardar() {
        if(dirtySesiones) getModel().updateSesiones(getInfoFromCurso(0), sesiones);

        if(dirtyInfo) {
            getModel().updateCurso(getInfoFromCurso(0), getView().getTxtNombre(), getView().getTxtDescripcion(), getView().getTxtPlazas());
        }

        if(dirtyProf) {
            // Compilar la tabla de profesores en una lista de docencias.
            List<DocenciaDTO> docencias = new ArrayList<>();
            String idCurso = getInfoFromCurso(0);
            String idDocente;
            String remuneracion;

            for(int i = 0; i < getView().getTableProfesores().getRowCount(); i++) {
                idDocente = getView().getTableProfesores().getModel().getValueAt(i, 0).toString();
                remuneracion = getView().getTableProfesores().getModel().getValueAt(i, 4).toString();
                docencias.add(new DocenciaDTO(idCurso, idDocente, remuneracion));
            }

            // Actualizar la remuneración de los profesores.
            getModel().updateDocencias(idCurso, docencias);
        }

        if(dirtyCostes) {
            // Compilar la lista de costes.
            List<CosteDTO> costes = new ArrayList<>();
            String idCurso = getInfoFromCurso(0);
            String idColectivo;
            String coste;

            for(int i = 0; i < getView().getTablaCostes().getRowCount(); i++) {
                idColectivo = getView().getTablaCostes().getModel().getValueAt(i, 0).toString();
                coste = getView().getTablaCostes().getModel().getValueAt(i, 2).toString();
                costes.add(new CosteDTO(idCurso, idColectivo, coste));
            }

            // Actualizar los costes.
            getModel().updateCostes(idCurso, costes);
        }

        // Actualizar dirty flags
        dirtyInfo = false;
        dirtyCostes = false;
        dirtySesiones = false;
        dirtyProf = false;

        // Actualizar la tabla de cursos.
        updateTable();

        // Mostrar mensaje de éxito.
        Dialog.show("Curso actualizado correctamente.");

        // Limpiar la vista.
        clear();
    }

    private boolean handleAddSession() {
        EventDialog ed;
        JTable table = getView().getTableSesiones();
        LocalDate check = getView().getMain().getToday();
        check = check.isAfter(start) ? check : start;
        if(sesiones.isEmpty()) ed = new EventDialog(check, end);
        else ed = new EventDialog(check, end, sesiones.getLast());

        if(!ed.showDialog()) return false;

        sesiones.addAll(ed.getSesiones());
        table.setModel(SwingUtil.getTableModelFromPojos(sesiones,
            new String[] {"loc", "fecha", "horaIni", "horaFin"},
            new String[] {"Localizacion", "Fecha", "Hora de inicio", "Hora de fin"},
            null
        ));

        dirtySesiones = true;
        return true;
    }

    private boolean handleRemoveSession() {
        JTable table = getView().getTableSesiones();
        int[] rows = table.getSelectedRows();
        if(rows.length == 0) return false;

        for(int i = 0; i < rows.length; i++) {
            sesiones.remove(rows[i]);
            ((DefaultTableModel) table.getModel()).removeRow(rows[i]);
            for(int j = i; j < rows.length; j++) if(rows[j] > rows[i]) rows[j]--;
        }

        dirtySesiones = true;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void updateTable() {
        JTable tabla = getView().getTableCursos();
        tabla.setModel(SwingUtil.getTableModelFromPojos(
            getModel().getListaCursos(getView().getMain().getToday()),
            new String[] { "id", "nombre", "estado", "plazas", "descripcion", "start_inscr", "end_inscr", "start", "end" },
            new String[] { "", "Nombre", "Estado", "", "", "", "", "", "" },
            null
        ));
        Util.removeColumn(tabla, 0, 2, 2, 2, 2, 2, 2);
        Util.sortTable(tabla, new Pair<>(1, SortOrder.ASCENDING), new Pair<>(0, SortOrder.ASCENDING));
        SwingUtil.autoAdjustColumns(tabla);
    }

    private String getInfoFromCurso(int col) {
        JTable tabla = getView().getTableCursos();
        TableModel model = tabla.getModel();
        return model.getValueAt(tabla.convertRowIndexToModel(tabla.getSelectedRow()), col).toString();
    }

    private void clear() {
        getView().getTxtNombre().setText("");
        getView().getTxtDescripcion().setText("");
        getView().getTxtPlazas().setText("");
        Util.emptyTable(getView().getTablaCostes());
        Util.emptyTable(getView().getTableProfesores());
        Util.emptyTable(getView().getTableSesiones());

        setControlStatus(CursoState.FINALIZADO);
    }

    @SuppressWarnings("unchecked") // Evita warnings de creación de varargs en la llamada a Util.sortTable
    private void handleSelect() {
        String cursoId = getInfoFromCurso(0);

        JTable tablaProfesores = getView().getTableProfesores();
        tablaProfesores.setModel(SwingUtil.getTableModelFromPojos(
            getModel().getListaProfesores(cursoId),
            new String[] { "id", "dni", "nombre", "apellidos", "remuneracion" },
            new String[] { "", "DNI", "Nombre", "Apellidos", "Remuneración" },
            new HashMap<Integer, Pattern> () {
                private static final long serialVersionUID = 1L;
                { put(4, Pattern.compile("\\d+(\\.\\d+)?")); }
            }
        ));
        Util.removeColumn(tablaProfesores, 0);
        Util.sortTable(tablaProfesores, new Pair<>(4, SortOrder.DESCENDING), new Pair<>(3, SortOrder.ASCENDING));
        getView().getTableProfesores().getModel().addTableModelListener(e -> dirtyProf = true);

        JTable tablaSesiones = getView().getTableSesiones();
        sesiones = new LinkedList<>(getModel().getSesionesFromCurso(cursoId));
        tablaSesiones.setModel(SwingUtil.getTableModelFromPojos(
            sesiones,
            new String[] { "id", "fecha", "horaIni", "horaFin", "loc" },
            new String[] { "", "Fecha", "Hora inicio", "Hora fin", "Localización" },
            null
        ));
        tablaSesiones.removeColumn(tablaSesiones.getColumnModel().getColumn(0));

        RowAppendableJTable tablaCostes = getView().getTablaCostes();
        tablaCostes.setData(
            getModel().getColectivosFromCurso(cursoId),
            new String[] { "id", "nombre", "coste" },
            new String[] { "id", "Nombre Colectivo", "Importe" },
            null
        );
        tablaCostes.removeColumn(tablaCostes.getColumnModel().getColumn(0));

        getView().getTxtNombre().setText(getInfoFromCurso(1));
        getView().getTxtPlazas().setText(getInfoFromCurso(3));
        getView().getTxtDescripcion().setText(getInfoFromCurso(4));

        getView().getInfoNombre().setText(getInfoFromCurso(1));
        getView().getInfoFechaInscr().setText(String.format("%s → %s", getInfoFromCurso(5), getInfoFromCurso(6)));
        getView().getInfoFechaCurso().setText(String.format("%s → %s", getInfoFromCurso(7), getInfoFromCurso(8)));

        start = LocalDate.parse(getInfoFromCurso(7));
        end = LocalDate.parse(getInfoFromCurso(8));
        setControlStatus(CursoState.valueOf(getInfoFromCurso(2)));
    }

    private void setControlStatus(CursoState status) {
        JComponent txtNombre = getView().getTxtNombre();
        JComponent txtDescripcion = getView().getTxtDescripcion();
        JComponent txtPlazas = getView().getTxtPlazas();
        JComponent tablaCostes = getView().getTablaCostes();
        JComponent btnGuardar = getView().getBtnGuardar();
        JComponent btnAddSesion = getView().getBtnAddSesion();
        JComponent btnRemoveSesion = getView().getBtnRemoveSesion();
        JComponent tablaProfesores = getView().getTableProfesores();

        txtNombre.setEnabled(true);
        txtDescripcion.setEnabled(true);
        txtPlazas.setEnabled(true);
        tablaCostes.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnAddSesion.setEnabled(true);
        btnRemoveSesion.setEnabled(true);
        tablaProfesores.setEnabled(true);

        switch(status) {
            case PLANEADO:
                break;
            case EN_INSCRIPCION:
                tablaCostes.setEnabled(getModel().checkIfCursoHasInscripciones(getInfoFromCurso(0)));
                break;
            case INSCRIPCION_CERRADA:
                tablaCostes.setEnabled(false);
                break;
            case EN_CURSO:
                tablaProfesores.setEnabled(false);
                txtPlazas.setEnabled(false);
                txtNombre.setEnabled(false);
                break;
            case CERRADO:
            case CANCELADO:
            case FINALIZADO:
                btnGuardar.setEnabled(false);
                btnAddSesion.setEnabled(false);
                btnRemoveSesion.setEnabled(false);
                txtDescripcion.setEnabled(false);
                break;
            default:
                throw new ApplicationException("Invalid status: " + status);
        }
    }

}
