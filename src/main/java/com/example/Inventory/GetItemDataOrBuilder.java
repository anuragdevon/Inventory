// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inventory.proto

package com.example.Inventory;

public interface GetItemDataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:inventory.GetItemData)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 id = 1;</code>
   * @return The id.
   */
  long getId();

  /**
   * <code>string name = 2;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 2;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>int64 quantity = 3;</code>
   * @return The quantity.
   */
  long getQuantity();

  /**
   * <code>int64 price = 4;</code>
   * @return The price.
   */
  long getPrice();
}