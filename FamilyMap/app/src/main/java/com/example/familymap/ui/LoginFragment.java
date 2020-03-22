package com.example.familymap.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.familymap.R;
import com.example.familymap.activities.MainActivity;
import com.example.familymap.network.LoginTask;
import com.example.familymap.network.RegisterTask;
import com.example.shared.response_.LoginResponse;
import com.example.shared.response_.RegisterResponse;

//public class LoginFragment extends Fragment implements LoginTask.Context {
public class LoginFragment extends Fragment {
    private EditText serverHostTextEntry;
    private EditText serverPortTextEntry;
    private EditText usernameTextEntry;
    private EditText passwordTextEntry;
    private EditText firstNameTextEntry;
    private EditText lastNameTextEntry;
    private EditText emailTextEntry;

    private RadioGroup genderRadioGroup;
    private RadioButton genderRadio;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;

    private Button loginBtn;
    private Button registerBtn;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Connect with view
        serverHostTextEntry = (EditText) v.findViewById(R.id.serverHostTextEntry);
        serverPortTextEntry = (EditText) v.findViewById(R.id.serverPortTextEntry);
        usernameTextEntry = (EditText) v.findViewById(R.id.usernameTextEntry);
        passwordTextEntry = (EditText) v.findViewById(R.id.passwordTextEntry);
        firstNameTextEntry = (EditText) v.findViewById(R.id.firstNameTextEntry);
        lastNameTextEntry = (EditText) v.findViewById(R.id.lastNameTextEntry);
        emailTextEntry = (EditText) v.findViewById(R.id.emailTextEntry);

        genderRadioGroup = (RadioGroup) v.findViewById(R.id.genderRadioGroup);
        maleRadio = (RadioButton) v.findViewById(R.id.maleRadio);
        femaleRadio = (RadioButton) v.findViewById(R.id.femaleRadio);

        loginBtn = (Button) v.findViewById(R.id.loginBtn);
        registerBtn = (Button) v.findViewById(R.id.registerBtn);

        loginBtn.setEnabled(false);
        registerBtn.setEnabled(false);

        // Listeners for EditTexts
        serverHostTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        serverPortTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        usernameTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        passwordTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        firstNameTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        lastNameTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        emailTextEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Listener for gender RadioButtons
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (RegisterFieldsFilled()) {
                    registerBtn.setEnabled(true);
                }
            }
        });

        // Listener for login Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClick();
            }
        });

        // Listener for register Button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClick();
            }
        });

        return v;
    }

    private void CheckTextEdits() {
        if (LoginFieldsFilled()) {
            loginBtn.setEnabled(true);
        }
        else {
            loginBtn.setEnabled(false);
        }
        if (RegisterFieldsFilled()) {
            registerBtn.setEnabled(true);
        }
        else {
            registerBtn.setEnabled(false);
        }
    }

    private Boolean LoginFieldsFilled() {
        return !("".equals(serverHostTextEntry.getText().toString()) || "".equals(serverPortTextEntry.getText().toString()) ||
                "".equals(usernameTextEntry.getText().toString()) || "".equals(passwordTextEntry.getText().toString()));
    }

    private Boolean RegisterFieldsFilled() {
        return !("".equals(serverHostTextEntry.getText().toString()) || "".equals(serverPortTextEntry.getText().toString()) ||
                "".equals(usernameTextEntry.getText().toString()) || "".equals(passwordTextEntry.getText().toString()) ||
                "".equals(firstNameTextEntry.getText().toString()) || "".equals(lastNameTextEntry.getText().toString()) ||
                "".equals(emailTextEntry.getText().toString()) || (!maleRadio.isChecked() && !femaleRadio.isChecked()));
    }


    private void loginClick() {
        try {
            LoginTask loginTask = new LoginTask(serverHostTextEntry.getText().toString(),
                    serverPortTextEntry.getText().toString());
//            loginTask.setContext(this);
            loginTask.execute();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private void registerClick() {
        try {
            RegisterTask registerTask = new RegisterTask(serverHostTextEntry.getText().toString(),
                    serverPortTextEntry.getText().toString());
//            registerTask.setContext(this);
            registerTask.execute();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loginComplete(LoginResponse response) {
        if (response.getSuccess()) {
            String toastMessage = "Welcome " + response.getUserName();
            Toast toast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT);
            toast.show();
            closeSelfFragment();
        }
        else {
            String toastMessage = response.getMessage();
            Toast toast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void registerComplete(RegisterResponse response) {
        if (response.getSuccess()) {
            String toastMessage = "Welcome " + response.getUserName();
            Toast toast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT);
            toast.show();
            closeSelfFragment();
        }
        else {
            String toastMessage = response.getMessage();
            Toast toast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void closeSelfFragment() {
        MainActivity parent = (MainActivity) getActivity();
        parent.replaceLoginFragment();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
}
