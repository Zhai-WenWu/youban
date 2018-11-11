package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.UserLocation;

/**
 * Created by xiawenquan on 17/11/23.
 */

public class LocationEvent {
    // 1 开始定位 ；2 停止定位 ；3 发送定位位置
    private int eventType;

    // 定位后拿到位置
    private UserLocation userLocation;

    public LocationEvent(int eventType) {
        this.eventType = eventType;

    }

    public LocationEvent(int eventType, UserLocation userLocation) {
        this.eventType = eventType;
        this.userLocation = userLocation;
    }



    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }
}
