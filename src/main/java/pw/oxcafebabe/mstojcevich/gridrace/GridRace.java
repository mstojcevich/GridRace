package pw.oxcafebabe.mstojcevich.gridrace;

import org.newdawn.slick.*;

import java.io.File;

/**
 * Created by marcusant on 9/21/2014.
 */
public class GridRace extends BasicGame {

    private static final String GAME_NAME = "GridRace";

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
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.drawString("Hello world!", 10, 50);
    }
}