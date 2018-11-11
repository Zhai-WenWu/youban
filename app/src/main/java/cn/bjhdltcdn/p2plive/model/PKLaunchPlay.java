package cn.bjhdltcdn.p2plive.model;

/**
 * Created by ZHAI on 2017/12/22.
 */

public class PKLaunchPlay {

    public static PKLaunchPlay pkDetailLaunchPlayKLaunchPlay;

    public static PKLaunchPlay getInstent() {
        if (pkDetailLaunchPlayKLaunchPlay == null) {
            pkDetailLaunchPlayKLaunchPlay = new PKLaunchPlay();
        }
        return pkDetailLaunchPlayKLaunchPlay;
    }

    private LaunchPlay mLaunchPlay = new LaunchPlay();

    public LaunchPlay getLaunchPlay() {
        return mLaunchPlay;
    }

    public void setLaunchPlay(LaunchPlay launchPlay) {
        this.mLaunchPlay.setLaunchId(launchPlay.getLaunchId());
        this.mLaunchPlay.setDescription(launchPlay.getDescription());
        this.mLaunchPlay.setTitle(launchPlay.getTitle());
        this.mLaunchPlay.setBaseUser(launchPlay.getBaseUser());
    }

}
