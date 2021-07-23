package com.example.sampleprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SqliteDatabase mDatabase;
    private ArrayList<Items> allContacts=new ArrayList<>();
    private ItemAdapter mAdapter;
    EditText main_Search;
    Calendar c;
    String todaysDate;
    String currentTime;
    int Month;
    CharSequence msearch="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        main_Search=findViewById(R.id.main_search);

        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);

        RecyclerView contactView = (RecyclerView)findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contactView.setLayoutManager(linearLayoutManager);
        contactView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allContacts = mDatabase.listContacts();


        c = Calendar.getInstance();
        Month = c.get(Calendar.MONTH);
        todaysDate =c.get(Calendar.DAY_OF_MONTH) +"/"+(Month+1)+"/"+c.get(Calendar.YEAR);
        currentTime = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
        Log.d("date", "onCreate: "+todaysDate);

        if(allContacts.size() > 0){
            contactView.setVisibility(View.VISIBLE);
            mAdapter = new ItemAdapter(this, allContacts);
            contactView.setAdapter(mAdapter);

        }else {
            contactView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no items in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                mAdapter.setFilter();

                return false;
            }
        });
//

        return super.onCreateOptionsMenu(menu);

    }


    private String pad(int i) {
        if (i < 10)
            return "0" + i;
        return String.valueOf(i);
    }


    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_contact_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText noField = (EditText)subView.findViewById(R.id.enter_phno);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new Item");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD ITEM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String ph_no = noField.getText().toString();
                final String date = "Date :"+todaysDate;
                final String time= "time:"+currentTime;

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{

                    Items newContact = new Items(name, ph_no,date,time);
                    mDatabase.addContacts(newContact);
//                    mAdapter.notifyDataSetChanged();
                    finish();
                    int insertIndex=1;
//                    mAdapter.notifyItemRangeInserted(insertIndex,allContacts.size());
                    startActivity(getIntent());

                }
            }
        });



        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }

}