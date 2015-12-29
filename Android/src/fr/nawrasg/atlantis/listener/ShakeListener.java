package fr.nawrasg.atlantis.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeListener implements SensorEventListener{
	private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
 
    private OnShakeListener nListener;
    private long nShakeTimestamp;
    private int nShakeCount;

    public interface OnShakeListener {
        public void onShake(int count);
    }
    
    public void setOnShakeListener(OnShakeListener listener) {
        nListener = listener;
    }
    
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (nListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
 
            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;
 
            // gForce will be close to 1 when there is no movement.
            float gForce = (float)Math.ceil(gX * gX + gY * gY + gZ * gZ);
 
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (nShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }
 
                // reset the shake count after 3 seconds of no shakes
                if (nShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    nShakeCount = 0;
                }
 
                nShakeTimestamp = now;
                nShakeCount++;
                nListener.onShake(nShakeCount);
            }
        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
