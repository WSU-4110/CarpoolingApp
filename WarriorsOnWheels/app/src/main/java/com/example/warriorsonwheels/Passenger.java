package com.example.warriorsonwheels;

public class Passenger {

    private String name;
    private String accessId;
    private String primaryLocation;
    private String phoneNum;

    private Passenger(final Builder builder) {
        name = builder.name;
        accessId = builder.accessId;
        phoneNum = builder.phoneNum;
        primaryLocation = builder.primaryLocation;
    }

    public String getName() {
        return name;
    }
    public String getAccessId() {
        return accessId;
    }
    public String getPhone() {
        return phoneNum;
    }
    public String getPrimaryLocation() {
        return primaryLocation;
    }

    public static class Builder {
        private String name;
        private String accessId;
        private String phoneNum;
        private String primaryLocation;

        public Builder setName(final String firstName) {
            this.name = firstName;
            return this;
        }

        public Builder setAccessId(final String access) {
            this.accessId = access;
            return this;
        }

        public Builder setPhoneNum(final String  num) {
            this.phoneNum = num;
            return this;
        }

        public Builder setPrimaryLocation(final String prime) {
            this.primaryLocation = prime;
            return this;
        }

        public Passenger create() {
            return new Passenger(this);
        }
    }
}

