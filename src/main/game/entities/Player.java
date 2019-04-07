package main.game.entities;

import main.game.boards.Camera;
import main.game.entities.hitboxes.BodyHitbox;
import main.game.entities.hitboxes.EntityRenderer;
import main.game.enums.Team;
import main.input.ControlMapping;
import main.input.InputManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import rendering.WindowManager;
import rendering.textures.Sprite;
import rendering.textures.SpriteData;
import rendering.textures.SpriteTexture;
import rendering.textures.TextureManager;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import static java.lang.Math.*;

public class Player extends Entity {
    private Sprite sprite;
    private final double maxHealth = 1;
    private double health = maxHealth;
    private double radius = 64;
    private double accel = 2000;
    private double maxSpeed = 300;

    private BodyHitbox hitbox;

    public Player() {
        super(Team.PLAYER);

        /*vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        int vboPositionID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboPositionID);
        FloatBuffer positionsBuffer = BufferUtils.createFloatBuffer(quadPositions.length);
        positionsBuffer.put(quadPositions);
        positionsBuffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionsBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);

        int vboTextureID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTextureID);
        FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(quadTextureCoords.length);
        textureBuffer.put(quadTextureCoords);
        textureBuffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);*/

        sprite = new Sprite(SpriteData.PLAYER, 0, 0, radius);

        hitbox = new BodyHitbox(this, team, radius) {
            @Override
            public void takeDamage(double damage, double knockback, double knockbackAngle) {
                health -= damage;
                xVel += knockback * Math.cos(knockbackAngle);
                yVel += knockback * Math.sin(knockbackAngle);
            }
        };

        bodyHitboxes = new BodyHitbox[] {hitbox};
    }

    @Override
    public void updatePre(double delta) {
        LinkedList<Double[]> heldKeys = InputManager.getHeldActionKeys();

        int moveX = 0;
        int moveY = 0;
        for(Double[] keys: heldKeys) {
            //System.out.println(keys[0] + " " + keys[1]);
            if(keys[0] == ControlMapping.MOVE_LEFT.keyCode)
                moveX--;
            if(keys[0] == ControlMapping.MOVE_RIGHT.keyCode)
                moveX++;
            if(keys[0] == ControlMapping.MOVE_DOWN.keyCode)
                moveY--;
            if(keys[0] == ControlMapping.MOVE_UP.keyCode)
                moveY++;
        }

        if(moveX == 0) {
            xVel = signum(xVel) * max(0, abs(xVel) - accel * delta);
        } else {
            xVel += moveX * accel * delta;
            xVel = signum(xVel) * min(maxSpeed, abs(xVel));
        }
        if(moveY == 0) {
            yVel = signum(yVel) * max(0, abs(yVel) - accel * delta);
        } else {
            yVel += moveY * accel * delta;
            yVel = signum(yVel) * min(maxSpeed, abs(yVel));
        }
    }

    @Override
    public void updatePost(double delta) {
        x += xVel * delta;
        y += yVel * delta;
        sprite.setRotation(sprite.getRotation() + 0.01);
        sprite.setImageIndex((int)(WindowManager.getTime() * 1.0) % 4);
        sprite.setPosition(x, y);
    }

    @Override
    public void registerSprites(EntityRenderer renderer) {
        renderer.registerSprite(sprite);

        //GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);


        /*GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(1, 0, 0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glVertex2d(x-w, y-w); GL11.glTexCoord2f(0, 0);
        GL11.glVertex2d(x+w, y-w); GL11.glTexCoord2f(1, 0);
        GL11.glVertex2d(x+w, y+w); GL11.glTexCoord2f(1, 1);
        GL11.glVertex2d(x-w, y+w); GL11.glTexCoord2f(0, 1);

        GL11.glEnd();*/
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }
}
