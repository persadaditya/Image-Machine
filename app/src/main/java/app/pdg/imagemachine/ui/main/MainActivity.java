package app.pdg.imagemachine.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import app.pdg.imagemachine.R;
import app.pdg.imagemachine.base.BaseApp;
import app.pdg.imagemachine.data.SessionHandler;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.databinding.ActivityMainBinding;
import app.pdg.imagemachine.ui.addedit.AddEditMachineActivity;
import app.pdg.imagemachine.ui.detail.DetailActivity;
import app.pdg.imagemachine.ui.scan.ScanQrActivity;

public class MainActivity extends AppCompatActivity implements MachineAdapter.MachineAdapterCallback {

    private ActivityMainBinding binding;
    public static final int CAMERA_PERMISSION = 101;
    public static final int READ_EXTERNAL_PERMISSION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.fab.setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION);
            } else {
                startActivity(new Intent(getApplicationContext(), ScanQrActivity.class));
            }
        });

        MachineAdapter adapter = new MachineAdapter(this, this);
        binding.recyclerImage.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerImage.setAdapter(adapter);

        viewModel.getMachineList().observe(this, new Observer<List<Machine>>() {
            @Override
            public void onChanged(List<Machine> machines) {
                if(machines==null){
                    return;
                }
                binding.tvNoData.setVisibility(View.GONE);
                adapter.setList(machines);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), ScanQrActivity.class));
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == READ_EXTERNAL_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), AddEditMachineActivity.class));
            } else {
                Toast.makeText(this, "read file permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.action_add){
            if(ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
            PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        READ_EXTERNAL_PERMISSION);
            } else {
                startActivity(new Intent(getApplicationContext(), AddEditMachineActivity.class));
            }
        } else if(item.getItemId() == R.id.action_sort){
            //TODO: build dialog to choose sort between name and type
            SessionHandler session = ((BaseApp)getApplication()).getSession();
            Toast.makeText(getApplicationContext(), "Not yet implemented: Name Sort=" + session.isSortByNameMode(), Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMachineItemClicked(Machine item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MACHINE_ID, item.getId().toString());
        startActivity(intent);
    }
}