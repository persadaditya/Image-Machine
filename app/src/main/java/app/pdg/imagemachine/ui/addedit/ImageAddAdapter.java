package app.pdg.imagemachine.ui.addedit;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import app.pdg.imagemachine.databinding.ItemImageBinding;

public class ImageAddAdapter extends
        RecyclerView.Adapter<ImageAddAdapter.ViewHolder> {

    private static final String TAG = ImageAddAdapter.class.getSimpleName();

    private Context context;
    private List<Uri> list;
    private ImageUriCallback mAdapterCallback;
    private boolean isAdd = true;


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
            holder.binding.ivMachine.setImageURI(item);

            if(!isAdd){
                holder.binding.btnCancel.setVisibility(View.GONE);
            }

            if(mAdapterCallback!=null){
                holder.binding.btnCancel.setOnClickListener(view -> {
                    mAdapterCallback.onImageUriDeleteClicked(item);
                });
            }

        }catch (Exception e){
            holder.binding.btnCancel.setVisibility(View.GONE);
        }





    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : Math.min(list.size(), 10);
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

    public void setIsAdd(boolean value){
        isAdd = value;
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
        void onImageUriDeleteClicked(Uri item);
    }
}