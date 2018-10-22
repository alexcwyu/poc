package com.bfam.data.serialization.avro;

import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CompatibilityTest {

    @Test
    public void shouldBackwardCompatible() throws Exception{
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

        Schema oldSchema = new SpecificData(User.class.getClassLoader()).getSchema(User.class);
        Schema newSchema = new SpecificData(User2.class.getClassLoader()).getSchema(User2.class);
        DatumReader<User2> userDatumReader2 = new SpecificDatumReader<User2>(oldSchema, newSchema);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(br, null);
        User2 user4 = userDatumReader2.read(null, decoder);
        User2 user5 = userDatumReader2.read(null, decoder);
        User2 user6 = userDatumReader2.read(null, decoder);

        assertUser(user1, user4);
        assertUser(user2, user5);
        assertUser(user3, user6);
    }


    @Test
    public void shouldForwardCompatible() throws Exception{
        User2 user1 = new User2();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        user1.setBod(20000101);

        // Alternate constructor
        User2 user2 = new User2("Ben", 7, "no nick", 20020101, "red");

        // Construct via builder
        User2 user3 = User2.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();


        DatumWriter<User2> datumWriter = new SpecificDatumWriter<>(User2.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);

        datumWriter.write(user1, encoder);
        datumWriter.write(user2, encoder);
        datumWriter.write(user3, encoder);
        encoder.flush();
        byte[] br = stream.toByteArray();

        Schema oldSchema = new SpecificData(User.class.getClassLoader()).getSchema(User.class);
        Schema newSchema = new SpecificData(User2.class.getClassLoader()).getSchema(User2.class);
        DatumReader<User> userDatumReader2 = new SpecificDatumReader<User>(newSchema, oldSchema);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(br, null);
        User user4 = userDatumReader2.read(null, decoder);
        User user5 = userDatumReader2.read(null, decoder);
        User user6 = userDatumReader2.read(null, decoder);

        assertUser(user4, user1);
        assertUser(user5, user2);
        assertUser(user6, user3);
    }

    public void assertUser(User oldUser, User2 newUser){
        if (oldUser.getName() != null || newUser.getName() !=null)
            assertEquals(oldUser.getName().toString(), newUser.getName().toString());
        assertEquals(oldUser.getFavoriteNumber(), newUser.getFavoriteNumber());
        if (oldUser.getFavoriteColor() != null || newUser.getFavoriteColor() !=null)
            assertEquals(oldUser.getFavoriteColor().toString(), newUser.getFavoriteColor().toString());

        assertTrue(newUser.getBod() > 0);

        System.out.println("old:\t"+oldUser);
        System.out.println("new:\t"+newUser);
    }

}
