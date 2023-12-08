import InterfazGrafica.Tablero;

import java.net.Socket;

public class Partida implements Runnable {
    private Socket player1;
    private Socket player2;
    private boolean finalizada;
    public Partida(Socket player1, Socket player2) {
        this.player1=player1;
        this.player2=player2;
        this.finalizada=false;
    }

    @Override
    public void run() {
        Tablero tablero = new Tablero();

    }
}
