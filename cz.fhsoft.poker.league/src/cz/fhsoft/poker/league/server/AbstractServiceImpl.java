package cz.fhsoft.poker.league.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public abstract class AbstractServiceImpl extends RemoteServiceServlet {

	// This is a workaround for stupid iOS 6 which caches POST requests if the following header is missing
	@Override
	protected void onBeforeRequestDeserialized(String serializedRequest) {
		getThreadLocalResponse().setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	}

}
