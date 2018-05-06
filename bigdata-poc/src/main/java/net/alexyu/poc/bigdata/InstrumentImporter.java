package net.alexyu.poc.bigdata;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class InstrumentImporter {

    public static void importETFFromDir(String directory) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path entry : dirStream) {
                try (Stream<String> lineStream = Files.lines(entry)) {
                    lineStream.forEach(System.out::println);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (DirectoryIteratorException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void importETF(Path entry) {

    }


    public static void main(String[] args) {
        importETFFromDir("/mnt/data/dev/workspaces/poc/bigdata-poc/src/main/resources/data");
    }

}
