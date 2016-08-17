package fr.nawrasg.atlantis.type;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nawras on 17/08/2016.
 */

public class Log {
    private static final String LOG_FILENAME = "log.at";
    private Context mContext;
    private FileOutputStream nFileOutputStream;
    private FileInputStream nFileInputStream;

    public Log(Context context){
        mContext = context;
    }

    public void log(String value) throws IOException {
        nFileOutputStream = mContext.openFileOutput(LOG_FILENAME, Context.MODE_APPEND);
        nFileOutputStream.write(value.getBytes());
        nFileOutputStream.close();
    }

    public String read() throws IOException {
        nFileInputStream = mContext.openFileInput(LOG_FILENAME);
        StringBuffer nContent = new StringBuffer("");
        byte[] nBuffer = new byte[1024];
        int nSize = 0;
        while((nSize = nFileInputStream.read(nBuffer)) != -1){
            nContent.append(new String(nBuffer, 0, nSize));
        }
        return nContent.toString();
    }
}
