package com.bfam.kakfa

data class MarketPrice(val symbol: String, val price: Double, val timestamp: Long)

data class Instrument(val symbol: String, val name: String, val type: String)

data class Trade(val tradeId: Long, val symbol: String, val counterParty: String, val portfolioId: String, val qty: Double, val price : Double, val timestamp: Long)

data class Portfolio(val portfolioId : String, val trader: String)

data class Position(val keyType: String, val keyValue: String, val symbol: String, val totalLongQty: Double, val avgLongPrice : Double, val totalShortQty : Double, val avgShortPrice : Double)


data class MarketPriceStats(val symbol: String, val last: Double=0.0, val avg: Double=0.0, val max : Double=0.0, val min : Double=0.0, val count: Int=0, val timestamp: Long=0)