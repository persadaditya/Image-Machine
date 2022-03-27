package app.pdg.imagemachine.ui.image;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.UUID;

import app.pdg.imagemachine.R;
import app.pdg.imagemachine.data.model.Image;
import app.pdg.imagemachine.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {
    public static final String DATA_IMAGE = "dataImageKey";
    public static final String DATA_TITLE = "dataTitleKey";

    private ActivityImageBinding binding;
    private Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent().hasExtra(DATA_IMAGE)){
            String id = getIntent().getStringExtra(DATA_IMAGE);

            ImageViewModel.Factory factory = new ImageViewModel.Factory(getApplication(), UUID.fromString(id));
            ImageViewModel viewModel = new ViewModelProvider(this, factory).get(ImageViewModel.class);

            viewModel.getImageLiveData().observe(this, new Observer<Image>() {
                @Override
                public void onChanged(Image image) {
                    if(image == null){
                        return;
                    }
                    ImageActivity.this.image = image;
                    Uri uriFrom = Uri.parse(image.getPath());
                    binding.photoView.setImageURI(uriFrom);

                }
            });

            binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.action_delete){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
                        builder.setTitle("Delete Image");
                        builder.setMessage("Are you sure you want to delete this image");
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(image!=null){
                                    viewModel.deleteImage(image);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error get Image", Toast.LENGTH_SHORT).show();
                                }
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
                    return false;
                }
            });
        }

        if(getIntent().hasExtra(DATA_TITLE)){
            String title = getIntent().getStringExtra(DATA_TITLE);
            binding.toolbar.setTitle(title);
        } else {
            binding.toolbar.setTitle("Detail Image");
        }



        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }
}