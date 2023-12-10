package cliente;

import juego.ChessGUI;

import java.io.*;
import java.net.Socket;

public class Cliente {
    private static ChessGUI tablero;
    public static void main(String[] args) throws IOException {    	
        try(Socket socket = new Socket("localhost", 6000);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream())){
        	tablero = new ChessGUI("cliente");            
            int n;
            int[] mapa= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            Thread.currentThread().sleep(3*1000);
            
            for(int i=0;i<64;i++) {
            	mapa[i]=dis.readInt();
            }           
            
        	tablero.setMapeo(mapa);
        	tablero.mapToTable();
        	
        	boolean terminada = dis.readBoolean();
        	
        	int c=dis.readInt();       		
            tablero.setContador(c);
            if(c==0) {
            	tablero.setText("Juegas con las blancas, te toca!");
            }else {
            	tablero.setText("Juegas con las negras, te toca!");
            }
            while(!tablero.getFinalizada()){            	
                n= tablero.getContador();
                while(n==tablero.getContador()){
                	Thread.currentThread().sleep(1*100);
                }                
                for(int i=0;i<64;i++) {
                	dos.writeInt(tablero.getMapeo()[i]);
                	dos.flush();
                }
                terminada=tablero.getFinalizada();
                dos.writeBoolean(terminada);
                dos.flush();
                if(!terminada) {
                	tablero.setText("Espera a que el otro jugador realice su turno");
	                for(int i=0;i<64;i++) {
	                	mapa[i]=dis.readInt();
	                }
	            	tablero.setMapeo(mapa);
	            	tablero.mapToTable();
	            	terminada=dis.readBoolean();	                
	                tablero.setContador(dis.readInt());	                
	                tablero.setText("Te toca!");
	                if(terminada) {
	                	tablero.setText("Has perdido!");
	                	tablero.finalizar();
	                }
                }
                else {

                    tablero.setText("Has ganado!!!!!");
                }
            }
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
