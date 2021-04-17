 /*
 * MIT License
 * Copyright (c) 2021.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This project was submitted by Tarek Bohdima as part of the Android Developer
 * Nanodegree At Udacity.
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 * I, the author of the project, allow you to check the code as a reference, but if you
 * submit it, it's your own responsibility if you get expelled.
 */

package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.bakingapp.BaseApplication;
import com.example.bakingapp.R;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipes;
import com.example.bakingapp.repository.Preferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

 /**
 * The configuration screen for the {@link RecipesWidgetProvider RecipesWidgetProvider} AppWidget.
 */
public class RecipesWidgetConfigureActivity extends AppCompatActivity {

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RadioGroup radioGroup;

    @Inject
    Preferences preferences;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = RecipesWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            String recipeName = ((AppCompatRadioButton) radioGroup.getChildAt(checkedRadioButtonId))
                    .getText().toString();

            preferences.saveRecipeTitle(appWidgetId, recipeName);
            preferences.saveCurrentRecipe(recipeName);
            Recipes currentRecipe = preferences.getCurrentRecipe();
            ArrayList<Ingredients> ingredientsArrayList = currentRecipe.getIngredients();

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipesWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId, recipeName,
                    currentRecipe, ingredientsArrayList);


            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public RecipesWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        preferences = ((BaseApplication) getApplication()).getBakingComponent().getPreferences();
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_configure);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        setupRadioGroup();

//        mAppWidgetText.setText(loadTitlePref(RecipesWidgetConfigureActivity.this, mAppWidgetId));
    }

     private void setupRadioGroup() {
         radioGroup = findViewById(R.id.radio_group_recipes);

         List<String> recipesNames = preferences.getRecipesFromPreferences();

         if (recipesNames.size() == 0) {
             Toast.makeText(this, "No Saved Recipes", Toast.LENGTH_SHORT).show();
             finish();
         }

         int currentRadioButtonIndex = 0;

         for (String recipeName :
                 recipesNames) {
             AppCompatRadioButton radioButton = new AppCompatRadioButton(this);
             radioButton.setText(recipeName);
             radioButton.setId(currentRadioButtonIndex++);
             radioGroup.addView(radioButton);
         }

         if (radioGroup.getChildCount() > 0) {
             ((AppCompatRadioButton)radioGroup.getChildAt(0)).setChecked(true);
         }
     }
 }