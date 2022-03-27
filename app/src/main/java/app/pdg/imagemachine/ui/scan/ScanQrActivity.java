package app.pdg.imagemachine.ui.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import app.pdg.imagemachine.R;
import app.pdg.imagemachine.Utils;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.databinding.ActivityScanQrBinding;
import app.pdg.imagemachine.ui.detail.DetailActivity;
import app.pdg.imagemachine.ui.main.MainActivity;

public class ScanQrActivity extends AppCompatActivity {

    private ActivityScanQrBinding binding;
    private CodeScanner mCodeScanner;
    private Machine mMachine;
    private ScanViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(view -> this.onBackPressed());
        binding.cardMachine.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int code = Integer.parseInt(result.getText());
                            viewModel.loadMachineData(code);
                            subscribeToViewModel(viewModel);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Invalid number detected",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                });
            }
        });

        binding.scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        binding.cardMachine.setOnClickListener(view -> {
            if(mMachine!=null){
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.MACHINE_ID, mMachine.getId().toString());
                startActivity(intent);
            }
        });


    }

    private void subscribeToViewModel(ScanViewModel viewModel){
        viewModel.getMachineByQr().observe(this, new Observer<Machine>() {
            @Override
            public void onChanged(Machine machine) {
                if (machine==null){
                    Toast.makeText(getApplicationContext(), "machine is not on database",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }


                Intent intent = new Intent(ScanQrActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.MACHINE_ID, machine.getId().toString());
                startActivity(intent);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}