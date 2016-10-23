package fr.nawrasg.atlantis.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.type.Log;

/**
 * Created by Nawras on 22/10/2016.
 */

public class LogActivity extends AppCompatActivity {

    @Bind(R.id.lblLogContent)
    TextView txtLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);
        try {
            loadLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLog() throws IOException {
        String nContent = (new Log(this)).read();
        txtLog.setText(nContent);
    }
}
