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

    private static final String GAME_NAME = "GridRace";

    private Map currentMap;
    private Player player;

    private Image optionsImage;

    private Image oobBlockImage;
    private Image availableBlockImage;

    public static void main(String[] args) throws Exception {
        AppGameContainer gameContainer = new AppGameContainer(new GridRace(GAME_NAME));
        gameContainer.setDisplayMode(640, 480, false);
        gameContainer.start();
    }

    public GridRace(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        optionsImage = new Image(48, 48);
        oobBlockImage = new Image("sprites/OOBBlock.png");
        availableBlockImage =  new Image("sprites/AvailableBlock.png");

        player = new Player(new Point(0, 0));
        try {
            currentMap = new Map(this.getClass().getResourceAsStream("/maps/testMap"));
            player.setPosition(currentMap.getStartingPoint());
            this.renderOptionsToImage(optionsImage);
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
        } else {
            //TODO render main menu
            graphics.drawString("Failed to load map", 20, 50);
        }
    }

    private void renderOptionsToImage(Image img) throws SlickException {
        Graphics g = img.getGraphics();

        int targetPointX = (int)player.getTargetPoint().getX();
        int targetPointY = (int)player.getTargetPoint().getY();

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                g.drawImage(currentMap.isBlockValid(targetPointX - 1 + x, targetPointY - 1 + y)
                        ? availableBlockImage : oobBlockImage, x*16, y*16);
            }
        }

        g.flush();
    }

    private void renderOptions(Graphics g) {
        int targetPointX = (int)player.getTargetPoint().getX()*16;
        int targetPointY = (int)player.getTargetPoint().getY()*16;

        g.drawImage(this.optionsImage, targetPointX - 16, targetPointY - 16);
    }
}