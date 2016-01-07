package tools.haha.com.androidtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import tools.haha.com.androidtools.demo.DemoMainActivity;
import tools.haha.com.androidtools.hotfix.HotfixException;
import tools.haha.com.androidtools.hotfix.PatchLoader;


public class MainActivity extends Activity implements View.OnClickListener{
    private static final String PATCH_PATH = "/sdcard/patcha-debug.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
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
        try {
            PatchLoader.load(this, PATCH_PATH);
            Toast.makeText(this, "Make patch succeed", Toast.LENGTH_SHORT).show();
        }catch (HotfixException e){
            Toast.makeText(this, "Make patch failed cause " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
