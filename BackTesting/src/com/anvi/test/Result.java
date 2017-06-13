package com.anvi.test;

import java.util.LinkedList;
import java.util.List;

public class Result {

	String name;
	int noOfDays;
	float invAmt;
	int profitDays;
	int lossDays;
	int nutralDays;
	float profit;
	int lotSize;
	float winStrike;
	int maxWinInRow;
	int maxLossInRow;
	int noOfBuyCall;
	int noOfSellCall;
	int buyProfit;
	int sellProfit;
	int brokeragePerLot;
	int noOfLot;
	List<TradeResult> resultList;
	

	public Result(String name,float invAmt, int lotSize,int brokeragePerLot,int noOfLot) {
		super();
		this.name = name;
		this.invAmt = invAmt;
		this.lotSize = lotSize;
		this.brokeragePerLot = brokeragePerLot;
		this.noOfLot = noOfLot;
		resultList = new LinkedList<TradeResult>();
	}

	public float getWinStrike() {
		return ((float) profitDays / (float) noOfDays) * 100.0f;
	}

	public void addProfit(TradeResult tradeResult) {
		noOfDays++;
		resultList.add(tradeResult);
		if(!tradeResult.isAutoExecute()){
			nutralDays++;
		}
		if (tradeResult.profit > 0) {
			profitDays++;
		} else if (tradeResult.profit <= 0) {
			lossDays++;
		} 
		if(tradeResult.type.equals("Buy")){
			noOfBuyCall++;
			if (tradeResult.profit > 0) {
				buyProfit++;
			}
		}else {
			noOfSellCall++;
			if (tradeResult.profit > 0) {
				sellProfit++;
			}
		}
		this.profit += tradeResult.profit;
	}

	private void updateWinLossInSeq() {
		int win = 0;
		int loss = 0;
		for (int i = 0; i < resultList.size(); i++) {
			//System.out.print(resultList.get(i).profit+",");
			if (resultList.get(i).profit > 0) {
				win++;
				//System.out.println("win");
				if (maxLossInRow < loss) {
					maxLossInRow = loss;
				}
				loss = 0;
			} else {
				loss++;
				//System.out.println("loss");
				if (maxWinInRow < win) {
					maxWinInRow = win;
				}
				win = 0;
			}
		}
		if (maxWinInRow < win) {
			maxWinInRow = win;
		}
		if (maxLossInRow < loss) {
			maxLossInRow = loss;
		}
		//System.out.println(" ,maxLossInRow"+maxLossInRow + "  ,maxWinInRow "+maxWinInRow);

	}

	@Override
	public String toString() {
		updateWinLossInSeq();
		return name + "," + noOfDays + "," + profitDays + "," + lossDays + ", " + nutralDays + "," + profit + ","
				+ profit*lotSize*noOfLot+","+(profit*lotSize - brokeragePerLot*noOfLot*noOfDays) + "," + getWinStrike() + "," + maxWinInRow + "," + maxLossInRow + ","
				+ (((float) buyProfit / (float) noOfBuyCall) * 100.0f)+","+(((float) sellProfit / (float) noOfSellCall) * 100.0f);
	}

}
