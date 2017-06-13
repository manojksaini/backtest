package com.anvi.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TradeResult {

	String type; //buy or sell
	Date tradeDate;
	Date startTime;
	Date endTime;
	float profit;
	float price;
	float target;
	float stopLoss;
	boolean autoExecute;
	
	public static DateFormat outPut = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat outPutMin = new SimpleDateFormat("HH:mm");

	public TradeResult(String type,Date tradeDate, Date startTime, Date endTime, float profit, float price, float target,
			float stopLoss, boolean autoExecute) {
		super();
		outPut.setTimeZone(TimeZone.getTimeZone("IST"));
		outPutMin.setTimeZone(TimeZone.getTimeZone("IST"));
		this.type = type;
		this.tradeDate = tradeDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.profit = profit;
		this.price = price;
		this.target = target;
		this.stopLoss = stopLoss;
		this.autoExecute = autoExecute;
	}
	

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public float getProfit() {
		return profit;
	}

	public void setProfit(float profit) {
		this.profit = profit;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getTarget() {
		return target;
	}

	public void setTarget(float target) {
		this.target = target;
	}

	public float getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(float stopLoss) {
		this.stopLoss = stopLoss;
	}

	public boolean isAutoExecute() {
		return autoExecute;
	}

	public void setAutoExecute(boolean autoExecute) {
		this.autoExecute = autoExecute;
	}
	
	public long getTradeTime(){
		return TimeUnit.MILLISECONDS.toMinutes(endTime.getTime() - startTime.getTime());
	}

	@Override
	public String toString() {
		return "TradeResult [type=" + type +",tradeDate=" + outPut.format(tradeDate) + ", startTime=" + outPutMin.format(startTime) + ", endTime=" + outPutMin.format(endTime) + ", profit="
				+ profit + ", price=" + price + ", target=" + target + ", stopLoss=" + stopLoss + ", autoExecute="
				+ autoExecute + ",TradeTime:"+getTradeTime()+"]";
	}

}
