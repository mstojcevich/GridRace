package pw.oxcafebabe.mstojcevich.gridrace;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import pw.oxcafebabe.mstojcevich.gridrace.map.Map;
import pw.oxcafebabe.mstojcevich.gridrace.map.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

/**
 * Created by marcusantlo on 9/21/2014.
 */
public class GridRace extends StateBasedGame {

    public static final int WIDTH = 640, HEIGHT = 480;

    private static final String GAME_NAME = "GridRace";

    public static final int MENU_ID = 0, GAME_ID = 1;

    public static void main(String[] args) throws Exception {
        AppGameContainer gameContainer = new AppGameContainer(new GridRace(GAME_NAME));
        gameContainer.setDisplayMode(WIDTH, HEIGHT, false);
        gameContainer.start();
    }

    public GridRace(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new GridRaceMenu());
        this.addState(new GridRaceGame());
    }
}