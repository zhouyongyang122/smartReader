package com.qudiandu.smartdub.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by Administrator on 2015/11/4.
 */
public class ZYMediaPlayerTool {
    private static MediaPlayer mp;

    public static void playSound(Context ctx, int rID, boolean isLoop) {
        try {
            if (null == mp) {
                mp = new MediaPlayer();
            } else {
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp.reset();
            }

            mp.setLooping(isLoop);
            if (!isLoop) {
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSound();
                    }
                });
            }
            mp.setDataSource(ctx, Uri.parse("android.resource://" + ctx.getPackageName() + "/" + rID));
            mp.prepare();
            mp.start();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static void stopSound() {
        if (null != mp) {
            mp.stop();
            mp.release();
            mp = null;
        }

    }
}
