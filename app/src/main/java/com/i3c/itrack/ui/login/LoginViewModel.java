package com.i3c.itrack.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.i3c.itrack.communication.LoginAsync;
import com.i3c.itrack.data.LoginRepository;
import com.i3c.itrack.data.Result;
import com.i3c.itrack.data.model.LoggedInUser;
import com.i3c.itrack.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private String UID,UNAME;
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        LoginAsync loginAsync=new LoginAsync(this);
        loginAsync.execute(username,password);
        //Result<LoggedInUser> result = loginRepository.login(username, password);

        //if (result instanceof Result.Success) {
        //    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
        //    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        //} else {
        //    loginResult.setValue(new LoginResult(R.string.login_failed));
        //}
    }
    public void setLoginResult(LoginResult result){
        if (result.getSuccess() instanceof LoggedInUserView) {
            LoggedInUser data = new LoggedInUser(result.getUserID().toString(),result.getSuccess().getDisplayName());
            this.UNAME=getUserName();
            this.UID=result.getUserID().toString();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null;
    }

    public String getUserID(){
        return this.UID;
    }
    public String getUserName(){
        return this.UNAME;
    }
}