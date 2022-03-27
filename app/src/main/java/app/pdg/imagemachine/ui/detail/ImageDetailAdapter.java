package app.pdg.imagemachine.ui.detail;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import app.pdg.imagemachine.databinding.ItemImageBinding;
import app.pdg.imagemachine.databinding.ItemImageDetailBinding;

public class ImageDetailAdapter extends
        RecyclerView.Adapter<ImageDetailAdapter.ViewHolder> {

    private static final String TAG = ImageDetailAdapter.class.getSimpleName();

    private Context context;
    private List<Uri> list;
    private ImageUriCallback mAdapterCallback;


    public ImageDetailAdapter(Context context, ImageUriCallback mAdapterCallback) {
        this.context = context;
        this.mAdapterCallback = mAdapterCallback;
    }

    public ImageDetailAdapter(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemImageDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        Uri item = list.get(position);
        holder.binding.ivMachine.setImageURI(item);

        if(mAdapterCallback!=null){
            holder.itemView.setOnClickListener(view -> {
                mAdapterCallback.onImageUriClicked(item);
            });
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
        ItemImageDetailBinding binding;

        public ViewHolder(ItemImageDetailBinding viewBinding) {
            super(viewBinding.getRoot());
            binding = viewBinding;
        }
    }

    public interface ImageUriCallback {
        void onImageUriClicked(Uri item);
    }
}