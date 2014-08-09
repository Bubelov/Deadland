package com.deadland.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.deadland.EntityManager;

/**
 * Created by inver on 09.08.2014.
 */
public class Bullet extends Entity {
    public static Texture texture = new Texture("bullet.png");

    private float speed = 500;

    private float power = 2;

    private Vector2 pos;
    private float age = 0;
    private float liveTime = 10;

    public Bullet(float x, float y, float dstX, float dstY) {
        pos = new Vector2(dstX - x, dstY - y);

        sprite = new Sprite(texture);
        sprite.setSize(8, 2);
        sprite.setCenter(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        sprite.setPosition(x, y);
        sprite.setRotation(pos.angle());

        boundingCircle = new Circle(x() + sprite.getWidth() / 2, y() + sprite.getHeight() / 2, 5);
    }

    @Override
    public void update() {
        super.update();

        float deltaSecs = Gdx.graphics.getDeltaTime();
        age = age + deltaSecs;
        if (age > liveTime) {
            EntityManager.instance.destroy(this);
        } else {
            pos.add(pos.nor().x * deltaSecs * speed, pos.nor().y * deltaSecs * speed);
            sprite.translate(pos.x, pos.y);
            boundingCircle.setPosition(x() + sprite.getWidth() / 2, y() + sprite.getHeight() / 2);
        }
    }

    @Override
    public void onCollision(Entity entity) {
        if (entity instanceof Zombie) {
            ((Zombie) entity).wound(power);
            if (((Zombie) entity).isDead()) {
                EntityManager.instance.destroy(entity);
                EntityManager.instance.add(new BloodMess(entity.x(), entity.y()));
            }
            EntityManager.instance.destroy(this);
        }
    }
}
