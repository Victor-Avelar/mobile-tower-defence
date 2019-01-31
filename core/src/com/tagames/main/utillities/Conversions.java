package com.tagames.main.utillities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tagames.main.TowerDefence;

/**
 * Created by DerekThompson on 7/22/17.
 */

public class Conversions {

    public static Vector2 worldCordToPixelCord(Vector2 vec2) {
        Vector2 rVec2 = new Vector2();
        rVec2.x = (vec2.x) * TowerDefence.SCALINGFACTOR;
        rVec2.y = (vec2.y)* TowerDefence.SCALINGFACTOR;
        return rVec2;
    }

    public static Vector2 worldCordToPixelCord(Vector2 vec2, float width, float height) {
        Vector2 rVec2 = new Vector2();
        rVec2.x = (vec2.x) * TowerDefence.SCALINGFACTOR - width;
        rVec2.y = (vec2.y) * TowerDefence.SCALINGFACTOR - width;
        return rVec2;
    }

    public static Vector2 pixelCordToWorldCord (Vector2 vec2){
        Vector2 rVec2 = new Vector2();
        rVec2.x = (vec2.x) / TowerDefence.SCALINGFACTOR;
        rVec2.y = (vec2.y) / TowerDefence.SCALINGFACTOR;
        return rVec2;
    }

    public static Vector2 pixelCordToWorldCord (Vector2 vec2, float width, float height){
        Vector2 rVec2 = new Vector2();
        rVec2.x = (vec2.x + width) / TowerDefence.SCALINGFACTOR;
        rVec2.y = (vec2.y + width) / TowerDefence.SCALINGFACTOR;
        return rVec2;
    }

    public static Vector2 velocityToPosition (Vector2 positionVec2a, Vector2 positionVec2b, float velocity) {
        Vector2 rVec2 = new Vector2();
        if (positionVec2b.x - positionVec2a.x != 0) {
            double angle = Math.atan2((positionVec2b.y - positionVec2a.y), (positionVec2b.x - positionVec2a.x));
            if (positionVec2b.x - positionVec2a.x < 0) {
                rVec2.x = -velocity * (float)Math.cos(angle);
                rVec2.y = velocity * (float)Math.sin(angle);
            } else {
                rVec2.x = velocity * (float)Math.cos(angle);
                rVec2.y = velocity * (float)Math.sin(angle);
            }
        } else {
            if (positionVec2b.y - positionVec2a.y < 0) {
                rVec2.y = -velocity;
            } else {
                rVec2.y = velocity;
            }
        }
        return rVec2;
    }

    public static Vector2 velocityToPositionWorld (Vector2 positionVec2a, Vector2 positionVec2b, float velocity) {
        Vector2 rVec2 = new Vector2();
        if (positionVec2b.x - positionVec2a.x != 0) {
            double angle = Math.atan2((positionVec2b.y - positionVec2a.y), (positionVec2b.x - positionVec2a.x));
            if (positionVec2b.x - positionVec2a.x < 0) {
                rVec2.x = velocity * (float)Math.cos(angle);
                rVec2.y = velocity * (float)Math.sin(angle);
            } else {
                rVec2.x = velocity * (float)Math.cos(angle);
                rVec2.y = velocity * (float)Math.sin(angle);
            }
        } else {
            if (positionVec2b.y - positionVec2a.y < 0) {
                rVec2.y = -velocity;
            } else {
                rVec2.y = velocity;
            }
        }
        rVec2.scl(1f/TowerDefence.SCALINGFACTOR);
        return rVec2;
    }

    public static Vector2 velocityToPositionWorld (Vector2 positionVec2a, Vector2 positionVec2b, float velocity, float width, float height) {
        Vector2 rVec2 = new Vector2();
        if (positionVec2b.x - positionVec2a.x != 0) {
            double angle = Math.atan2((positionVec2b.y - positionVec2a.y), (positionVec2b.x - positionVec2a.x));
            if (positionVec2b.x - positionVec2a.x < 0) {
                rVec2.x = velocity * (float)Math.cos(angle);
                rVec2.y = velocity * (float)Math.sin(angle);
            } else {
                rVec2.x = velocity * (float)Math.cos(angle);
                rVec2.y = velocity * (float)Math.sin(angle);
            }
        } else {
            if (positionVec2b.y - positionVec2a.y < 0) {
                rVec2.y = -velocity;
            } else {
                rVec2.y = velocity;
            }
        }
        rVec2.scl(1f/TowerDefence.SCALINGFACTOR);
        return rVec2;
    }


}
