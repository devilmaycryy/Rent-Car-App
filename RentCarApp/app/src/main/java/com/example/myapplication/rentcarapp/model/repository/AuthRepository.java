package com.example.myapplication.rentcarapp.model.repository;

import android.net.Uri;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.rentcarapp.model.firestore.models.Client;
import com.example.myapplication.rentcarapp.model.firestore.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Objects;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;

    public AuthRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public LiveData<User> getUserByUsername(String username){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        firestore.collection("users").document(username).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().exists()){
                    userMutableLiveData.setValue(task.getResult().toObject(User.class));
                }else{
                    userMutableLiveData.setValue(null);
                }
            }else{
                userMutableLiveData.setValue(null);
                Log.i("Errors", "Exception", task.getException());
            }
        });
        return userMutableLiveData;
    }

    public LiveData<User> getUserByUsernameAndPassword(String username, String password){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        firestore.collection("users").whereEqualTo("username", username).whereEqualTo("password", password).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                    userMutableLiveData.setValue(queryDocumentSnapshot.toObject(User.class));
                }
            }else{
                userMutableLiveData.setValue(null);
                Log.i("Exceptions", "Error", task.getException());
            }
        });
        return userMutableLiveData;
    }

    public LiveData<String> getClientEmailByUsername(String username){
        MutableLiveData<String> clientEmail = new MutableLiveData<>();
        firestore.collection("clients").whereEqualTo("user", username).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                    clientEmail.setValue(queryDocumentSnapshot.toObject(Client.class).getEmail());
                }
            }else{
                clientEmail.setValue(null);
                Log.i("Exceptions", "Error", task.getException());
            }
        });
        return clientEmail;
    }

    public LiveData<String> getClientUserByEmail(String email){
        MutableLiveData<String> userMutableLiveData = new MutableLiveData<>();
        firestore.collection("clients").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                    String username = queryDocumentSnapshot.toObject(Client.class).getUser();
                    userMutableLiveData.setValue(username);
                }
            }else{
                userMutableLiveData.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return userMutableLiveData;
    }

    public LiveData<Client> getClient(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        MutableLiveData<Client> clientMutableLiveData = new MutableLiveData<>();
        if(firebaseUser != null){
            String token = firebaseUser.getUid();
            firestore.collection("clients").document(token).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    clientMutableLiveData.setValue(task.getResult().toObject(Client.class));
                }else{
                    clientMutableLiveData.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }else{
            clientMutableLiveData.setValue(null);
        }
        return clientMutableLiveData;
    }

    public void logOut(){
        firebaseAuth.signOut();
    }

    public void updateUsersPassword(User user, String email, String newPassword){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, user.getPassword());
        if(firebaseUser != null){
            firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    firebaseUser.updatePassword(newPassword);
                    updatePasswordInFirestore(user.getUsername(), newPassword);
                }else{
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
    }

    public void updatePasswordInFirestore(String username, String newPassword){
        firestore.collection("users").document(username).update("password", newPassword).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }

    public void updateUser(String oldUsername, String newUsername){
        firestore.collection("users").document(oldUsername).update("username", newUsername).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }

    public LiveData<String> sendEmailLetterToResetPassword(String email){
        MutableLiveData<String> message = new MutableLiveData<>();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://rentcarapp.page.link/resetpassword")
                .build();
        firebaseAuth.sendPasswordResetEmail(email, actionCodeSettings).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                message.setValue("Letter was sent");
            }else{
                message.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return message;
    }


    public void authenticationByGoogle(AuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    User newUser = new User(user.getDisplayName(), user.getProviderId());
                    createUser(newUser);
                    Client newClient = new Client(user.getDisplayName(), user.getPhoneNumber(), user.getEmail(), user.getDisplayName(), Objects.requireNonNull(user.getPhotoUrl()).toString());
                    newClient.setToken(user.getUid());
                    createClient(newClient);
                }
            }else{
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }

    public void signUpWithEmailAndPassword(Client client, String password){
        firebaseAuth.createUserWithEmailAndPassword(client.getEmail(), password).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Exceptions", "Error", task.getException());
            }else{
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    client.setToken(user.getUid());
                    saveImageInCloudStorage(client);

                }
            }
        });
    }

    public void signInByEmailAndPassword(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }

    public void createUser(User user){
        firestore.collection("users").document(user.getUsername()).set(user).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception", task.getException());
            }
        });
    }

    public void createClient(Client client){
        firestore.collection("clients").document(client.getToken()).set(client).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception", task.getException());
            }
        });
    }

    public void saveImageInCloudStorage(Client client){
        Uri image = Uri.parse(client.getPhoto());
        StorageReference storageReference = firebaseStorage.getReference().child("image/" + image.getLastPathSegment());
        storageReference.putFile(image).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                storageReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                    client.setPhoto(task1.getResult().toString());
                    createClient(client);
                });
            }else{
                Log.i("Errors", "Exception", task.getException());
            }
        });
    }

}
