package app.pdg.imagemachine.ui.main;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.List;

import app.pdg.imagemachine.Utils;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.databinding.ItemImageBinding;
import app.pdg.imagemachine.databinding.ItemMachineBinding;

public class MachineAdapter extends
        RecyclerView.Adapter<MachineAdapter.ViewHolder> {

    private static final String TAG = MachineAdapter.class.getSimpleName();

    private Context context;
    private List<Machine> list;
    private MachineAdapterCallback mAdapterCallback;


    public MachineAdapter(Context context, MachineAdapterCallback mAdapterCallback) {
        this.context = context;
        this.mAdapterCallback = mAdapterCallback;
    }

    public MachineAdapter(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMachineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        Machine item = list.get(position);
        Log.d("aap", "machine:" +item.getName());

        holder.binding.tvName.setText(item.getName());
        holder.binding.tvType.setText(item.getMachineType());

//        DateTimeFormatter formatter = new DateTimeFormatter();
        holder.binding.tvLastMaintenance.setText(Utils.formatDate(item.getLastMaintenanceDate()));


        if(mAdapterCallback!=null){
            holder.binding.getRoot().setOnClickListener(view -> {
                mAdapterCallback.onMachineItemClicked(item);
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

    public void setList(List<Machine> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMachineBinding binding;

        public ViewHolder(ItemMachineBinding viewBinding) {
            super(viewBinding.getRoot());
            binding = viewBinding;
        }
    }

    public interface MachineAdapterCallback {
        void onMachineItemClicked(Machine item);
    }
}