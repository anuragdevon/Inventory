// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inventory.proto

package com.example.Inventory;

public interface GetItemResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:inventory.GetItemResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 status = 1;</code>
   * @return The status.
   */
  long getStatus();

  /**
   * <code>string error = 2;</code>
   * @return The error.
   */
  java.lang.String getError();
  /**
   * <code>string error = 2;</code>
   * @return The bytes for error.
   */
  com.google.protobuf.ByteString
      getErrorBytes();

  /**
   * <code>.inventory.GetItemData data = 3;</code>
   * @return Whether the data field is set.
   */
  boolean hasData();
  /**
   * <code>.inventory.GetItemData data = 3;</code>
   * @return The data.
   */
  com.example.Inventory.GetItemData getData();
  /**
   * <code>.inventory.GetItemData data = 3;</code>
   */
  com.example.Inventory.GetItemDataOrBuilder getDataOrBuilder();
}
