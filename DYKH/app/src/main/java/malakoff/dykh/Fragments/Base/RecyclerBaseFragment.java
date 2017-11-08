package malakoff.dykh.Fragments.Base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import malakoff.dykh.R;
import malakoff.dykh.Recycler.AnimatedReAdapter;
import malakoff.dykh.Recycler.ReAdapterViewProvider;

/**
 * Created by user on 23/06/2016.
 */

public abstract class RecyclerBaseFragment<T> extends InstanceBaseFragement implements SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView recyclerView;
    protected AnimatedReAdapter<T> adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected boolean loading = false;
    protected int visibleThreshold = 2;
    protected int pageCount = 0;
    protected boolean noMoreData = false;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            if(totalItemCount == 0 || noMoreData){
                return;
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if(layoutManager instanceof LinearLayoutManager){
                int lastItemVisible = ((LinearLayoutManager)layoutManager).findFirstCompletelyVisibleItemPosition();

                if(!loading && (totalItemCount - visibleItemCount) <= (lastItemVisible + visibleThreshold)){
                     getAdapterCellBuilder().loadNextPage();
                    loading = true;
                }
            }
        }
    };


    protected boolean askForPagination(){
        return false;
    }

    protected abstract void loadQuery();

    protected abstract ReAdapterViewProvider<T> getAdapterCellBuilder();

    protected int getPageCount(){return ++pageCount;}



    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);
        setListLayoutManager();

        if(swipeRefreshLayout != null){
            swipeRefreshLayout.setOnRefreshListener(this);
        }

        if(askForPagination()){
            recyclerView.addOnScrollListener(scrollListener);
        }

        adapter = new AnimatedReAdapter<>();
        adapter.setListCellBuilder(getAdapterCellBuilder());
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void assignViews(View view) {
        pageCount = 0;
        recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
    }

    private void setListLayoutManager() {
        int scrollToPostion = 0;
        if(recyclerView.getLayoutManager() != null){
         scrollToPostion = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        layoutManager = buildLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollToPostion);
    }

    private RecyclerView.LayoutManager buildLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(false);
        pageCount = 0;
        loadQuery();
    }

    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    public void refreshFinished() {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);
    }

}
