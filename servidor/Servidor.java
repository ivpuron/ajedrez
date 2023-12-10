package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static void main(String [] args){
        ExecutorService pool = Executors.newCachedThreadPool();
        try(ServerSocket ss = new ServerSocket(6000);){
            while(true){
                Socket player1 = ss.accept();
                Socket player2 = ss.accept();
                pool.execute(new Partida(player1, player2));
            }

        }catch(IOException e){
            e.printStackTrace();
            pool.shutdown();
        }
    }


}
