package app.pdg.imagemachine.ui.addedit;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import app.pdg.imagemachine.databinding.ItemImageBinding;

public class ImageAddAdapter extends
        RecyclerView.Adapter<ImageAddAdapter.ViewHolder> {

    private static final String TAG = ImageAddAdapter.class.getSimpleName();

    private Context context;
    private List<Uri> list;
    private ImageUriCallback mAdapterCallback;


    public ImageAddAdapter(Context context, ImageUriCallback mAdapterCallback) {
        this.context = context;
        this.mAdapterCallback = mAdapterCallback;
    }

    public ImageAddAdapter(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        try{
            Uri item = list.get(position);
            Log.d("aap", "path:" + item.getPath());
            Glide.with(context)
                    .load(item)
                    .into(holder.binding.ivMachine);


            if(mAdapterCallback!=null){
                holder.binding.btnCancel.setOnClickListener(view -> {
                    mAdapterCallback.onImageUriClicked(item);
                });
            }

        }catch (Exception e){
            holder.binding.btnCancel.setVisibility(View.GONE);
        }





    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    public void clear() {
        int size = this.list.size();
        this.list.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void setList(List<Uri> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemImageBinding binding;

        public ViewHolder(ItemImageBinding viewBinding) {
            super(viewBinding.getRoot());
            binding = viewBinding;
        }
    }

    public interface ImageUriCallback {
        void onImageUriClicked(Uri item);
    }
}