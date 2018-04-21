import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyGame extends JFrame implements KeyListener {
    static GamePanel component;

    public static void main(String[] args) {
        MyGame game = new MyGame();
        game.setResizable(false);
//        game.setAlwaysOnTop(true);
        game.setLocation(0, 0);
        game.setTitle("flappy голубь");
        game.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        component = new GamePanel();
        game.add(component);

        game.setExtendedState(JFrame.MAXIMIZED_BOTH);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setUndecorated(true);
        game.addKeyListener(game);

        game.setVisible(true);


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (component.gameMode == 0 || component.gameMode == 2) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                component.reset();
                component.StartTime = System.currentTimeMillis();
                component.gameMode = 1;
            }

        } else
        if (component.gameMode==1){
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                component.StartBirdCoordinate = component.lastBirdCoord;
                component.StartBirdSpeed = 30;
                component.StartTime = System.currentTimeMillis();
            }
            if (e.getKeyCode() == KeyEvent.VK_1) {
                component.gameMode = 0;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}

