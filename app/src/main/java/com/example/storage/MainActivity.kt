package com.example.storage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storage.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference: StorageReference
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("myPhotos")

        binding.imageView.setOnClickListener {
            getImageContent.launch("image/*")
        }

    }

    var imageUrl = ""
    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
        uri?: return@registerForActivityResult
        val task = reference.child("Men").putFile(uri)

        task.addOnSuccessListener {
            if (it.task.isSuccessful){
                val downloadUrl = it.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener {imageUri->
                    imageUrl = imageUri.toString()
                }
            }
            binding.imageView.setImageURI(uri)
        }
        task.addOnFailureListener{
            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
        }
    }


}