# Warriors On Wheels

[![Build Status](https://travis-ci.com/WSU-4110/CarpoolingApp.svg?branch=feature%2Fbackend)](https://travis-ci.com/WSU-4110/CarpoolingApp)

## Development
dev api link: https://carpool-api-r64g2xh4xa-uc.a.run.app

docs: https://carpool-api-r64g2xh4xa-uc.a.run.app/docs

The API can be used two ways: Running locally through Node.js and npm, or directly hitting the dev link above.

local development: 
Please use the `feature/backend` branch for the latest API code. 

```bash
cd /backend
npm install
npm run dev
```

## Using Postman
https://www.postman.com/downloads/

Collection Link (4/10): https://www.postman.com/collections/667746efefc1bb389fa0

Postman is a GUI application for designing and testing HTTP requests. This is the most efficient way to test the API. Add the collection above for a starter set of API calls.

The following environment variables will be needed if you are using the collection's default requests:
 ```
url: endpoint of the API you are using
access_id: any generic access id you wish to use for your mock user
jwt: The JSON Web Token you will receive when first logging in (explained below)
```
#### Authorization
steps to log in:
```
1. Pick an access ID and password you want to use
2. Create user by calling POST /user
3. Receive a token by calling POST /user/auth with your access id and password
4. Use this token in the headers of all of your calls: Set the "jwt" environment variable mentioned earlier
  "Authorization: {{jwt}}"
```

## Bug Reporting
Please create an issue outlining the bug and add any log messages you may have seen related to the bug. Assign the issue either to Evan if it is an API bug, or Nidhi, Saloni, or Darpan if it is a frontend bug. 
