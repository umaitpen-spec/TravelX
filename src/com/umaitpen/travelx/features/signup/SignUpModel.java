package com.umaitpen.travelx.features.signup;

import com.umaitpen.travelx.data.dto.User;
import com.umaitpen.travelx.data.repository.TravelXDB;

public class SignUpModel {
    private final TravelXDB db;

    public SignUpModel(TravelXDB db) {
        this.db = db;
    }

    public User register(String name, String email, String password, String mobileNo, User.Role role) {
        return db.createUser(name, email, password, mobileNo, role);
    }
}
