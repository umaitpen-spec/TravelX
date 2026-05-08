package com.umaitpen.travelx.features.signin;

import com.umaitpen.travelx.data.dto.User;
import com.umaitpen.travelx.data.repository.TravelXDB;

import java.util.Optional;

public class SignInModel {
    private final TravelXDB db;
    private final SignInView signInView;

    public SignInModel(TravelXDB db) {
        this.db = db;
        signInView = new SignInView(this);
    }

    public Optional<User> signIn(String email, String password) {
        return db.login(email, password);
    }

    public SignInView getSignInView() {
        return signInView;
    }
}
