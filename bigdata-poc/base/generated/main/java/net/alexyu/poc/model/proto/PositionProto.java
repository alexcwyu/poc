// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: position.proto

package net.alexyu.poc.model.proto;

public final class PositionProto {
  private PositionProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Position_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Position_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016position.proto\"\206\001\n\010Position\022\017\n\007acct_id" +
      "\030\001 \001(\t\022\017\n\007inst_id\030\002 \001(\t\022\021\n\ttotal_qty\030\003 \001" +
      "(\001\022\021\n\tavg_price\030\004 \001(\001\022\030\n\020create_timestam" +
      "p\030\005 \001(\003\022\030\n\020update_timestamp\030\006 \001(\003B-\n\032net" +
      ".alexyu.poc.model.protoB\rPositionProtoP\001" +
      "b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_Position_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Position_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Position_descriptor,
        new java.lang.String[] { "AcctId", "InstId", "TotalQty", "AvgPrice", "CreateTimestamp", "UpdateTimestamp", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}