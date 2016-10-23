package fr.nawrasg.atlantis.type;

import android.content.Context;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import fr.nawrasg.atlantis.App;

import static android.content.Context.MODE_APPEND;

/**
 * Created by Nawras on 17/08/2016.
 */

public class Log {
    private static final String LOG_FILENAME = "log.at";
    private Context mContext;

    public Log(Context context) {
        mContext = context;
    }

    public void log(String value) throws IOException {
        if (!App.getPrefBoolean(mContext, "log")) {
            return;
        }
        FileOutputStream nStream = mContext.openFileOutput(LOG_FILENAME, MODE_APPEND);
        OutputStreamWriter nWriter = new OutputStreamWriter(nStream);
        nWriter.write(value + "\n");
        nWriter.flush();
        nWriter.close();
    }

    public String read() throws IOException {
        String nContent = "";
        FileInputStream nStream = mContext.openFileInput(LOG_FILENAME);
        DataInputStream nData = new DataInputStream(nStream);
        String nLine = null;
        while ((nLine = nData.readLine()) != null) {
            nContent += nLine + "\n";
        }
        nData.close();
        nStream.close();
        return nContent;
    }
}
