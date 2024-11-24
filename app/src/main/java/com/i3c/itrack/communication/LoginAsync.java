package com.i3c.itrack.communication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.i3c.itrack.HomeActivity;
import com.i3c.itrack.config.ConfigData;
import com.i3c.itrack.data.Result;
import com.i3c.itrack.data.model.LoggedInUser;
import com.i3c.itrack.ui.login.LoggedInUserView;
import com.i3c.itrack.ui.login.LoginActivity;
import com.i3c.itrack.ui.login.LoginResult;
import com.i3c.itrack.ui.login.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginAsync extends AsyncTask<String,String, LoginResult> {
    private LoginResult log_result;
    private LoginViewModel loginViewModel;
    private ConfigData config;
    //protected LoginActivity loginActivity;
    ProgressDialog pd;

    public LoginAsync(LoginViewModel loginactivity) {
        config=new ConfigData();
        this.loginViewModel=loginactivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //pd = new ProgressDialog(this.loginActivity);
        //pd.setMessage("Please wait....");
        //pd.show();
    }
    @Override
    protected LoginResult doInBackground(String... strings) {
        Log.i("LOGIN","Sending Data:"+ strings[0]+","+strings[1]);
        try {
            this.log_result=executeHttpRequest(strings);
            return this.log_result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(LoginResult loginResult) {
        //if (pd != null)
        //{
        //    pd.dismiss();
        //}
        //if(loginResult.getSuccess()!=null) {
            loginViewModel.setLoginResult(loginResult);
            //Intent i=new Intent(this.loginViewModel.,HomeActivity.class);
            //i.putExtra("UID",loginResult.getUserID());
            //i.putExtra("Name",loginResult.getSuccess().getDisplayName());
            //this.loginActivity.startActivity(i);
            //this.loginActivity.finish();
        //}
        //else{
            //this.loginActivity.viewLoginFailed();
        //}
    }

    private LoginResult executeHttpRequest(String... strings) throws JSONException {
        String url_str= config.HOST_URL+"/ngs_user.php";
        httpExecutor http=new httpExecutor(url_str);

        Map<String, Object> params=new LinkedHashMap<>();
        params.put("UN",strings[0]);
        params.put("PS",strings[1]);
        //params.put("UN","kumara");
        //params.put("PS","aaa");
        params.put("SID","14");
        JSONArray jsonArray=http.getJSON(params);
        LoginResult login_res = null;
        if(jsonArray!=null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Log.i("LOGIN", obj.getString("result"));
                if (obj.getString("result").equals("1")) {
                    if (obj.has("user_id") && !obj.isNull("user_id")) {
                        if (obj.get("user_id") instanceof String) {
                            LoggedInUserView loggedInUserview=new LoggedInUserView(strings[0]);
                            login_res=new LoginResult(loggedInUserview);
                            login_res.setUserID(obj.getInt("user_id"));
                            System.out.println("XXXXX:"+login_res.getUserID());
                        }
                        else {
                            login_res=new LoginResult(-1);
                        }
                    }
                    else{
                        login_res=new LoginResult(-1);
                    }

                } else {
                    login_res=new LoginResult(-1);
                }
            }
        }else{
            login_res=new LoginResult(-1);
        }
        return login_res;
    }
}
