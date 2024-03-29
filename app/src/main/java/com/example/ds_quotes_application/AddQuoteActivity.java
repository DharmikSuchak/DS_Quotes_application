package com.example.ds_quotes_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddQuoteActivity extends AppCompatActivity {

    private EditText quoteEditText;
    private EditText authorEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quote);

        //bindViews

        quoteEditText = (EditText) findViewById(R.id.editTextQuote);
        authorEditText= (EditText) findViewById(R.id.editTextAuthor);
        addButton = (Button) findViewById(R.id.addButton);

        //Attaching Click Listener to the add button

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get text
                String quote=quoteEditText.getText().toString();
                String author=authorEditText.getText().toString();

                // check if empty
                if(quote.isEmpty()){
                    quoteEditText.setError("Cannot be Empty");
                    return;
                }

                if(author.isEmpty()){
                    authorEditText.setError("Cannot be Empty");
                    return;
                }

                // add it to the database
                addQuoteToDB(quote, author);
            }
        });

    }

    private void addQuoteToDB(String quote, String author) {
        //create a hashmap

        HashMap<String, Object> quoteHashMap=new HashMap<>();

        quoteHashMap.put("quote",quote);
        quoteHashMap.put("author",author);

        //instantiate database connection
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quotesRef = database.getReference("quotes");

        String key=quotesRef.push().getKey();
        quoteHashMap.put("key",key);

        quotesRef.child(key).setValue(quoteHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddQuoteActivity.this, "Added", Toast.LENGTH_SHORT).show();
                quoteEditText.getText().clear();
                authorEditText.getText().clear();
            }
        });

    }
}