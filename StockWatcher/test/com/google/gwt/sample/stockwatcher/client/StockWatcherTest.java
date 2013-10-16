package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

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

	public void testServlet(){
		final GreetingServiceAsync greetingService = GWT
				.create(GreetingService.class);
		//60秒まではレスポンスを待つ(ブレークを張ってもよいように)
		delayTestFinish(60 * 1000);
		//NOTE:RequestURI=/com.google.gwt.sample.stockwatcher.StockWatcher.JUnit/greet
		greetingService.greetServer("hoge",
				new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				fail();
			}

			public void onSuccess(String result) {
				finishTest();
			}
		});
	}
}
