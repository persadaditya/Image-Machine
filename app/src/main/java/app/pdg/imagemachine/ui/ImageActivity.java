package app.pdg.imagemachine.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import app.pdg.imagemachine.R;
import app.pdg.imagemachine.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO: implement Image for fullscreen
    }
}