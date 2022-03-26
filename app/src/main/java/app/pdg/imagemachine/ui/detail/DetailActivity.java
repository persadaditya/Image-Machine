package app.pdg.imagemachine.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import app.pdg.imagemachine.R;
import app.pdg.imagemachine.Utils;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    public static final String MACHINE_ID = "dataMachineIdKey";
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.action_edit){
                    //TODO: Implement edit mode
                    Toast.makeText(getApplicationContext(), "Edit mode", Toast.LENGTH_SHORT).show();
                } else if(item.getItemId()==R.id.action_delete){
                    //TODO: make dialog then delete data
                    Toast.makeText(getApplicationContext(), "Delete Mode", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });


        if(getIntent().hasExtra(MACHINE_ID)){
            String id = getIntent().getStringExtra(MACHINE_ID);
            DetailViewModel.Factory factory = new DetailViewModel.Factory(getApplication(), UUID.fromString(id));
            DetailViewModel viewModel = new ViewModelProvider(this,factory).get(DetailViewModel.class);


            viewModel.getMachineLiveData().observe(this, new Observer<Machine>() {
                @Override
                public void onChanged(Machine machine) {
                    try{
                        binding.tvName.setText(machine.getName());
                        binding.tvType.setText(String.format("Type : %s", machine.getMachineType()));
                        binding.tvNumber.setText(String.format("QR number: %s", machine.getQrNumber()));
                        binding.tvLastMaintenance.setText(String.format("Last Maintenance: %s",
                                Utils.formatDate(machine.getLastMaintenanceDate())));
                    } catch (Exception e){
                        Log.e(DetailActivity.this.getLocalClassName(), e.getMessage());
                    }
                }
            });

            viewModel.getImageList().observe(this, new Observer<List<Image>>() {
                @Override
                public void onChanged(List<Image> images) {
                    if(images==null){
                        return;
                    }

                    //TODO: implement view pager to load image based on machine id
                }
            });

        }
    }
}