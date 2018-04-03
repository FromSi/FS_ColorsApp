package kz.colorsapp.sgq.colorsapp.ui.fragment;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.colorsapp.sgq.colorsapp.R;
import kz.colorsapp.sgq.colorsapp.mvp.model.ColorsModelImpl;
import kz.colorsapp.sgq.colorsapp.mvp.presenter.ColorsPresenterImpl;
import kz.colorsapp.sgq.colorsapp.mvp.presenter.interfaces.ColorsPresenter;
import kz.colorsapp.sgq.colorsapp.mvp.view.ColorsView;
import kz.colorsapp.sgq.colorsapp.room.AppDatabase;
import kz.colorsapp.sgq.colorsapp.ui.adapters.RecyclerAdapterColors;
import kz.colorsapp.sgq.colorsapp.ui.model.ItemColor;

public class FragmentColors extends Fragment implements ColorsView {

    @BindView(R.id.rv_colors)
    RecyclerView rv_colors;

    @BindView(R.id.download)
    LinearLayout download;

    @BindView(R.id.imageDownload)
    ImageView imageDownload;

    @BindView(R.id.textDownload)
    TextView textDownload;

    @BindView(R.id.progressDownload)
    ProgressBar progressDownload;

    private LinearLayoutManager layoutManager;
    private RecyclerAdapterColors adapterColors;

    private ColorsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colors, container, false);
        ButterKnife.bind(this, view);
        init(view.getContext());
        setUpLoadMoreListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new ColorsPresenterImpl(this);
    }

    private void init(Context context){
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_colors.setLayoutManager(layoutManager);
        adapterColors = new RecyclerAdapterColors();
        rv_colors.setAdapter(adapterColors);
    }

    private void setUpLoadMoreListener(){
        rv_colors.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                presenter.handlerColorListner(layoutManager.getItemCount(),
                        layoutManager.findLastVisibleItemPosition());
            }
        });
    }

    @OnClick(R.id.download)
    public void onClickDownload(){
        presenter.downloadAllDB();
        download.setVisibility(View.GONE);
        imageDownload.setVisibility(View.GONE);
        textDownload.setVisibility(View.GONE);
        progressDownload.setVisibility(View.VISIBLE);
        rv_colors.setVisibility(View.GONE);
    }

    @Override
    public void showDownloadDB() {
        download.setVisibility(View.VISIBLE);
        imageDownload.setVisibility(View.VISIBLE);
        textDownload.setVisibility(View.VISIBLE);
        progressDownload.setVisibility(View.GONE);
        rv_colors.setVisibility(View.GONE);
    }

    @Override
    public void showColorList() {
        download.setVisibility(View.GONE);
        imageDownload.setVisibility(View.GONE);
        textDownload.setVisibility(View.GONE);
        progressDownload.setVisibility(View.GONE);
        rv_colors.setVisibility(View.VISIBLE);
    }

    @Override
    public void addItemsDB(List<ItemColor> colorList) {
        adapterColors.addItems(colorList);
        adapterColors.notifyDataSetChanged();
    }
}
