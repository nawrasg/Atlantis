package fr.nawrasg.atlantis.activities;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nawrasg.atlantis.App;
import fr.nawrasg.atlantis.R;
import fr.nawrasg.atlantis.other.AtlantisContract;

/**
 * Created by Nawras on 15/10/2016.
 */

public class IntroActivity extends AppCompatActivity {
    @Bind(R.id.coordinatorIntroLayout)
    CoordinatorLayout nLayout;
    @Bind(R.id.txtIntroServer)
    EditText txtServer;
    @Bind(R.id.txtIntroApi)
    EditText txtAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnIntroValidate)
    public void validate() {
        String nServer = txtServer.getText().toString();
        String nAPI = txtAPI.getText().toString();

        if (nServer.equals("") || nAPI.equals("")) {
            Snackbar nSnackbar = Snackbar.make(nLayout, getString(R.string.intro_validate_error), Snackbar.LENGTH_LONG);
            nSnackbar.show();
        } else {
            App.setString(this, "api", nAPI);
            App.setString(this, "urlExterne", nServer);
            App.setPrefBoolean(this, "first_launch", true);
            sync();
            finish();
        }
    }

    private void sync(){
        Bundle nBundle = new Bundle();
        nBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        nBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(null, AtlantisContract.AUTHORITY, nBundle);
    }
}
