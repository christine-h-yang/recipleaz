package cs371m.com.recipleaz;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class LoginAndSignupDialogFragment extends DialogFragment {

    public interface LoginAndSignupInterface {
        void signup(String email, String password);
        void login(String email, String password);
    }

    private LoginAndSignupInterface loginAndSignupInterface;

    public void setLoginAndSignupInterface(LoginAndSignupInterface loginAndSignupInterface) {
        this.loginAndSignupInterface = loginAndSignupInterface;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.login_dialog, null);

        builder.setView(view)
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText email = view.findViewById(R.id.email);
                        EditText password = view.findViewById(R.id.password);

                        if (!email.getText().toString().trim().equals("")
                                && !password.getText().toString().trim().equals("")) {
                            loginAndSignupInterface.login(email.getText().toString(),
                                    password.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.signup, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText email = view.findViewById(R.id.email);
                        EditText password = view.findViewById(R.id.password);

                        if (!email.getText().toString().trim().equals("")
                                && !password.getText().toString().trim().equals("")) {
                            loginAndSignupInterface.signup(email.getText().toString(),
                                    password.getText().toString());
                        }
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginAndSignupDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
