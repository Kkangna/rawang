package VO;

/**
 * Created by Administrator on 2017-12-08.
 */

public class Ramen {
    private int id;
    private String title;
    private int timer;
    private int type;
    private String description;
    private byte[] image;

    public Ramen(int id, String title, int timer, int type, String description, byte[] image) {
        this.id = id;
        this.title = title;
        this.timer = timer;
        this.type = type;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTimer() {
        return timer;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }
}
