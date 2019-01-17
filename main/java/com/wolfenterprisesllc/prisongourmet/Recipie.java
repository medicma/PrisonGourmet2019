package com.wolfenterprisesllc.prisongourmet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wolfenterprisesllc.prisongourmet.dummy.DummyContent;

public class Recipie extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    DataHolder global;
    TextView name;
    TextView ingredients;
    Button favorites;
    Button home;
    DBHandler db;
    Animation animation;
    Animation animation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_recipie);

        favorites = findViewById(R.id.btnFavorites);
        global = ((DataHolder) getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.REVERSE);
        favorites.startAnimation(animation);

        db = new DBHandler(this, null, this, 1);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (db.isItThere(extras.getString("recipieName")) == 1) {
                animation2 = new AlphaAnimation(0, 0);
                favorites.startAnimation(animation2);
                favorites.setVisibility(View.INVISIBLE);
            }
        }

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setFavorites();
                view.clearAnimation();
            }
        });

        home = findViewById(R.id.btnReturn2);
        if (home != null) {
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        name = findViewById(R.id.txtRName);
        ingredients = findViewById(R.id.txtIngredients);
        if (ingredients != null) {
            ingredients.setMovementMethod(new ScrollingMovementMethod());
        }

        Intent intent = getIntent();
        ParseQuery<ParseObject> queryClass = ParseQuery.getQuery("Recipies");
        queryClass.whereContains("Name", intent.getStringExtra("recipieName"));//global.getList()
        queryClass.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject object, ParseException e) {
                                                try {
                                                    if (name != null) {
                                                        name.setText(object.getString("Name"));
                                                    }
                                                    try {
                                                        if (ingredients != null) {
                                                            ingredients.setText(object.getString("Ingredients"));
                                                        }
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        }
        );
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

   public void setFavorites (){
       final Bundle extras = getIntent().getExtras();
       if (extras != null) {
           db.addRecipie(extras.getString("recipieName"));
           favorites.setVisibility(View.INVISIBLE
           );
       }
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.viewFavorites:
                ingredients.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                favorites.setVisibility(View.INVISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                ItemFragment fragment = new ItemFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_list, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                fragmentTransaction.commit();
                break;
            case R.id.home:
                Intent intent2 = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.contact:
                String[] TO = {"wolfnremtreview@yahoo.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:theprisongourmet@gmail.com"));
                emailIntent.setType("text/plain");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client to use:"));
                break;
            case R.id.action_settings:
                final Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.website:
                goToUrl("http://fireladychicago.wix.com/theprisongourmet");
                break;
            case R.id.chef:
                goToUrl("http://fireladychicago.wix.com/theprisongourmet#!chef/c42f");
                break;
            case R.id.blog:
                goToUrl("http://fireladychicago.wix.com/theprisongourmet#!blog/t0cr0");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}