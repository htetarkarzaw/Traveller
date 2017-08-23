package and.htetarkarzaw.tuntravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Zinmin2K on 7/3/2017.
 */
public class Login_Account extends AppCompatActivity {
    EditText etUsername,etPassword;
    Button btnLogin;
    public static final String MyPREFERENCES ="MyPrefs";
    public static final String Username = "usernameKey";
    public static final String Password = "passwordKey";
    SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_account);
        etUsername = (EditText) findViewById(R.id.etusername);
        etPassword = (EditText) findViewById(R.id.etpassword);
        btnLogin = (Button) findViewById(R.id.btnlogin);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login_Account.this,Grid_View.class);
                startActivity(i);
                String user = etUsername.getText().toString();
                String pass = etPassword.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Username , user);
                    editor.putString(Password , pass);
                    editor.commit();
                    Toast.makeText(Login_Account.this, "Login Success", Toast.LENGTH_SHORT).show();

            }
        });
    }
}