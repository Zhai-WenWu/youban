package cn.bjhdltcdn.p2plive.event;


import cn.bjhdltcdn.p2plive.model.Props;

/**
 * 一对一视频礼物特效事件
 */

public class One2OneVideoPropsEvent {
    private Props props;

    public One2OneVideoPropsEvent(Props props) {
        this.props = props;
    }

    public Props getProps() {
        return props;
    }

    public void setProps(Props props) {
        this.props = props;
    }
}
