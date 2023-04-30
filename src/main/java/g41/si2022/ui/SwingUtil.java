package g41.si2022.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.commons.beanutils.PropertyUtils;

import g41.si2022.ui.util.Dialog;
import g41.si2022.util.TableColumnAdjuster;
import g41.si2022.util.exception.ApplicationException;
import g41.si2022.util.exception.UnexpectedException;

/**
 * Metodos de utilidad para interfaces de usuario con swing (poblar tablas a partir de un objeto POJO
 * que ha sido obtenido desde la base de datos, manejo de excepciones para metodos del
 * controlador, autoajuste de la dimension de columnas, etc)
 */
public class SwingUtil {

	private SwingUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Ejecuta un metodo en respuesta a un sesión envolviendolo en un manejador de excepciones estandar
	 * que muestra un mensaje informativo o mensaje de error en funcion de la excepcion producida
	 * (utilizado en el Controlador al instalar los handlers en respuesta a los sesiones de swing)
	 * NOTA: Si devolviese parametros utilizar Consumer en vez de Runnable: http://www.baeldung.com/java-lambda-exceptions
	 * @param consumer Metodo a ejecutar (sin parametros de entrada ni valores de salida)
	 */
	public static void exceptionWrapper(Runnable consumer) {
		try {
			consumer.run();
		} catch (ApplicationException e) { // Excepción controlada de la que se puede recuperar la aplicación
			Dialog.showWarning(e.getMessage());
		} catch (RuntimeException e) { // Resto de excepciones, además de la ventana informativa muestra el stacktrace
			e.printStackTrace();
			Dialog.showError(e);
		}
	}

	public static void showMessage(String message, String title, int type) {
		/* Como este metodo no recibe el contexto de la ventana de la aplicación,
		 * no usa el metodo estatico showMessageDialog de JOptionPane
		 * y establece la posicion para que no aparezca en el centro de la pantalla
		 */
	    JOptionPane pane = new JOptionPane(message, type, JOptionPane.DEFAULT_OPTION);
	    pane.setOptions(new Object[] {"ACEPTAR"}); // Fija este valor para que no dependa del idioma
	    JDialog d = pane.createDialog(pane, title);
	    d.setLocation(200,200);
	    d.setVisible(true);
	}

