package com.example.sampleprice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import static java.util.Collections.addAll;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ContactViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Items> listItems;
    private ArrayList<Items> mArrayList;
    private ArrayList<String> SearcharrayList;

    private SqliteDatabase mDatabase;
    Calendar c;
    String todaysDate;
    String currentTime;
    int Month;


    public ItemAdapter(Context context, ArrayList<Items> listItems) {
        this.context = context;
        this.listItems = listItems;
        this.mArrayList=listItems;
        mDatabase = new SqliteDatabase(context);
    }
    public static final class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView name,price,date,time;
        public ImageView deleteItem;
        public  ImageView editItem;

        public ContactViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.contact_name);
            price = (TextView)itemView.findViewById(R.id.ph_no);
            date = (TextView)itemView.findViewById(R.id.Date);
            time = (TextView)itemView.findViewById(R.id.Time);
            deleteItem = (ImageView)itemView.findViewById(R.id.delete_contact);
            editItem = (ImageView)itemView.findViewById(R.id.edit_contact);
        }
    }





    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_layout, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final Items items = listItems.get(position);

        holder.name.setText(items.getName());
        holder.price.setText("Price :"+items.getprice());
        holder.date.setText(items.getDate());
        holder.time.setText(items.getTime());


        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(items);
            }
        });

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
//                view.animate().setDuration(5000).x(-view.getWidth()).alpha(770f);

                final Animation animation= AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
                view.startAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.deleteContact(items.getId());
                        mArrayList.remove(items);
                        notifyItemRemoved(position);
                        ((Activity)context).finish();
                        Intent intent =new Intent(context,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                        notifyItemRangeChanged(position,listItems.size());
                        notifyDataSetChanged();
                        animation.cancel();

                    }
                },200);

            }
        });
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                ArrayList<Items> filteredList = new ArrayList<>();

                if (charString.isEmpty()) {

                    filteredList.addAll(mArrayList);
                } else {

                    for (Items items : mArrayList) {

                        if (items.getName().toLowerCase().contains(charString)||items.getName().toUpperCase().contains(charString)) {
                            filteredList.add(items);
                        }
                    }
                }
//                listItems = filteredList;

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                listItems = (ArrayList<Items>) filterResults.values;
                listItems.clear();
                listItems.addAll((Collection<? extends Items>) filterResults.values);
//
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setFilter(){
        ArrayList<Items> filteredList = new ArrayList<>();
        listItems=new ArrayList<>();
        listItems.addAll(filteredList);
//        notifyDataSetChanged();

    }

    private void editTaskDialog(final Items items){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_contact_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText contactField = (EditText)subView.findViewById(R.id.enter_phno);

        if(items != null){
            nameField.setText(items.getName());
            contactField.setText(String.valueOf(items.getprice()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit item");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT ITEMS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String ph_no = contactField.getText().toString();
                c = Calendar.getInstance();
                Month = c.get(Calendar.MONTH);
                todaysDate =c.get(Calendar.DAY_OF_MONTH) +"/"+(Month+1)+"/"+c.get(Calendar.YEAR);
                currentTime = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
//                final String time1 = currentTime;
                Log.d("time", "onCreate: "+currentTime);


                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updateContacts(new Items(items.getId(), name, ph_no, "Date:" + todaysDate, "Time:" + currentTime));
                    //refresh the activity
                    ((Activity)context).finish();
                    Intent intent =new Intent(context,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);

                }
            }
            private String pad(int i) {
                if (i < 10)
                    return "0" + i;
                return String.valueOf(i);
            }
        });


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}