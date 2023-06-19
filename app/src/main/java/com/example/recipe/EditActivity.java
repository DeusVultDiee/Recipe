package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.TextKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.recipe.adapter.ListItem;
import com.example.recipe.db.AppExecuter;
import com.example.recipe.db.MyConstans;
import com.example.recipe.db.MyDBHelper;
import com.example.recipe.db.MyDBManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity
{
    private EditText edTitle, edDesc, edProducts, edCalories;
    private final int PIC_IMAGE_CODE = 123;
    private MyDBManager myDbManager;
    private MyDBHelper myDBHelper;
    private ImageView imNewImage;
    private FloatingActionButton fbAddImage, fbEditEnable, fbSaveImage, fbFav;
    private ConstraintLayout imageContainer;
    private String tempUri = "empty";
    private boolean isEditState = true;
    private ImageButton imEditImage, imDeleteImage;
    private ListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getMyIntents();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        myDbManager.openDb();
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PIC_IMAGE_CODE && data != null)
        {
            tempUri = data.getData().toString();
            imNewImage.setImageURI(data.getData());
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    void init()
    {
        myDbManager = new MyDBManager(this);
        myDBHelper = new MyDBHelper(this);
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        edCalories = findViewById(R.id.edCalories);
        edProducts = findViewById(R.id.edProducts);
        imNewImage = findViewById(R.id.imNewImage);
        imageContainer = findViewById(R.id.imageContainer);
        imEditImage = findViewById(R.id.imEditImage);
        imDeleteImage = findViewById(R.id.imDeleteImage);
        fbAddImage = findViewById(R.id.fbAddImage);
        fbEditEnable = findViewById(R.id.fbEditEnable);
        fbSaveImage = findViewById(R.id.fbSaveImage);
        fbFav = findViewById(R.id.fbFav);
    }

    private void getMyIntents()
    {
        fbEditEnable.setVisibility(View.GONE);
        Intent i = getIntent();
        if (i != null)
        {
            item = (ListItem) i.getSerializableExtra(MyConstans.LIST_ITEM_INTENT);
            isEditState = i.getBooleanExtra(MyConstans.EDIT_STATE, true);

            if (!isEditState)
            {
                edTitle.setText(item.getTitle());
                edDesc.setText(item.getDesc());
                edProducts.setText(item.getProducts());
                edCalories.setText(item.getCalories());
                edTitle.setEnabled(false);
                edDesc.setKeyListener(null);
                edCalories.setEnabled(false);
                edProducts.setEnabled(false);
                fbAddImage.setVisibility(View.GONE);
                fbFav.setVisibility(View.VISIBLE);
                fbEditEnable.setVisibility(View.VISIBLE);
                if (!item.getUri().equals("empty"))
                {
                    tempUri = item.getUri();
                    imageContainer.setVisibility(View.VISIBLE);
                    imNewImage.setImageURI(Uri.parse(item.getUri()));
                    imDeleteImage.setVisibility(View.GONE);
                    imEditImage.setVisibility(View.GONE);
                }
            }
        }
    }

    public void onClickEditEnable(View view)
    {
        edTitle.setEnabled(true);
        edDesc.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.SENTENCES, true));
        edDesc.setFocusable(true);
        edDesc.setFocusableInTouchMode(true);
        edCalories.setEnabled(true);
        edProducts.setEnabled(true);
        fbFav.setVisibility(View.GONE);
        fbEditEnable.setVisibility(View.GONE);
        imDeleteImage.setVisibility(View.VISIBLE);
        imEditImage.setVisibility(View.VISIBLE);
        fbAddImage.setVisibility(View.VISIBLE);
    }

    public void onClickSave(View view)
    {

        final String title = edTitle.getText().toString();
        final String desc = edDesc.getText().toString();
        final String products = edProducts.getText().toString();
        final String calories = edCalories.getText().toString();
        final int fav = 0;

        if (title.equals("") || desc.equals("") || products.equals("") || calories.equals(""))
        {
            Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show();
        } else
        {
            if (isEditState)
            {
                AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbManager.insertToDb(title, products, desc, calories, tempUri, fav);
                    }
                });
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            } else
            {
                myDbManager.update(title, products, desc, calories, tempUri, item.getId(), fav);
            }
            myDbManager.closeDb();
            finish();
        }

    }

    public void onClickDeleteImage(View view)
    {
        imNewImage.setImageResource(R.drawable.ic_delete_image_black);
        tempUri = "empty";
        imageContainer.setVisibility(View.GONE);
        fbAddImage.setVisibility(View.VISIBLE);
    }

    public void onClickAddImage(View view)
    {
        imageContainer.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void onClickChooseImage(View view)
    {
        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooser.setType("image/*");
        startActivityForResult(chooser, PIC_IMAGE_CODE);
    }

    public void onClickFav(View view)
    {
        final String title = edTitle.getText().toString();
        final String desc = edDesc.getText().toString();
        final String products = edProducts.getText().toString();
        final String calories = edCalories.getText().toString();

        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(MyConstans.TABLE_NAME, new String[]{ "favorite" }, "_id = ?",
                new String[]{String.valueOf(item.getId())}, null, null, null);
        cursor.moveToFirst();
        int current = cursor.getInt(0);
        cursor.close();

        if (current == 0)
        {
            myDbManager.update(title, products, desc, calories, tempUri, item.getId(), 1);
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
        }
        else if (current == 1)
        {
            myDbManager.update(title, products, desc, calories, tempUri, item.getId(), 0);
            Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show();
        }
    }
}
