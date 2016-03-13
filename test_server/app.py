#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Test server to receive requests from the android app.
"""


from __future__ import print_function

import json

from flask import Flask, request, jsonify
from uuid import uuid4

app = Flask(__name__)
app.secret_key = str(uuid4())


@app.route("/test_post", methods=["POST"])
def test_post():
    """Route for receiving post data."""
    param1 = request.form.get("id", None)
    param2 = request.form.get("password", None)
    param3 = request.form.get("crns", None)

    print("param1: ", param1)
    print("param2: ", param2)
    print("param3: ", param3, json.loads(param3))

    return jsonify(
        param1=param1 or "None",
        param2=param2 or "None",
        param3=param3 or "None"
    )


if __name__ == "__main__":
    app.run(debug=True, port=8080, host="0.0.0.0")
