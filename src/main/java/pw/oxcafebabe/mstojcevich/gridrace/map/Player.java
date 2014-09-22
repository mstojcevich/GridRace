package pw.oxcafebabe.mstojcevich.gridrace.map;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by marcusant on 9/21/2014.
 */
public class Player {

    private Point2D position, targetPoint;
    private int velocityUp, velocityOver;
    private Map map;

    private static final Image playerImage;

    static {
        org.newdawn.slick.Image playerImageTemp;
        try {
            playerImageTemp = new org.newdawn.slick.Image("sprites/Player.png");
        } catch (SlickException e) {
            playerImageTemp = null;
            e.printStackTrace();
        }
        playerImage = playerImageTemp;
    }

    public Player(Point2D startPosition, Map map) {
        this.map = map;
        this.position = startPosition;
        this.targetPoint = new Point((int)this.position.getX() + this.velocityOver, (int)this.position.getY() + this.velocityUp);
    }

    public void render(Graphics g) {
        g.drawImage(playerImage, ((int)position.getX())*map.getTileWidth(), ((int)position.getY())*map.getTileHeight());
    }

    public void offsetPosition(int deltaX, int deltaY) {
        this.velocityOver = deltaX;
        this.velocityUp = deltaY;
        this.position.setLocation(this.position.getX() + deltaX, this.position.getY() + deltaY);
        this.targetPoint = new Point((int)this.position.getX() + this.velocityOver, (int)this.position.getY() + this.velocityUp);
    }

    public void setPosition(Point2D position) {
        this.position = position;
        this.targetPoint = new Point((int)this.position.getX() + this.velocityOver, (int)this.position.getY() + this.velocityUp);
    }

    public Point2D getPosition() {
        return this.position;
    }

    public void resetVelocity() {
        this.velocityUp = 0;
        this.velocityOver = 0;
    }

    public Point2D getTargetPoint() {
        this.targetPoint = new Point((int)this.position.getX() + this.velocityOver, (int)this.position.getY() + this.velocityUp);
        return this.targetPoint;
    }

    public int getVelocityUp() {
        return this.velocityUp;
    }

    public int getVelocityOver() {
        return this.velocityOver;
    }

    public void setMap(Map map) {
        this.map = map;
    }

}
