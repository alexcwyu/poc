package com.bfam.data.serialization.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;
import java.nio.file.Files;

public class ByteArrayDeserializer {

    public static void main(String[] args) throws Exception {
        //serial

        File file2 = new File("avro2.binary");
        byte[] fileContent = Files.readAllBytes(file2.toPath());

        //TODO from Schema registry
        Schema oldSchema = SchemaBuilder
                .record("User").namespace("com.bfam.data.serialization.avro")
                .fields()
                .name("name").type().stringType().noDefault()
                .name("favorite_number").type(SchemaBuilder
                        .unionOf()
                        .intType()
                        .and()
                        .nullType().endUnion()).noDefault()
                .name("favorite_color").type(SchemaBuilder
                .unionOf()
                .stringType()
                .and()
                .nullType().endUnion()).noDefault()
                .endRecord();
        System.out.println(oldSchema);

        Schema newSchema = new SpecificData(User2.class.getClassLoader()).getSchema(User2.class);
        DatumReader<User2> userDatumReader2 = new SpecificDatumReader<User2>(oldSchema, newSchema);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(fileContent, null);
        while(!decoder.isEnd()) {
            User2 user = userDatumReader2.read(null, decoder);
            System.out.println(user);
        }

    }
}
