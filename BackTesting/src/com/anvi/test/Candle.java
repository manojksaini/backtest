package com.anvi.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Candle {
	
	private Date date;
	private float open;
	private float high;
	private float low;
	private float close;
	private long  volume;
	private int candleType;
	public static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
	
	public Candle(Date date, float open, float high, float low, float close, long volume) {
		super();
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.candleType = close > open ? 1 :0;
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
	}
	public String getDateString() {
		return sdf.format(date);
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getOpen() {
		return open;
	}
	public void setOpen(float open) {
		this.open = open;
	}
	public float getHigh() {
		return high;
	}
	public void setHigh(float high) {
		this.high = high;
	}
	public float getLow() {
		return low;
	}
	public void setLow(float low) {
		this.low = low;
	}
	public float getClose() {
		return close;
	}
	public void setClose(float close) {
		this.close = close;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	
	public int getCandleType() {
		return candleType;
	}
	public void setCandleType(int candleType) {
		this.candleType = candleType;
	}
	@Override
	public String toString() {
		return "Candle [date=" + sdf.format(date) + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close
				+ ", volume=" + volume + ", candleType=" + candleType + "]";
	}
	
	
	
	
	

}
