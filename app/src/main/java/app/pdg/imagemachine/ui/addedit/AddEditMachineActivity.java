package app.pdg.imagemachine.ui.addedit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.net.Uri;
import android.os.Bundle;
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
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.databinding.ActivityAddEditMachineBinding;
import app.pdg.imagemachine.ui.main.MainActivity;
import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class AddEditMachineActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ImageAddAdapter.ImageUriCallback {

    private ActivityAddEditMachineBinding binding;
    public static final String DATA_ADD_EDIT = "addEditKey";
    private Boolean isAdd = null;
    private Date date;
    private UUID uuid;
    private List<Uri> uriListImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditMachineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        Machine machine = null;

        if(getIntent().hasExtra(DATA_ADD_EDIT)){
            //edit mode
            isAdd = false;
            binding.toolbar.setTitle("Update Machine");
            machine = getIntent().getParcelableExtra(DATA_ADD_EDIT);
            uuid = machine.getId();
        } else {
            //add mode
            isAdd = true;
            binding.toolbar.setTitle("Add Machine");
            uuid = UUID.randomUUID();
        }

        AddEditViewModel.Factory factory = new AddEditViewModel.Factory(getApplication(),
                machine != null ? machine.getId() : null);
        AddEditViewModel viewModel = new ViewModelProvider(this, factory)
                .get(AddEditViewModel.class);


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
            TedImagePicker.with(this)
                    .startMultiImage(new OnMultiSelectedListener() {
                        @Override
                        public void onSelected(@NonNull List<? extends Uri> list) {
                            if(isAdd){
                                viewModel.setImagesPath(null); //always refresh after add data
                                List<Uri> data = new ArrayList<>(list);
                                viewModel.setImagesPath(data);
                            } else {

                            }

                        }
                    });
        });

        ImageAddAdapter addAdapter = new ImageAddAdapter(this, this);
        binding.rvImage.setLayoutManager(new GridLayoutManager(this, 5));
        binding.rvImage.setAdapter(addAdapter);

        viewModel.getPathImage().observe(this, new Observer<List<Uri>>() {
            @Override
            public void onChanged(List<Uri> uris) {

                if(uris==null){
                    return;
                }

                addAdapter.setList(uris);
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

            Machine addMachine = new Machine(uuid, name, type, number, date, now);
            viewModel.insertUpdate(addMachine);

            //for add
            if(isAdd){
                for(int i=0; i<uriListImage.size();i++){
                    String path = uriListImage.get(i).getPath();
                    Image image = new Image(UUID.randomUUID(), path, uuid);
                    viewModel.insertImage(image);
                }
            } else {
                //TODO: implement update image
            }
        });


    }

    @SuppressWarnings("deprecation")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = new Date(year, monthOfYear, dayOfMonth);
        binding.tieMaintenance.setText(String.format("%s-%s-%s", year, monthOfYear+1, dayOfMonth));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onImageUriClicked(Uri item) {

    }
}