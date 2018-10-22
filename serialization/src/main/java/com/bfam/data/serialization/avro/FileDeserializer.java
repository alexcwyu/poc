package com.bfam.data.serialization.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;

public class FileDeserializer {

    public static void main(String[] args) throws Exception {
        //serial
        File file = new File("avro1.binary");
        //deserial

        DatumReader<User2> userDatumReader  = new SpecificDatumReader<User2>(User2.class);
        DataFileReader<User2> dataFileReader = new DataFileReader<User2>(file, userDatumReader);
        User2 user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}
