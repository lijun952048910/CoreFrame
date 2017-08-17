package com.core.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class FirstFragment extends Fragment {
    RecyclerView recycler_view;
    private int index =1;
    private GankAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager linearLayoutManager;
    public static FirstFragment instance() {
        FirstFragment view = new FirstFragment();
		return view;
	}
    private void loadApi(final int page){
        OkGo.get("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/"+page).execute(new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    List<String> newDatas = new ArrayList<String>();
                    JSONObject json = new JSONObject(s);
                    JSONArray array = new JSONArray(json.getString("results"));
                    for(int i = 0;i<array.length();i++){
                        JSONObject ob = array.getJSONObject(i);
                        newDatas.add(ob.getString("url"));
                        Log.e("tag","========== url: "+ob.getString("url"));
                    }
                    mAdapter.addItem(newDatas);
                    swipeRefreshLayout.setRefreshing(false);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, null);
        recycler_view= (RecyclerView)view.findViewById(R.id.recycler_view);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.demo_swiperefreshlayout);
        swipeRefreshLayout.setColorSchemeColors(0xfffc6496);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(linearLayoutManager);
       // recycler_view.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_view.setAdapter(mAdapter=new GankAdapter());
        loadApi(index);
        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index=(int)(40.0*Math.random()) + 1;
                loadApi(index);
            }
        });
        return view;
    }
    class GankAdapter extends  RecyclerView.Adapter<GankAdapter.ViewHolder> {
        private  List<String> mListItem =new ArrayList<String>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(v);
        }

        public void addItem(List<String> newDatas) {
            mListItem.clear();
            mListItem.addAll(newDatas);
            notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String url = mListItem.get(position);
            Glide.with(getActivity())
                    .load(url)
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mListItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }
}
