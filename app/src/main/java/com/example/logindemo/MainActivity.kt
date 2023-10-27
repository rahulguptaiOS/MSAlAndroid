package com.example.logindemo

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.logindemo.ui.theme.LoginDemoTheme
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException

class MainActivity : ComponentActivity() {
    private var msalApplication: IMultipleAccountPublicClientApplication? = null
    private var account: IAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    Button(onClick = {
                        if(msalApplication != null) {
                            logIn()
                        }
                    }) {

                    }
                }
            }
        }

        PublicClientApplication.createMultipleAccountPublicClientApplication(
            this,
            R.raw.auth_config_b2c,
            object : IPublicClientApplication.IMultipleAccountApplicationCreatedListener{
                override fun onCreated(application: IMultipleAccountPublicClientApplication?) {
                    msalApplication = application
                }

                override fun onError(exception: MsalException?) {
                    print(exception!!)
                }
            }
        )


    }

    private fun logIn() {
        val scopes = arrayOf("https://projectcenterdev.onmicrosoft.com/cfa95631-3dff-4a46-b8b2-0103cb67b592/Files.Read")
        msalApplication!!.acquireToken(this,
            scopes,
            object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                account = authenticationResult!!.account
            }

            override fun onError(exception: MsalException?) {
                print(exception!!)
            }

            override fun onCancel() {
                print("Cancelled")
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginDemoTheme {
        Greeting("Android")
    }
}