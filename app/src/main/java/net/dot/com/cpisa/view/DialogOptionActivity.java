package net.dot.com.cpisa.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.dot.com.cpisa.R;


/**
 * 错误信息提示页面
 */
public class DialogOptionActivity extends Activity {
    private TextView subMit, subSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_option);

        Intent intent = this.getIntent();
        String messageString = intent.getStringExtra("message");

        subMit = (TextView) findViewById(R.id.subMit);
        subMit.setText(messageString);

        subSure = (TextView) findViewById(R.id.subSure);
        subSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
