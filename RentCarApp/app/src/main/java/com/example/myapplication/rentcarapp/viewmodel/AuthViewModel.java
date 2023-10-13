package com.example.myapplication.rentcarapp.viewmodel;

import android.hardware.usb.UsbRequest;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.regex.*;

import com.example.myapplication.rentcarapp.model.firestore.models.Client;
import com.example.myapplication.rentcarapp.model.firestore.models.User;
import com.example.myapplication.rentcarapp.model.repository.AuthRepository;
import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends ViewModel {
    private MutableLiveData<Boolean> checkEmailAndPassword = new MutableLiveData<>();
    private MutableLiveData<Boolean> checkPhone = new MutableLiveData<>();
    private MutableLiveData<Boolean> checkNewPassword = new MutableLiveData<>();

    private AuthRepository authRepository;

    public AuthViewModel(){
        authRepository = new AuthRepository();
    }

    public LiveData<Boolean> isEmailAndPasswordWriteCorrect(String email, String newPassword){
        if(isEmailWriteCorrect(email) && isPasswordLargeThanOrEqualEight(newPassword)){
            checkEmailAndPassword.setValue(true);
        }else{
            checkEmailAndPassword.setValue(false);
        }
        return checkEmailAndPassword;
    }

    public LiveData<Boolean> isUserPhoneCorrect(String phone){
        if(isPhoneWriteCorrect(phone)){
            checkPhone.setValue(true);
        }else{
            checkPhone.setValue(false);
        }
        return checkPhone;
    }

    public LiveData<Boolean> isNewPasswordWriteCorrect(String newPassword, String confirmNewPassword){
        if(isNewPasswordEqualConfirmNewPassword(newPassword, confirmNewPassword) && isPasswordLargeThanOrEqualEight(newPassword)){
            checkNewPassword.setValue(true);
        }else{
            checkNewPassword.setValue(false);
        }
        return checkNewPassword;
    }

    public LiveData<User> getUserByUsername(String username){
        return authRepository.getUserByUsername(username);
    }

    public LiveData<User> getUserByUsernameAndPassword(String username, String password){
        return authRepository.getUserByUsernameAndPassword(username, password);
    }

    public LiveData<Client> getClient(){
        return authRepository.getClient();
    }

    public LiveData<String> getClientUserByEmail(String email){
        return authRepository.getClientUserByEmail(email);
    }

    public LiveData<String> getClientEmailByUsername(String username){
        return authRepository.getClientEmailByUsername(username);
    }

    public LiveData<String> sendEmailLetterToResetPassword(String email){
        return authRepository.sendEmailLetterToResetPassword(email);
    }

    public void saveImageInCloudStorage(Client client){
        authRepository.saveImageInCloudStorage(client);
    }

    public void updateUsersPassword(User user, String email, String newPassword){
        authRepository.updateUsersPassword(user, email, newPassword);
    }

    public void signUpWithEmailAndPassword(Client client, String password){
        authRepository.signUpWithEmailAndPassword(client, password);
    }

    public void signInByEmailAndPassword(String email, String password){
        authRepository.signInByEmailAndPassword(email, password);
    }

    public void authenticationByGoogle(AuthCredential credential){
        authRepository.authenticationByGoogle(credential);
    }

    public void updateClient(Client client){
        authRepository.createClient(client);
    }

    public void updateUser(String oldUsername, String newUsername){
        authRepository.updateUser(oldUsername, newUsername);
    }

    public void logOut(){
        authRepository.logOut();
    }

    public void createUser(User user){
        authRepository.createUser(user);
    }

    private boolean isNewPasswordEqualConfirmNewPassword(String newPassword, String confirmNewPassword){
        return newPassword.equals(confirmNewPassword);
    }

    private boolean isPasswordLargeThanOrEqualEight(String newPassword){
        return newPassword.length() >= 8;
    }

    private boolean isEmailWriteCorrect(String email){
        return email.endsWith("@gmail.com");
    }

    private boolean isPhoneWriteCorrect(String phone){
        String pattern = "^\\+380\\d{9}$";
        return Pattern.matches(pattern, phone);
    }
}
