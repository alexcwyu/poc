// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: marketdata.proto

package net.alexyu.poc.model.proto;

public interface MarketPriceOrBuilder extends
    // @@protoc_insertion_point(interface_extends:MarketPrice)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string inst_id = 1;</code>
   */
  java.lang.String getInstId();
  /**
   * <code>string inst_id = 1;</code>
   */
  com.google.protobuf.ByteString
      getInstIdBytes();

  /**
   * <code>int64 timestamp = 2;</code>
   */
  long getTimestamp();

  /**
   * <code>double last_price = 3;</code>
   */
  double getLastPrice();
}