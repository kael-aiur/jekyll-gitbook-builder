package kael.jekyll.gitbook;

import kael.jekyll.gitbook.client.HttpClient;
import kael.jekyll.gitbook.server.HttpServer;

import java.util.Scanner;


/**
 * Created by kael on 2016/12/12.
 */
public class Main {
    public static void main(String[] args) {
        int port = 8000;
        new Thread(() -> {
            HttpServer server = new HttpServer();
            try {
                server.start(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();



        /*
        HttpClient client = new HttpClient();
        try {
            do{
                Scanner scanner = new Scanner(System.in);
                String cmd = scanner.next();
                if(!"exit".equals(cmd)){
                    client.connect("127.0.0.1", port);
                }else{
                    break;
                }
            }while (true);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
