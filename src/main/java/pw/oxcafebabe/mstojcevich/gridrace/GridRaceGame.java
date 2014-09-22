package pw.oxcafebabe.mstojcevich.gridrace;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import pw.oxcafebabe.mstojcevich.gridrace.map.Map;
import pw.oxcafebabe.mstojcevich.gridrace.map.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by marcusant on 9/21/2014.
 */
public class GridRaceGame extends BasicGameState {

    private Map currentMap;
    private Player player;

    private Image optionsImage;

    private Image oobBlockImage;
    private Image availableBlockImage;
    private Image youLose;

    private int moveCount = 0, hsMovecount = 0;
    private boolean lost;

    private int mapNumber = 1;

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        optionsImage = new Image(48, 48);
        oobBlockImage = new Image("sprites/OOBBlock.png");
        availableBlockImage = new Image("sprites/AvailableBlock.png");
        youLose = new Image("sprites/YouLose.png");

        try {
            this.newGame(new Map(this.getClass().getResourceAsStream("/maps/map" + mapNumber)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame game, int i) throws SlickException {
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame game, Graphics graphics) throws SlickException {
        if(currentMap != null) {
            currentMap.render(graphics);
            this.renderOptions(graphics);
            player.render(graphics);
            if(lost) {
                graphics.drawImage(youLose, 0, 0);
            }
            graphics.drawString("Moves: " + moveCount, 10, 30);
            graphics.drawString("High score: " + hsMovecount, 10, 30);
        } else {
            //TODO render main menu
            graphics.drawString("Failed to load map", 20, 50);
        }
    }

    private void renderOptionsToImage(Image img) throws SlickException {
        if(currentMap == null)return;
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
                    if(currentMap.isBlockWin(newX, newY)) {
                        this.onWin();
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

    private void onWin() {
        this.saveHighScore();

        this.mapNumber++;
        try {
            this.newGame(new Map(this.getClass().getResourceAsStream("maps/map" + mapNumber)));
        } catch (Exception e) {
            e.printStackTrace();
            this.currentMap = null;
        }
    }

    private void newGame(Map map) throws SlickException {
        if(player == null)player = new Player(new Point(0, 0));
        currentMap = map;
        player.setPosition((Point2D)currentMap.getStartingPoint().clone());
        player.resetVelocity();
        this.renderOptionsToImage(optionsImage);
        moveCount = 0;
        this.loadHighScore();
    }

    private void loadHighScore() {
        this.hsMovecount = 0;
        String fileName = "hs" + File.separator + "map" + this.mapNumber + "hs";
        if(new File(fileName).exists()) {
            try {
                this.hsMovecount = Integer.parseInt(new String(Files.readAllBytes(Paths.get(fileName))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveHighScore() {
        String fileName = "hs" + File.separator + "map" + this.mapNumber + "hs";
        File file = new File(fileName);
        new File(file.getParent()).mkdirs();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.write(Paths.get(fileName), Integer.toString(Math.min(this.moveCount,
                    this.hsMovecount == 0 ? Integer.MAX_VALUE :
                            this.hsMovecount)).getBytes(),
                                StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
