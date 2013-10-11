package com.google.gwt.sample.stockwatcher.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
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
	
	
	private static final int STOCK_NAME_COLUMN_IDX = 0;
	private static final int PRICE_COLUMN_IDX = 1;
	private static final int CHANGE_PRICE_COLUMN_IDX = 2;
	private static final int REMOTE_BUTTON_COLUMN_IDX = 3;
	
	private static final int REFLESH_INTERVAL_MS = 5000; //mill second
	
	//TODO これはテーブルに保持させたい。モデルと部品を同時に操作しなけれいけないのは面倒
	private List<String> stocks = new ArrayList<String>();
	
	public void onModuleLoad() {
		initStockTable();
		assemblePanel();
		initHandler();
		initTimer();
	}

	

	private void refleshWatchList(){
		final BigDecimal MAX_PRICE = BigDecimal.valueOf(100);
		final BigDecimal MAX_PRICE_CHANGE_RATE = new BigDecimal("0.02");
		
		List<StockPrice> prices = new ArrayList<StockPrice>();
		for (String stock : stocks) {
			BigDecimal price = BigDecimal.valueOf(Random.nextDouble()).multiply(MAX_PRICE);
			BigDecimal change = price.multiply(MAX_PRICE_CHANGE_RATE).multiply(BigDecimal.valueOf(Random.nextDouble() * 2.0 - 1.0));
			
			StockPrice stockPrice = new StockPrice(stock,price,change);
			prices.add(stockPrice);
		}
		
		updateTables(prices);
	}
	
	private void updateTables(List<StockPrice> prices) {
		for (StockPrice stockPrice : prices) {
			if(!stocks.contains(stockPrice.getSymbol())){
				return;
			}
			
			int row = stocks.indexOf(stockPrice.getSymbol()) + 1;
			
			String priceText = NumberFormat.getFormat("#,##0.00").format(stockPrice.getPrice());
			NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
			String changeText = changeFormat.format(stockPrice.getChange());
			String changePerText = changeFormat.format(stockPrice.getChangePercent());
			
			stockFlexTable.setText(row, PRICE_COLUMN_IDX, priceText);
			stockFlexTable.setText(row, CHANGE_PRICE_COLUMN_IDX, changeText+ "(" + changePerText + "%)");
		}
		
		//更新日時を設定
		 lastUpdatedLabel.setText("Last update : "
			        + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
	}

	private void initStockTable() {
		//カラム初期化
		stockFlexTable.setText(0, STOCK_NAME_COLUMN_IDX, "Symbol");
		stockFlexTable.setText(0, PRICE_COLUMN_IDX, "Price");
		stockFlexTable.setText(0, CHANGE_PRICE_COLUMN_IDX, "Change");
		stockFlexTable.setText(0, REMOTE_BUTTON_COLUMN_IDX, "Remove");
		//スタイル適用
		//ヘッダ行。テーブル全体のものよりも優先される？
		stockFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		//テーブル全体
		stockFlexTable.addStyleName("watchList");
		//ヘッダ行スタイル適用
		setRowStyle(0);
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
	
	private void initTimer() {
		Timer refleshTimer = new Timer() {
			@Override
			public void run() {
				refleshWatchList();
			}
		};
		refleshTimer.scheduleRepeating(REFLESH_INTERVAL_MS);
	}

	private boolean validateAddStock(String symbol){
		if(!symbol.matches("^[0-9A-Z\\.]{1,10}$")){
			Window.alert("'" + symbol + "' is not a valid symbol.");
			return false;
		}
		if(stocks.contains(symbol)){
			Window.alert("'" + symbol + "' exists.");
			return false;
		}
		return true;
	}
	
	private void addStock(){
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();

		if(!validateAddStock(symbol)){
			return;
		}
		addStockRow(symbol);
		//価格更新
		refleshWatchList();
		
		newSymbolTextBox.setFocus(true);
		newSymbolTextBox.setText("");
	}


	private void addStockRow(final String symbol) {
		int row = stockFlexTable.getRowCount();
		stocks.add(symbol);
		//新しいセルの追加。リサイズは自動で行われる
		stockFlexTable.setText(row, STOCK_NAME_COLUMN_IDX, symbol);
		
		//削除ボタンの追加
		Button removeStockButton = new Button("x");
		//依存スタイル設定
		removeStockButton.setStyleDependentName("remove", true);
		//アクション
		removeStockButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int removeIndex = stocks.indexOf(symbol);
				stocks.remove(removeIndex);
				stockFlexTable.removeRow(removeIndex+1);
			}
		});
		stockFlexTable.setWidget(row, REMOTE_BUTTON_COLUMN_IDX, removeStockButton);
		//行スタイル適用
		setRowStyle(row);
	}
	
	
	private void setRowStyle(int rowIndex){
		//スタイルの適用
		// 数字列用スタイル。 XXX 列単位では適用できない？
		stockFlexTable.getCellFormatter().addStyleName(rowIndex, PRICE_COLUMN_IDX, "watchListNumericColumn");
		stockFlexTable.getCellFormatter().addStyleName(rowIndex, CHANGE_PRICE_COLUMN_IDX, "watchListNumericColumn");
		//削除ボタン列
		stockFlexTable.getCellFormatter().addStyleName(rowIndex, REMOTE_BUTTON_COLUMN_IDX, "watchListRemoveColumn");
	}
}
