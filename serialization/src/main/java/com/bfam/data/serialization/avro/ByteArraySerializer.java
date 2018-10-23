package com.bfam.data.serialization.avro;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class ByteArraySerializer {

    public static void main(String[] args) throws Exception {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();


        DatumWriter<User> datumWriter = new SpecificDatumWriter<>(User.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);

        datumWriter.write(user1, encoder);
        datumWriter.write(user2, encoder);
        datumWriter.write(user3, encoder);
        encoder.flush();

        byte[] br = stream.toByteArray();

        try (FileOutputStream fos = new FileOutputStream("avro2.binary")) {
            fos.write(br);
        }
    }
}
