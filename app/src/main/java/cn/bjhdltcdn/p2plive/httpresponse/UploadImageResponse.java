package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.Image;

/**
 * Created by xiawenquan on 17/11/21.
 */

public class UploadImageResponse extends BaseResponse {

    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
