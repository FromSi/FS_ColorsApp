package kz.colorsapp.sgq.colorsapp.ui.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import kz.colorsapp.sgq.colorsapp.R;
import kz.colorsapp.sgq.colorsapp.ui.adapters.interfaces.OnItemClickListener;
import kz.colorsapp.sgq.colorsapp.ui.model.ItemColor;

public class RecyclerAdapterColors extends RecyclerView.Adapter<RecyclerAdapterColors.HolderColors> {

    List<ItemColor> listItems = new ArrayList<>();
    List<Boolean> listVisibly = new ArrayList<>();
    private OnItemClickListener clickListener;

    public void addItems(List<ItemColor> listItems) {
        this.listItems.addAll(listItems);

        for (int i = 0; i < listItems.size(); i++) {
            listVisibly.add(true);
        }

        notifyDataSetChanged();
    }

    public void clearItems() {
        listItems.clear();
        listItems.clear();
    }

    public void updateItems(int index) {
        ItemColor itemColor = listItems.get(index);
        itemColor.setLike(false);
        listItems.set(index, itemColor);
        notifyDataSetChanged();
    }

    public List<ItemColor> getColorItemList(){
        return listItems;
    }

    public void SetOnItemClickListener(final OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public HolderColors onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colors, parent, false);
        return new HolderColors(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderColors holder, int position) {
        holder.setImagesView(listItems.get(position));
        holder.setLiked(listItems.get(position).getId()
                , listItems.get(position).isLike());
        holder.setViewButton(listItems.get(position));
        holder.onLoadVisibly(listVisibly.get(position));
        holder.items.setOnClickListener(v -> {
            if (listVisibly.get(position)) {
                holder.onLoadVisibly(View.GONE);
                listVisibly.set(position, false);
            } else {
                holder.onLoadVisibly(View.VISIBLE);
                listVisibly.set(position, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class HolderColors extends RecyclerView.ViewHolder {

        @BindViews({R.id.itemOne, R.id.itemTwo,
                R.id.itemThree, R.id.itemFour,
                R.id.itemFive})
        List<ImageView> listImage;

        @BindView(R.id.like)
        LikeButton likeButton;

        @BindView(R.id.view)
        LikeButton viewButton;

        @BindView(R.id.items)
        LinearLayout items;

        @BindView(R.id.itemsTwo)
        LinearLayout itemsTwo;

        HolderColors(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setImagesView(ItemColor itemColor) {
            for (int i = 0; i < 5; i++) {
                itemsTwo.setVisibility(View.VISIBLE);
                if (itemColor.getColors().size() > i) {
                    listImage.get(i)
                            .setBackgroundColor(Color
                                    .parseColor(itemColor
                                            .getColors()
                                            .get(i)));
                    listImage.get(i)
                            .setVisibility(View.VISIBLE);
                } else {
                    listImage.get(i)
                            .setVisibility(View.GONE);
                }
            }
        }

        private void setLiked(final int id, final boolean b) {
            likeButton.setLiked(b);
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    clickListener.onItemLikeClick(likeButton.getRootView(), id, true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    clickListener.onItemLikeClick(likeButton.getRootView(), id, false);
                }
            });

        }

        private void setViewButton(final ItemColor itemColor) {
            viewButton.setOnClickListener(v -> {
                clickListener.onItemViewClick(likeButton.getRootView(), itemColor);
                viewButton.setLiked(false);
            });
        }

        private void onLoadVisibly(int visibly) {
            itemsTwo.setVisibility(visibly);
        }

        private void onLoadVisibly(boolean b) {
            if (b)
                itemsTwo.setVisibility(View.VISIBLE);
            else
                itemsTwo.setVisibility(View.GONE);
        }
    }
}
