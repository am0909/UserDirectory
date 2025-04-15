package com.ameya.livefront.userdirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ameya.livefront.userdirectory.ui.UserListDetailPaneScaffold
import com.ameya.livefront.userdirectory.ui.theme.UserDirectoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserDirectoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserListDetailPaneScaffold(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
