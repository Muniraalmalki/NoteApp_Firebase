package com.example.noteapp_firebase

import android.app.Application
import android.icu.text.CaseMap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp_firebase.Model.Note
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class ViewModel(application: Application): AndroidViewModel(application) {

    private var notes:MutableLiveData<List<Note>> = MutableLiveData()
    private val db = Firebase.firestore
    private var TAG = "MainActivity"


    fun addNote(note: Note){
        CoroutineScope(Dispatchers.IO).launch{
           val note = hashMapOf(
               note.noteTitle to note.noteDescription,
           )
            db.collection("notes").add(note)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    getAllNote()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }


    fun getAllNote():MutableLiveData<List<Note>>{
        db.collection("notes")
            .get()
            .addOnSuccessListener {
                result ->
                var details = arrayListOf<Note>()
                for (document in result){
//                    var count =1L
//                    var desc = ""
//                    var title = ""
//                    for (value in document
//                        .data.values)
//                    {
//                        if (count % 2.0 == 0.0)
//                        {
//
//                            title = value.toString()
//                        }
//                        desc = value.toString()
//                        count ++
////                        notes.postValue(details)
//                        Log.d(TAG, "${document.id} => ${value}")
//                    }
//                    Log.d(TAG, "${document.id} => ${title}, ${desc}")
//                    details.add(Note(document.id,title,desc))
                    document.data.map{
                        (key,value)-> details.add(Note(document.id,key.toString(),value.toString()))
                    }
                    notes.postValue(details)
                        Log.d(TAG, "${document.id} => ${document.data.keys}")


                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
        return notes

    }


    fun updateNote(id:String,noteTitle:String,noteDescription:String)
    {
        CoroutineScope(Dispatchers.IO).launch {
        db.collection("notes")
            .get()
            .addOnSuccessListener {
                result ->
                for (document in result){
                    if (document.id == id){
                        db.collection("notes").document(id).update("noteTitle",noteTitle,"noteDescription",noteDescription)
                    }

                }
                getAllNote()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }
    }

    fun deleteNote(id: String){
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("notes")
                .get()
                .addOnSuccessListener {
                    result ->
                    for(document in result){
                        if(document.id == id){
                            db.collection("notes").document(id).delete()
                        }
                    }
                    getAllNote()
                }
                .addOnFailureListener { exception->
                    Log.w(TAG,"Error deleted document",exception)
                }
        }
    }
}