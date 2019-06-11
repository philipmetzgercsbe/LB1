package com.example.lb1.Image;

import android.database.Cursor;
import android.net.Uri;

import com.example.lb1.MainActivity;
import com.example.lb1.Models.ImageData;
import com.example.lb1.SQL.SQLiteCursor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ImageContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ImageData> ITEMS = new ArrayList<>();


    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, ImageData> ITEM_MAP = new HashMap<Integer, ImageData>();

    private static final int DISPLAY_MAX = 25;

    public static void loadImage() {
        SQLiteCursor Sql = new SQLiteCursor(MainActivity.CursorDriver,null,null);
        ArrayList<ImageData> Images = new ArrayList<>();
        ImageData Image = new ImageData();
        Images = Sql.getAll(MainActivity.Db);
        for(ImageData img: Images){
            if(!Images.isEmpty()){
                addItem(Image);
            }
        }

    }

    private static void addItem(ImageData item) {
        ITEMS.add(0, item);
    }











}
