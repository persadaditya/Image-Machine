package app.pdg.imagemachine.ui.addedit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import app.pdg.imagemachine.R;
import app.pdg.imagemachine.Utils;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.databinding.ActivityAddEditMachineBinding;
import app.pdg.imagemachine.ui.main.MainActivity;
import app.pdg.imagemachine.ui.scan.ScanQrActivity;
import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class AddEditMachineActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ImageAddAdapter.ImageUriCallback {

    private ActivityAddEditMachineBinding binding;
    public static final String DATA_ADD_EDIT = "addEditKey";
    private Boolean isAdd = null;
    private final Calendar date = Calendar.getInstance();
    private UUID uuid;
    private List<Uri> uriListImage = new ArrayList<>();
    private final List<Uri> uriUpdateDb = new ArrayList<>();
    private String machineId = null;
    private ImageAddAdapter addAdapter;
    private AddEditViewModel viewModel;

    public static final int CAMERA_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditMachineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        if(getIntent().hasExtra(DATA_ADD_EDIT)){
            //edit mode
            isAdd = false;
            binding.toolbar.setTitle("Update Machine");
            binding.btnAddUpdate.setText("Update Machine");
            machineId = getIntent().getStringExtra(DATA_ADD_EDIT);
            uuid = UUID.fromString(machineId);
        } else {
            //add mode
            isAdd = true;
            binding.toolbar.setTitle("Add Machine");
            uuid = UUID.randomUUID();
        }

        AddEditViewModel.Factory factory = new AddEditViewModel.Factory(getApplication(),
                machineId != null ? uuid : null);
        viewModel = new ViewModelProvider(this, factory)
                .get(AddEditViewModel.class);

        addAdapter = new ImageAddAdapter(this, this);
        binding.rvImage.setLayoutManager(new GridLayoutManager(this, 5));
        binding.rvImage.setAdapter(addAdapter);
        addAdapter.setIsAdd(isAdd);

        if(!isAdd){
            viewModel.getMachineLiveData().observe(this, new Observer<Machine>() {
                @Override
                public void onChanged(Machine machine) {
                    if(machine==null){
                        return;
                    }
                    binding.tieName.setText(machine.getName());
                    binding.tieNumber.setText(String.valueOf(machine.getQrNumber()));
                    binding.tieType.setText(machine.getMachineType());
                    binding.tieMaintenance.setText(Utils.formatDate(machine.getLastMaintenanceDate()));
                    date.setTime(machine.getLastMaintenanceDate());
                }
            });

            viewModel.getImageLiveData().observe(this, new Observer<List<Image>>() {
                @Override
                public void onChanged(List<Image> images) {
                    if(images==null){
                        return;
                    }

                    for(int i=0;i<images.size();i++){
                        Uri data = Uri.parse(images.get(i).getPath());
                        uriListImage.add(data);
                        uriUpdateDb.add(data);
                        addAdapter.setList(uriListImage);
                    }
                }
            });
        }


        binding.tieMaintenance.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Initial day selection
            );
            dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        });

        binding.btnAddImage.setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION);
                return;
            }

            if(uriListImage.size()>9){
                Toast.makeText(getApplicationContext(), "Max image 10", Toast.LENGTH_SHORT).show();
                return;
            }

            openImagePicker();
        });



        viewModel.getPathImage().observe(this, new Observer<List<Uri>>() {
            @Override
            public void onChanged(List<Uri> uris) {
                if(uris==null){
                    return;
                }
                addAdapter.setList(uris);
                if(isAdd){
                    uriListImage = uris;
                } else {
                    uriListImage.addAll(uris);
                }
            }
        });


        binding.btnAddUpdate.setOnClickListener(view -> {
            if(binding.tieName.getText().toString().isEmpty()){
                Toast.makeText(this, "Name must be filled", Toast.LENGTH_SHORT).show();
                return;
            } else if(binding.tieType.getText().toString().isEmpty()){
                Toast.makeText(this, "Type must be filled", Toast.LENGTH_SHORT).show();
                return;
            } else if(binding.tieMaintenance.getText().toString().isEmpty()){
                Toast.makeText(this, "Last Maintenance must be choose", Toast.LENGTH_SHORT).show();
                return;
            } else if(binding.tieNumber.getText().toString().isEmpty()){
                Toast.makeText(this, "Machine Number must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = binding.tieName.getText().toString();
            String type = binding.tieType.getText().toString();
            int number = Integer.parseInt(binding.tieNumber.getText().toString());
            Date now = Calendar.getInstance().getTime();

            Machine addMachine = new Machine(uuid, name, type, number, date.getTime(), now);
            viewModel.insertUpdate(addMachine);

            //for update images
            if(!isAdd){
                //if data had already in our db remove it first
                uriListImage.removeAll(uriUpdateDb);
            }

            ///maximal 10 images
            int size = Math.min(uriListImage.size(), 10);

            for(int i=0; i<size;i++){
                String path = uriListImage.get(i).toString();
                Log.d("aap", "listPath:" + path);
                Image image = new Image(UUID.randomUUID(), path, uuid);
                viewModel.insertImage(image);
            }

            onBackPressed();
        });


    }

    private void openImagePicker(){
        TedImagePicker.with(this)
                .startMultiImage(new OnMultiSelectedListener() {
                    @Override
                    public void onSelected(@NonNull List<? extends Uri> list) {

                        //always refresh after add data
                        List<Uri> data = new ArrayList<>(list);
                        if(isAdd){
                            viewModel.setImagesPath(data);
                        } else {
                            data.removeAll(uriListImage);
                            uriListImage.addAll(data);
                            if(uriListImage.size()>10){
                                Toast.makeText(getApplicationContext(), "Last data will be remove, max 10 images",
                                        Toast.LENGTH_SHORT).show();
                                uriListImage.subList(10, uriListImage.size()).clear();

                            }

                            addAdapter.setList(uriListImage);
                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date.set(year, monthOfYear, dayOfMonth);
        binding.tieMaintenance.setText(String.format("%s-%s-%s", year, monthOfYear+1, dayOfMonth));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onImageUriDeleteClicked(Uri item) {
        if(isAdd){
            uriListImage.remove(item);
            addAdapter.setList(uriListImage);
        }
    }
}