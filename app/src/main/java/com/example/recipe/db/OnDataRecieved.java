package com.example.recipe.db;

import com.example.recipe.adapter.ListItem;

import java.util.List;

public interface OnDataRecieved
{
    void onRecived(List<ListItem> list);
}
