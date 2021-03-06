package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class PiyoView extends Composite implements HasText {

	private static HelloViewUiBinder uiBinder = GWT
			.create(HelloViewUiBinder.class);

	interface HelloViewUiBinder extends UiBinder<Widget, PiyoView> {
	}

	public PiyoView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button button;

	public PiyoView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
		button.ensureDebugId("helloButtonId");
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("piyo!");
	}

	public void setText(String text) {
		button.setText(text);
	}

	public String getText() {
		return button.getText();
	}

}
