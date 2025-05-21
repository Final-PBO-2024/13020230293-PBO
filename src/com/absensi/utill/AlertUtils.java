package com.absensi.util;

import raven.modal.Toast;
import raven.modal.option.Location;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;


public class AlertUtils {
    public static ToastOption getOptionAlert() {
        ToastOption option = Toast.createOption();
        Location h = Location.CENTER;
        Location v = Location.TOP;
        ToastStyle.BackgroundType backgroundType = ToastStyle.BackgroundType.DEFAULT;
        ToastStyle.BorderType borderType = ToastStyle.BorderType.OUTLINE;
        
        option.setAnimationEnabled(true)
              .setPauseDelayOnHover(true)
              .setAutoClose(true)
              .setCloseOnClick(true);
        
        option.setLayoutOption(ToastLocation.from(h, v).getLayout());
        option.getStyle().setBackgroundType(backgroundType)
              .setBorderType(borderType)
              .setShowLabel(true)
              .setIconSeparateLine(true)
              .setShowCloseButton(true)
              .setPromiseLabel("Saving...");
        
        return option;
    }
}
