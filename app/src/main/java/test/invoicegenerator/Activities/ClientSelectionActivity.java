package test.invoicegenerator.Activities;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.invoicegenerator.Activities.BaseActivity;
import test.invoicegenerator.Libraries.Progressbar;
import test.invoicegenerator.NetworksCall.IResult;
import test.invoicegenerator.NetworksCall.NetworkURLs;
import test.invoicegenerator.NetworksCall.VolleyService;
import test.invoicegenerator.R;
import test.invoicegenerator.adapters.ClientAdapter;
import test.invoicegenerator.fragments.FragmentEditReport;
import test.invoicegenerator.model.ClientModel;

public class ClientSelectionActivity extends BaseActivity {

    ListView listView;
    FloatingActionButton floating_AddClient;
    ClientAdapter clientAdapter;
    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    SearchView searchView;
    Snackbar snackbar;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    Button buttonBack;
    int AddPosition = 0;
    int OpenPosition = 0;
    Progressbar cdd;
    ArrayList<ClientModel> clientModels=new ArrayList<ClientModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_selection);

        listView = (ListView) findViewById(R.id.clientList);
        searchView = (SearchView)findViewById(R.id.searchView); // inititate a search view
        buttonBack=findViewById(R.id.back_btn);
        floating_AddClient = (FloatingActionButton) findViewById(R.id.floating_add_new_client);

        init();
       // unbinder= ButterKnife.bind(ClientSelectionActivity.this);
        GetClientList();
    }
    private void init() {
        cdd=new Progressbar(ClientSelectionActivity.this);
        searchView.setQueryHint("Search Client");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null,
                null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        EditText editText = (EditText) searchView.findViewById(id);
        editText.setHintTextColor(Color.GRAY);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                clientAdapter.getFilter().filter(newText);

                return false;
            }
        });


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

      /*  BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setVisibility(View.GONE);
*/
        floating_AddClient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  loadFragment(new FragmentAddClient(),null);
                Toast.makeText(ClientSelectionActivity.this, "In Progroess", Toast.LENGTH_SHORT).show();
            }
        });



        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenPosition = position;
                FragmentEditReport.SelectedClientId=clientModels.get(OpenPosition).getId();
                Toast.makeText(ClientSelectionActivity.this, "Selected Client Id:" +String.valueOf(FragmentEditReport.SelectedClientId), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void loadFragment(Fragment dashboardFragment, Bundle bundle) {
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        dashboardFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_frame, dashboardFragment);
        fragmentTransaction.addToBackStack(/*dashboardFragment.toString()*/null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
    public void GetClientList()
    {

        cdd.ShowProgress();
        initVolleyCallbackForClientList();
        mVolleyService = new VolleyService(mResultCallback, ClientSelectionActivity.this);
        mVolleyService.getDataVolley("GETCALL", NetworkURLs.BaseURL+ NetworkURLs.GetClientList);

    }

    void initVolleyCallbackForClientList() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray clients = data.getJSONArray("clients");

                        for (int i = 0; i < clients.length(); i++) {
                            ClientModel clientModel = new ClientModel(clients.getJSONObject(i));
                            clientModels.add(clientModel);
                        }

                        clientAdapter = new ClientAdapter(ClientSelectionActivity.this,clientModels);
                        listView.setAdapter(clientAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                cdd.HideProgress();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                cdd.HideProgress();
            }

            @Override
            public void notifySuccessResponseHeader(NetworkResponse response) {

            }
        };
    }
}
