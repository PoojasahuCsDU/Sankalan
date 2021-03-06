package com.example.sankalan.ui.login.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sankalan.R
import com.example.sankalan.ui.login.data.LoggedInUser
import com.example.sankalan.ui.login.data.LoginFormState
import com.example.sankalan.ui.login.data.LoginResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val repository: AuthenticationRepository):ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginForm:LiveData<LoginFormState> = _loginForm



    private val _signUpForm = MutableLiveData<LoginFormState>()
    val signUpForm:LiveData<LoginFormState> = _signUpForm


    private val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
    val result_login = MutableLiveData<LoginResult>()

    fun login(email: String, password: String){
        if(isValidEmail(email) && isValidPassword(password)){
            viewModelScope.launch {
                val v:LoginResult = repository.login(email,password)
                result_login.value = v
            }

        }else{
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email, passError = R.string.invalid_password)
        }

    }
    fun signUp(email: String, password: String, data:LoggedInUser){
       viewModelScope.launch {
           val res:LoginResult = repository.signUp(email, password, data)
           result_login.value = res
       }

    }


    fun onLoginDataChange(email:String , password:String ){
        if (!isValidEmail(email)){
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        }else if(!isValidPassword(password)){
            _loginForm.value = LoginFormState(passError = R.string.invalid_password)
        }else{
            _loginForm.value = LoginFormState(isValid = true)
        }
    }
    fun onSignupDataChange(email:String , password:String ){
        if (!isValidEmail(email)){
            _signUpForm.value = LoginFormState(emailError = R.string.invalid_email)
        }else if(!isValidPassword(password)){
            _signUpForm.value = LoginFormState(passError = R.string.invalid_password)
        }else{
            _signUpForm.value = LoginFormState(isValid = true)
        }
    }


     fun isValidEmail(email:String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String):Boolean{
        return passwordRegex.matches(password) && password.length >5
    }


}