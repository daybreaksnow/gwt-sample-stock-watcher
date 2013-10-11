package com.google.gwt.sample.stockwatcher.client;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StockPrice {
	private String symbol;
	private BigDecimal price;
	private BigDecimal change;
	
	
	
	public StockPrice(String symbol, BigDecimal price, BigDecimal change) {
		this.symbol = symbol;
		this.price = price;
		this.change = change;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getChange() {
		return change;
	}
	public void setChange(BigDecimal change) {
		this.change = change;
	}
	
	
	public BigDecimal getChangePercent() {
	    return BigDecimal.valueOf(10).multiply(change).divide(price,2,RoundingMode.HALF_UP);
	  }
}
