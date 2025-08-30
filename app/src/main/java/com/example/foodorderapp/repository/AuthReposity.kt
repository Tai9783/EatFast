package com.example.foodorderapp.repository
import android.content.Context
import android.media.session.MediaSession.Token
import android.util.Log
import com.example.foodorderapp.model.InforUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthReposity {
    private val firebaseAuth= FirebaseAuth.getInstance()


    fun login(email: String,pass: String, onCallBack: (String,Boolean)-> Unit){
        firebaseAuth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {task->
                if (task.isSuccessful)
                    onCallBack(email,true)
                else
                    onCallBack(email,false)
            }
    }

    fun registerUser(pass: String,item: InforUser, onCallBack: (Boolean)->Unit) {
         firebaseAuth
            .createUserWithEmailAndPassword(item.email,pass)
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    FirebaseRepositoryGetUserMaxId().addUser(item){isDone->
                        onCallBack(isDone)
                    }
                }
            }
    }
    fun resetPassword(email: String, onCallBack: (Boolean) -> Unit){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    onCallBack(true)
                }
                else
                    onCallBack(false)
            }
    }
    fun firebaseAuthWithGoogle(idToken: String, onCallBack: (Boolean, String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val nameUser = user?.displayName
                    val email = user?.email

                    Log.d("GoogleAuth", "User authenticated: $email")

                    if (email != null) {
                        // Kiểm tra xem user đã tồn tại trong database chưa
                        FirebaseReposityGetInforUser().getInforUser(email) { existingUser ->
                            if (existingUser.email == email) {
                                // User đã tồn tại, đăng nhập thành công
                                Log.d("GoogleAuth", "Existing user found")
                                onCallBack(true, email)
                            } else {
                                // User chưa tồn tại, tạo user mới
                                Log.d("GoogleAuth", "Creating new user")
                                if (nameUser != null) {
                                    FirebaseRepositoryGetUserMaxId().getUserMaxId { newId ->
                                        if (newId.isNotEmpty()) {
                                            val newUser = InforUser(
                                                user_id = newId,
                                                email = email,
                                                full_name = nameUser,
                                            )
                                            FirebaseRepositoryGetUserMaxId().addUser(newUser) { isDone ->
                                                if (isDone) {
                                                    Log.d("GoogleAuth", "New user created successfully")
                                                    onCallBack(true, email)
                                                } else {
                                                    Log.e("GoogleAuth", "Failed to create new user")
                                                    onCallBack(false, "")
                                                }
                                            }
                                        } else {
                                            Log.e("GoogleAuth", "Failed to get new user ID")
                                            onCallBack(false, "")
                                        }
                                    }
                                } else {
                                    Log.e("GoogleAuth", "Display name is null")
                                    onCallBack(false, "")
                                }
                            }
                        }
                    } else {
                        Log.e("GoogleAuth", "Email is null")
                        onCallBack(false, "")
                    }
                } else {
                    Log.e("GoogleAuth", "Authentication failed: ${task.exception?.message}")
                    onCallBack(false, "")
                }
            }
    }



}