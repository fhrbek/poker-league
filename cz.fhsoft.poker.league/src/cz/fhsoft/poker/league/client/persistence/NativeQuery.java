package cz.fhsoft.poker.league.client.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NativeQuery implements Serializable {

	private static final long serialVersionUID = 5103942421725307213L;

	private String queryString;

	private Map<Integer, Object> parameters = new HashMap<Integer, Object>();

	protected NativeQuery() {
	}

	public NativeQuery(String queryString) {
		this.queryString = queryString;
	}
	
	public void setParameter(int position, Object parameter) {
		parameters.put(position, parameter);
	}
	
	public String getQueryString() {
		return queryString;
	}

	public Object getParameter(int position) {
		return parameters.get(position);
	}

	public Map<Integer, Object> getParameters() {
		return parameters;
	}
}
