package test.invoicegenerator.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.R;
import test.invoicegenerator.model.User;
import test.invoicegenerator.adapters.CustomAdapter;

public class UsersActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    public ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);
        init();
    }
    private void init()
    {
        getUserDataFromFirebase();

    }
    private void getUserDataFromFirebase()
    {
        final ArrayList<User> users=new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user=new User(ds.child("user_id").getValue(String.class),ds.child("name").getValue(String.class),
                            ds.child("email").getValue(String.class),ds.child("company_name").getValue(String.class),
                            ds.child("password").getValue(String.class));
                    users.add(user);


                }
                CustomAdapter adapter = new CustomAdapter(UsersActivity.this, users);

                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
    }

}
