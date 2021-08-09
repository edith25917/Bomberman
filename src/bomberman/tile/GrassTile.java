/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman.tile;

import bomberman.controllers.ImageResourceController;
import bomberman.util.Global;
import bomberman.util.ImagePath;
import java.awt.image.BufferedImage;

/**
 *
 * @author user
 */
public class GrassTile extends Tile {

    private int id = 0;

    public GrassTile(int x, int y, int tileID, int id) {
        super(x, y);
        if (id > 0) {
            id = 0;
        }
        this.id = id;
        setImage(tileID);
    }

    public void setImage(int tileID) {
        switch (tileID) {
            case 1:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_GRASSTILE);
                break;
            case 2:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV2_GRASSTILE);
                break;
            case 3:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV3_GRASSTILE + String.valueOf(this.id) + ".png");
                break;
            case 4:
                this.img = ImageResourceController.getInstance().tryGeyImage(ImagePath.LV1_GRASSTILE);
                break;
        }
    }
}
