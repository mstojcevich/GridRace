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
        oobBlockImage = new Image("sprites/OOBBlock.png");
        oobBlockImage.setFilter(Image.FILTER_NEAREST);
        availableBlockImage = new Image("sprites/AvailableBlock.png");
        availableBlockImage.setFilter(Image.FILTER_NEAREST);
        youLose = new Image("sprites/YouLose.png");
        youLose.setFilter(Image.FILTER_NEAREST);

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
            graphics.drawString("High score: " + hsMovecount, 10, 50);
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
                boolean blockValid = !this.isPathBlocked((int)player.getPosition().getX(), (int)player.getPosition().getY(),
                        targetPointX - 1 + x, targetPointY - 1 + y) && currentMap.isBlockValid(targetPointX - 1 + x, targetPointY - 1 + y);
                g.drawImage(blockValid
                        ? availableBlockImage : oobBlockImage, x*currentMap.getTileWidth(), y*currentMap.getTileHeight(),
                        x*currentMap.getTileWidth()+currentMap.getTileWidth(),
                        y*currentMap.getTileHeight()+currentMap.getTileHeight(),
                        0, 0, 16, 16);
                if(blockValid && !(targetPointX - 1 + x == player.getPosition().getX() && targetPointY - 1 + y
                        == player.getPosition().getY())) {
                    validPoints++;
                }
            }
        }

        this.lost = validPoints <= 0;

        g.flush();
    }

    /**
     * Credits to tech-algorithm.com for the bresenham implementation
     */
    private boolean isPathBlocked(int startX, int startY, int targetX, int targetY) {
        int w = targetX - startX;
        int h = targetY - startY;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            if(!currentMap.isBlockValid(startX, startY)) {
                return true;
            }
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                startX += dx1 ;
                startY += dy1 ;
            } else {
                startX += dx2 ;
                startY += dy2 ;
            }
        }

        return false;
    }

    private void renderOptions(Graphics g) {
        int targetPointX = (int)player.getTargetPoint().getX()*currentMap.getTileWidth();
        int targetPointY = (int)player.getTargetPoint().getY()*currentMap.getTileHeight();

        g.drawImage(this.optionsImage, targetPointX - currentMap.getTileWidth(), targetPointY - currentMap.getTileHeight());
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if(button == 2) {
            this.onWin();
            return;
        }

        if(lost) {
            this.lost = false;
            try {
                this.newGame(this.currentMap);
            } catch (SlickException e) {
                e.printStackTrace();
            }
        } else {
            int glY = y;

            int targetPointX = (int) player.getTargetPoint().getX() * currentMap.getTileWidth();
            int targetPointY = (int) player.getTargetPoint().getY() * currentMap.getTileHeight();
            if (x < targetPointX + currentMap.getTileWidth()*2 && x > targetPointX - currentMap.getTileWidth()) {
                if (glY < targetPointY + currentMap.getTileHeight()*2 && y > targetPointY - currentMap.getTileHeight()) {
                    int newX = (int) Math.floor(x / currentMap.getTileWidth());
                    int newY = (int) Math.floor(glY / currentMap.getTileHeight());
                    boolean blockValid = !this.isPathBlocked((int)player.getPosition().getX(), (int)player.getPosition().getY(),
                            newX, newY) && currentMap.isBlockValid(newX, newY);
                    if (blockValid && player.getPosition().distance(newX, newY) > 0) {
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
            this.newGame(new Map(this.getClass().getResourceAsStream("/maps/map" + mapNumber)));
        } catch (Exception e) {
            e.printStackTrace();
            this.currentMap = null;
        }
    }

    private void newGame(Map map) throws SlickException {
        if(player == null)player = new Player(new Point(0, 0), map);
        currentMap = map;
        optionsImage = new Image(currentMap.getTileWidth()*3, currentMap.getTileHeight()*3);
        optionsImage.setFilter(Image.FILTER_NEAREST);
        player.setPosition((Point2D)currentMap.getStartingPoint().clone());
        player.setMap(map);
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
