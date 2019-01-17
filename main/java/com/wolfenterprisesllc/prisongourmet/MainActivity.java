package com.wolfenterprisesllc.prisongourmet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wolfenterprisesllc.prisongourmet.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    protected DataHolder globalHolder;
    private List<RecipieHolder> recipieHolderList = new ArrayList<>();
    private RecyclerViewAdapter mAdapter;
    public RecyclerView recyclerView;

    RecipieHolder mine;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        TextView intro = findViewById(R.id.txtIntro);
        ImageView cover = findViewById(R.id.ivCover);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome to Prison Gourmet!");
        setSupportActionBar(toolbar);

        globalHolder = ((DataHolder) getApplication());
        recyclerView = findViewById(R.id.myList);

        cover.setVisibility(View.VISIBLE);
        intro.setVisibility(View.VISIBLE);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setWriteAccess(ParseUser.getCurrentUser(), false);
        ParseACL.setDefaultACL(defaultACL, true);

        mAdapter = new RecyclerViewAdapter(recipieHolderList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RecipieHolder holder = recipieHolderList.get(position);
                holder.setRecipie(recipieHolderList.get(position).getRecipie());
                Intent intent = new Intent(getApplicationContext(), Recipie.class);
                intent.putExtra("recipieName", recipieHolderList.get(position).getRecipie());
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
                view.startAnimation(anim);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        Button favorites = findViewById(R.id.btnFavorites);
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
                anim.cancel();
                TextView intro = findViewById(R.id.txtIntro);
                ImageView cover = findViewById(R.id.ivCover);
                Button exit = findViewById(R.id.btnExit);
                exit.setVisibility(View.VISIBLE);
                cover.setVisibility(View.INVISIBLE);
                intro.setVisibility(View.INVISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                ItemFragment fragment = new ItemFragment();
                RecyclerView rv = findViewById(R.id.myList);
                rv.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_list, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                fragmentTransaction.commit();
            }
        });

        Button exit = findViewById(R.id.btnExit);
        if (exit != null) {
            exit.setText(R.string.Exit);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
            });
        }
        doThis();
    }

    private void doThis() {  //
        ParseQuery<ParseObject> queryRecipies = ParseQuery.getQuery("Recipies");
        queryRecipies.orderByAscending("Name");
        queryRecipies.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list1, ParseException e) {
                if (e == null) {
                    ArrayList<String> listHolder = new ArrayList<>();
                    for (int i = 0; i < list1.size(); i++) {
                        try {
                            listHolder.add(list1.get(i).getString("Name"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (!listHolder.isEmpty()) {
                            mine = new RecipieHolder(listHolder.toString().replace("[", "").replace("]", ""));
                            recipieHolderList.add(mine);
                            listHolder.clear();
                        } else {
                            Toast.makeText(getApplicationContext(), "Oops. There was an error. Please try again or contact us if the problem persists.  MA01", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void goToUrl() {
        Uri uriUrl = Uri.parse("http://fireladychicago.wix.com/theprisongourmet");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.contact:
                String[] TO = {"wolfnremtreview@yahoo.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:theprisongourmet@gmail.com"));
                emailIntent.setType("text/plain");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client to use:"));
                break;
            case R.id.action_settings:
                final Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.website:
                goToUrl();
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
