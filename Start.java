import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File ;
import java.io.* ;

public class Start extends JFrame implements ActionListener {
    private Button newGameButton;

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

        newGameButton = new Button("New Game");
        newGameButton.addActionListener(this); //this refers to your current frame
        add(newGameButton);

        setTitle("Welcome to TyperMan!");
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
