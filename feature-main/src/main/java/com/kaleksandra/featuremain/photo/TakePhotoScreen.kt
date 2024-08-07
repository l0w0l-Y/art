package com.kaleksandra.featuremain.photo

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kaleksandra.corecommon.ext.date.DateFormat
import com.kaleksandra.coretheme.Dimen
import com.kaleksandra.coreui.compose.string
import com.taekwondo.featuremain.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executor

private const val IMAGE_FORMAT_SUFFIX = ".jpg"
private const val DEFAULT_IMAGE_WIDTH = 412.5f
private const val DEFAULT_IMAGE_HEIGHT = 636f

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(onImageSaved: (Uri) -> Unit) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.CAMERA)
    )
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
        if (!permissionState.allPermissionsGranted) {
            Toast.makeText(context, R.string.permissions_no_camera, Toast.LENGTH_SHORT).show()
        }
    }
    val previewView: PreviewView = remember { PreviewView(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    LaunchedEffect(key1 = Unit) {
        cameraController.bindToLifecycle(lifecycleOwner)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        previewView.controller = cameraController
    }
    val stroke = remember {
        Stroke(
            width = 10f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Box(
            Modifier
                .width(275.dp)
                .height(425.dp)
                .drawBehind {
                    drawRoundRect(
                        color = Color.LightGray,
                        style = stroke,
                        cornerRadius = CornerRadius(Dimen.padding_20.toPx())
                    )
                }
                .align(Alignment.Center),
        ) {}
        Text(
            text = string(id = R.string.title_take_photo),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Dimen.padding_16),
            onClick = {
                takePhoto(
                    context,
                    cameraController,
                    onImageSaved
                )
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Circle,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(54.dp)
            )
        }
    }
}

private fun takePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onImageSaved: (Uri) -> Unit,
    width: Float = DEFAULT_IMAGE_WIDTH,
    height: Float = DEFAULT_IMAGE_HEIGHT,
) {
    val executor: Executor = ContextCompat.getMainExecutor(context)
    val photoFile = createImageFile(context)
    val outputFileOptions =
        ImageCapture.OutputFileOptions.Builder(photoFile).build()
    cameraController.takePicture(
        outputFileOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(error: ImageCaptureException) {
                //TODO: Update on error
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                val croppedBitmap: Bitmap =
                    cropCenter(bitmap, height.toPx(), width.toPx())
                val matrix = Matrix()
                matrix.postRotate(1f * 90)
                val rotatedBitmap = Bitmap.createBitmap(
                    croppedBitmap,
                    0,
                    0,
                    croppedBitmap.width,
                    croppedBitmap.height,
                    matrix,
                    true
                )
                try {
                    val fos = FileOutputStream(photoFile)
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                onImageSaved(savedUri)
            }
        }
    )
}

fun Float.toPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

private fun cropCenter(source: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val x = (source.width - newWidth) / 2
    val y = (source.height - newHeight) / 2
    if (x < 0 || y < 0) {
        return source
    }
    val cropRect = Rect(x, y, x + newWidth, y + newHeight)
    val croppedBitmap = Bitmap.createBitmap(
        source,
        cropRect.left,
        cropRect.top,
        cropRect.width(),
        cropRect.height()
    )
    source.recycle()
    return croppedBitmap
}

private fun createImageFile(context: Context): File {
    val timeStamp = DateFormat.IMAGE_DATE_TIME_FORMAT.toDateFormat(value = System.currentTimeMillis()) ?: ""
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        timeStamp,
        IMAGE_FORMAT_SUFFIX,
        storageDir
    )
}