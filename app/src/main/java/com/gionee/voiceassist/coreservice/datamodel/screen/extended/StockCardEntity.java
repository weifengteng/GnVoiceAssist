package com.gionee.voiceassist.coreservice.datamodel.screen.extended;

import com.gionee.voiceassist.coreservice.datamodel.ScreenExtendedDirectiveEntity;

import java.util.Date;

/**
 * Created by liyingheng on 3/5/18.
 */

public class StockCardEntity extends ScreenExtendedDirectiveEntity {

    private String stockName;        // 股票名字

    private String stockCode;       // 股票编号

    private String marketStatus;    // 市场交易状况

    private float markerPrice;      // 交易额

    private float changeInPercentage;   // 升幅

    private float changeInPrice;    // 每股价格

    private Date lastUpdateTime;    // 最后更新时间

    private float openPrice;        // 开市价

    private float previousClosePrice;   // 上次收盘价

    private float dayHighPrice;     // 日最高价

    private float dayLowPrice;      // 日最低价

    private float priceEarningRatio;        // 日赚比例

    private String markerName;      // 市场名称

    public StockCardEntity(String stockName, String stockCode, String marketStatus, float markerPrice, float changeInPercentage, float changeInPrice, Date lastUpdateTime, float openPrice, float previousClosePrice, float dayHighPrice, float dayLowPrice, float priceEarningRatio, String markerName) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.marketStatus = marketStatus;
        this.markerPrice = markerPrice;
        this.changeInPercentage = changeInPercentage;
        this.changeInPrice = changeInPrice;
        this.lastUpdateTime = lastUpdateTime;
        this.openPrice = openPrice;
        this.previousClosePrice = previousClosePrice;
        this.dayHighPrice = dayHighPrice;
        this.dayLowPrice = dayLowPrice;
        this.priceEarningRatio = priceEarningRatio;
        this.markerName = markerName;
    }

    @Override
    public ExtendedCardType getCardType() {
        return ExtendedCardType.STOCK;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus) {
        this.marketStatus = marketStatus;
    }

    public float getMarkerPrice() {
        return markerPrice;
    }

    public void setMarkerPrice(float markerPrice) {
        this.markerPrice = markerPrice;
    }

    public float getChangeInPercentage() {
        return changeInPercentage;
    }

    public void setChangeInPercentage(float changeInPercentage) {
        this.changeInPercentage = changeInPercentage;
    }

    public float getChangeInPrice() {
        return changeInPrice;
    }

    public void setChangeInPrice(float changeInPrice) {
        this.changeInPrice = changeInPrice;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public float getPreviousClosePrice() {
        return previousClosePrice;
    }

    public void setPreviousClosePrice(float previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }

    public float getDayHighPrice() {
        return dayHighPrice;
    }

    public void setDayHighPrice(float dayHighPrice) {
        this.dayHighPrice = dayHighPrice;
    }

    public float getPriceEarningRatio() {
        return priceEarningRatio;
    }

    public void setPriceEarningRatio(float priceEarningRatio) {
        this.priceEarningRatio = priceEarningRatio;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    @Override
    public String toString() {
        return "StockCardEntity{" +
                "stockName='" + stockName + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", marketStatus='" + marketStatus + '\'' +
                ", markerPrice=" + markerPrice +
                ", changeInPercentage=" + changeInPercentage +
                ", changeInPrice=" + changeInPrice +
                ", lastUpdateTime=" + lastUpdateTime +
                ", openPrice=" + openPrice +
                ", previousClosePrice=" + previousClosePrice +
                ", dayHighPrice=" + dayHighPrice +
                ", priceEarningRatio=" + priceEarningRatio +
                ", markerName='" + markerName + '\'' +
                '}';
    }
}
