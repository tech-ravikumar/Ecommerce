package com.example.ecommerceproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomRecycler_Adapter extends RecyclerView.Adapter<CustomRecycler_Adapter.MyViewHolder> implements View.OnCreateContextMenuListener{


    Context context;
    List<Product> arraylist;

    int positions;
    View viewItem;
    Onclick onclick;

    public CustomRecycler_Adapter(Context context, List<Product>arraylist){


        this.context=context;
        this.arraylist=arraylist;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View photoView
                = inflater
                .inflate(R.layout.trendingrecyclerview,
                        parent, false);


        return new CustomRecycler_Adapter.MyViewHolder(photoView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setOnCreateContextMenuListener(this);
        if(position==arraylist.size()-1){
            Glide.with(context).load(""+arraylist.get(position).getPimage()).into(holder.image);
            holder.ttitle.setVisibility(View.INVISIBLE);
            holder.tsubtitle.setVisibility(View.INVISIBLE);
            holder.tcutprice.setVisibility(View.INVISIBLE);
            holder.tprice.setVisibility(View.INVISIBLE);
            holder.addCart.setVisibility(View.INVISIBLE);
            holder.toff.setVisibility(View.INVISIBLE);
            onclick.onAddProduct(holder.itemView,position);

        }else{
            holder.ttitle.setVisibility(View.VISIBLE);
            holder.tsubtitle.setVisibility(View.VISIBLE);
            holder.tcutprice.setVisibility(View.VISIBLE);
            holder.tprice.setVisibility(View.VISIBLE);
            holder.addCart.setVisibility(View.VISIBLE);
            holder.toff.setVisibility(View.VISIBLE);
            String url=Url.BASEURL+"upload/";
            Glide.with(context).load(url+arraylist.get(position).getPimage()).into(holder.image);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onclick.checkbox(v,holder.getAdapterPosition());

                }
            });
        }

        holder.ttitle.setText(arraylist.get(position).getPtittle());
        holder.cid.setText(arraylist.get(position).getPid());
        holder.tsubtitle.setText(arraylist.get(position).getPsubtittle());
        holder.tcutprice.setText(arraylist.get(position).getPcutprice());
        holder.tprice.setText(arraylist.get(position).getPprice());
        holder.toff.setText(arraylist.get(position).getPoff());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("onlongclick","run");
                setPositionAndView(position,holder.itemView);
                return false;
            }
        });

    }
    private int getPositions(){
        return positions;
    }
    private View getViewItem(){
        return viewItem;
    }
    private void setPositionAndView(int position,View view){
        this.positions=position;
        this.viewItem=viewItem;
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image,addCart;

        RelativeLayout relativeLayout;
        TextView ttitle,tsubtitle,tcutprice,tprice,toff,cid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.img_view);
            ttitle=itemView.findViewById(R.id.txt_title);
            tsubtitle=itemView.findViewById(R.id.txt2);
            tcutprice=itemView.findViewById(R.id.txt3);
            addCart=itemView.findViewById(R.id.cart);
            relativeLayout=itemView.findViewById(R.id.test);
            tprice=itemView.findViewById(R.id.txt4);
            toff=itemView.findViewById(R.id.offs);
            cid = itemView.findViewById(R.id.cid);

        }
    }
    private final MenuItem.OnMenuItemClickListener onChange= new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
            Log.d("onlongclick","runto");
            switch (menuItem.getItemId()){
                case 1:
                    Log.d("onlongclick","runto3");
                    onclick.onEditProduct(getViewItem(),getPositions());
                    return true;
                case 2:
                    onclick.onDeleteProduct(getViewItem(),getPositions());
                    return true;
            }
            return false;
        }
    };
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        MenuItem Edit = contextMenu.add(Menu.NONE, 1, 1, "Edit");
        MenuItem Delete=contextMenu.add(Menu.NONE,2,2,"Delete");


        Log.d("onlongclick","runto4");
        Edit.setOnMenuItemClickListener(onChange);
        Delete.setOnMenuItemClickListener(onChange);

    }


    public void setonItemClick(Onclick onclick) {
        this.onclick = onclick;
    }


}
