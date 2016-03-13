# DrexelAutoRegisterAndroid
(Development) Android client for DrexelAutoRegister service.

Note that this is just a proof of concept, so the design of the app is very minimal.

This app was tested on Android v4.4.2 (kitkat).


## Setup
A pre-built debug apk is provided in the `buils/`, but instructions are provided on how to build from the command line.

### Building
These are the steps I take for installing the app on a phone from the command line.

First, since `local.properties` isn't included in this repo, it must be added in the root directory of the repo on the same level as `build.xml` and this README.md. The only thing that must be placed inside this file is the (absolute) path to the android sdk:
```
sdk.dir=/Users/myusername/android-sdk-myoperatingsystem
```
This part is only done once and doesn't need to be done again, unless you change the location of the sdk, in which case, you will need to update this line.


After that, the app should just need to be built and installed.
The app can be built from the repo root directory with ant:
```sh
$ ant debug  # I have only been testing with the debug version
```

The app can be installed with adb like so:
```sh
$ adb install bin/DrexelAutoRegister-debug.apk
```

To make this easier, both these commands are in a shell script that can be called to automatically build and run the app, and additionally log entries associated with the tag `TAG`:
```sh
$ ./install.sh
```

To reinstall the app again, it must be uninstalled from the phone, or the uninstall script can be used to bring up a prompt on the phone to uninstall it:
```sh
$ ./uninstall.sh
```


## Usage
Enter drexel id (abc123), drexel login password, comma separated list of valid crns (12345,67890), and time ticket date and time
and submit to have the app register you for classes automatically.

The app **MUST NOT** be close in order for this to work. The app can run in the background of the phone, even when it's asleep, and will automatically send a request to the server which will attempt register you for the courses represented by the crns provided.


## Testing
Provided under `test_server/` is a flask program for testing that the app can communicate with the actual server.

### Server setup
To get the dependencies and get the scripts to work, you will need `pip` and `virtualenv`.

Instructions on installing pip [here](https://pip.pypa.io/en/stable/installing/).
Instructions on installing virtualenv [here](http://virtualenv.readthedocs.org/en/latest/installation.html).

Once these two are installed, use virtualenv to create a new virtual envorinment where the script dependencies can be isolated.

```sh
$ virtualenv venv  # Create a new venv named venv
$ source venv/bin/activate  # Activate the virtual env
(venv) $ pip install -r requirements.txt  # Install dependencies
```

### Running server
```sh
(venv) $ python app.py  # Start server on YOUR_COMPUTER_IP:8080
```

