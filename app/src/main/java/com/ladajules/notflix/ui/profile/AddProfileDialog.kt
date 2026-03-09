package com.ladajules.notflix.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ladajules.notflix.R
import com.ladajules.notflix.data.model.Profile
import com.ladajules.notflix.utils.showToast
import java.util.UUID

class AddProfileDialog(
    private val userId: String,
    private val onProfileAdded: (Profile) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var etProfileName: EditText
    private lateinit var btnCancel: TextView
    private lateinit var btnSave: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var ivEditAvatar: ImageView

    private var selectedAvatar = "default"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = layoutInflater.inflate(R.layout.bottom_sheet_add_profile, null, false)
        dialog.setContentView(view)

        // Initialize views
        etProfileName = view.findViewById(R.id.etProfileName)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnSave = view.findViewById(R.id.btnSave)
        ivAvatar = view.findViewById(R.id.ivAvatar)
        ivEditAvatar = view.findViewById(R.id.ivEditAvatar)

        // Setup click listeners
        btnCancel.setOnClickListener {
            dismiss()
        }

        btnSave.setOnClickListener {
            saveProfile()
        }

        ivEditAvatar.setOnClickListener {
            // TODO: Show avatar selection options
        }

        // Setup bottom sheet behavior - show fully expanded immediately
        val bottomSheet = view.parent as View
        val behavior = BottomSheetBehavior.from(bottomSheet)

        behavior.apply {
            peekHeight = 0
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isHideable = true
            isDraggable = false // Disable dragging to keep it fully open
        }

        // Set expanded height immediately (half screen height for better UX)
        view.post {
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            val params = bottomSheet.layoutParams
            params.height = (screenHeight * 0.75).toInt() // 75% of screen height
            bottomSheet.layoutParams = params
            behavior.peekHeight = 0
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return dialog
    }

    private fun saveProfile() {
        val profileName = etProfileName.text.toString().trim()

        if (profileName.isEmpty()) {
            requireContext().showToast("Please enter a profile name")
            return
        }

        if (profileName.length > 50) {
            requireContext().showToast("Profile name is too long")
            return
        }

        val newProfile = Profile(
            id = UUID.randomUUID().toString(),
            userId = userId,
            name = profileName,
            avatarUrl = selectedAvatar
        )

        onProfileAdded(newProfile)
        dismiss()
    }
}