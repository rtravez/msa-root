
package com.rtravez.msa.exception;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 * 
 * @author renetravez
 * @version $1.0$
 */

public class ExceptionManager extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExceptionManager() {
	}

	public ExceptionManager(String exception) {
		super(exception);
	}

	public static class NotValidFieldException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public NotValidFieldException(String info) {
			super(info);
		}
	}

	public static class NullEntityException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public NullEntityException(String info) {
			super(info);
		}
	}

	public static class EmptyFieldException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public EmptyFieldException(String info) {
			super(info);
		}
	}

	public static class NotValidFormatException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public NotValidFormatException(String info) {
			super(info);
		}
	}

	public static class DeletingException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public DeletingException(String info) {
			super(info);
		}
	}

	public static class ForeignException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public ForeignException(String info) {
			super(info);
		}
	}

	public static class GettingException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public GettingException(String info) {
			super(info);
		}
	}

	public static class FindingException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public FindingException(String info) {
			super(info);
		}
	}

	public static class BalanceNotAvailableException extends ExceptionManager {
		private static final long serialVersionUID = 1L;

		public BalanceNotAvailableException(String info) {
			super(info);
		}
	}

}
