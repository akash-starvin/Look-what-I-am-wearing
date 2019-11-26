package com.example.android.lookwhatiamwaring2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<ModelData> modelList;
    ArrayList<ModelData> arrayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("User Data");


    StorageReference photoRef;

    public ListViewAdapter(Context context, List<ModelData> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<ModelData>();
        this.arrayList.addAll(modelList);

    }
    public class ViewHolder
    {
        TextView mPackDate,mPackDes;
        Button mPackDelete;
        ImageView mPackImg;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get( i );
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {



        final ListViewAdapter.ViewHolder  holder;
        if (view == null) {
            holder = new ListViewAdapter.ViewHolder();

            view = inflater.inflate(R.layout.sticker_main, null);

            holder.mPackDate = view.findViewById(R.id.sticker_date);
            holder.mPackDes = view.findViewById(R.id.sticker_des);
            holder.mPackDelete = view.findViewById(R.id.sticker_btn);
            holder.mPackImg = view.findViewById(R.id.sticker_img);
            view.setTag(holder);

        } else {
            holder = (ListViewAdapter.ViewHolder) view.getTag();
        }
        holder.mPackDate.setText( modelList.get(i).getDate() );
        holder.mPackDes.setText( modelList.get(i).getDes() );
        Picasso.get().load(modelList.get(i).getUrl()).into(holder.mPackImg);

        holder.mPackDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoRef = FirebaseStorage.getInstance().getReferenceFromUrl( modelList.get(i).getUrl() );
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!

                    }
                });


                ref.child(modelList.get(i).getDBKey()).removeValue();


            }
        });



        return view;
    }

}
