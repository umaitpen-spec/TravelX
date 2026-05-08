package com.umaitpen.travelx.features.signin;

public class SignInView {
    private final SignInModel signInModel;

    public SignInView(SignInModel signInModel) {
        this.signInModel = signInModel;
    }

    public void showInvalidLogin() {
        System.out.println("Invalid credentials or inactive account.");
    }

    public SignInModel getSignInModel() {
        return signInModel;
    }
}
