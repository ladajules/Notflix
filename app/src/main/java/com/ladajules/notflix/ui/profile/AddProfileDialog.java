package com.ladajules.notflix.ui.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ladajules.notflix.R;
import com.ladajules.notflix.data.model.Profile;
import com.ladajules.notflix.utils.Extensions;

import java.util.UUID;

public class AddProfileDialog extends BottomSheetDialogFragment {

    private final String userId;
    private final OnProfileAddedListener onProfileAddedListener;

    private EditText etProfileName;
    private TextView btnCancel;
    private TextView btnSave;
    private ImageView ivAvatar;
    private ImageView ivEditAvatar;

    private String selectedAvatar = "default";

    public interface OnProfileAddedListener {
        void onProfileAdded(Profile profile);
    }

    public AddProfileDialog(String userId, OnProfileAddedListener listener) {
        this.userId = userId;
        this.onProfileAddedListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_add_profile, null);
        dialog.setContentView(view);

        // Initialize views
        etProfileName = view.findViewById(R.id.etProfileName);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSave = view.findViewById(R.id.btnSave);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        ivEditAvatar = view.findViewById(R.id.ivEditAvatar);

        // Setup click listeners
        btnCancel.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> saveProfile());
        ivEditAvatar.setOnClickListener(v -> {
            // TODO: Show avatar selection options
        });

        // Setup bottom sheet behavior
        View bottomSheet = (View) view.getParent();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
        
        behavior.setPeekHeight(0);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        behavior.setHideable(true);
        behavior.setDraggable(false);

        // Set height
        view.post(() -> {
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();
            params.height = (int) (screenHeight * 0.75);
            bottomSheet.setLayoutParams(params);
        });

        return dialog;
    }

    private void saveProfile() {
        String profileName = etProfileName.getText().toString().trim();

        if (profileName.isEmpty()) {
            Extensions.showToast(requireContext(), "Please enter a profile name");
            return;
        }

        if (profileName.length() > 50) {
            Extensions.showToast(requireContext(), "Profile name is too long");
            return;
        }

        Profile newProfile = new Profile(
                UUID.randomUUID().toString(),
                userId,
                profileName,
                selectedAvatar,
                System.currentTimeMillis()
        );

        onProfileAddedListener.onProfileAdded(newProfile);
        dismiss();
    }
}
