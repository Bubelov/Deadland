package com.deadland.model.menu;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.deadland.Chelper;
import com.deadland.ControlManager;
import com.deadland.EHelper;
import com.deadland.EntityManager;
import com.deadland.model.Entity;

/**
 * Created by inver on 10.08.2014.
 */
public abstract class MenuButton extends Entity {
    public static Texture texture = new Texture("menu_gunTower.png");

    private Vector2 pos;
    private Entity spirit = null;

    public MenuButton(float x, float y) {
        pos = new Vector2(x, y);
        sprite = new Sprite(getTexture());
        sprite.setSize(32, 32);
        sprite.setCenter(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setPosition(x, y);

        z = -1000;
    }

    protected abstract Texture getTexture();

    @Override
    public void update() {
        Camera c = Chelper.camera();
        sprite.setPosition(
                c.position.x - c.viewportWidth / 2 + pos.x,
                c.position.y - c.viewportHeight / 2 + pos.y
        );
        super.update();
    }

    @Override
    public void onTap(float x, float y, int count, int button) {
        if (isButtonDown(x, y)) {
            BuildingSpirit bs = createSpirit(x - 16, y - 16);
            if (ControlManager.instance.spirit != null) {
                ControlManager.instance.spirit.disable();
                EntityManager.instance.destroy(ControlManager.instance.spirit);
                if (ControlManager.instance.spirit.getClass() == bs.getClass()) {
                    ControlManager.instance.isUnderConstruction = null;
                    ControlManager.instance.justLeftConstruction = true;
                    ControlManager.instance.spirit = null;
                } else {
                    ControlManager.instance.spirit = bs;
                    EHelper.add(bs);
                    ControlManager.instance.isUnderConstruction = this;
                }
            } else {
                ControlManager.instance.spirit = bs;
                EHelper.add(bs);
                ControlManager.instance.isUnderConstruction = this;
            }
        }
    }

    public boolean isButtonDown(float x, float y) {
        Camera c = ControlManager.instance.camera;
        float pX = c.position.x - c.viewportWidth / 2 + pos.x;
        float pY = c.position.y - c.viewportHeight / 2 + pos.y;
        if (x >= pX && x <= pX + 32 && y >= pY && y <= pY + 32)
            return true;
        return false;
    }

    protected abstract BuildingSpirit createSpirit(float x, float y);
}