package g41.si2022.util;

/**
 * Excepción producida por la aplicación antes situaciones incontroladas.
 * (excepciones al acceder a la base de datos o al utlizar metodos que declaran excepciones throwable, etc)
 */
public class UnexpectedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public UnexpectedException(Throwable e) { super(e); }
	public UnexpectedException(String s) { super(s); }
}
