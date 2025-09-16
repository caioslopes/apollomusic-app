package br.com.apollomusic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.apollomusic.ui.theme.Rose
import br.com.apollomusic.ui.theme.White

@Composable
fun ApolloWelcomeTemplate(
    content: @Composable () -> Unit
){
    Scaffold { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Rose))
        {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Apollo Music",
                    color = White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "A música nunca para.", color = White)
            }
            Surface (
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ){
                Column (
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    content()
                }
            }
        }
    }
}