package com.cheese.demo.mock;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;

public class DeviceDummy implements Device {

    private boolean normal;
    private boolean mobile;
    private boolean tablet;

    @Override
    public boolean isNormal() {
        return normal;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    @Override
    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean isTablet() {
        return tablet;
    }

    public void setTablet(boolean tablet) {
        this.tablet = tablet;
    }

    @Override
    public DevicePlatform getDevicePlatform() {
        return null;
    }
}
