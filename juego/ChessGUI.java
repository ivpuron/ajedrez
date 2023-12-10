package juego;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.net.URL;
import javax.imageio.ImageIO;



public class ChessGUI {
    /**
	 * 
	 */
	private static int contador = 0;

    public int getContador() {
        return contador;
    }

    private static JFrame frame;
    private JButton pieza_seleccionada = null;
    public JButton getPieza_seleccionada(){return this.pieza_seleccionada;}

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private BufferedImage[][] chessPieceImages = new BufferedImage[2][6];
    private JPanel chessBoard;
    private final JLabel message = new JLabel(
            "Chess Champ is ready to play!");
    private static final String COLS = "ABCDEFGH";
    public static final int KING = 0, QUEEN = 1,
            ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
    public static final int[] STARTING_ROW = {
            ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK
    };
    public static final int BLACK = 0, WHITE = 1;
    private final int[] iconosBlancos = {11,12,13,14,15,16};
    private final int[] iconosNegros = {1,2,3,4,5,6};
    private boolean finalizada = false;
    public boolean getFinalizada(){return this.finalizada;}
    public void setFinalizada(boolean fin) {finalizada=fin;}
    public void finalizar() {this.finalizada=true;}
    private int[] mapeo = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    public int[] getMapeo() {return this.mapeo;}
    public void setMapeo(int[] mapa) {this.mapeo=mapa;}
    private boolean turnoBlancas = true;
    private boolean turnoNegras = false;
    public void setContador(int n) {contador=n;turnoBlancas=(contador%2==0);
    turnoNegras=(contador%2==1);
    if(turnoBlancas) {
		message.setText("Turno de blancas!");
	}else {
		message.setText("Turno de negras!");
	}}

