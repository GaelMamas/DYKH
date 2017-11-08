package malakoff.dykh.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import malakoff.dykh.Activities.MainActivity;
import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;
import malakoff.dykh.Fragments.Base.BaseFragment;
import malakoff.dykh.Network.MySingleton;
import malakoff.dykh.R;
import malakoff.dykh.User.User;

/**
 * Created by vm32776n on 30/10/2017.
 */

public class LoginFragment extends BaseFragment implements TextView.OnEditorActionListener {

    private EditText emailEditText, pwdEditText;
    private Button loginButton;

    RequestQueue queue;

    @Override
    protected void assignViews(View view) {
        loginButton = (Button) view.findViewById(R.id.button_login_login);
        emailEditText = (EditText) view.findViewById(R.id.edittext_login_email);
        pwdEditText = (EditText) view.findViewById(R.id.edittext_login_password);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState) {

        emailEditText.setOnEditorActionListener(this);

        pwdEditText.setOnEditorActionListener(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login(emailEditText.getText().toString(),
                        pwdEditText.getText().toString());

            }
        });

        loginButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                login("mviviengael@gmail.com", "Admin");

                return true;
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        queue = MySingleton.getInstance(getContext().getApplicationContext()).getRequestQueue();
        queue.start();
    }

    private void login(String email, String password) {
        JSONObject params;

        try {
            params = new JSONObject();

            params.put("email", email);
            params.put("name", password);


            JsonObjectRequest jsonObjectGsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.SERVER_URL_ROOT + Constants.SERVER_URL_USER_ROUT + "/checkUser",
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            User user = new Gson().fromJson(String.valueOf(response),
                                    new TypeToken<User>() {
                                    }.getType());

                            if (user != null) {
                                AppApplication.setUserInfo(user);

                                MainActivity.open(getActivity());
                            } else {
                                Toast.makeText(getContext(), "Unknown User", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getContext(), "It didn't work", Toast.LENGTH_SHORT).show();

                }
            });

            MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectGsonRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }


}
