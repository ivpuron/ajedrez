package cliente;

import InterfazGrafica.ChessGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    private static ChessGUI tablero=null;
    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket("localhost", 6000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())){
            tablero = (ChessGUI) ois.readObject();
            int n = tablero.getContador();
            while(!tablero.getFinalizada()){
                while(n==tablero.getContador()){

                }
                oos.writeObject(tablero);
                oos.flush();
                tablero = (ChessGUI) ois.readObject();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