    public ChessGUI(String name) {
        initializeGui();
        setupNewGame();
        ChessGUI cg = this;
        Runnable r = new Runnable() {

            @Override
            public void run() {
                //ChessGUI cg = new ChessGUI();

                frame = new JFrame(name);
                frame.add(cg.getGui());
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See https://stackoverflow.com/a/7143398/418556 for demo.
                frame.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                frame.pack();
                // ensures the minimum size is enforced.
                frame.setMinimumSize(frame.getSize());
                frame.setVisible(true);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }



    public final void initializeGui() {
        // create the images for the chess pieces
        createImages();

        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        /*Action newGameAction = new AbstractAction("New") {

            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame();
            }
        };
        tools.add(newGameAction);
        tools.addSeparator();*/
        /*JButton btnRendirse = new JButton("rendirse");
        tools.add(btnRendirse); 
        btnRendirse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRendirseHandler();
            }
            public void btnRendirseHandler(){
            	finalizar();
            }
        });
        
        tools.addSeparator();*/
        tools.add(message);

        gui.add(new JLabel("?"), BorderLayout.LINE_START);

        chessBoard = new JPanel(new GridLayout(0, 9)) {

            /**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstaint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };
        chessBoard.setBorder(new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
        ));
        // Set the BG to be ochre
        Color ochre = new Color(204,119,34);
        chessBoard.setBackground(ochre);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(ochre);
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        // create the chess board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                //ImageIcon icon = new ImageIcon(
                        //new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(null);
                if ((jj % 2 == 1 && ii % 2 == 1)
                        //) {
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	if(!finalizada) {
                    		btnHandler();
                    	}
                    }
                    public void btnHandler(){
                        if(pieza_seleccionada==null && turnoBlancas==isWhite(b)){                        	
                            if(b.getIcon()!=null){
                                pieza_seleccionada=b;
                            }
                        }else{
                        	
                        	if(pieza_seleccionada!=null ) {
                        		/*if(b.getIcon()==null) {
                            		if(turnoBlancas && isWhite(pieza_seleccionada)){
                                        if(movePiece(pieza_seleccionada,b)){
                                            contador++;
                                            if(checkMate(turnoNegras)) {System.out.println("jaque mate");finalizar();}
                                            else{
	                                            turnoBlancas=false;
	                                            turnoNegras=true;
	                                            message.setText("Turno de negras!");
                                            }
                                        }
                                    }else if(turnoNegras && isBlack(pieza_seleccionada)){
                                        if(movePiece(pieza_seleccionada,b)){
                                            contador++;
                                            if(checkMate(turnoBlancas)) {System.out.println("jaque mate");finalizar();}
                                            else{ 
                                            	turnoBlancas=true;                                            
	                                            turnoNegras=false;
	                                            message.setText("Turno de blancas!");}
                                        }
                                    }
                                    
                            	}else*/ if(b.getIcon()==null || turnoBlancas && isBlack(b) || turnoNegras && isWhite(b)) {
                            		if(moverPieza(pieza_seleccionada,b)) {
                            			contador++;
                            			if(checkMate(turnoBlancas)) {
                            				finalizar();}
                            			else{
                            				turnoBlancas=!turnoBlancas;                            			
                            				turnoNegras=!turnoNegras;}
                            			if(turnoBlancas) {
                            				message.setText("Turno de blancas!");
                            			}else {
                            				message.setText("Turno de negras!");
                            			}
                            		}
                            	}
                            	
                        	}pieza_seleccionada=null;
                        	
                        }

                    }
                });
                chessBoardSquares[jj][ii] = b;
            }
        }

        /*
         * fill the chess board
         */
        chessBoard.add(new JLabel(""));
        // fill the top row
        for (int ii = 0; ii < 8; ii++) {
            chessBoard.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                            SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                switch (jj) {
                    case 0:
                        chessBoard.add(new JLabel("" + (9-(ii + 1)),
                                SwingConstants.CENTER));
                    default:
                        chessBoard.add(chessBoardSquares[jj][ii]);
                }
            }
        }
    }



    


    
	



	public final JComponent getGui() {
        return gui;
    }

    private final void createImages() {
        try {
            URL url = new URL("https://i.stack.imgur.com/memI0.png");
            //URL url = new URL("ajedrez//piezas.png");
            BufferedImage bi = ImageIO.read(url);
            for (int ii = 0; ii < 2; ii++) {
                for (int jj = 0; jj < 6; jj++) {
                    chessPieceImages[ii][jj] = bi.getSubimage(
                            jj * 64, ii * 64, 64, 64);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Initializes the icons of the initial chess board piece places
     */
    private final void setupNewGame() {
        message.setText("Turno de blancas!");
        // set up the black pieces
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            chessBoardSquares[ii][0].setIcon(new ImageIcon(
                    chessPieceImages[BLACK][STARTING_ROW[ii]]));
            if(ii<5) {
            	mapeo[ii]= ii+2;
            }else {
            	mapeo[ii]=8-ii+1; 
            }
        }
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            chessBoardSquares[ii][1].setIcon(new ImageIcon(
                    chessPieceImages[BLACK][PAWN]));
            mapeo[ii+8]=1;
        }


        // set up the white pieces
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            chessBoardSquares[ii][6].setIcon(new ImageIcon(
                    chessPieceImages[WHITE][PAWN]));
            mapeo[ii+8*6]=11;
        }
        for (int ii = 0; ii < STARTING_ROW.length; ii++) {
            chessBoardSquares[ii][7].setIcon(new ImageIcon(
                    chessPieceImages[WHITE][STARTING_ROW[ii]]));
            if(ii<5) {
            	mapeo[ii+8*7]= ii+12;
            }else {
            	mapeo[ii+8*7]=8-ii+11; 
            }
        }
    }
    
    public void mapToTable() {
    	for(int i = 0 ; i < 64 ;i++) {
    		switch(mapeo[i]) {
    			case 0:
    				chessBoardSquares[i%8][i/8].setIcon(null);
    				break;
    			case 1:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[BLACK][PAWN]));
    				break;
    			case 11:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[WHITE][PAWN]));
    				break;
    			case 2:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[BLACK][ROOK]));
    				break;
    			case 12:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[WHITE][ROOK]));
    				break;
    			case 3:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[BLACK][KNIGHT]));
    				break;
    			case 13:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[WHITE][KNIGHT]));
    				break;
    			case 4:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[BLACK][BISHOP]));
    				break;
    			case 14:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[WHITE][BISHOP]));
    				break;
    			case 5:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[BLACK][QUEEN]));
    				break;
    			case 15:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[WHITE][QUEEN]));
    				break;
    			case 6:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[BLACK][KING]));
    				break;
    			case 16:
    				chessBoardSquares[i%8][i/8].setIcon(new ImageIcon(
    	                    chessPieceImages[WHITE][KING]));
    				break;    				
    		}
    		
    	}
    	frame.pack();
    }



    private boolean moverPieza(JButton origen, JButton destino){    	
        if(!movimientoCorrecto(origen,destino) ){return false;}
        Icon aux =destino.getIcon();
        destino.setIcon(origen.getIcon());
        origen.setIcon(null);
        
        int x=-1,y=-1,m=-1,n=-1;
        for(int i = 0 ; i<8;i++){
            for(int j = 0 ; j<8; j++){
                if(chessBoardSquares[i][j].equals(origen)){
                    x=i;
                    y=j;
                }
            }
        }
        
        for(int i = 0 ; i<8;i++){
            for(int j = 0 ; j<8; j++){
                if(chessBoardSquares[i][j].equals(destino)){
                    m=i;
                    n=j;
                }
            }
        }
        int naux =mapeo[n*8+m];
        mapeo[n*8+m]=mapeo[y*8+x];
        mapeo[y*8+x]=0;
        if(check(!turnoBlancas)){
        	turnoBlancas=!turnoBlancas;
        	turnoNegras=!turnoNegras;
        	origen.setIcon(destino.getIcon());
        	destino.setIcon(aux);
        	mapeo[y*8+x]=mapeo[n*8+m];
        	mapeo[n*8+m]=naux;
        	return false;
        }
        if(mapeo[n*8+m]==1 && n==7) {
        	mapeo[n*8+m]=5;
        	chessBoardSquares[m][n].setIcon(new ImageIcon(
                    chessPieceImages[BLACK][QUEEN]));
        }
        if(mapeo[n*8+m]==11 && n==0) {
        	mapeo[n*8+m]=15;
        	chessBoardSquares[m][n].setIcon(new ImageIcon(
                    chessPieceImages[WHITE][QUEEN]));
        }
        
        
        frame.pack();
        return true;
    }


    private boolean movimientoCorrecto(JButton origen, JButton destino) {    	
    	int x=-1,y=-1,m=-1,n=-1;
        for(int i = 0 ; i<8;i++){
            for(int j = 0 ; j<8; j++){
                if(chessBoardSquares[i][j].equals(origen)){
                    x=i;
                    y=j;
                }
            }
        }
        
        for(int i = 0 ; i<8;i++){
            for(int j = 0 ; j<8; j++){
                if(chessBoardSquares[i][j].equals(destino)){
                    m=i;
                    n=j;
                }
            }
        }
        if(m==x && y==n) {return false;}
        switch(mapeo[y*8+x]) {
        case 0:
        	return false;
        case 1:
        	if(isEmpty(chessBoardSquares[m][n])) {
	        	if(x!=m) {return false;}
	        	else {
	        		if(y==1) {
	        			if(n-y==2 && isEmpty(chessBoardSquares[x][y+1]) || n-y==1) {return true;}
	        			else {return false;}
	        		}else {return n-y==1;}
	        	}
        	}
        	else {
        		return n-y==1 && Math.abs(x-m)==1;
        	}
        case 11:
        	if(isEmpty(chessBoardSquares[m][n])) {
	        	if(x!=m) {return false;}
	        	else {
	        		if(y==6) {
	        			if(y-n==2 && isEmpty(chessBoardSquares[x][y-1]) || y-n==1) {
	        				return true;
	        			}else {return false;}
	        		}return y-n==1;
	        	}
        	}else {
        		return y-n==1 && Math.abs(x-m)==1;
        	}
        case 2:
        	if(x==m) {
        		if(y<n) {
	        		for(int i = y+1; i<=n;i++) {
	        			if(!isEmpty(chessBoardSquares[x][i])) {
	        				if(i!=n || mapeo[x+8*i]<10) {return false;}
	        			}
	        		}
        		}
        		if(y>n) {
        			for(int i = n; i<y;i++) {
            			if(!isEmpty(chessBoardSquares[x][i])) {
            				if(i!=n || mapeo[x+8*i]<10) {return false;}
            			}
            				
            		}
        		}
        		return true;
        	}else if(y==n) {
        		if(x<m) {
	        		for(int i = x+1; i<=m;i++) {
	        			if(!isEmpty(chessBoardSquares[i][y])) {
	        				if(i!=m || mapeo[i+8*y]<10) {return false;}
	        			}
	        		}
        		}
        		if(x>m) {
        			for(int i = m; i<x;i++) {
            			if(!isEmpty(chessBoardSquares[i][y])) {
	        				if(i!=m || mapeo[i+8*y]<10) {return false;}
	        			}
            		}
        		}
        		return true;
        	}
        	return false;
        case 12:
        	if(x==m) {
        		if(y<n) {
	        		for(int i = y+1; i<=n;i++) {
	        			if(!isEmpty(chessBoardSquares[x][i])) {
	        				if(i!=n || mapeo[x+8*i]>10) {return false;}
	        			}
	        		}
        		}
        		if(y>n) {
        			for(int i = n; i<y;i++) {
            			if(!isEmpty(chessBoardSquares[x][i])) {
	        				if(i!=n || mapeo[x+8*i]>10) {return false;}
	        			}
            		}
        		}
        		return true;
        	}else if(y==n) {
        		if(x<m) {
	        		for(int i = x+1; i<=m;i++) {
	        			if(!isEmpty(chessBoardSquares[i][y])) {
	        				if(i!=m || mapeo[i+8*y]>10) {return false;}
	        			}
	        		}
        		}
        		if(x>m) {
        			for(int i = m; i<x;i++) {
            			if(!isEmpty(chessBoardSquares[i][y])) {
	        				if(i!=m || mapeo[i+8*y]>10) {return false;}
	        			}
            		}
        		}
        		return true;
        	}
        	return false;
        case 4:
        	int j= Math.abs(x-m);
        	if(j>0 && j == Math.abs(y-n)) {
        		if(x>m && y>n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x-i][y-i])) 
        				{
        					if(x-i!=m || mapeo[x-i+8*(y-i)]<10) {return false;}
        				}
        			}
        			return true;
        		}else if(x<m && y>n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x+i][y-i])) {
        					if(x+i!=m || mapeo[x+i+8*(y-i)]<10) {return false;}
        				}
        			}
        			return true;
        		}else if(x>m && y<n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x-i][y+i])) {
        					if(x-i!=m || mapeo[x-i+8*(y+i)]<10) {return false;}
        				}
        			}
        			return true;
        			
        		}else if(x<m && y<n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x+i][y+i])) {
        					if(x+i!=m || mapeo[x+i+8*(y+i)]<10) {return false;}
        				}
        			}
        			return true;
        		}
        		return false;
        	}else {return false;}
        case 14:
        	j= Math.abs(x-m);
        	if(j>0 && j == Math.abs(y-n)) {
        		if(x>m && y>n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x-i][y-i])) {
        					if(x-i!=m || mapeo[x-i+8*(y-i)]>10) {return false;}
        				}
        			}
        			return true;
        		}else if(x<m && y>n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x+i][y-i])) {
        					if(x+i!=m || mapeo[x+i+8*(y-i)]>10) {return false;}
        				}
        			}
        			return true;
        		}else if(x>m && y<n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x-i][y+i])) {
        					if(x-i!=m || mapeo[x-i+8*(y+i)]>10) {return false;}
        				}
        			}
        			return true;
        			
        		}else if(x<m && y<n) {
        			for(int i=1;i<=j;i++) {
        				if(!isEmpty(chessBoardSquares[x+i][y+i])) {
        					if(x+i!=m || mapeo[x+i+8*(y+i)]>10) {return false;}
        				}
        			}
        			return true;
        		}
        		return false;
        	}else {return false;}
        case 3:
        	return (Math.abs(x-m) == 1 && Math.abs(y-n)==2) || (Math.abs(x-m) == 2  && Math.abs(y-n)==1);
        case 13:
        	return (Math.abs(x-m) == 1 && Math.abs(y-n)==2) || (Math.abs(x-m) == 2  && Math.abs(y-n)==1);
        case 5:
        	if(x==m) {
        		if(y<n) {
	        		for(int i = y+1; i<=n;i++) {
	        			if(!isEmpty(chessBoardSquares[x][i])){
	        				if(i!=n || mapeo[x+8*i]<10) {return false;}
	        			}
	        		}
        		}
        		if(y>n) {
        			for(int i = n; i<y;i++) {
            			if(!isEmpty(chessBoardSquares[x][i])){
            				if(i!=n || mapeo[x+8*i]<10) {return false;}
            			}
            		}
        		}
        		return true;
        	}else if(y==n) {
        		if(x<m) {            		
	        		for(int i = x+1; i<=m;i++) {
	        			if(!isEmpty(chessBoardSquares[i][y])) {
	        				if (i!=m || mapeo[i+8*y]<10) {;return false;}
	        			}
	        		}
        		}
        		if(x>m){
        			for(int i = m; i<x;i++) {
            			if(!isEmpty(chessBoardSquares[i][y])) {
            				if(i!=m || mapeo[i+8*y]<10) {return false;}
            			}
            		}
        		}return true;
        	}else {
        		j= Math.abs(x-m);
            	if(j>0 && j == Math.abs(y-n)) {
            		if(x>m && y>n) {
            			for(int i=1;i<=j;i++) {
            				if(!isEmpty(chessBoardSquares[x-i][y-i])) {
            					if(x-i!=m || mapeo[x-i+8*(y-i)]<10 ) {return false;}
            				}
            			}
            			return true;
            		}else if(x<m && y>n) {
            			for(int i=1;i<=j;i++) {
            				if(!isEmpty(chessBoardSquares[x+i][y-i])){
            					if(x+i!=m || mapeo[x+i+8*(y-i)]<10 ) {return false;}
            				}
            			}
            			return true;
            		}else if(x>m && y<n) {
            			for(int i=1;i<=j;i++) {
            				if(!isEmpty(chessBoardSquares[x-i][y+i])){
            					if(x-i!=m || mapeo[x-i+8*(y+i)]<10 ) {return false;}
            				}
            			}
            			return true;
            			
            		}else if(x<m && y<n) {
            			for(int i=1;i<=j;i++) {
            				if(!isEmpty(chessBoardSquares[x+i][y+i])) {
            					if(x+i!=m || mapeo[x+i+8*(y+i)]<10 ) {return false;}
            				}
            			}
            			return true;
            		}
            		return false;
            	}else {return false;}
        	}
        	
        case 15:
        	if(x==m) {
        		if(y<n) {
	        		for(int i = y+1; i<=n;i++) {
	        			if(!isEmpty(chessBoardSquares[x][i])) {
	        				
	        				if(i!=n || mapeo[x+8*i]>10) {return false;}
	        			}	        			
	        		}
        		}
        		if(y>n) {
        			for(int i = n; i<y;i++) {
            			if(!isEmpty(chessBoardSquares[x][i])){            				
            				if(i!=n || mapeo[x+8*i]>10) {;return false;}
            			}
            		}
        		}
        		return true;
        	}else if(y==n) {
        		if(x<m) {
	        		for(int i = x+1; i<=m;i++) {
	        			if(!isEmpty(chessBoardSquares[i][y])) {
	        				if( i!=m || mapeo[i+8*y]>10) {return false;}
	        			}
	        		}
        		}
        		if(x>m) {
        			for(int i = m; i<x;i++) {
            			if(!isEmpty(chessBoardSquares[i][y])) {
            				if( i!=m || mapeo[i+8*y]>10) {return false;}
            			}
            		}
        		}return true;
        	}else {
        		j= Math.abs(x-m);
            	if(j>0 && j == Math.abs(y-n)) {
            		if(x>m && y>n) {
            			for(int i=1;i<=j;i++) {
            				if(!isEmpty(chessBoardSquares[x-i][y-i])){
            					if(x-i!=m || mapeo[x-i+8*(y-i)]>10) {return false;}
            				}
            			}
            			return true;
            		}else if(x<m && y>n) {
            			for(int i=1;i<=j;i++) {
            				if(!isEmpty(chessBoardSquares[x+i][y-i])){
            					if(x+i!=m || mapeo[x+i+8*(y-i)]>10) {return false;}
            				}
            			}
            			return true;
            		}else if(x>m && y<n) {
            			for(int i=1;i<=j;i++) {
            				if(!isEmpty(chessBoardSquares[x-i][y+i])) {
            					if(x-i!=m || mapeo[x-i+8*(y+i)]>10) {return false;}
            				}
            			}
            			return true;
            			
            		}else if(x<m && y<n) {
            			for(int i=1;i<j;i++) {
            				if(!isEmpty(chessBoardSquares[x+i][y+i])){
            					if(x+i!=m || mapeo[x+i+8*(y+i)]>10) {return false;}
            				}
            			}
            			return true;
            		}
            		return false;
            	}else {return false;}
        	}
        case 6:
        	if(Math.abs(m-x)<=1 && Math.abs(y-n)<=1) {
        		if(!isEmpty(chessBoardSquares[m][n])) {
        			if(mapeo[m+8*n]<10) {return false;}
        		}
        		return true;
        	}else {
        		return false;
        	}
        case 16:
        	if(Math.abs(m-x)<=1 && Math.abs(y-n)<=1) {
        		if(!isEmpty(chessBoardSquares[m][n])) {
        			if(mapeo[m+8*n]>10) {return false;}
        		}
        		return true;
        	}else {
        		return false;
        	}
        }
        return false;
    }
    
    /*protected boolean comerPieza(JButton origen, JButton destino) {
    	int x=-1,y=-1,m=-1,n=-1;
        for(int i = 0 ; i<8;i++){
            for(int j = 0 ; j<8; j++){
                if(chessBoardSquares[i][j].equals(origen)){
                    x=i;
                    y=j;
                }
            }
        }
        if(mapeo[y*8+x]==1 || mapeo[y*8+x]==11) {
        	for(int i = 0 ; i<8;i++){
                for(int j = 0 ; j<8; j++){
                    if(chessBoardSquares[i][j].equals(destino)){
                        m=i;
                        n=j;
                    }
                }
            }
        	if(mapeo[y*8+x]==1 && (y+1 != n || Math.abs(x-m)!=1)) {
        		return false;
        	}else if(mapeo[y*8+x]==11 && (y-1 != n || Math.abs(x-m)!=1)){
        		
        		return false;
        	}
        	destino.setIcon(origen.getIcon());
            origen.setIcon(null);
        	mapeo[n*8+m]=mapeo[y*8+x];
            mapeo[y*8+x]=0;
            
            frame.pack();
            return true;
        }else {return movePiece(origen,destino);}
        
        
	}*/



    public boolean isWhite(JButton casilla) {
    	int n=0;
        for(int i = 0; i<8;i++) {
        	for(int j=0;j<8;j++) {
        		if(chessBoardSquares[i][j].equals(casilla)) {
        			n=j*8+i;
        			break;
        		}
        	}
        }
        return mapeo[n]>=10;
    }

    public boolean isBlack(JButton casilla){
    	int n=0;
        for(int i = 0; i<8;i++) {
        	for(int j=0;j<8;j++) {
        		if(chessBoardSquares[i][j].equals(casilla)) {
        			n=j*8+i;
        			break;
        		}
        	}
        }
        return mapeo[n]<10 && mapeo[n]>0;
    }

    public boolean isEmpty(JButton casilla){
        return casilla.getIcon()==null;
    }

    public boolean check(boolean colorBlanco){
    	if(colorBlanco) {
    		for(int n = 0 ;n<64;n++) {
    			if(mapeo[n]>10) {
    				for(int i=0;i<8;i++) {
    					for(int j=0;j<8;j++) {
    						if(mapeo[j*8+i]==6 && movimientoCorrecto(chessBoardSquares[n%8][n/8],chessBoardSquares[i][j])) {
    							return true;
    						}
    					}
    				}
    			}
    		}
    		return false;
    	}else {
    		for(int n = 0 ;n<64;n++) {
    			if(mapeo[n]>0 && mapeo[n]<10 ) {
    				for(int i=0;i<8;i++) {
    					for(int j=0;j<8;j++) {
    						if(mapeo[j*8+i]==16 && movimientoCorrecto(chessBoardSquares[n%8][n/8],chessBoardSquares[i][j])) {
    							return true;
    						}
    					}
    				}
    			}
    		}
    		return false;
    	}        
    }

    private boolean checkMate(boolean colorBlanco) {
    	int naux;
    	Icon iaux;
    	boolean evitaJaque;
    	if(check(colorBlanco)) {
	    	if(colorBlanco) {
	    		for(int n = 0 ;n<64;n++) {
	    			if(mapeo[n]>0 && mapeo[n]<10 ) {
	    				for(int i=0;i<8;i++) {
	    					for(int j=0;j<8;j++) {
	    						naux=mapeo[j*8+i];
	    						iaux=chessBoardSquares[i][j].getIcon();
	    						evitaJaque=evitaJaque(chessBoardSquares[n%8][n/8],chessBoardSquares[i][j]) ;	    						
	    						if(evitaJaque) {
	    							mapeo[n]=mapeo[j*8+i];
		    						mapeo[j*8+i]=naux;
		    						chessBoardSquares[n%8][n/8].setIcon(chessBoardSquares[i][j].getIcon());
		    						chessBoardSquares[i][j].setIcon(iaux);
	    							return false;
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}else {
	    		for(int n = 0 ;n<64;n++) {
	    			if(mapeo[n]>10) {
	    				for(int i=0;i<8;i++) {
	    					for(int j=0;j<8;j++) {
	    						naux=mapeo[j*8+i];
	    						iaux=chessBoardSquares[i][j].getIcon();
	    						evitaJaque=evitaJaque(chessBoardSquares[n%8][n/8],chessBoardSquares[i][j]) ;	    						
	    						if(evitaJaque) {
	    							mapeo[n]=mapeo[j*8+i];
		    						mapeo[j*8+i]=naux;
		    						chessBoardSquares[n%8][n/8].setIcon(chessBoardSquares[i][j].getIcon());
		    						chessBoardSquares[i][j].setIcon(iaux);
	    							return false;
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	        return true;
    	}else {return false;}
    }
	private boolean evitaJaque(JButton origen, JButton destino) {
		if(!movimientoCorrecto(origen,destino) ){return false;}
        Icon aux =destino.getIcon();
        destino.setIcon(origen.getIcon());
        origen.setIcon(null);
        
        int x=-1,y=-1,m=-1,n=-1;
        for(int i = 0 ; i<8;i++){
            for(int j = 0 ; j<8; j++){
                if(chessBoardSquares[i][j].equals(origen)){
                    x=i;
                    y=j;
                }
            }
        }
        
        for(int i = 0 ; i<8;i++){
            for(int j = 0 ; j<8; j++){
                if(chessBoardSquares[i][j].equals(destino)){
                    m=i;
                    n=j;
                }
            }
        }
        int naux =mapeo[n*8+m];
        mapeo[n*8+m]=mapeo[y*8+x];
        mapeo[y*8+x]=0;
        if(check(turnoBlancas)){  
        	origen.setIcon(destino.getIcon());
        	destino.setIcon(aux);
        	mapeo[y*8+x]=mapeo[n*8+m];
        	mapeo[n*8+m]=naux;
        	return false;
        }
        
        frame.pack();
        return true;
	}
	public void printMapeo() {
		for(int i=0; i<64;i++) {
			System.out.print(mapeo[i]+" ");
			if(i%8==7) {
				System.out.println();
			}
		}
		
	}
	public void setText(String string) {
		message.setText(string);
	}
	
	public boolean reyAhogado(boolean colorBlancas) {
		for(int n=0;n<64;n++) {
			if(colorBlancas) {
				if(mapeo[n]>10) {
					for(int i=0;i<8;i++) {
						for(int j=0;j<8;j++) {
							if(movimientoCorrecto(chessBoardSquares[n%8][n/8],chessBoardSquares[i][j])) {
								return false;
							}
						}
					}
				}
			}else {
				if(mapeo[n]>0 && mapeo[n]<10) {
					for(int i=0;i<8;i++) {
						for(int j=0;j<8;j++) {
							if(movimientoCorrecto(chessBoardSquares[n%8][n/8],chessBoardSquares[i][j])) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
    
    

    /*public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                ChessGUI cg = new ChessGUI("Mi Ajedrez");

                frame = new JFrame("MiAjedrez");
                frame.add(cg.getGui());
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See https://stackoverflow.com/a/7143398/418556 for demo.
                frame.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                frame.pack();
                // ensures the minimum size is enforced.
                frame.setMinimumSize(frame.getSize());
                frame.setVisible(true);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }*/
}
