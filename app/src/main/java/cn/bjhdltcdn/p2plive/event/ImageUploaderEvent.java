package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.Image;

/**
 * Created by xiawenquan on 17/11/28.
 */

public class ImageUploaderEvent {

    private Object tag;
    private Image image;
    private int position;

    public ImageUploaderEvent(Object tag) {
        this.tag = tag;
    }

    public ImageUploaderEvent(String tag, Image image) {
        this.tag = tag;
        this.image = image;
    }

    public ImageUploaderEvent(String tag, Image image, int position) {
        this.tag = tag;
        this.image = image;
        this.position = position;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
