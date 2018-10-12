package net.alexyu.poc.chat;

import net.alexyu.poc.chat.service.ChatClientService;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by alex on 3/30/17.
 */
public class ChatConsoleClient {

    private static final Scanner in = new Scanner(System.in, Charset.defaultCharset().name());
    public static void main(String[] args) throws Exception {
        String name = null;
        if (args.length > 0) {
            name = args[0];
        }
        else {
            System.out.print("enter name: ");
            name = in.nextLine();
        }


        ChatClientService client = new ChatClientService(name, "localhost", 6565);
        client.start();

        while (true){
            String message = in.nextLine();
            client.chat(message);
        }
    }
}
