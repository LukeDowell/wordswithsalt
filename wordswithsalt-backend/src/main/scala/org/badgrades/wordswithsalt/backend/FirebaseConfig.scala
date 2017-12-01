package org.badgrades.wordswithsalt.backend

import java.io.InputStream

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.{FirebaseApp, FirebaseOptions}

object FirebaseConfig {
  val serviceAccountInputStream: InputStream = ClassLoader.getSystemClassLoader.getResourceAsStream("firebase-credentials.json")
  val options: FirebaseOptions = new FirebaseOptions.Builder()
    .setCredentials(GoogleCredentials.fromStream(serviceAccountInputStream))
    .setDatabaseUrl(Constants.FirebaseUrl)
    .build()
  val app: FirebaseApp = FirebaseApp.initializeApp(options)
}
