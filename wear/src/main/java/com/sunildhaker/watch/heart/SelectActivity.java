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

public class SelectActivity extends Activity
        implements WearableListView.ClickListener {

    // Sample dataset for the list
    String[] elements = {"someone", "everyone"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select2);

        // Get the list component from the layout of the activity
        WearableListView listView =
                (WearableListView) findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        listView.setAdapter(new Adapter(this, elements));

        // Set a click listener
        listView.setClickListener(this);
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        int position = Integer.parseInt(viewHolder.itemView.getTag().toString());

        Intent i;
        if(position == 0) i = new Intent(this , SelectActivity2.class);
        else i = new Intent(this , ConnectActivity.class);

        startActivity(i);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }




}