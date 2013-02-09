package cz.fhsoft.poker.league.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

public class VerificationViewImpl extends Composite implements VerificationView {
	
	private static VerificationUiBinder uiBinder = GWT
			.create(VerificationUiBinder.class);

	@UiTemplate("VerificationView.ui.xml")
	interface VerificationUiBinder extends UiBinder<Widget, VerificationViewImpl> {
	}

	private Presenter presenter;
	
	@UiField
	PasswordTextBox password;
	
	@UiField
	Label errorText;

	public VerificationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("password")
	void onEnterKey(KeyDownEvent event) {
		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
			presenter.onPassword(password.getText());
	}

	@UiHandler("confirmButton")
	void onConfirmButtonClicked(ClickEvent event) {
		presenter.onPassword(password.getText());
	}

	@Override
	public void clearForm() {
		password.setText("");
		errorText.setText("");
	}

	@Override
	public void reportWrongPassword() {
		password.setText("");
		errorText.setText("Toto heslo bohužel není platné, zkuste znovu.");
	}

}
