package com.gebeya.facedetection

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_pica.*

class ImagePica : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pica)

        user_input.setOnEditorActionListener { _, action, _ ->
            if(action == EditorInfo.IME_ACTION_GO) {
                Picasso.get()
                    .load(user_input.text.toString())
                    .into(photo)
                true
            }
            false
        }

        action_button.setOnClickListener {

            // Rest of the code goes here
            val detectorOptions =
                FirebaseVisionFaceDetectorOptions.Builder()
                    .setContourMode(
                        FirebaseVisionFaceDetectorOptions.ALL_CONTOURS
                    ).build()

            val detector = FirebaseVision
                .getInstance()
                .getVisionFaceDetector(detectorOptions)

            val visionImage = FirebaseVisionImage.fromBitmap(
                (photo.drawable as BitmapDrawable).bitmap
            )
            detector.detectInImage(visionImage).addOnSuccessListener {
                // More code here
            }

                // More code here
            }
        }
    }