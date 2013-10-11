package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.sample.stockwatcher.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stockFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	
	//TODO これはテーブルに保持させたい
	private List<String> stocks = new ArrayList<String>();
	
	public void onModuleLoad() {
		initStockTable();
		assemblePanel();
		initHandler();
	}


	private void initStockTable() {
		//カラム初期化
		stockFlexTable.setText(0, 0, "Symbol");
		stockFlexTable.setText(0, 1, "Price");
		stockFlexTable.setText(0, 2, "Change");
		stockFlexTable.setText(0, 3, "Remove");
	}
	
	private void assemblePanel() {
		//追加ボタンエリア
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);
		
		//ルートエリア
		mainPanel.add(stockFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);
		
		//HTMLにはめ込む
		RootPanel.get("stockList").add(mainPanel);
	}

	private void initHandler() {
		//追加ボタン
		addStockButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addStock();
			}
		});
		
		//名前入力テキストEnterキー押下時
		newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER){
					addStock();
				}
			}
		});
		
	}

	private void addStock(){
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		//validate
		if(!symbol.matches("^[0-9A-Z\\.]{1,10}$")){
			Window.alert("'" + symbol + "' is not a valid symbol.");
			newSymbolTextBox.selectAll();
			return;
		}
		if(stocks.contains(symbol)){
			Window.alert("'" + symbol + "' exists.");
			return;
		}
		//セルの追加
		int row = stockFlexTable.getRowCount();
		stocks.add(symbol);
		//新しいセルの追加。リサイズは自動で行われる
		stockFlexTable.setText(row, 0, symbol);
		//削除ボタンの追加
		Button removeStockButton = new Button("x");
		removeStockButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int removeIndex = stocks.indexOf(symbol);
				stocks.remove(removeIndex);
				stockFlexTable.removeRow(removeIndex+1);
			}
		});
		stockFlexTable.setWidget(row, 3, removeStockButton);
		
		newSymbolTextBox.setFocus(true);
		newSymbolTextBox.setText("");
	}
}
