package com.example.android.lookwhatiamwaring2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {


    ListView listView;
    ArrayList<ModelData> arrayList = new ArrayList<ModelData>();
    ListViewAdapter adapter;
    ModelData modelData;
    String sUrl, sDes, sDate, skey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main_page_activity );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        listView = findViewById( R.id.myList );

        final Query packQuery = FirebaseDatabase.getInstance().getReference( "User Data" ).orderByChild( "email" ).equalTo( Constant.EMAIL );
        packQuery.addValueEventListener( packListVLE );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent( getApplicationContext(),AddItem.class );
            startActivity( intent );
        }

        return super.onOptionsItemSelected( item );
    }
    ValueEventListener packListVLE = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                arrayList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    sDate = "" + postSnapshot.child( "date" ).getValue();
                    sUrl = "" + postSnapshot.child( "url" ).getValue();
                    sDes = "" + postSnapshot.child( "des" ).getValue();
                    skey = "" + postSnapshot.getKey();
                    modelData = new ModelData( sUrl, sDate, sDes, skey );
                    arrayList.add( modelData );
                }
                adapter = new ListViewAdapter( getApplicationContext(), arrayList );
                listView.setAdapter( adapter );
            } catch (Exception e) {
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
