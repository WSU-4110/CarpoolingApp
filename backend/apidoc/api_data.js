define({ "api": [
  {
    "type": "get",
    "url": "/passengers",
    "title": "/passengers",
    "name": "PassengerGet",
    "group": "passenger",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n    \"error\": false,\n    \"data\": [\n        {\n            \"id\": 2,\n            \"name\": \"darpan\",\n            \"phone_number\": \"1412122234\",\n            \"location\": \"vegas baby\",\n            \"access_id\": \"ab1234\"\n        }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 5xx": [
          {
            "group": "Error 5xx",
            "type": "String",
            "optional": false,
            "field": "500",
            "description": "<p>Internal Error: {error message}</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "./controllers/passenger.js",
    "groupTitle": "passenger"
  }
] });
