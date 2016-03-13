#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Test server to receive requests from the android app.
"""


from __future__ import print_function


from flask import Flask, request, jsonify
from uuid import uuid4

app = Flask(__name__)
app.secret_key = str(uuid4())


@app.route("/test_post", methods=["POST"])
def test_post():
    """Route for receiving post data."""
    param1 = request.form.get("param1", None)
    param2 = request.form.get("param2", None)

    print("param1: ", param1)
    print("param2: ", param2)

    return jsonify(
        param1=param1 or "None",
        param2=param2 or "None"
    )


if __name__ == "__main__":
    app.run(debug=True, port=8080, host="0.0.0.0")
