package pw.oxcafebabe.mstojcevich.gridrace.map;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcusant on 9/21/2014.
 */
public class Map {

    private final Boolean[][] blockValidity;
    private final Boolean[][] winList;
    private static final Image oobBlockImage;
    private static final Image blockImage;

    private Point2D startingPoint;

    static {
        Image oobBlockImage1;
        Image blockImage1;
        try {
            oobBlockImage1 = new Image("sprites/OutsideBlock.png");
        } catch (SlickException e) {
            oobBlockImage1 = null;
            e.printStackTrace();
        }
        oobBlockImage = oobBlockImage1;
        try {
            blockImage1 = new Image("sprites/Block.png");
        } catch (SlickException e) {
            blockImage1 = null;
            e.printStackTrace();
        }
        blockImage = blockImage1;
    }

    private Image mapImage;

    public Map(InputStream mapFileStream) throws IOException, SlickException {
        BufferedReader mapReader = new BufferedReader(new InputStreamReader(mapFileStream));
        List<Boolean[]> validityList = new ArrayList<Boolean[]>();
        List<Boolean[]> finishList = new ArrayList<Boolean[]>();
        String currentLine;
        int x = 0, y = 0;
        while((currentLine = mapReader.readLine()) != null) {
            List<Boolean> currentLineValidity = new ArrayList<Boolean>();
            List<Boolean> currentLineFinish = new ArrayList<Boolean>();
            for(char c : currentLine.toCharArray()) {
                if(Character.toLowerCase(c) == 's') {
                    this.startingPoint = new Point(x, y);
                }
                if(Character.toLowerCase(c) == 'f') {
                    currentLineFinish.add(true);
                } else {
                    currentLineFinish.add(false);
                }
                currentLineValidity.add(c == '1' || c == 'f' || c == 's' ? true : false);
                x+=1;
            }
            validityList.add(currentLineValidity.toArray(new Boolean[]{}));
            finishList.add(currentLineFinish.toArray(new Boolean[]{}));
            x=0;
            y+=1;
        }
        blockValidity = validityList.toArray(new Boolean[][]{});
        winList = finishList.toArray(new Boolean[][]{});

        mapImage = new Image(640, 480);
        renderMapToImage(mapImage);
    }

    public boolean isBlockValid(int x, int y) {
        if(y > blockValidity.length-1 || y < 0) {
            return false;
        } else if(x > blockValidity[y].length-1 || x < 0) {
            return false;
        }
        return blockValidity[y][x];
    }

    public boolean isBlockWin(int x, int y) {
        if(y > winList.length-1 || y < 0) {
            return false;
        } else if(x > winList[y].length-1 || x < 0) {
            return false;
        }
        return winList[y][x];
    }

    private void renderMapToImage(Image target) throws SlickException {
        Graphics g = target.getGraphics();

        int x = 0;
        int y = 0;
        for(Boolean[] booleans : blockValidity) {
            for(boolean blockValid : booleans) {
                g.drawImage(blockValid ? blockImage : oobBlockImage, x, y);
                x += 16;
            }
            x = 0;
            y += 16;
        }

        g.flush();
    }

    public void render(Graphics g) {
        g.drawImage(this.mapImage, 0, 0);
    }

    public Point2D getStartingPoint() {
        return this.startingPoint;
    }

}
