package com.webapplication.domain;

public enum Role {
    User {
        @Override
        public String toString() {
            return "user";
        }
    },

    Admin {
        @Override
        public String toString() {
            return "admin";
        }
    }
}