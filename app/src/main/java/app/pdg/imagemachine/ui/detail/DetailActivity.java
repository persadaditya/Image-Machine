package app.pdg.imagemachine.ui.detail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
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
import app.pdg.imagemachine.ui.addedit.AddEditMachineActivity;

public class DetailActivity extends AppCompatActivity {
    public static final String MACHINE_ID = "dataMachineIdKey";
    private ActivityDetailBinding binding;
    private String machineId;
    private Machine mMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());


        if(getIntent().hasExtra(MACHINE_ID)){
            machineId = getIntent().getStringExtra(MACHINE_ID);
            DetailViewModel.Factory factory = new DetailViewModel.Factory(getApplication(), UUID.fromString(machineId));
            DetailViewModel viewModel = new ViewModelProvider(this,factory).get(DetailViewModel.class);


            viewModel.getMachineLiveData().observe(this, new Observer<Machine>() {
                @Override
                public void onChanged(Machine machine) {
                    mMachine = machine;
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


            binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.action_edit){
                        //TODO: Implement edit mode
                        Intent intent = new Intent(getApplicationContext(), AddEditMachineActivity.class);
                        intent.putExtra(AddEditMachineActivity.DATA_ADD_EDIT, machineId);
                        startActivity(intent);
                    } else if(item.getItemId()==R.id.action_delete){
                        //DONE: make dialog then delete data
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                        builder.setTitle("Delete Machine");
                        builder.setMessage("Are you sure you want to delete this machine?");
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewModel.deleteMachine(mMachine);
                                dialogInterface.dismiss();
                                finish();
                            }
                        });

                        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    return true;
                }
            });

        }
    }
}