package cz.fhsoft.poker.league.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface VerificationView {
	
	public interface Presenter extends cz.fhsoft.poker.league.client.presenter.Presenter {
		void onPassword(String password);
	}

	Widget asWidget();

	void setPresenter(Presenter presenter);

	void clearForm();

	void reportWrongPassword();

}
