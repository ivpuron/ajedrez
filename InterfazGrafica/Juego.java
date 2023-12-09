package InterfazGrafica;

import javax.swing.*;

import static InterfazGrafica.ChessGUI.QUEEN;

public class Juego {
    private ChessGUI tablero;

    public Juego(){
        this.tablero=new ChessGUI();
    }

    public void start(){
        while (!jaqueMate()){
            //mueven las blancas
            JButton piezaSeleccionada = tablero.getPieza_seleccionada();
            if(piezaSeleccionada!=null && tablero.isWhite(piezaSeleccionada)){
                if(piezaSeleccionada.equals(QUEEN)){

                }
            }
            if(!jaqueMate()){
                //muevenlasnegras
            }

        }

    }

    public boolean jaque(){
        boolean jaque = false;
        return jaque;
    }
    public boolean jaqueMate(){
        boolean mate = false;
        return mate;
    }

}
