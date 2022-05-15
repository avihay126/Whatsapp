import javax.swing.*;

public class Window extends JFrame {

    public static final int WIN_WID = 900, WIN_HEI = 700, START_X = 0, START_Y = 0;

    public static void main(String[] args) {
        Window window = new Window();
    }

    public Window() {
        this.setSize(WIN_WID, WIN_HEI);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        this.setTitle("WhatsApp Web");
        WhatsAppScene whatsAppScene=new WhatsAppScene(START_X,START_Y,WIN_WID,WIN_HEI);
        this.add(whatsAppScene);
        this.setVisible(true);


    }
}