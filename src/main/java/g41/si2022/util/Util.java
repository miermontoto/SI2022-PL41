package g41.si2022.util;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;

import org.apache.commons.beanutils.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import g41.si2022.util.db.Database;
import g41.si2022.util.exception.ApplicationException;

/**
 * Utilidades varias con metodos generales de serializacion, conversion a csv y conversion de fechas
 */
public class Util {

	public static final String EMAIL_REGEX = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	public static String getInfoFromTable(JTable table, int colIndex) {
		return table.getModel().getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), colIndex).toString();
	}

	public static NumberFormatter getMoneyFormatter() {
		NumberFormatter formatter = new NumberFormatter(java.text.NumberFormat.getInstance());

		formatter.setValueClass(Double.class);
		formatter.setMinimum(Double.MIN_VALUE);
		formatter.setMaximum(Double.MAX_VALUE);
		formatter.setAllowsInvalid(true);
		formatter.setCommitsOnValidEdit(true);
		formatter.setFormat(null); // disable automatic formatting

		return formatter;
	}

	public static void emptyTable(JTable table) {
		while (table.getRowCount() > 0)
			((javax.swing.table.DefaultTableModel) table.getModel()).removeRow(0);
	}

	public static void removeColumn(JTable table, int... column) {
		for(int i : column)
			table.removeColumn(table.getColumnModel().getColumn(i));
	}

	@SuppressWarnings("unchecked") // es oficial, odio java
	public static void sortTable(JTable table, Pair<Integer, SortOrder>... columnPair) {
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		List<SortKey> sortKeys = new java.util.ArrayList<>();
		for (Pair<Integer, SortOrder> pair : columnPair)
			sortKeys.add(new SortKey(pair.getFirst(), pair.getSecond()));
		sorter.setSortKeys(sortKeys);
	}

	/**
	 * Returns the data contained in this table.
	 * <p>
	 * This data is represented by a List of Maps. Each entry in the list is one row,
	 * the map is a relation between the column names and the value they hold.<br>
	 * If a column's data has not been filled, {@code null} will be set in that position of the returned data structure.
	 * </p> <p>
	 * Note that this method is O(n^2) and that it will not return the last empty row (nor any empty rows if that was possible at all).
	 * </p>
	 *
	 * @return Data structure containing the data from this table.
	 */
	public static List<Map<String, String>> getData (javax.swing.JTable theTable) {
		List<Map<String, String>> out = new java.util.ArrayList<> ();
		for (int i = 0 ; i < theTable.getRowCount() ; i++) {
			out.add(new java.util.HashMap<> ());
			for (int j = 0 ; j < theTable.getColumnCount() ; j++)
				out.get(i).put(theTable.getColumnName(j),
						theTable.getValueAt(i, j).toString().trim().equals(theTable.getColumnName(j).trim()) ||
						theTable.getValueAt(i, j).toString().trim().isEmpty()
								? null
								: theTable.getValueAt(i, j).toString()
				);
		}
		return out.stream()
			.filter(row -> row.values().stream()
			.anyMatch(java.util.Objects::nonNull))
			.collect(java.util.stream.Collectors.toList());
	}

	private Util() {
	    throw new IllegalStateException("Utility class");
	}

	public static boolean verifyEmailInAlumno(Database db, String email) {
		String sql = "select count(*) from alumno where email like ?";
		return (int) db.executeQueryArray(sql, email).get(0)[0] > 0;
	}

	public static boolean verifyEmailInGrupo(Database db, String email) {
		String sql = "select count(*) from entidad where email like ?";
		return (int) db.executeQueryArray(sql, email).get(0)[0] > 0;
	}

	public static boolean verifyEmailStructure(String email) {
		return email.matches(EMAIL_REGEX);
	}

	/**
	 * Serializa una lista de objetos a formato json insertando saltos de linea entre cada elemento
	 * para facilitar la comparacion de resultados en las pruebas utilizando jackson-databind
	 * (opcionalmente permite obtene una representacion similar a csv).
	 * @param pojoList Lista de objetos a serializar
	 * @param asArray si es true codifica los diferentes campos del objeto como un array
	 * y elimina comillas para facilitar la comparacion, si es false devuelve el json completo
	 * @return el string que representa la lista serializada
	 */
	public static String serializeToJson(Class<?> pojoClass, List<?> pojoList, boolean asArray) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			if (asArray) {
		        mapper.configOverride(pojoClass).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY));
		        String value = mapper.writeValueAsString(pojoList);
		    	return value.replace("],", "],\n").replace("\"", ""); // Con saltos de linea y sin comillas
				// Otra alternativa es utilizar las clases especificas para csv que suministra Jackson (jackson-dataformat-csv)
			} else return mapper.writeValueAsString(pojoList).replace("},", "}, \n"); // Con saltos de linea
		} catch (JsonProcessingException e) {
			throw new ApplicationException(e);
		}
	}

	public static void sendEmail(String address, String subject, String content) {
		try (FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/target/" + address + ".txt")) {
			fw.write("[" + subject + ", " + LocalDateTime.now() + "] " + content + "\n");
		} catch (IOException e) { throw new ApplicationException(e); }
	}

	/**
	 * Convierte una lista de objetos a formato csv
	 * @param pojoList Lista de objetos a serializar
	 * @param fields campos de cada objeto a incluir en el csv
	 */
	public static String pojosToCsv(List<?> pojoList, String[] fields) {
		return pojosToCsv(pojoList,fields,false,",","","","");
	}

	/**
	 * Convierte una lista de objetos a formato csv con varios parametros para personalizar el aspecto
	 * @param pojoList Lista de objetos a serializar
	 * @param fields campos de cada objeto a incluir en el csv
	 * @param headers si es true incluye una primera fila con las cabeceras
	 * @param separator caracter que separa cada columna
	 * @param begin caracter a incluir al principio de cada linea
	 * @param end caracter a incluir al final de cada linea
	 * @param nullAs Texto que se incluira cuando el valor es null
	 * @return el string que representa la lista serializada en csv
	 */
	public static String pojosToCsv(List<?> pojoList, String[] fields, boolean headers, String separator, String begin, String end, String nullAs) {
		StringBuilder sb = new StringBuilder();
		if (headers) addPojoLineToCsv(sb,null,fields,separator,begin,end,nullAs);
		for (int i = 0; i < pojoList.size(); i++) {
			try {
				//utiliza Apache commons BeanUtils para obtener los atributos del objeto en un map
				Map<String, String> objectAsMap = BeanUtils.describe(pojoList.get(i));
				addPojoLineToCsv(sb,objectAsMap, fields, separator, begin, end, nullAs);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new ApplicationException(e);
			}
		}
		return sb.toString();
	}

	private static void addPojoLineToCsv(StringBuilder sb, Map<String, String> objectAsMap, String[] fields, String separator, String begin, String end, String nullAs) {
		sb.append(begin);
		for (int j = 0; j < fields.length; j++) {
			String value;
			if (objectAsMap == null) value = fields[j]; //nombre del campo si no hay map
			else //valor del campo o el especificado para null
				value = objectAsMap.get(fields[j]) == null ? nullAs : objectAsMap.get(fields[j]);
			sb.append((j == 0 ? "" : separator) + value);
		}
		sb.append(end + "\n");
	}

	/**
	 * Convierte un array bidimensional de strings a csv (usado para comparaciones del ui con AssertJ Swing)
	 */
	public static String arraysToCsv(String[][] arrays) {
		return arraysToCsv(arrays,null,",","","");
	}

	/**
	 * Convierte un array bidimensional de strings a csv permitiendo parametrizacion
	 * (usado para comparaciones del ui con AssertJ Swing y JBehave)
	 */
	public static String arraysToCsv(String[][] arrays, String[] fields, String separator, String begin, String end) {
		StringBuilder sb = new StringBuilder();
		if (fields != null) addArrayLineToCsv(sb, fields, separator, begin, end);
		for (int i = 0; i < arrays.length; i++) addArrayLineToCsv(sb, arrays[i], separator, begin, end);
		return sb.toString();
	}
	private static void addArrayLineToCsv(StringBuilder sb, String[] array, String separator, String begin, String end) {
		sb.append(begin);
		for (int j = 0; j<array.length; j++) sb.append((j == 0 ? "" : separator) + array[j]);
		sb.append(end);
		sb.append("\n");
	}

	/**
	 * Convierte fecha repesentada como un string iso a fecha java (para conversion de entradas de tipo fecha)
	 */
	public static Date isoStringToDate(String isoDateString) {
		try { return new SimpleDateFormat("yyyy-MM-dd").parse(isoDateString); }
		catch (ParseException e) { throw new ApplicationException("Formato ISO incorrecto para fecha: " + isoDateString); }
	}

	/**
	 * Convierte fecha java a un string formato iso (para display o uso en sql)
	 */
	public static String dateToIsoString(Date javaDate) {
		return new SimpleDateFormat("yyyy-MM-dd").format(javaDate);
	}

	/**
	 * Print a receipt of a payment made by cashier
	 *
	 * @param idAlumno id of the student making the payment
	 * @param nombreCompleto name and surnames of the student making the payment
	 * @param curso course for which payment is done
	 * @param importe amount paid for inscription
	 * @param fecha date of payment
	 */
	public static void printReceipt(String idAlumno, String nombreCompleto,
			String nombreCurso, String importe, String fecha) {
		try (FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/target/" + "Recibo" + idAlumno + ".txt")) {
			fw.write("\t\t\t\t\t\t\t\t" + "COMPROBANTE DE PAGO" + "\n\n");
			fw.write("Fecha de hoy: " + fecha + "\n");
			fw.write("Recibí de: " + nombreCompleto);
			fw.write("La cantidad de: " + importe + "€\n");
			fw.write("Por concepto de: Inscripción al curso " + nombreCurso);
			fw.write("\n\n");
			fw.write("Pago realizado en caja en la sede del colegio\n");
			fw.write("[COIIPA]: Colegio Oficial de Ingenieros en Informática del Principado de Asturias");
		} catch (IOException e) { throw new ApplicationException(e); }
	}
}
