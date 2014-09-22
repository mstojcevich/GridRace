package pw.oxcafebabe.mstojcevich.gridrace;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import pw.oxcafebabe.mstojcevich.gridrace.map.Map;
import pw.oxcafebabe.mstojcevich.gridrace.map.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

/**
 * Created by marcusantlo on 9/21/2014.
 */
public class GridRace extends BasicGame {

    public static final int WIDTH = 640, HEIGHT = 480;

    private static final String GAME_NAME = "GridRace";

    private Map currentMap;
    private Player player;

    private Image optionsImage;

    private Image oobBlockImage;
    private Image availableBlockImage;
    private Image youLose;

    private int moveCount = 0;
    private boolean lost;

    public static void main(String[] args) throws Exception {
        AppGameContainer gameContainer = new AppGameContainer(new GridRace(GAME_NAME));
        gameContainer.setDisplayMode(WIDTH, HEIGHT, false);
        gameContainer.start();
    }

    public GridRace(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        optionsImage = new Image(48, 48);
        oobBlockImage = new Image("sprites/OOBBlock.png");
        availableBlockImage = new Image("sprites/AvailableBlock.png");
        youLose = new Image("sprites/YouLose.png");

        try {
            this.newGame(new Map(this.getClass().getResourceAsStream("/maps/testMap")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if(currentMap != null) {
            currentMap.render(graphics);
            this.renderOptions(graphics);
            player.render(graphics);
            if(lost) {
                graphics.drawImage(youLose, 0, 0);
            }
            graphics.drawString("Moves: " + moveCount, 10, 30);
        } else {
            //TODO render main menu
            graphics.drawString("Failed to load map", 20, 50);
        }
    }

    private void renderOptionsToImage(Image img) throws SlickException {
        Graphics g = img.getGraphics();

        int targetPointX = (int)player.getTargetPoint().getX();
        int targetPointY = (int)player.getTargetPoint().getY();

        int validPoints = 0;
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                boolean blockValid = currentMap.isBlockValid(targetPointX - 1 + x, targetPointY - 1 + y);
                g.drawImage(blockValid
                        ? availableBlockImage : oobBlockImage, x*16, y*16);
                if(blockValid && !(targetPointX - 1 + x == player.getPosition().getX() && targetPointY - 1 + y == player.getPosition().getY())) {
                    validPoints++;
                }
            }
        }
        
        this.lost = validPoints <= 0;

        g.flush();
    }

    private void renderOptions(Graphics g) {
        int targetPointX = (int)player.getTargetPoint().getX()*16;
        int targetPointY = (int)player.getTargetPoint().getY()*16;

        g.drawImage(this.optionsImage, targetPointX - 16, targetPointY - 16);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if(lost) {
            this.lost = false;
            try {
                this.newGame(this.currentMap);
            } catch (SlickException e) {
                e.printStackTrace();
            }
        } else {
            int glY = y;

            int targetPointX = (int) player.getTargetPoint().getX() * 16;
            int targetPointY = (int) player.getTargetPoint().getY() * 16;
            if (x < targetPointX + 32 && x > targetPointX - 16) {
                if (glY < targetPointY + 32 && y > targetPointY - 16) {
                    int newX = (int) Math.floor(x / 16);
                    int newY = (int) Math.floor(glY / 16);
                    if (currentMap.isBlockValid(newX, newY) && player.getPosition().distance(newX, newY) > 0) {
                        player.offsetPosition(newX - (int) player.getPosition().getX(), newY - (int) player.getPosition().getY());
                        moveCount++;
                    }
                    try {
                        this.renderOptionsToImage(optionsImage);
                    } catch (SlickException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void newGame(Map map) throws SlickException {
        if(player == null)player = new Player(new Point(0, 0));
        currentMap = map;
        player.setPosition((Point2D)currentMap.getStartingPoint().clone());
        System.out.println(currentMap.getStartingPoint().getX());
        player.resetVelocity();
        this.renderOptionsToImage(optionsImage);
        moveCount = 0;
    }
}