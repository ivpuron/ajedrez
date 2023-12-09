import InterfazGrafica.ChessGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Partida implements Runnable {
    private Socket player1;
    private Socket player2;
    private ChessGUI tablero;
    private boolean finalizada;
    public Partida(Socket player1, Socket player2) {
        this.player1=player1;
        this.player2=player2;
        this.finalizada=false;
        this.tablero = new ChessGUI();
    }

    @Override
    public void run() {
        ChessGUI tablero = new ChessGUI();
        try(ObjectInputStream ois1 = new ObjectInputStream(this.player1.getInputStream());
            ObjectInputStream ois2 = new ObjectInputStream(this.player2.getInputStream());
            ObjectOutputStream oos1 = new ObjectOutputStream(this.player1.getOutputStream());
            ObjectOutputStream oos2 = new ObjectOutputStream(this.player2.getOutputStream());) {
            while(!tablero.getFinalizada()){
                oos1.writeObject(this.tablero);
                oos1.flush();
                this.tablero = (ChessGUI) ois1.readObject();
                oos2.writeObject(this.tablero);
                oos2.flush();
                this.tablero = (ChessGUI) ois2.readObject();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
