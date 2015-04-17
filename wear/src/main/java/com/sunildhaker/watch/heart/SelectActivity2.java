/*
 * Copyright (C) 2014 Marc Lester Tan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunildhaker.watch.heart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WearableListView;
import android.widget.Toast;

import java.util.List;

public class SelectActivity2 extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    String[] elements = {"Suhani Kashyap", "Riya Arora" , "Priya Mishra" , "Neetu Kapoor"};

    String[] elements2 = {"Due date in : 5 months", "Due date in : 3 months" , "Due date in : 4 months" , "Due date in : 2 months" } ;

    String[] elements3 = {"Bangalore,Karnataka", "Ahemedabadh,Gujarat" , "Jaipure,Rajasthan" , "Kota , Rajasthan"};

    int[] faces = {R.drawable.person_1 ,R.drawable.person_2 ,R.drawable.person3 , R.drawable.person4 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select2_2);

        // Get the list component from the layout of the activity
        WearableListView listView =
                (WearableListView) findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        listView.setAdapter(new Adapter2(this, elements , elements2 , elements3 , faces ));

        // Set a click listener
        listView.setClickListener(this);
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        Intent i = new Intent(this , ConnectActivity.class);
        startActivity(i);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }




}