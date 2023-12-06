import java.net.Socket;

public class Partida implements Runnable {
    private Socket player1;
    private Socket player2;
    public Partida(Socket player1, Socket player2) {
        this.player1=player1;
        this.player2=player2;
    }

    @Override
    public void run() {

    }
}
