package com.example.smart_hog

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class HealthFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PigAdapter
    private lateinit var cardAddPig: MaterialCardView
    private lateinit var ivPreviewNewPig: ImageView
    private var isAddCardVisible = false
    private var tempBitmap: Bitmap? = null
    
    private val pigList = ArrayList<Pig>()
    
    private val NEW_PIG_UPLOAD_REQUEST = 2000
    private val EDIT_PIG_REQUEST_BASE = 1000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_health, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back_health)
        btnBack?.setOnClickListener {
            findNavController().navigateUp()
        }

        // Initialize Components
        cardAddPig = view.findViewById(R.id.card_add_pig)
        ivPreviewNewPig = view.findViewById(R.id.iv_preview_new_pig)
        
        // Find EditText inside the card - FIXED ID to match XML
        val etPigIdInput = cardAddPig.findViewById<EditText>(R.id.et_pig_id_input)
        
        val btnShowAdd = view.findViewById<ImageButton>(R.id.btn_show_add_card)
        val btnUpload = view.findViewById<View>(R.id.btn_upload_new_pig)
        val btnSave = view.findViewById<View>(R.id.btn_save_batch)

        // Initial Data
        if (pigList.isEmpty()) {
            pigList.add(Pig("#PG-1024", "Batch: B-2024-MAR", "85 kg", "Ahead"))
            pigList.add(Pig("#PG-1025", "Batch: B-2024-MAR", "82 kg", "Normal"))
            pigList.add(Pig("#PG-1026", "Batch: B-2024-MAR", "80 kg", "Below"))
        }

        recyclerView = view.findViewById(R.id.pigRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PigAdapter(pigList, this) // Pass Fragment
        recyclerView.adapter = adapter

        // Toggle Logic for Add Card
        btnShowAdd?.setOnClickListener {
            isAddCardVisible = !isAddCardVisible
            cardAddPig.visibility = if (isAddCardVisible) View.VISIBLE else View.GONE
            btnShowAdd.setImageResource(if (isAddCardVisible) android.R.drawable.ic_menu_close_clear_cancel else android.R.drawable.ic_input_add)
        }

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, NEW_PIG_UPLOAD_REQUEST)
        }

        btnSave.setOnClickListener {
            val idText = etPigIdInput?.text?.toString() ?: ""
            if (idText.isNotEmpty()) {
                val newPig = Pig(idText, "Batch: " + java.text.SimpleDateFormat("yyyy-MMM", java.util.Locale.US).format(java.util.Date()).uppercase(), "0 kg", "New")
                newPig.imageBitmap = tempBitmap
                pigList.add(0, newPig) // Add to top
                adapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)
                
                // Reset
                etPigIdInput?.setText("")
                ivPreviewNewPig.visibility = View.GONE
                tempBitmap = null
                cardAddPig.visibility = View.GONE
                isAddCardVisible = false
                btnShowAdd?.setImageResource(android.R.drawable.ic_input_add)
                
                Toast.makeText(requireContext(), "Batch Saved Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter Pig ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val selectedImage = data?.data
            selectedImage?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                
                if (requestCode == NEW_PIG_UPLOAD_REQUEST) {
                    tempBitmap = bitmap
                    ivPreviewNewPig.setImageBitmap(bitmap)
                    ivPreviewNewPig.visibility = View.VISIBLE
                } else if (requestCode >= EDIT_PIG_REQUEST_BASE) {
                    val position = requestCode - EDIT_PIG_REQUEST_BASE
                    if (position < pigList.size) {
                        pigList[position].imageBitmap = bitmap
                        adapter.notifyItemChanged(position)
                        Toast.makeText(requireContext(), "Pig image updated!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
