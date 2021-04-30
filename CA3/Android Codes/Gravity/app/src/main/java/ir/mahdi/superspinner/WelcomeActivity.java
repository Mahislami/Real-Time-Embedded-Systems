package ir.mahdi.superspinner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WelcomeActivity extends AppCompatActivity {


    private float xMax;
    private float yMax;
    public static final String EXTRA_MESSAGE_XMOON  = "ir.mahdi.SuperSpinner.XMOON";
    public static final String EXTRA_MESSAGE_YMOON  = "ir.mahdi.SuperSpinner.YMOON";
    public static final String EXTRA_MESSAGE_XSUN   = "ir.mahdi.SuperSpinner.XSUN";
    public static final String EXTRA_MESSAGE_YSUN   = "ir.mahdi.SuperSpinner.YSUN ";
    public static final String EXTRA_MESSAGE_VxMOON = "ir.mahdi.SuperSpinner.VxMOON";
    public static final String EXTRA_MESSAGE_VyMOON = "ir.mahdi.SuperSpinner.VyMOON";
    public static final String EXTRA_MESSAGE_VxSUN  = "ir.mahdi.SuperSpinner.VxSUN";
    public static final String EXTRA_MESSAGE_VySUN  = "ir.mahdi.SuperSpinner.VySUN";

    public void openMainActivity(Intent intent1) {
        EditText editText1 = (EditText) findViewById(R.id.xMoon);
        String message1 = (editText1.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_XMOON, message1);

        EditText editText2 = (EditText) findViewById(R.id.yMoon);
        String message2 = (editText2.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_YMOON, message2);

        EditText editText3 = (EditText) findViewById(R.id.xSun);
        String message3 = (editText3.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_XSUN, message3);

        EditText editText4 = (EditText) findViewById(R.id.ySun);
        String message4 = (editText4.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_YSUN, message4);

        EditText editText5 = (EditText) findViewById(R.id.vx1);
        String message5 = (editText5.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_VxMOON, message5);

        EditText editText6 = (EditText) findViewById(R.id.vy1);
        String message6 = (editText6.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_VyMOON, message6);

        EditText editText7 = (EditText) findViewById(R.id.vx2);
        String message7 = (editText7.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_VxSUN, message7);

        EditText editText8 = (EditText) findViewById(R.id.vy2);
        String message8 = (editText8.getText().toString());
        intent1.putExtra(EXTRA_MESSAGE_VySUN, message8);

        if ( TextUtils.isEmpty(editText1.getText()) || TextUtils.isEmpty(editText2.getText()) ||
             TextUtils.isEmpty(editText3.getText()) || TextUtils.isEmpty(editText4.getText()) ||
             TextUtils.isEmpty(editText5.getText()) || TextUtils.isEmpty(editText6.getText()) ||
             TextUtils.isEmpty(editText7.getText()) || TextUtils.isEmpty(editText8.getText()) ) {
            Toast.makeText(this, "Empty Field!", Toast.LENGTH_LONG).show();
        }
        else if (Math.sqrt(Math.pow(Float.parseFloat(message1) - Float.parseFloat(message3), 2) +
                Math.pow(Float.parseFloat(message2) - Float.parseFloat(message4), 2)) > 130f) {
            startActivity(intent1);
        }
        else {
            Toast.makeText(this, "Ball Overlap!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.btn_gravity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity(new Intent(WelcomeActivity.this, GravityActivity.class));
            }

        });

        findViewById(R.id.btn_gyroscope).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity(new Intent(WelcomeActivity.this, GyroscopeActivity.class));
            }

        });
    }

    }




