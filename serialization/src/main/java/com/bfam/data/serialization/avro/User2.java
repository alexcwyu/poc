/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.bfam.data.serialization.avro;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class User2 extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2548576902297975230L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"User2\",\"namespace\":\"com.bfam.data.serialization.avro\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"favorite_number\",\"type\":[\"int\",\"null\"]},{\"name\":\"nickname\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"bod\",\"type\":\"int\",\"default\":19000101},{\"name\":\"favorite_color\",\"type\":[\"string\",\"null\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<User2> ENCODER =
      new BinaryMessageEncoder<User2>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<User2> DECODER =
      new BinaryMessageDecoder<User2>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<User2> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<User2> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<User2>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this User2 to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a User2 from a ByteBuffer. */
  public static User2 fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public CharSequence name;
  @Deprecated public Integer favorite_number;
  @Deprecated public CharSequence nickname;
  @Deprecated public int bod;
  @Deprecated public CharSequence favorite_color;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public User2() {}

  /**
   * All-args constructor.
   * @param name The new value for name
   * @param favorite_number The new value for favorite_number
   * @param nickname The new value for nickname
   * @param bod The new value for bod
   * @param favorite_color The new value for favorite_color
   */
  public User2(CharSequence name, Integer favorite_number, CharSequence nickname, Integer bod, CharSequence favorite_color) {
    this.name = name;
    this.favorite_number = favorite_number;
    this.nickname = nickname;
    this.bod = bod;
    this.favorite_color = favorite_color;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return favorite_number;
    case 2: return nickname;
    case 3: return bod;
    case 4: return favorite_color;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: name = (CharSequence)value$; break;
    case 1: favorite_number = (Integer)value$; break;
    case 2: nickname = (CharSequence)value$; break;
    case 3: bod = (Integer)value$; break;
    case 4: favorite_color = (CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'name' field.
   * @return The value of the 'name' field.
   */
  public CharSequence getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'favorite_number' field.
   * @return The value of the 'favorite_number' field.
   */
  public Integer getFavoriteNumber() {
    return favorite_number;
  }

  /**
   * Sets the value of the 'favorite_number' field.
   * @param value the value to set.
   */
  public void setFavoriteNumber(Integer value) {
    this.favorite_number = value;
  }

  /**
   * Gets the value of the 'nickname' field.
   * @return The value of the 'nickname' field.
   */
  public CharSequence getNickname() {
    return nickname;
  }

  /**
   * Sets the value of the 'nickname' field.
   * @param value the value to set.
   */
  public void setNickname(CharSequence value) {
    this.nickname = value;
  }

  /**
   * Gets the value of the 'bod' field.
   * @return The value of the 'bod' field.
   */
  public Integer getBod() {
    return bod;
  }

  /**
   * Sets the value of the 'bod' field.
   * @param value the value to set.
   */
  public void setBod(Integer value) {
    this.bod = value;
  }

  /**
   * Gets the value of the 'favorite_color' field.
   * @return The value of the 'favorite_color' field.
   */
  public CharSequence getFavoriteColor() {
    return favorite_color;
  }

  /**
   * Sets the value of the 'favorite_color' field.
   * @param value the value to set.
   */
  public void setFavoriteColor(CharSequence value) {
    this.favorite_color = value;
  }

  /**
   * Creates a new User2 RecordBuilder.
   * @return A new User2 RecordBuilder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Creates a new User2 RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new User2 RecordBuilder
   */
  public static Builder newBuilder(Builder other) {
    return new Builder(other);
  }

  /**
   * Creates a new User2 RecordBuilder by copying an existing User2 instance.
   * @param other The existing instance to copy.
   * @return A new User2 RecordBuilder
   */
  public static Builder newBuilder(User2 other) {
    return new Builder(other);
  }

  /**
   * RecordBuilder for User2 instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<User2>
    implements org.apache.avro.data.RecordBuilder<User2> {

    private CharSequence name;
    private Integer favorite_number;
    private CharSequence nickname;
    private int bod;
    private CharSequence favorite_color;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.favorite_number)) {
        this.favorite_number = data().deepCopy(fields()[1].schema(), other.favorite_number);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.nickname)) {
        this.nickname = data().deepCopy(fields()[2].schema(), other.nickname);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.bod)) {
        this.bod = data().deepCopy(fields()[3].schema(), other.bod);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.favorite_color)) {
        this.favorite_color = data().deepCopy(fields()[4].schema(), other.favorite_color);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing User2 instance
     * @param other The existing instance to copy.
     */
    private Builder(User2 other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.favorite_number)) {
        this.favorite_number = data().deepCopy(fields()[1].schema(), other.favorite_number);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.nickname)) {
        this.nickname = data().deepCopy(fields()[2].schema(), other.nickname);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.bod)) {
        this.bod = data().deepCopy(fields()[3].schema(), other.bod);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.favorite_color)) {
        this.favorite_color = data().deepCopy(fields()[4].schema(), other.favorite_color);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'name' field.
      * @return The value.
      */
    public CharSequence getName() {
      return name;
    }

    /**
      * Sets the value of the 'name' field.
      * @param value The value of 'name'.
      * @return This builder.
      */
    public Builder setName(CharSequence value) {
      validate(fields()[0], value);
      this.name = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'name' field.
      * @return This builder.
      */
    public Builder clearName() {
      name = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'favorite_number' field.
      * @return The value.
      */
    public Integer getFavoriteNumber() {
      return favorite_number;
    }

    /**
      * Sets the value of the 'favorite_number' field.
      * @param value The value of 'favorite_number'.
      * @return This builder.
      */
    public Builder setFavoriteNumber(Integer value) {
      validate(fields()[1], value);
      this.favorite_number = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'favorite_number' field has been set.
      * @return True if the 'favorite_number' field has been set, false otherwise.
      */
    public boolean hasFavoriteNumber() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'favorite_number' field.
      * @return This builder.
      */
    public Builder clearFavoriteNumber() {
      favorite_number = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'nickname' field.
      * @return The value.
      */
    public CharSequence getNickname() {
      return nickname;
    }

    /**
      * Sets the value of the 'nickname' field.
      * @param value The value of 'nickname'.
      * @return This builder.
      */
    public Builder setNickname(CharSequence value) {
      validate(fields()[2], value);
      this.nickname = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'nickname' field has been set.
      * @return True if the 'nickname' field has been set, false otherwise.
      */
    public boolean hasNickname() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'nickname' field.
      * @return This builder.
      */
    public Builder clearNickname() {
      nickname = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'bod' field.
      * @return The value.
      */
    public Integer getBod() {
      return bod;
    }

    /**
      * Sets the value of the 'bod' field.
      * @param value The value of 'bod'.
      * @return This builder.
      */
    public Builder setBod(int value) {
      validate(fields()[3], value);
      this.bod = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'bod' field has been set.
      * @return True if the 'bod' field has been set, false otherwise.
      */
    public boolean hasBod() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'bod' field.
      * @return This builder.
      */
    public Builder clearBod() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'favorite_color' field.
      * @return The value.
      */
    public CharSequence getFavoriteColor() {
      return favorite_color;
    }

    /**
      * Sets the value of the 'favorite_color' field.
      * @param value The value of 'favorite_color'.
      * @return This builder.
      */
    public Builder setFavoriteColor(CharSequence value) {
      validate(fields()[4], value);
      this.favorite_color = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'favorite_color' field has been set.
      * @return True if the 'favorite_color' field has been set, false otherwise.
      */
    public boolean hasFavoriteColor() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'favorite_color' field.
      * @return This builder.
      */
    public Builder clearFavoriteColor() {
      favorite_color = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public User2 build() {
      try {
        User2 record = new User2();
        record.name = fieldSetFlags()[0] ? this.name : (CharSequence) defaultValue(fields()[0]);
        record.favorite_number = fieldSetFlags()[1] ? this.favorite_number : (Integer) defaultValue(fields()[1]);
        record.nickname = fieldSetFlags()[2] ? this.nickname : (CharSequence) defaultValue(fields()[2]);
        record.bod = fieldSetFlags()[3] ? this.bod : (Integer) defaultValue(fields()[3]);
        record.favorite_color = fieldSetFlags()[4] ? this.favorite_color : (CharSequence) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<User2>
    WRITER$ = (org.apache.avro.io.DatumWriter<User2>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<User2>
    READER$ = (org.apache.avro.io.DatumReader<User2>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
