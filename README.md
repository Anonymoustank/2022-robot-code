Welcome to the 2036 code repo!

# Development values
- *High-Octane*: Good code is fast to onboard to, fast to add to, and fast to build and deploy.
- *Modular*: Not just good code, but good software is extendable and configurable.
- *Robust*: Good code stands up to any obstacle it faces.

# Poetry
Poetry is a dependency management tool for Python. What makes this a great alternative to pip is that it almost ensures fully reproducible builds and development environments on Windows, MacOS, Linux, and the robot Raspberry Pi. Follow the installation instructions in its [documentation](https://python-poetry.org/docs/). Then, for any project that uses poetry, run `poetry install` to create a virtualenv, and run `poetry shell` to boot into that virtualenv. Then run whatever python command you normally use to run the project!

# robot/
This directory contains all of the WPILib code that runs on the roboRIO. It is a standard Kotlin project, and can be built and deployed using Gradle.

# vision/

## tflite-runtime, Poetry, and you
BallVision needs a specific library called tflite-runtime which a) does not work with Poetry and b) only works on Linux. When you need to use this system, install `tflite-runtime` by running `python3 -m pip install tflite-runtime`. I've added nessecary code that only adds this layer if its running on a linux system.

## JSON configuration
The configuration for cameras is done through JSON. Do not modify the config.robot.json; this is the config that is used on the RaspberryPi on the robot for production code.
Instead, write your own config file (perhaps named config.development.json) and pass it into the script.

Here is the format that VisionInstance expects:

```
{
    "team": This must be the team number (2036). Right now this is not needed but may be used later down the line for network verification/identification.
    "cameras": [
        This is the array of camera instances.
        {
            "name": Give the camera a name. THIS IS HOW YOU'LL IDENTIFY AND GET A CAMERA'S FRAMES IN CODE!
            "id": The id of the camera. Can either be identified through an index (for windows) or through a device path. (for linux, macos(?), and robotpi)
            "props": [
                This is an array of properties that will be set when the VisionInstance first runs.
                {
                    "key": What property you want to set. You can find the available ones to tune here: https://docs.opencv.org/4.6.0/d4/d15/group__videoio__flags__base.html#gaeb8dd9c89c10a5c63c139bf7c4f5704d.
                    "value": What value you want to set the property to.
                }
            ]
        }
    ] 
}
```

# liverecord/
This is the code for the viewing of LiveRecords - graph (cartesian) data from the robot.

The protocol is as follows:
* There exists a NetworkTables table called `liverecord`.
* The keys of entries of `liverecord` are the data it is collecting, for example `piddata` or `encoderposition`.
* The values of entries of `liverecord` are csv values containing the x data and y data, for example `2.5,3.6` or `20,19.7`. As of right now liverecord **only supports 2d data**.

There is a weird issue with node_modules not being ignored by git, always make sure you do not commit that directory when working with this module!

The backend of liverecord uses Flasks, which recieves the NetworkTables values and sends them to the frontend using SocketIO. The frontend uses Svelte, HighCharts, and SocketIO for rendering and recieving data respectively.

# pid-solver/
This is the code for PID solving. It will output values needed for the Cohen-Coon Open Loop and Ziegler Nichols Open Loop tuning methods. 

It takes a csv file containing columns for time, observed velocity, and the set velocity (in order from left to right), ignoring the first row containing headers. 

Run pid.py and specify the flag "--help" for more information on the arguments it accepts.

Here is a sample CSV that the code would accept. 

```
time, observed_velocity, set_velocity
0, 0, 2.5
1, 0, 2.5
2, 1.1, 2.5
3, 2, 2.5
4, 2.3, 2.5
5, 2.5, 2.5
```

Example way to output PID values for both of the methods: ```python3 pid.py filename.csv BOTH```
Example way to output PID values for the Ziegler Nichols Method: ```python3 pid.py filename.csv ZN```
Example way to output PID values for the Cohen-Coon Method: ```python3 pid.py filename.csv CC```