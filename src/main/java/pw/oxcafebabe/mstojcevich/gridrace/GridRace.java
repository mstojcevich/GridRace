package pw.oxcafebabe.mstojcevich.gridrace;

import org.newdawn.slick.*;
import pw.oxcafebabe.mstojcevich.gridrace.map.Map;

import java.io.File;
import java.io.IOException;

/**
 * Created by marcusantlo on 9/21/2014.
 */
public class GridRace extends BasicGame {

    private static final String GAME_NAME = "GridRace";

    private Map currentMap;

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
        try {
            currentMap = new Map(this.getClass().getResourceAsStream("/maps/testMap"));
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
        } else {
            //TODO render main menu
            graphics.drawString("Failed to load map", 20, 50);
        }
    }
}