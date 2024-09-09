//package com.guard.investments
//
//import App
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            App()
//        }
//    }
//}
//
//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}

package com.guard.investments

import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the XML layout
        setContentView(R.layout.activity_main)
    }
}
