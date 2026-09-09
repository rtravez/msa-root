
package com.rtravez.msa.exception;

import java.io.Serial;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 * 
 * @author renetravez
 * @version $1.0$
 */

public class ExceptionManager extends RuntimeException {

	@Serial
    private static final long serialVersionUID = 1L;

	public ExceptionManager() {
	}

	public ExceptionManager(String exception) {
		super(exception);
	}

	public static class NotValidFieldException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public NotValidFieldException(String info) {
			super(info);
		}
	}

	public static class NullEntityException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public NullEntityException(String info) {
			super(info);
		}
	}

	public static class EmptyFieldException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public EmptyFieldException(String info) {
			super(info);
		}
	}

	public static class NotValidFormatException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public NotValidFormatException(String info) {
			super(info);
		}
	}

	public static class DeletingException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public DeletingException(String info) {
			super(info);
		}
	}

	public static class ForeignException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public ForeignException(String info) {
			super(info);
		}
	}

	public static class GettingException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public GettingException(String info) {
			super(info);
		}
	}

	public static class FindingException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public FindingException(String info) {
			super(info);
		}
	}

	public static class ServiceUnavailableException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public ServiceUnavailableException(String info) {
			super(info);
		}
	}

	public static class BalanceNotAvailableException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public BalanceNotAvailableException(String info) {
			super(info);
		}
	}

	public static class MovementDeletionException extends ExceptionManager {
		@Serial
        private static final long serialVersionUID = 1L;

		public MovementDeletionException(String info) {
			super(info);
		}
	}

}
