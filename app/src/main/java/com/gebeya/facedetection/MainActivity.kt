package com.gebeya.facedetection

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionPoint
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var alertDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        alertDialog = AlertDialog.Builder(this)
            .setMessage("Please wait...")
            .setCancelable(false)
            .create();

        btn_detect.setOnClickListener {
            camera_view.captureImage { cameraKitView, byteArray ->
                camera_view.onStop()
                alertDialog.show()
                var bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
                bitmap = Bitmap.createScaledBitmap(bitmap, camera_view?.width ?: 0, camera_view?.height ?: 0, false)
                runDetector(bitmap)
            }

            graphic_overlay.clear()
        }
    }

    private fun runDetector(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .enableTracking()
            .build()

        // Real-time contour detection of multiple faces
        val realTimeOpts = FirebaseVisionFaceDetectorOptions.Builder()
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
            .build()

        val detector = FirebaseVision.getInstance()
            .getVisionFaceDetector(options)

        detector.detectInImage(image)
            .addOnSuccessListener { faces ->
                processFaceResult(faces)
                for (face in faces) {

                    val bottomMouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM)
                    bottomMouth?.let {
                        val bottomMouthPos = bottomMouth.position
                        Toast.makeText(applicationContext, "Bottom Mouth position+ $bottomMouthPos", Toast.LENGTH_LONG).show()

//                        val myPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//                        myPaint.color = Color.parseColor("#99ff0000")
//                        bottomMouthPos.apply { myPaint.color }
//                        Toast.makeText(applicationContext, "Bottom Mouth color + ${bottomMouthPos.apply { myPaint.color }}", Toast.LENGTH_LONG).show()
//
//                            bottomMouthPos.x.equals("#99ff0000")
//                            bottomMouthPos.x.apply { Paint(Paint.ANTI_ALIAS_FLAG)}
//                            bottomMouthPos.y.equals("#99ff0000")
//                            bottomMouthPos.z?.equals("#99ff0000")
                    }
//                    }

                }

            }.addOnFailureListener {
                it.printStackTrace()
            }

    }
    private fun processFaceResult(faces: MutableList<FirebaseVisionFace>) {
        faces.forEach {
            val bounds = it.boundingBox
            val rectOverLay = RectOverlay(graphic_overlay, bounds)
            graphic_overlay.add(rectOverLay)
        }
        for (face in faces) {

            val bottomMouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM)
            bottomMouth?.let {
                val bottomMouthPos = bottomMouth.position
                Toast.makeText(applicationContext, "Bottom Mouth position+ $bottomMouthPos", Toast.LENGTH_LONG).show()

//                camera_view.captureImage { _, byteArray ->
//                    var bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
//                    bitmap = Bitmap.createScaledBitmap(
//                        bitmap,
//                        camera_view?.width ?: 0,
//                        camera_view?.height ?: 0,
//                        false
//                    )
//                    runDetector(bitmap)
//                    val canvas = Canvas(bitmap)
//
//                    val myPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//                    myPaint.color = Color.parseColor("#99ff0000")
//
//                    val path = Path()
//                    val xy = path.moveTo(bottomMouthPos.x, bottomMouthPos.y)
//                    path.close()
//                    canvas.drawPath(path, myPaint)
//                    //faces.setImageBitmap(mutableBitmap)
//                }
            }

        }
        alertDialog.dismiss()
    }


    override fun onResume() {
        super.onResume()
        camera_view.onResume()
    }
    override fun onPause() {
        super.onPause()
        camera_view.onPause()
    }
    override fun onStart() {
        super.onStart()
        camera_view.onStart()
    }
    override fun onStop() {
        super.onStop()
        camera_view.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera_view.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}






