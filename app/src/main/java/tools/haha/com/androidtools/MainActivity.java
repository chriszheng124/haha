package tools.haha.com.androidtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import tools.haha.com.androidtools.demo.DemoMainActivity;
import tools.haha.com.androidtools.hotfix.PatchLoader;
import tools.haha.com.androidtools.hotfix.PatchNotification;


public class MainActivity extends Activity implements View.OnClickListener{
    private static final String PATCH_MAIN = "zzh.com.patcha.PatchMain";
    private static final String PATCH_PATH = "/sdcard/patcha-debug.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        BusFactory.getBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_demo:
                intent.setClassName(this, DemoMainActivity.class.getName());
                startActivity(intent);
                break;
            case R.id.btn_hotfix:
                runPatch();
                break;
        }
    }

    private void runPatch(){
        PatchLoader patchLoader = new PatchLoader(this, PATCH_PATH, PATCH_MAIN);
        patchLoader.load();
    }

    @Subscribe
    public void onPatchEvent(PatchNotification.PatchEvent event){
        if(event instanceof PatchNotification.PatchSuccess){
            Toast.makeText(this, "Make patch succeed", Toast.LENGTH_SHORT).show();
        }else if (event instanceof PatchNotification.PatchFail){
            Toast.makeText(this,
                    "Make patch failed cause " + ((PatchNotification.PatchFail) event).getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
