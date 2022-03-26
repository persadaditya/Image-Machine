package app.pdg.imagemachine.ui.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import app.pdg.imagemachine.R;
import app.pdg.imagemachine.data.model.Machine;
import app.pdg.imagemachine.databinding.ActivityScanQrBinding;
import app.pdg.imagemachine.ui.main.MainActivity;

public class ScanQrActivity extends AppCompatActivity {

    private ActivityScanQrBinding binding;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(view -> this.onBackPressed());

        ScanViewModel viewModel = new ViewModelProvider(this).get(ScanViewModel.class);

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
                            Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
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




    }

    private void subscribeToViewModel(ScanViewModel viewModel){
        viewModel.getMachineByQr().observe(this, new Observer<Machine>() {
            @Override
            public void onChanged(Machine machine) {
                if (machine==null){
                    return;
                }

                //TODO: bind data if qr has already in our database

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