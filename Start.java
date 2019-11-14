import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File ;
import java.io.* ;

public class Start extends JFrame implements ActionListener {
    private Button btn1;
    private Button btn2;

    public Start() {
        try {
            final Image backgroundImage = javax.imageio.ImageIO.read(new File("bg.jpg"));
            setContentPane(new JPanel(new BorderLayout()) {
                    @Override public void paintComponent(Graphics g) {
                        g.drawImage(backgroundImage, 0, 0, null);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setLayout(new FlowLayout());
        btn1 = new Button("New Game");
        btn1.addActionListener(this); //this refers to your current frame
        add(btn1);
        setTitle("Welcome to Typer-Man-Game");
        setSize(400, 400);
        setVisible(true);
    }

    public static void main(String[] args){
        new Start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            TyperManPanel newgame = new TyperManPanel();
            this.setVisible(false);
            newgame.setVisible(true);
            this.dispose();
        } catch (FileNotFoundException E) {
            System.exit(0);
        }
    }
}
