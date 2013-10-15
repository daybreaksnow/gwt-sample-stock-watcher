package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.junit.client.GWTTestCase;

//GWTTestCaseを継承しなければならない
public class StockWatcherTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		//gwt.xmlの名前と合わせる
		return "com.google.gwt.sample.stockwatcher.StockWatcher";
	}
	
	public void testSimple(){
		assertTrue(true);
	}
}
