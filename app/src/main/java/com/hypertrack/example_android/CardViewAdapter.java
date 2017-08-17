package com.hypertrack.example_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyHolder> {

    static ArrayList<customUser> list = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public CardViewAdapter(Context context, ArrayList<customUser> list) {


        this.context = context;
        clearData();
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void clearData() {
        int size = this.list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.list.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    public ArrayList<customUser> getList() {
        return list;
    }

//    public void addItem(P item) {
//        list.add(item);
//    }

//    public void addMoreItems(ArrayList<PostInformation> extraList) {
//        for (int i = 0; i < extraList.size(); i++) {
//            list.add(extraList.get(i));
//        }
//    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        final customUser current = list.get(position);

     holder.user.setText(current.getName());
       holder.user.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent=new Intent(context,WEbView2.class);
               intent.putExtra("link",current.getId());
              context.startActivity(new Intent(intent));
           }
       });
       }


    @Override
    public int getItemCount() {
        Log.d("karma","size of list is "+list.size());
            return list.size();

    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView user;


        public MyHolder(View itemView) {
            super(itemView);
          user= (TextView) itemView.findViewById(R.id.user);
        }
    }
}
