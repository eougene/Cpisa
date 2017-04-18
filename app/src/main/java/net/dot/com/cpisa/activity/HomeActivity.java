package net.dot.com.cpisa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.dot.com.cpisa.R;

public class HomeActivity extends Activity {
    private ImageView backImageView;
    private TextView title;
    private RelativeLayout changeRela;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.app_name);

        backImageView = (ImageView) findViewById(R.id.backImageView);
        backImageView.setBackgroundResource(R.mipmap.edotlogo);


        changeRela = (RelativeLayout) findViewById(R.id.changeRela);
        changeRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

    }
}
