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
import g41.si2022.dto.CursoDTO;
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

    private List<CursoDTO> cursos;
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

        // Habilitar botón de eliminar solo si la sesión es en el futuro.
        getView().getBtnRemoveSesion().setEnabled(getToday().isBefore(LocalDate.parse(sesiones.get(row).getFecha())));
    }

    private void handleGuardar() {
        CursoDTO curso = getDTO();
        String idCurso = curso.getId();

        if(dirtySesiones) getModel().updateSesiones(curso, sesiones, getToday());

        if(dirtyInfo) getModel().updateCurso(curso, getView().getTxtNombre().getText(),
                getView().getTxtDescripcion().getText(), getView().getTxtPlazas().getText());

        if(dirtyProf) {
            // Compilar la tabla de profesores en una lista de docencias.
            List<DocenciaDTO> docencias = new ArrayList<>();

            String idDocente;
            String remuneracion;

            TableModel model = getView().getTableProfesores().getModel();
            for(int i = 0; i < getView().getTableProfesores().getRowCount(); i++) {
                idDocente = model.getValueAt(i, 0).toString();
                remuneracion = model.getValueAt(i, 4).toString();
                docencias.add(new DocenciaDTO(idCurso, idDocente, remuneracion));
            }

            // Actualizar la remuneración de los profesores.
            getModel().updateDocencias(idCurso, docencias);
        }

        if(dirtyCostes) {
            // Compilar la lista de costes.
            List<CosteDTO> costes = new ArrayList<>();
            String idColectivo;
            String coste;

            TableModel model = getView().getTablaCostes().getModel();
            for(int i = 0; i < getView().getTablaCostes().getRowCount(); i++) {
                idColectivo = model.getValueAt(i, 0).toString();
                coste = model.getValueAt(i, 2).toString();
                costes.add(new CosteDTO(idCurso, idColectivo, coste));
            }

            getModel().updateCostes(idCurso, costes); // Actualizar los costes.
        }

        // Actualizar dirty flags
        dirtyInfo = false;
        dirtyCostes = false;
        dirtySesiones = false;
        dirtyProf = false;

        updateTable();
        Dialog.show("Curso actualizado correctamente.");
        clear();
    }

    private boolean handleAddSession() {
        EventDialog ed;
        JTable table = getView().getTableSesiones();
        LocalDate check = getToday();
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
        cursos = getModel().getListaCursos(getToday());

        JTable tabla = getView().getTableCursos();
        tabla.setModel(SwingUtil.getTableModelFromPojos(
            cursos,
            new String[] { "nombre", "state" },
            new String[] { "Nombre", "Estado" },
            null
        ));

        Util.sortTable(tabla, new Pair<>(1, SortOrder.ASCENDING), new Pair<>(0, SortOrder.ASCENDING));
        SwingUtil.autoAdjustColumns(tabla);
    }

    private CursoDTO getDTO() {
        JTable tabla = getView().getTableCursos();
        return cursos.get(tabla.convertRowIndexToModel(tabla.getSelectedRow()));
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
        CursoDTO curso = getDTO();
        String cursoId = curso.getId();

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

        getView().getTxtNombre().setText(curso.getNombre());
        getView().getTxtPlazas().setText(curso.getPlazas());
        getView().getTxtDescripcion().setText(curso.getDescripcion());

        start = LocalDate.parse(curso.getStart());
        end = LocalDate.parse(curso.getEnd());

        getView().getInfoNombre().setText(curso.getNombre());
        getView().getInfoFechaInscr().setText(String.format("%s → %s", curso.getStart_inscr(), curso.getEnd_inscr()));
        getView().getInfoFechaCurso().setText(String.format("%s → %s", start, end));
        getView().getInfoPlazasOcupadas().setText(String.valueOf(curso.getOcupadas()));

        setControlStatus(curso.getState());
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
                tablaCostes.setEnabled(getDTO().getOcupadas().equals("0"));
                break;
            case INSCRIPCION_CERRADA:
                tablaCostes.setEnabled(false);
                break;
            case EN_CURSO:
                tablaCostes.setEnabled(false);
                tablaProfesores.setEnabled(false);
                txtPlazas.setEnabled(false);
                txtNombre.setEnabled(false);
                break;
            case CERRADO:
            case CANCELADO:
            case FINALIZADO:
                tablaCostes.setEnabled(false);
                tablaProfesores.setEnabled(false);
                txtPlazas.setEnabled(false);
                txtNombre.setEnabled(false);
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
