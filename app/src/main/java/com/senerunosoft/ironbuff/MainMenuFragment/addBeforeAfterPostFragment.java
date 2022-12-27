package com.senerunosoft.ironbuff.MainMenuFragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.PostImageAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAddBeforeAfterPostBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class addBeforeAfterPostFragment extends Fragment {

    FragmentAddBeforeAfterPostBinding binding;
    List<String> urlList;
    Context context;
    boolean editPhoto;
    ActivityResultLauncher<String> permissionLauncher;
    ActivityResultLauncher<Intent> activityResultLauncher;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout openGallery, openCamera;
    Uri newUri;
    List<Uri> uriList;

    public static addBeforeAfterPostFragment newInstance() {
        Bundle args = new Bundle();
        addBeforeAfterPostFragment fragment = new addBeforeAfterPostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBeforeAfterPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        registerLauncher();

        defVariable();
        setImage();


    }


    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                        newUri = intentFromResult.getData();
                        urlList.add(newUri.toString());
                        uriList.add(newUri);
                        changeLastPhoto();
                        binding.observerText.setText("uri:"+uriList.size()+"   url:"+urlList.size());
                        bottomSheetDialog.hide();
                        binding.addPostImageViewPager.getAdapter().notifyDataSetChanged();

                    }
                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                } else {
                    Toast.makeText(getContext(), "Permission Needed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeLastPhoto() {
        int size = urlList.size();
        String uri = urlList.get(size-2);
        urlList.remove(size-2);
        urlList.add(uri);

    }

    private void defVariable() {
        urlList = new ArrayList<>();
        uriList = new ArrayList<>();

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.image_uploaded_bottom_sheet_diaolog);
        openCamera = bottomSheetDialog.findViewById(R.id.open_camera);
        openGallery = bottomSheetDialog.findViewById(R.id.open_gallery);

    }


    private void setImage() {
        Uri uri = (new Uri.Builder()).scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(getResources().getResourcePackageName(R.drawable.add_image))
                .appendPath(getResources().getResourceTypeName(R.drawable.add_image))
                .appendPath(getResources().getResourceEntryName(R.drawable.add_image))
                .build();
        urlList.add(uri.toString());
        editPhoto = true;
        PostImageAdapter adapter = new PostImageAdapter(getContext(), urlList, editPhoto);
        binding.addPostImageViewPager.setAdapter(adapter);

        adapter.setOnItemClick(new PostImageAdapter.OnItemClick() {
            @Override
            public void getPosition() {
                bottomSheetDialog.show();
                getImage();

            }
        });

    }

    private void getImage() {
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Snackbar.make(view, "Needed permission for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                            }
                        }).show();
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    bottomSheetDialog.hide();
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
