// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inventory.proto

package com.example.Inventory;

public interface GetAllItemsResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:inventory.GetAllItemsResponse)
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
   * <code>repeated .inventory.GetItemData data = 3;</code>
   */
  java.util.List<com.example.Inventory.GetItemData> 
      getDataList();
  /**
   * <code>repeated .inventory.GetItemData data = 3;</code>
   */
  com.example.Inventory.GetItemData getData(int index);
  /**
   * <code>repeated .inventory.GetItemData data = 3;</code>
   */
  int getDataCount();
  /**
   * <code>repeated .inventory.GetItemData data = 3;</code>
   */
  java.util.List<? extends com.example.Inventory.GetItemDataOrBuilder> 
      getDataOrBuilderList();
  /**
   * <code>repeated .inventory.GetItemData data = 3;</code>
   */
  com.example.Inventory.GetItemDataOrBuilder getDataOrBuilder(
      int index);
}
