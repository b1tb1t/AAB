package com.b1tb1t.aab

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.b1tb1t.aab.layout.Greeting222
import com.b1tb1t.aab.service.AabAccessibilityService
import com.b1tb1t.aab.ui.theme.AdbTheme
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : ComponentActivity() {

    private val TESS_DATA = "/tessdata"
    private val DATA_FILENAME = "chi_sim.traineddata"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //image.setImageBitmap(bitmapFromAssets)
        setContent {
            AdbTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting222("Android")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        //连续启动Service
        val intentOne = Intent(this, AabAccessibilityService::class.java)
        //startService(intentOne)

        val bitmapFromAssets: Bitmap? = getBitmapFromAssets(this@MainActivity, "test1.png")
        recognizeTextFromBitmap(bitmapFromAssets)
    }

    private fun getBitmapFromAssets(context: Context, filename: String): Bitmap? {
        var bitmap: Bitmap? = null
        val assetManager = context.assets
        try {
            val is11: InputStream = assetManager.open(filename)
            bitmap = BitmapFactory.decodeStream(is11)
            is11.close()
            Log.i("TAG", "图片读取成功。")
//            Toast.makeText(getApplicationContext(), "图片读取成功。", Toast.LENGTH_SHORT).show();
        } catch (e: Exception) {
            Log.i("TAG", "图片读取失败。")
//            Toast.makeText(getApplicationContext(), "图片读取失败。", Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }
        return bitmap
    }


    private fun recognizeTextFromBitmap(image: Bitmap?) {
        if (image == null) {
            Toast.makeText(
                applicationContext,
                "bitmap is null",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        prepareTess()
        // 创建TessBaseAPI实例（这在内部创建本机Tesseract实例）
        val tess = TessBaseAPI()
        //给定的路径必须包含子目录“tessdata”，其中是“*.traineddata”语言文件
        //路径必须可由应用程序直接读取
        val dataPath = getExternalFilesDir("/")?.path + "/"
        if (!tess.init(dataPath, "chi_sim")) { // could be multiple languages, like "eng+deu+fra"
            //初始化Tesseract时出错（数据路径错误/无法访问或语言文件不存在）
            //释放本机Tesseract实例
            tess.recycle()
            return
        }
        //加载图像（文件路径、位图、像素…）
        //（在Tesseract生命周期内可以调用多次）
        tess.setImage(image)
        //启动识别（如果尚未对此图像进行识别）并检索结果
        //（在Tesseract生命周期内可以调用多次）
        val text = tess.utF8Text
        //tv_result.text = text
        //当您不想再使用本机Tesseract实例时，请将其释放
        //在该调用之后，无法在此TessBaseAPI实例上调用任何方法
        Toast.makeText(
            applicationContext,
            "识别成功: $text",
            Toast.LENGTH_SHORT
        ).show()
        Log.v("b1tb1t>>>", "res: $text")
        tess.recycle()
    }

    // 为Tesserect复制(从assets中复制过去)所需的数据
    private fun prepareTess() {
        try {
            // 先创建必须的目录
            val dir = getExternalFilesDir(TESS_DATA)
            if (dir?.exists() == false) {
                if (!dir.mkdir()) {
                    Toast.makeText(
                        applicationContext,
                        "目录 ${dir.path} 没有创建成功",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // 从assets中复制必须的数据
            val pathToDataFile = dir?.path + "/" + DATA_FILENAME
            if (!(File(pathToDataFile)).exists()) {
                val `in` = assets.open(DATA_FILENAME)
                val out = FileOutputStream(pathToDataFile)
                val buff = ByteArray(1024)
                var len: Int
                while (`in`.read(buff).also { len = it } > 0) {
                    out.write(buff, 0, len)
                }
                `in`.close()
                out.close()
            }
        } catch (e: Exception) {
            Log.e("TAG", e.message ?: "")
        }
    }

}
