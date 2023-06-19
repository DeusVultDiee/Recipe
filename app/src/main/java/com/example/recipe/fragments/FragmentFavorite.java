package com.example.recipe.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.adapter.ListItem;
import com.example.recipe.adapter.MainAdapter;
import com.example.recipe.db.AppExecuter;
import com.example.recipe.db.MyDBManager;
import com.example.recipe.db.OnDataRecieved;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FragmentFavorite extends Fragment implements OnDataRecieved
{

    private MyDBManager myDbManager;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;
    public FragmentFavorite() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        init();
    }

    public void init()
    {
        myDbManager = new MyDBManager(this.getActivity());
        rcView = (RecyclerView)getActivity().findViewById(R.id.rcView1);
        mainAdapter = new MainAdapter(this.getActivity());
        rcView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rcView.setAdapter(mainAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        myDbManager.openDb();
        readFromDB("");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        myDbManager.closeDb();
    }

    @Override
    public void onRecived(List<ListItem> list)
    {
        AppExecuter.getInstance().getMainIO().execute(new Runnable() {
            @Override
            public void run() {
                mainAdapter.updateAdapter(list);
            }
        });
    }

    private void readFromDB(final String text)
    {
        AppExecuter.getInstance().getSubIO().execute(new Runnable() {
            @Override
            public void run() {
                myDbManager.getFromDbFav(text, FragmentFavorite.this);
            }
        });
    }
}
