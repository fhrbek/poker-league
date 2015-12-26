package cz.fhsoft.poker.league.shared.util;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TransferrableException extends Exception implements IsSerializable {

	private static final long serialVersionUID = -5461591599823655501L;

	public TransferrableException() {
	}

	public TransferrableException(String message) {
		super(message);
	}
}
