package com.example.recipe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import com.example.recipe.adapter.MainAdapter;
import com.example.recipe.db.MyDBManager;
import com.example.recipe.fragments.FragmentCalory;
import com.example.recipe.fragments.FragmentFavorite;
import com.example.recipe.fragments.FragmentRecipe;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
private DrawerLayout drawer;
private Toolbar toolbar;

private AppBarConfiguration mAppBarConfiguration;
private MainAdapter mainAdapter;
private MyDBManager myDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grayAction)));

        FragmentRecipe fragment = new FragmentRecipe();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, "FragmentRecipe");
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (id == R.id.id_calory)
        {
            FragmentCalory fragment = new FragmentCalory();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, "FragmentCalory");
            fragmentTransaction.commit();
        } else if (id == R.id.id_recipe) {
            FragmentRecipe fragment = new FragmentRecipe();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, "FragmentRecipe");
            fragmentTransaction.commit();
        } else if (id == R.id.id_favorite) {
            FragmentFavorite fragment = new FragmentFavorite();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, "FragmentFavorite");
            fragmentTransaction.commit();
        }
        return true;
    }

    public void onClickAdd(View view)
    {
        Intent i = new Intent(this, EditActivity.class);
        startActivity(i);
    }
}