	public static void showMessage(String message, String title) {
	    showMessage(message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static boolean passwordDialog() {
		JPasswordField jpf = new JPasswordField();
		int option = JOptionPane.showConfirmDialog(null, jpf, "Introduzca la contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			return new String(jpf.getPassword()).equals("test");
		} else return false;
	}

	/**
	 * Ajusta todas las columnas de la tabla al tamanyo correspondiente al contenido del tablemodel
	 */
	public static void autoAdjustColumns(JTable table) {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Si se usa ON, la ultima columna se expandirá en el panel
		TableColumnAdjuster tca = new TableColumnAdjuster(table);
		tca.adjustColumns();
	}

	/**
	 * Obtiene la key (primera columna) de la fila seleccionada en la tabla de carreras o string vacio (si no existe)
	 */
	public static String getSelectedKey(JTable table) {
		int row = table.getSelectedRow(); // El item de primera columna es el id de carrera
		if (row >= 0) return (String) table.getModel().getValueAt(row, 0);
		else return ""; // No hay filas seleccionadas
	}

	/**
	 * Selecciona la fila de la tabla con la clave indicada y devuelve el valor la clave de la fila seleccionada resultante
	 * (la misma clave o string vacio si no existe la fila)
	 */
	public static String selectAndGetSelectedKey(JTable table, String key) {
		for (int i=0; i<table.getModel().getRowCount(); i++)
			if (table.getModel().getValueAt(i, 0).equals(key)) {
				table.setRowSelectionInterval(i, i);
				return key;
			}
		return ""; // Ya no existe esta clave
	}

	/**
	 * Crea un tablemodel a partir de una lista de objetos POJO con las columnas que se indican.
	 * @param pojos Lista de objetos cuyos atributos se utilizaran para crear el tablemodel
	 * (utiliza apache commons beanutils). Si es null solamente crea el tablemodel con las cabeceras de columna
	 * @param colProperties Los nombres de atributo de los objetos (ordenados) que se incluiran en el tablemodel
	 * (cada uno debe disponer del correspondiente getter)
	 */
	public static <E> TableModel getTableModelFromPojos(List<E> pojos, String[] colProperties) {
		// Creación inicial del tablemodel y dimensionamiento
		// Hay que tener en cuenta que para que la tabla pueda mostrar las columnas deberá estar dentro de un JScrollPane
		TableModel tm;
		if (pojos == null) return new DefaultTableModel(colProperties, 0); //solo las columnas (p.e. para inicializaciones)
		else tm = new DefaultTableModel(colProperties, pojos.size());

		// Carga cada uno de los valores de pojos usando PropertyUtils (de apache coommons beanutils)
		for (int i = 0; i < pojos.size(); i++) {
			for (int j=0; j<colProperties.length; j++) {
				try {
					Object pojo=pojos.get(i);
					Object value=PropertyUtils.getSimpleProperty(pojo, colProperties[j]);
					tm.setValueAt(value, i, j);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new UnexpectedException(e);
				}
			}
		}
		return tm;
	}

	/**
	 * Overloaded from {@link #getTableModelFromPojos(List, String[])}.<br>
	 * <p>
	 * Recibe un {@link String[]} que corresponde a los titulos que se mostraran en la GUI para cada columna.
	 * Este array debe tener la misma longitud que <code>colProperties</code>.
	 * </p> <p>
	 * Recibe un {@link Map} que se puede utilizar para crear columnas extra que acepten datos introducidos
	 * por el usuario ({@code null} en caso de ninguna).
	 * </p> <p>
	 * Para cada columna, se le asocia un {@link Pattern}. En esa columa solo se podran escribir datos que
	 * cumplan con dicho Regex.
	 * </p>
	 *
	 * @param pojos Plan Old Java Objects a utilizar para rellenar datos.
	 * @param colProperties Propiedades de los {@code pojos} a obtener.
	 * @param colNames Nombres de las columnas.
	 * @param writeableColumns Columnas extra con permiso de escritura.
	 *
	 * @author Alex // UO281827
	 *
	 * @see #getTableModelFromPojos(List, String[])
	 */
	public static <E> TableModel getTableModelFromPojos(List<E> pojos, String[] colProperties,
			String[] colNames, java.util.Map<Integer, java.util.regex.Pattern> writeableColumns) {
		// Creación inicial del tablemodel y dimensionamiento
		// Hay que tener en cuenta que para que la tabla pueda mostrar las columnas deberá estar dentro de un JScrollPane
		TableModel tm;
		if (colNames.length != colProperties.length) throw new UnexpectedException("colProperties es distinto en tamaño a colNames");
		if (pojos == null) return new DefaultTableModel(colNames, 0); //solo las columnas (p.e. para inicializaciones)
		else tm = new DefaultTableModel(colNames, pojos.size()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void setValueAt(Object value, int row, int column) {
				if (writeableColumns == null) super.setValueAt(value, row, column);
				else if (value == null ||
						value.equals("") ||
						!writeableColumns.containsKey(column) ||
						writeableColumns.get(column).matcher(value.toString()).matches()
					) {
					super.setValueAt(value, row, column);
				}
			}

			@Override
			public boolean isCellEditable (int row, int column) {
				return writeableColumns == null ? false : writeableColumns.keySet().stream().anyMatch( x -> x == column);
			}
		};

		// Carga cada uno de los valores de pojos usando PropertyUtils (de apache coommons beanutils)
		for (int i = 0; i < pojos.size(); i++) {
			for (int j=0; j<colProperties.length; j++) {
				try {
					Object pojo=pojos.get(i);
					Object value=PropertyUtils.getSimpleProperty(pojo, colProperties[j]);
					tm.setValueAt(value, i, j);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new UnexpectedException(e);
				}
			}
		}
		return tm;
	}

	public static <E> TableModel getRecordModelFromPojo(E pojo, String[] colProperties) {
		/* Creación inicial del tablemodel y dimensionamiento
		 * Como solo habrá dos columnas pongo una cabecera con dos valores vacios, de forma que
		 * aparezca muy reducida pero con el handler necesario para poder redimensionarla.
		 */
		TableModel tm = new DefaultTableModel(new String[] {"",""}, colProperties.length);
		// Carga cada uno de los valores de pojos usando PropertyUtils (de apache commons beanutils)
			for (int j = 0; j < colProperties.length; j++) {
				try {
					tm.setValueAt(colProperties[j], j, 0);
					Object value=PropertyUtils.getSimpleProperty(pojo, colProperties[j]);
					tm.setValueAt(value, j, 1);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new UnexpectedException(e);
				}
			}
		return tm;
	}

	/**
	 * Crea un Comboboxmodel a partir de una lista de objetos.
	 * @param lst Lista de arrays de objetos de los cuales se usara el primero de cada uno de ellos para poblar el combo
	 */
	public static ComboBoxModel<Object> getComboModelFromList(List<Object[]> lst) {
		DefaultComboBoxModel<Object> cm = new DefaultComboBoxModel<>();
		for (int i = 0; i < lst.size(); i++) {
			Object value = lst.get(i)[0];
			cm.addElement(value);
		}
		return cm;
	}
}
