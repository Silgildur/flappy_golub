import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;

public class GamePanel extends JPanel implements ActionListener {
    static int gameMode = 0;
    private final int birdHeight = 50;
    private final int birdWidth = 50;
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
    int height = defaultToolkit.getScreenSize().height;
    int StartBirdSpeed;
    int StartBirdCoordinate = height / 2;
    int lastBirdCoord;
    Image birdImage;
    int score = 0;
    double g = 9.8;
    long StartTime;
    LinkedList<PipePair> pipes = new LinkedList<>();

    GamePanel() {
        try {
            birdImage = ImageIO.read(GamePanel.class.getResource("pigeon_PNG3408.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        javax.swing.Timer timer = new javax.swing.Timer(1000 / 100, this);


        timer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.BLUE);
        graphics.fillRect(0, 0, 900000000, 900000000);


        if (gameMode == 0) {
            graphics.setColor(Color.black);
            graphics.setFont(new Font("TimesRoman", Font.BOLD, 35));
            String pauseText = "нажмите пробел чтобы играть, или Esc чтобы СДОХНУТЬ(выйти) (пауза на 1)";
            graphics.drawString(
                    pauseText,
                    width / 2 - graphics.getFontMetrics().stringWidth(pauseText) / 2,
                    height / 2);
        }
        if (gameMode == 1) {
            int Coordinate = calculateBirdCoordinate();
            graphics.drawImage(birdImage, width / 4, Coordinate, birdWidth, birdHeight, null);
            processPipes();
            drawPipes(graphics);
            if (checkLost(Coordinate)) {
                gameMode = 2;
            }
            processScore();
            graphics.setFont(new Font("TimesRoman", Font.BOLD,40));
            graphics.setColor(Color.black);
            graphics.drawString(
                    String.valueOf(score),
                    width / 2 - graphics.getFontMetrics().stringWidth(String.valueOf(score)) / 2,
                    (int) (height * 0.1));
        } else if (gameMode == 2) {
            graphics.setColor(Color.black);
            graphics.setFont(new Font("TimesRoman", Font.BOLD, 35));
            String pauseText = "*вы нецензурно бранитесь*  ваш счёт:" + score;
            graphics.drawString(
                    pauseText,
                    width / 2 - graphics.getFontMetrics().stringWidth(pauseText) / 2,
                    height / 2);
        }
    }
    void playSound(String sound_name) {
        Media media = null;
        try {
            media = new Media(GamePanel.class.getResource(sound_name).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    void reset() {
        score = 0;
        pipes.clear();
        StartBirdCoordinate = height / 2;
    }



    int calculateBirdCoordinate() {
        long time = System.currentTimeMillis() - StartTime;
        time = time / 60;
        lastBirdCoord = (int) (StartBirdCoordinate - StartBirdSpeed * time + g * time * time / 2);
        return lastBirdCoord;
    }

    void processPipes() {
        if (pipes.size() == 0) {
            pipes.add(new PipePair(width + 50, height));
        } else {
            boolean addNewPipe = false;
            Iterator<PipePair> iterator = pipes.iterator();
            while (iterator.hasNext()) {
                PipePair pipe = iterator.next();
                int xCoordinate = pipe.calculateXCoordinate();
                if (xCoordinate + PipePair.pipeWidth < 0) {
                    iterator.remove();
                }
                if (!iterator.hasNext() &&
                        xCoordinate + PipePair.pipeWidth + PipePair.promegutokMezhduPipePairs < width) {
                    addNewPipe = true;
                }
            }
            if (addNewPipe) {
                pipes.add(new PipePair(width, height));
            }
        }
    }
    void processScore() {
        Iterator<PipePair> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            PipePair pipe = iterator.next();
            int xCoordinate = pipe.calculateXCoordinate();
            if (xCoordinate < width / 4 && !pipe.isCounted) {
                pipe.isCounted = true;
                score++;

                return;
            }
        }
    }

    void drawPipes(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        Iterator<PipePair> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            PipePair pipe = iterator.next();
            int xPipeCoord = pipe.calculateXCoordinate();
            int yCoord = pipe.yGapCoorddinate + PipePair.gapHeight / 2;
            graphics.fillRect(
                    xPipeCoord,
                    yCoord, PipePair.pipeWidth, height - yCoord);
            graphics.fillRect(xPipeCoord, 0, PipePair.pipeWidth,
                    pipe.yGapCoorddinate - PipePair.gapHeight / 2);

        }
    }

    boolean checkLost(int topBirdEdge) {
        int leftBirdEdge = width / 4;
        int rightBirdEdge = leftBirdEdge + birdWidth;
        int bottomBirdEdge = topBirdEdge + birdHeight;
        Iterator<PipePair> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            PipePair pipe = iterator.next();

            int leftPipeEdge = pipe.calculateXCoordinate();
            int rightPipeEdge = leftPipeEdge + PipePair.pipeWidth;
            int topGapEdge = pipe.yGapCoorddinate -  PipePair.gapHeight / 2;
            int bottomGapEdge = pipe.yGapCoorddinate + PipePair.gapHeight / 2;


            if (topBirdEdge < 0 || bottomBirdEdge > height) {
                return true;
            }
            if ((rightBirdEdge > leftPipeEdge && rightBirdEdge < rightPipeEdge)
                    || (leftBirdEdge > leftPipeEdge && leftBirdEdge < rightPipeEdge)) {
                if (topBirdEdge < topGapEdge || bottomBirdEdge > bottomGapEdge) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        defaultToolkit.sync();
    }
}
