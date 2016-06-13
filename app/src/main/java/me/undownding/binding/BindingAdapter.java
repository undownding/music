package me.undownding.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by undownding on 16-2-24.
 */
public abstract class BindingAdapter<T> extends RecyclerView.Adapter<BindingAdapter.Holder> {

    private final List<T> list;
    private final int itemLayoutId;
    private final int brId;

    public BindingAdapter(List<T> list, int itemLayoutId, int brId) {
        this.list = list;
        this.itemLayoutId = itemLayoutId;
        this.brId = brId;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, itemLayoutId, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final T item = list.get(position);
        holder.binding.setVariable(brId, list.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindingAdapter.this.onClick(v, item, holder.getLayoutPosition());
            }
        });

    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
    }

    public List<T> getData() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected abstract void onClick(View view, T item, int position);

    public void appendData(List<T> mDataList) {
        if (list != null) {
            list.addAll(mDataList);
        }
    }

    public void setData(List<T> mDataList) {
        if (list != null) {
            list.clear();
            list.addAll(mDataList);
        }
    }

    static class Holder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public Holder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}