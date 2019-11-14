import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File ;
import java.io.* ;

public class End extends JFrame implements ActionListener {
    private Button btn1;
    private Button btn2;

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
        btn1 = new Button("Quit Game");
        btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
			}
        });

        btn2 = new Button("Restart Game");
        btn2.addActionListener(this);

        add(btn1);
        add(btn2);

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
