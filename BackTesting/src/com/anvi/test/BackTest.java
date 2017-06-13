package com.anvi.test;

import java.awt.color.ProfileDataException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BackTest {

	public static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
	public static DateFormat outPut = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat outPutMin = new SimpleDateFormat("HH:mm:ss");

	public static void main(String[] args) throws IOException, NumberFormatException, JSONException, ParseException {
		// String fileName = "C:\\test\\SBI15MinChart.txt";
		String fileName = "C:\\test\\ICICI15MinChart.txt";
		outPut.setTimeZone(TimeZone.getTimeZone("IST"));
		outPutMin.setTimeZone(TimeZone.getTimeZone("IST"));
		List<Candle> candleData = getCandleData(fileName);
		int totalCandle = candleData.size();
		Date startDate = candleData.get(0).getDate();
		Date endDate = candleData.get(totalCandle - 1).getDate();
		Map<String, List<Candle>> candleDataByDays = getCandleDataByDays(candleData);
		// System.out.println("Start Date :" + outPut.format(startDate) + " ,End
		// Date:" + outPut.format(endDate)
		// + " ,Total Days :" + candleDataByDays.size() + " ,Total Candles :" +
		// totalCandle);

		getResults(candleDataByDays, 2.5f, 2.5f);
		getResults(candleDataByDays, 3.5f, 3f);
		getResults(candleDataByDays, 4f, 2.5f);
		getResults(candleDataByDays, 2.5f, 2);
		getResults(candleDataByDays, 4f, 3f);
	}

	public static void getResults(Map<String, List<Candle>> candleDataByDays, float t, float s) {
		System.out.printf("1, 2, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 1, 2, t, s));
		System.out.printf("1, 3, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 1, 3, t, s));
		System.out.printf("2, 3, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 2, 3, t, s));
		System.out.printf("2, 4, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 2, 4, t, s));
		// System.out.printf("3, 4, %f, %f,%s \n",t,s,
//		System.out.printf("0, 1, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 0, 1, t, s));
//		System.out.printf("0, 2, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 0, 2, t, s));
//		System.out.printf("0, 3, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 0, 3, t, s));
//		System.out.printf("1, 2, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 1, 2, t, s));
//		System.out.printf("1, 3, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 1, 3, t, s));
//		System.out.printf("2, 3, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 2, 3, t, s));
//		System.out.printf("3, 4, %f, %f,%s \n", t, s, processSingleCandleStrategy(candleDataByDays, 3, 4, t, s));
	}

	public static Result processSingleCandleStrategy(Map<String, List<Candle>> candleDataByDays, int decisionCandle,
			int buySellCandle, float target, float stoploss) {
		float invAmt = 100000;
		Result result = new Result("ICICI", invAmt, 2500, 200, 1);
		for (Iterator<String> iterator = candleDataByDays.keySet().iterator(); iterator.hasNext();) {
			String date = iterator.next();
			List<Candle> oneDayData = candleDataByDays.get(date);
			TradeResult tradeResult = profitLossBySingleCandle(decisionCandle, buySellCandle, target, stoploss,oneDayData);
//			TradeResult tradeResult = profitLossByMultipleCandle(decisionCandle, buySellCandle, target, stoploss,
//					oneDayData);
			// System.out.println(tradeResult);
			if (tradeResult != null) {
				result.addProfit(tradeResult);
				float profitLoss = tradeResult.getProfit() * result.lotSize - result.brokeragePerLot * result.noOfLot;
				invAmt += profitLoss;
				// System.out.println("Invetment Amt value on "+date+" is
				// "+invAmt +",Profilt loss for today is "+profitLoss);
			}
		}
		return result;
	}

	public static List<Candle> getCandleData(String fileName)
			throws IOException, NumberFormatException, JSONException, ParseException {
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		List<Candle> candleList = new ArrayList<Candle>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			JSONObject jsonObj = new JSONObject(sCurrentLine);
			JSONArray priceCandlesList = jsonObj.getJSONObject("data").getJSONArray("candles");
			for (int i = 0; i < priceCandlesList.length(); i++) {
				JSONArray candleData = priceCandlesList.getJSONArray(i);
				Candle c = new Candle(sdf.parse(candleData.get(0).toString()),
						Float.parseFloat(candleData.get(1).toString()), Float.parseFloat(candleData.get(2).toString()),
						Float.parseFloat(candleData.get(3).toString()), Float.parseFloat(candleData.get(4).toString()),
						Long.parseLong(candleData.get(5).toString()));
				candleList.add(c);
				// System.out.println(c);
			}
		}
		br.close();
		return candleList;
	}

	public static Map<String, List<Candle>> getCandleDataByDays(List<Candle> candleData) {
		Map<String, List<Candle>> candleDataByDays = new LinkedHashMap<String, List<Candle>>();
		String oldDate = "";
		List<Candle> dayCandles = new ArrayList<Candle>();
		for (Iterator<Candle> iterator = candleData.iterator(); iterator.hasNext();) {
			Candle candle = iterator.next();
			String newDate = outPut.format(candle.getDate());
			if (oldDate.equalsIgnoreCase("")) {
				oldDate = newDate;
			} else if (!oldDate.equalsIgnoreCase(newDate)) {
				candleDataByDays.put(oldDate, dayCandles);
				dayCandles = new ArrayList<Candle>();
				oldDate = newDate;
			}
			dayCandles.add(candle);
		}
		candleDataByDays.put(oldDate, dayCandles);
		return candleDataByDays;
	}

	public static TradeResult profitLossBySingleCandle(int decisionCandle, int buySellCandle, float target,
			float stopLoss, List<Candle> oneDayData) {

		// System.out.println(oneDayData);
		if (oneDayData.get(0).getDate().getMinutes() != 45) {
			System.out.println("No valid data for day " + oneDayData);
			return null;
		}
		String call = oneDayData.get(decisionCandle).getCandleType() == 1 ? "Buy" : "Sell";
		float profit = 0;
		float price = oneDayData.get(buySellCandle).getOpen();
		Date buyTime = oneDayData.get(buySellCandle).getDate();
		target = .01f*price;
		stopLoss = target-0.5f;
		float targetPrice = price;
		float stopLossPrice = price;
		boolean isAutoExecute = false;
		Candle c = null;
		if (call.equalsIgnoreCase("Buy")) {
			targetPrice = price + target;
			stopLossPrice = price - stopLoss;
			for (int i = buySellCandle; i < oneDayData.size() - 1; i++) {
				c = oneDayData.get(i);
				if (c.getLow() <= stopLossPrice) {
					profit = -stopLoss;
					isAutoExecute = true;
					break;
				}
				if (c.getHigh() >= targetPrice) {
					profit = target;
					isAutoExecute = true;
					break;
				}
			}
			if (!isAutoExecute) {
				profit = c.getOpen() - price;
			}
		} else {
			targetPrice = price - target;
			stopLossPrice = price + stopLoss;
			for (int i = 2; i < oneDayData.size() - 1; i++) {
				c = oneDayData.get(i);
				if (c.getHigh() >= stopLossPrice) {
					profit = -stopLoss;
					isAutoExecute = true;
					break;
				}
				if (c.getLow() <= targetPrice) {
					profit = target;
					isAutoExecute = true;
					break;
				}
			}
			if (!isAutoExecute) {
				profit = price - c.getOpen();
			}
		}
		return new TradeResult(call, c.getDate(), buyTime, c.getDate(), profit, price, targetPrice, stopLossPrice,
				isAutoExecute);
	}
	
	public static TradeResult profitLossByMultipleCandle(int candleOne, int candleTwo, float target,
			float stopLoss, List<Candle> oneDayData) {

		// System.out.println(oneDayData);
		if (oneDayData.get(0).getDate().getMinutes() != 45) {
			System.out.println("No valid data for day " + oneDayData);
			return null;
		}
		String call1 = oneDayData.get(candleOne).getCandleType() == 1 ? "Buy" : "Sell";
		String call2 = oneDayData.get(candleTwo).getCandleType() == 1 ? "Buy" : "Sell";
		String call = "Buy";
		if(!call1.equalsIgnoreCase(call2)) {
			call = call1;
		}else {
			return null;
		}
		float profit = 0;
		int buySellCandle = candleTwo+1;
		float price = oneDayData.get(buySellCandle).getOpen();
		Date buyTime = oneDayData.get(buySellCandle).getDate();
		float targetPrice = price;
		float stopLossPrice = price;
		boolean isAutoExecute = false;
		Candle c = null;
		if (call == "Buy") {
			targetPrice = price + target;
			stopLossPrice = price - stopLoss;
			for (int i = buySellCandle; i < oneDayData.size() - 1; i++) {
				c = oneDayData.get(i);
				if (c.getLow() <= stopLossPrice) {
					profit = -stopLoss;
					isAutoExecute = true;
					break;
				}
				if (c.getHigh() >= targetPrice) {
					profit = target;
					isAutoExecute = true;
					break;
				}
			}
			if (!isAutoExecute) {
				profit = c.getOpen() - price;
			}
		} else {
			targetPrice = price - target;
			stopLossPrice = price + target;
			for (int i = 2; i < oneDayData.size() - 1; i++) {
				c = oneDayData.get(i);
				if (c.getHigh() >= stopLossPrice) {
					profit = -stopLoss;
					isAutoExecute = true;
					break;
				}
				if (c.getLow() <= targetPrice) {
					profit = target;
					isAutoExecute = true;
					break;
				}
			}
			if (!isAutoExecute) {
				profit = price - c.getOpen();
			}
		}
		return new TradeResult(call, c.getDate(), buyTime, c.getDate(), profit, price, targetPrice, stopLossPrice,
				isAutoExecute);
	}
}
