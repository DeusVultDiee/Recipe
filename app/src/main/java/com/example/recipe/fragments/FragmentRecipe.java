package com.example.recipe.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.EditActivity;
import com.example.recipe.MainActivity;
import com.example.recipe.R;
import com.example.recipe.adapter.ListItem;
import com.example.recipe.adapter.MainAdapter;
import com.example.recipe.db.AppExecuter;
import com.example.recipe.db.MyDBManager;
import com.example.recipe.db.OnDataRecieved;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FragmentRecipe extends Fragment implements OnDataRecieved
{
    private MyDBManager myDbManager;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;
    public FragmentRecipe() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_recipe, container, false);
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
        rcView = (RecyclerView)getActivity().findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this.getActivity());
        rcView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        getItemTouchHelper().attachToRecyclerView(rcView);
        rcView.setAdapter(mainAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.id_searchV);
        SearchView sv = (SearchView) item.getActionView();
        super.onCreateOptionsMenu(menu, inflater);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                readFromDB(s);
                return false;
            }
        });

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

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                int pos = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setMessage("Вы уверены, что хотите удалить этот рецепт?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainAdapter.removeItem(pos, myDbManager);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainAdapter.notifyItemChanged(pos);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target)
            {
                return false;
            }
        });
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
                myDbManager.getFromDb(text, FragmentRecipe.this);
            }
        });
    }
}
