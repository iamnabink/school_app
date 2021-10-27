package com.swipecrafts.school.ui.dashboard.dresscode;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.DressCode;
import com.swipecrafts.school.data.model.db.Gender;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

public class DressCodeListAdapter extends RecyclerView.Adapter<DressCodeListAdapter.ViewHolder> {

    private final ImageLoader<Gender> mImgLoader;
    private List<DressCode> genderList;

    public DressCodeListAdapter(List<DressCode> genderList, ImageLoader<Gender> loader) {
        this.genderList = genderList;
        this.mImgLoader = loader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dress_code_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.model = genderList.get(position);


        List<Gender> genders = holder.model.getGender();
        if (genders == null || genders.isEmpty()) {
            LogUtils.errorLog("Gender", "there is no gender data!!");
        } else {
            for (int i = 0; i < genders.size(); i++) {

                if (i == 0) holder.boy = genders.get(i);
                else if (i == 1) holder.girl = genders.get(i);
            }

            holder.dayNameTV.setText(holder.model.getDay());
            holder.bindView();
            mImgLoader.loadImage(holder.categoryType1IV, holder.boy);
            mImgLoader.loadImage(holder.categoryType2IV,holder.girl);
        }
    }

    public void updateDressCode(List<DressCode> list) {
        this.genderList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return genderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public DressCode model;

        private Gender boy;
        private Gender girl;

        TextView dayNameTV;
        TextView categoryType1TV;
        TextView categoryType2TV;
        TextView categoryType1CodeTV;
        TextView categoryType2CodeTV;
        TextView categoryType1DescTV;
        TextView categoryType2DescTV;
        ImageView categoryType1IV;
        ImageView categoryType2IV;

        LinearLayout gender1Lyt;
        LinearLayout gender2Lyt;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            gender1Lyt = (LinearLayout) view.findViewById(R.id.gender1Lyt);
            gender2Lyt = (LinearLayout) view.findViewById(R.id.gender2Lyt);

            dayNameTV = (TextView) view.findViewById(R.id.dayNameTV);
            categoryType1TV = (TextView) view.findViewById(R.id.categoryType1TV);
            categoryType1CodeTV = (TextView) view.findViewById(R.id.categoryType1CodeTV);
            categoryType1DescTV = (TextView) view.findViewById(R.id.categoryType1DescTV);
            categoryType1IV = (ImageView) view.findViewById(R.id.categoryType1IV);

            categoryType2TV = (TextView) view.findViewById(R.id.categoryType2TV);
            categoryType2CodeTV = (TextView) view.findViewById(R.id.categoryType2CodeTV);
            categoryType2DescTV = (TextView) view.findViewById(R.id.categoryType2DescTV);
            categoryType2IV = (ImageView) view.findViewById(R.id.categoryType2IV);
        }

        public void bindView() {
            if (boy != null) {
                categoryType1TV.setText(boy.getCategory());
                categoryType1CodeTV.setText(boy.getCode());
                categoryType1DescTV.setText(boy.getDescription());
            }else {
                gender1Lyt.setVisibility(View.GONE);
            }

            if (girl != null) {
                categoryType2TV.setText(girl.getCategory());
                categoryType2CodeTV.setText(girl.getCode());
                categoryType2DescTV.setText(girl.getDescription());
            }else {
                gender2Lyt.setVisibility(View.GONE);
            }
        }
    }
}
