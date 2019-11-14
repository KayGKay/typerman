import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;

public class End extends JFrame implements ActionListener {
    private Button quitButton;
    private Button restartButton;

    public End() {
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
        quitButton = new Button("Quit Game");
        quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
			}
        });

        restartButton = new Button("Restart Game");
        restartButton.addActionListener(this);

        add(quitButton);
        add(restartButton);

        setTitle("Game Over!");
        setSize(400, 400);
        setVisible(true);
    }

    public static void main(String[] args){
        new End();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
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
