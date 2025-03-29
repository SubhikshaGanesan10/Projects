package edu.uga.cs.hungerhero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Add timer for splash screen
    private static int SPLASH_SCREEN = 5000;

    //Variables
    Animation titleAnimation, imageAnimation;
    ImageView image;
    TextView title,tagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        titleAnimation = AnimationUtils.loadAnimation(this, R.anim.title_animation);
        imageAnimation = AnimationUtils.loadAnimation(this, R.anim.image_animation);

        //Get Variables
        title = findViewById(R.id.titleTextView);
        tagline = findViewById(R.id.taglineTextView);
        image = findViewById(R.id.imageView);

        title.setAnimation(titleAnimation);
        tagline.setAnimation(titleAnimation);
        image.setAnimation(imageAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}