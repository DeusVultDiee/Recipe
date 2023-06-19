package com.example.recipe.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.recipe.R;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class FragmentCalory extends Fragment {

    EditText editTextAge, editTextWeight, editTextHeight;
    RadioGroup radioGroupGender;
    RadioButton radioButtonMale, radioButtonFemale;
    Spinner spinnerActivity;
    Button buttonCalculate;
    TextView textViewResult;
    private SharedPreferences sharedPreferences;
    private static final String DAILY_CALORIES_KEY = "daily_calories";
    private static final String AGE_KEY = "age";
    private static final String HEIGHT_KEY = "height";
    private static final String WEIGHT_KEY = "weight";

    public FragmentCalory() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_calory, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        editTextAge = (EditText)getActivity().findViewById(R.id.edit_text_age);
        editTextWeight = (EditText)getActivity().findViewById(R.id.edit_text_weight);
        editTextHeight = (EditText)getActivity().findViewById(R.id.edit_text_height);
        radioGroupGender = (RadioGroup)getActivity().findViewById(R.id.radio_group_gender);
        radioButtonMale = (RadioButton)getActivity().findViewById(R.id.radio_button_male);
        radioButtonFemale = (RadioButton)getActivity().findViewById(R.id.radio_button_female);
        spinnerActivity = (Spinner)getActivity().findViewById(R.id.spinner_activity_level);
        buttonCalculate = (Button)getActivity().findViewById(R.id.button_calculate);
        textViewResult = (TextView)getActivity().findViewById(R.id.textViewResult);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (sharedPreferences.contains(DAILY_CALORIES_KEY))
        {
            String dailyCaloriesString = sharedPreferences.getString(DAILY_CALORIES_KEY, "");
            String ageString = sharedPreferences.getString(AGE_KEY, "");
            String heightString = sharedPreferences.getString(HEIGHT_KEY, "");
            String weightString = sharedPreferences.getString(WEIGHT_KEY, "");
            textViewResult.setText(dailyCaloriesString);
            editTextAge.setText(ageString);
            editTextHeight.setText(heightString);
            editTextWeight.setText(weightString);
        }


        buttonCalculate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context context = getActivity();
                String heightText = editTextHeight.getText().toString();
                String weightText = editTextHeight.getText().toString();
                String ageText = editTextHeight.getText().toString();
                int genderId = radioGroupGender.getCheckedRadioButtonId();

                if(heightText.equals("") || weightText.equals("") || ageText.equals("") || genderId == -1)
                {
                    Toast.makeText(context, "Не все поля заполнены", Toast.LENGTH_SHORT).show();
                } else
                {
                    calculateDailyCalorieIntake();
                }
            }
        });
    }

    private void calculateDailyCalorieIntake()
    {

        double activity = 0;
        int age = Integer.parseInt(editTextAge.getText().toString());
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        double height = Double.parseDouble(editTextHeight.getText().toString());
        int gender = radioGroupGender.getCheckedRadioButtonId();
        int activityLevel = spinnerActivity.getSelectedItemPosition();

        switch (activityLevel)
        {
            case 0:
                activity = 1.2;
                break;
            case 1:
                activity = 1.375;
                break;
            case 2:
                activity = 1.55;
                break;
            case 3:
                activity = 1.725;
                break;
            case 4:
                activity = 1.9;
                break;
        }

        double bmr;
        if (gender == radioButtonMale.getId())
        {
            bmr = 88.36 + (13.4 * weight) + (4.8 * height) - (5.7 * age);
        } else
        {
            bmr = 447.6 + (9.2 * weight) + (3.1 * height) - (4.3 * age);
        }
        double dailyCalorieIntake = bmr * activity;

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String result = decimalFormat.format(dailyCalorieIntake);

        textViewResult.setText(result + " кк");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DAILY_CALORIES_KEY, result + "кк");
        editor.putString(HEIGHT_KEY, editTextHeight.getText().toString());
        editor.putString(WEIGHT_KEY, editTextWeight.getText().toString());
        editor.putString(AGE_KEY, editTextAge.getText().toString());
        editor.apply();
    }
}