package servidor;

import juego.ChessGUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Partida implements Runnable {
    private Socket player1;
    private Socket player2;
    private boolean finalizada;
    private int[] mapa = {2,3,4,5,6,4,3,2,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,11,11,11,11,11,11,11,11,12,13,14,15,16,14,13,12};
    public Partida(Socket player1, Socket player2) {
        this.player1=player1;
        this.player2=player2;
        this.finalizada=false;
    }

    @Override
    public void run() {        
        try(DataInputStream dis1 = new DataInputStream(player1.getInputStream());
        		DataInputStream dis2 = new DataInputStream(player2.getInputStream());
        		DataOutputStream dos1 = new DataOutputStream(player1.getOutputStream());
        		DataOutputStream dos2 = new DataOutputStream(player2.getOutputStream());){
        	for(int i=0;i<64;i++) {
        		dos1.writeInt(mapa[i]);
        		dos1.flush();
        	}
        	dos1.writeBoolean(finalizada);
        	int ii = 0;
        	dos1.writeInt(ii);
            while(!finalizada){            	
            	for(int i = 0;i<64;i++) {
            		mapa[i]=dis1.readInt();
            	}
            	finalizada=dis1.readBoolean();
            	ii++;            	
            	for(int i=0;i<64;i++) {
            		dos2.writeInt(mapa[i]);
            		dos2.flush();
            	}
            	dos2.writeBoolean(finalizada);
               	dos2.flush();
            	dos2.writeInt(ii);
            	dos2.flush();
            	
                if(!finalizada) {
                	for(int i = 0;i<64;i++) {
                		mapa[i]=dis2.readInt();
                	}
                	finalizada=dis2.readBoolean();
                	for(int i=0;i<64;i++) {
                		dos1.writeInt(mapa[i]);
                		dos1.flush();
                	}
                	dos1.writeBoolean(finalizada);
                	dos1.flush();
                	ii++;
                	dos1.writeInt(ii);
                	dos1.flush();
                	
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        
        }finally{
        	try{player1.close();
        	player2.close();}
        	catch(IOException e) {e.printStackTrace();}
        	}

    }
}
