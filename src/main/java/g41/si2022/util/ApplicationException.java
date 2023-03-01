package g41.si2022.util;

/**
 * Excepcion producida por la aplicacion antes situaciones que no deberian ocurrir pero que son controladas
 * y por tanto, la aplicacion se puede recuperar (validacion de datos, prerequisitos que no se cumplen, etc)
 */
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ApplicationException(Throwable e) { super(e); }
	public ApplicationException(String s) { super(s); }
}
