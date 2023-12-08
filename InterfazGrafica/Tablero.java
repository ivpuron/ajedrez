package InterfazGrafica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tablero extends JFrame {
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private static JButton[][] casillas = new JButton[8][8];
    private JPanel tablero;
    private final JLabel message = new JLabel(
            "Chess Champ is ready to play!");
    private static final String COLS = "ABCDEFGH";

    public Tablero() {
        initializeGui();
    }

    public final void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(new JButton("New")); // TODO - add functionality!
        tools.add(new JButton("Save")); // TODO - add functionality!
        tools.add(new JButton("Restore")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(new JButton("Resign")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(message);

        gui.add(new JLabel("?"), BorderLayout.LINE_START);

        tablero = new JPanel(new GridLayout(0, 9));
        tablero.setBorder(new LineBorder(Color.BLACK));
        gui.add(tablero);


        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < casillas.length; ii++) {
            for (int jj = 0; jj < casillas[ii].length; jj++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1)
                        //) {
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }
                casillas[jj][ii] = b;
            }
        }

        tablero.add(new JLabel(""));
        // fill the top row
        for (int ii = 0; ii < 8; ii++) {
            tablero.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                            SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                switch (jj) {
                    case 0:
                        tablero.add(new JLabel("" + (ii + 1),
                                SwingConstants.CENTER));
                    default:
                        tablero.add(casillas[jj][ii]);
                }
            }
        }
    }

    public final JComponent getTablero() {
        return tablero;
    }

    public final JComponent getGui() {
        return gui;
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Tablero tb =
                        new Tablero();

                JFrame f = new JFrame("Ajedrez");
                f.add(tb.getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);
                //dibujarPiezas();

                f.pack();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    /*private static void dibujarPiezas() {
        Font font = new Font("Chess Cases", Font.TRUETYPE_FONT, 46);
        String piezas = "tmvwlvmt";

        for (int i = 0; i < casillas.length; i++) {

            // Inserción piezas negras
            g.setColor(Color.BLACK);
            rect.setBounds(radio * i, 0, radio, radio);
            centrarTexto(g, piezas.charAt(i) + "", rect, font);
            rect.setBounds(radio * i, radio, radio, radio);
            centrarTexto(g, "o", rect, font);

            // Inserción piezas blancas
            g.setColor(Color.WHITE);
            rect.setBounds(radio * i, radio * 7, radio, radio);
            centrarTexto(g, piezas.charAt(i) + "", rect, font);
            rect.setBounds(radio * i, radio * 6, radio, radio);
            centrarTexto(g, "o", rect, font);

        }
    }*/
}


