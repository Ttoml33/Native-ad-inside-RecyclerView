package com.leestream.nativeinrcview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdapterClass extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //position to display ad native
    static int OFFSET=2;

//    determining what layout to inflate
    private static final int TYPE_AD= 0;
    private static final int TYPE_NORMAL= 1;
    private Context mContext;
    private ArrayList<NativeAd> ads= new ArrayList<>();
    private ArrayList<Object> data= new ArrayList<>();
    public AdapterClass (Context mContext){
        this.mContext=mContext;
    }
    public void setData(List<UserModel>mUser){
        this.data.addAll(mUser);
    }

//merging both arrays for ads and items
    void mergeData()
    {
        if(data.size()==0) return;
        if(ads.size()!=0)
        {
            List<Object> o = new ArrayList<>();
            int num = 0;
            for (int i = 0; i < data.size(); i++)
            {
                if(num+ OFFSET == i )
                {
                    num+=OFFSET;
                    int x = new Random().nextInt(ads.size());
                    o.add(ads.get(x));
                }
                o.add(data.get(i));
            }
            data.clear();
            data.addAll(o);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_AD)
        {
            View view = LayoutInflater.from(this.mContext).inflate(R.layout.native_ad_layout,parent,false);
            return new NativeADViewHolder(view);
        }
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.rc_item,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_AD) {
            NativeADViewHolder vh = (NativeADViewHolder) holder;
            if (data.get(position) instanceof NativeAd) {
                vh.setUnifiedNativeAd((NativeAd) data.get(position));
            } else {
                // Handle unexpected type or log an error
            }
            return;
        }
        if (viewType == TYPE_NORMAL) {
            UserViewHolder vh = (UserViewHolder) holder;
            if (data.get(position) instanceof UserModel) {
                UserModel userModel = (UserModel) data.get(position);
                vh.txtName.setText(userModel.getName());
                vh.txtEmail.setText(userModel.getEmail());
                Picasso.get()
                        .load(userModel.getImgUrl())
                        .into(vh.imgEmp);
            } else {
                // Handle unexpected type or log an error
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) instanceof NativeAd?TYPE_AD:TYPE_NORMAL;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtName,txtEmail;
        public ImageView imgEmp;
        public UserViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtMail);
            imgEmp =itemView.findViewById(R.id.imgdp);
        }
    }
    public class NativeADViewHolder extends RecyclerView.ViewHolder
    {
        public TemplateView templateView;
        public NativeADViewHolder(@NonNull View itemView)
        {
            super(itemView);
            templateView = itemView.findViewById(R.id.ad_template_sm);
            NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                    .withMainBackgroundColor(new ColorDrawable(Color.parseColor("#ffffff"))).build();
            templateView.setStyles(style);

        }
        public void setUnifiedNativeAd(NativeAd ads)
        {
            //inflating the ad to layout
            templateView.setNativeAd(ads);
        }
    }
    public void setAds(List<NativeAd> ads)
    {
        this.ads.addAll(ads);
    }

}
